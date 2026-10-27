package dataaccess;

import model.UserData;
import java.util.Map;
import java.util.HashMap;



public class MemoryUserDAO implements UserDAO{
    private final Map<String, UserData> users = new HashMap<>();


    public void createUser(UserData u) throws DataAccessException{
        users.put(u.username(), u);
    }

    public UserData getUser(String username) throws DataAccessException{
            return users.get(username);
    }

    public String getPassword(String username) throws DataAccessException{//my diagram did a getPassword, by username, but should PWs be hashed?
        return users.get(username).password();
    }

    public void clear() throws DataAccessException{
        users.clear();
    }

}
