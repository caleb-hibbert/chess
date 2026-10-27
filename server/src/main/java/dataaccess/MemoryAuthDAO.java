package dataaccess;
import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;


public class MemoryAuthDAO implements AuthDAO{
    private final Map<String, AuthData> users = new HashMap<>();

    public void createAuth(AuthData authData) throws DataAccessException{
        users.put(authData.authToken(), authData);
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        return users.get(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        users.remove(authToken);
    }
    public void clear(){
        users.clear();
    }

}
