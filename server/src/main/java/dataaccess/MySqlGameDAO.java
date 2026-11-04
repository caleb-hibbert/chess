package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

import model.UserData;
import dataaccess.DatabaseManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.sql.*;
import com.google.gson.Gson;
import java.sql.SQLException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;




public class MySqlGameDAO implements GameDAO{
    Gson gson = new Gson();

    public MySqlGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        String statement  = "INSERT INTO game (whiteUsername, blackUsername, gameName, gameState) values (?, ?, ?, ?)";
        ChessGame newGame = new ChessGame();

        try (Connection conn = DatabaseManager.getConnection()){
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.setString(1, null);
                ps.setString(2, null);
                ps.setString(3, gameName);
                ps.setString(4, gson.toJson(newGame));

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                    throw new DataAccessException("Unable to retrieve generated game ID");
                }
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to create new game", ex);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String statement  = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM game WHERE gameID = ?";

        try (Connection conn = DatabaseManager.getConnection()){

            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameID);

                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        ChessGame chessGame = gson.fromJson(rs.getString("gameState"), ChessGame.class);
                        GameData gameData = new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), chessGame);
                        return gameData;
                    }
                    return null;
                }
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to get individual game for getGame", ex);
        }
    }


    @Override
    public Map<Integer, GameData> listGames() throws DataAccessException {
        String statement  = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM game";
        Map<Integer, GameData> gameList = new HashMap<>();


        try (Connection conn = DatabaseManager.getConnection()){

            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    ChessGame chessGame = gson.fromJson(rs.getString("gameState"), ChessGame.class);
                    GameData gameData = new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), chessGame);
                    gameList.put(gameData.gameID(), gameData);
                }
                return gameList;
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to retrieve auth token", ex);
        }
    }


    private final Map<Integer, GameData> games = new HashMap<>();//FIXME - just to keep below happy until implemented

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {//how to represent "gameIDAndUsernameAndColor"?
        String statement  = "UPDATE game SET whiteUsername =?, blackUsername = ?, gameName = ?, gameState = ? WHERE gameID = ?";

        try (Connection conn = DatabaseManager.getConnection()){

            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, updatedGame.whiteUsername());
                ps.setString(2, updatedGame.blackUsername());
                ps.setString(3, updatedGame.gameName());
                ps.setString(4, gson.toJson(updatedGame.game()));
                ps.setInt(5, updatedGame.gameID());
                ps.executeUpdate();
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to retrieve auth token", ex);
        }




    }//        games.put(updatedGame.gameID(), updatedGame);





    @Override
    public void clear() throws DataAccessException{
        String statement  = "DELETE FROM game";

        try (Connection conn = DatabaseManager.getConnection()){

            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to clear game data", ex);
        }
    }





    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS game (
        gameID INT NOT NULL AUTO_INCREMENT,
        whiteUsername VARCHAR(255),
        blackUsername VARCHAR(255),
        gameName VARCHAR(255) NOT NULL,
        gameState JSON NOT NULL,
        PRIMARY KEY (gameID)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """
    };


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("Unable to configure game database", ex);
        }
    }

}
