package dataaccess;
import model.GameData;
import model.UserData;

public interface GameDAO {

    int createGame(String gameName) throws DataAccessException;

    void getGame(int gameID) throws DataAccessException;

    void listGames() throws DataAccessException;

    void updateGame(/*gameIDAndUsernameAndColor*/) throws DataAccessException;//how to represent "gameIDAndUsernameAndColor"


}
