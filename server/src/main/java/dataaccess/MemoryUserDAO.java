package dataaccess;

import model.UserData;
import java.util.Map;
import java.util.HashMap;



public class MemoryUserDAO implements UserDAO{
    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData u) throws DataAccessException{
        users.put(u.username(), u);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException{
            return users.get(username);
    }

    @Override
    public String getPasswordHash(String username) throws DataAccessException{//my diagram did a getPassword, by username, but should PWs be hashed?
        return users.get(username).password();
    }

    @Override
    public void clear() throws DataAccessException{
        users.clear();
    }
}
