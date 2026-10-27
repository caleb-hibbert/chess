package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();
    private int nextGameID = 0;

    public int createGame(String gameName) throws DataAccessException {
        nextGameID += 1;
        GameData gamedata = new GameData(nextGameID, null, null, gameName, new ChessGame());
        return nextGameID;
    }

    public void getGame(int gameID) throws DataAccessException {

    }

    public void listGames() throws DataAccessException {

    }

    public void updateGame(/*gameIDAndUsernameAndColor*/) throws DataAccessException {//how to represent "gameIDAndUsernameAndColor"?


    }

    public void clear() throws DataAccessException{
        games.clear();
    }


}
