package dataaccess.sql;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class MySqlGameDAOTests {
    private AuthDAO authDAO;
    private UserDAO userDAO;
    private GameDAO gameDAO;

    @BeforeEach
    void initializeTests() throws DataAccessException {
        authDAO = new MySqlAuthDAO();
        userDAO = new MySqlUserDAO();
        gameDAO = new MySqlGameDAO();
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }


    @Test
    void createGetGameSuccess() throws DataAccessException{
        int gameID = gameDAO.createGame("myGame");

        assertEquals(gameID, gameDAO.getGame(gameID).gameID());
        assertNotEquals(-1, gameDAO.getGame(gameID).gameID());
    }


    @Test
    void listGamesSuccess() throws DataAccessException{
        assertEquals(0, gameDAO.listGames().size());
        gameDAO.createGame("myGame");
        gameDAO.createGame("myGame2");

        Map<Integer, GameData> listOfGames = gameDAO.listGames();
        assertNotNull(listOfGames);
        assertEquals(2, listOfGames.size());
    }


    @Test
    void updateGameSuccess() throws DataAccessException{
        int gameID = gameDAO.createGame("myGame");
        GameData updatedGameData = new GameData(gameID, "caleb", null, "myGame", gameDAO.getGame(gameID).game());
        GameData badGameData = new GameData(99, "null", "caleb2", "myGame2", new ChessGame());

        gameDAO.updateGame(updatedGameData);

        assertEquals(updatedGameData, gameDAO.getGame(gameID));
        assertNotEquals(badGameData, gameDAO.getGame(gameID));
    }


    @Test
    void clearSuccess() throws DataAccessException{
        int gameID = gameDAO.createGame("myGame");

        assertEquals(gameID, gameDAO.getGame(gameID).gameID());

        gameDAO.clear();

        assertNull(gameDAO.getGame(gameID));
        assertEquals(new HashMap<>(), gameDAO.listGames());
        assertEquals(0, gameDAO.listGames().size());


    }




}
