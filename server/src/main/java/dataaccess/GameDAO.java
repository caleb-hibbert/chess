package dataaccess;
import model.GameData;
import model.UserData;

import java.util.Map;

public interface GameDAO {

    int createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Map<Integer, GameData> listGames() throws DataAccessException;

    void updateGame(GameData updatedGame) throws DataAccessException;//how to represent "gameIDAndUsernameAndColor"

    void clear() throws DataAccessException;
}
