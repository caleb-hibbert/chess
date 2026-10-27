package dataaccess;
import model.UserData;
public interface UserDAO {




    void createUser(UserData u) throws DataAccessException;



    UserData getUser(String username) throws DataAccessException;

    String getPassword(String username) throws DataAccessException;//my diagram did a getPassword, by username, but should PWs be hashed?

    void clear() throws DataAccessException;

}
