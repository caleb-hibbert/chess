package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();
    private int GameID = 0;

    public int createGame(String gameName) throws DataAccessException {
        GameID += 1;
        GameData gamedata = new GameData(GameID, null, null, gameName, new ChessGame());
        games.put(GameID, gamedata);
        return GameID;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    public Map<Integer, GameData> listGames() throws DataAccessException {
        return games;
    }

    public void updateGame(GameData updatedGame) throws DataAccessException {//how to represent "gameIDAndUsernameAndColor"?
        games.put(updatedGame.gameID(), updatedGame);
    }

    public void clear() throws DataAccessException{
        games.clear();
    }
}
