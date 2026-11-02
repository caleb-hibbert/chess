package dataaccess;

import model.UserData;
import dataaccess.DatabaseManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.sql.*;
import com.google.gson.Gson;



public class MySqlUserDAO implements UserDAO {

    @Override
    public void createUser(UserData u) throws DataAccessException{
        String statement = "INSERT INTO user (username, password_hash, email) values (?, ?, ?)";
        String json = new Gson().toJson(u);

        try (Connection conn  = DatabaseManager.getConnection()){

        }
        catch (Exception e){

        }
    }


//    public Pet addPet(Pet pet) throws ResponseException {
//        var statement = "INSERT INTO pet (name, type, json) VALUES (?, ?, ?)";
//        String json = new Gson().toJson(pet);
//        int id = executeUpdate(statement, pet.name(), pet.type(), json);
//        return new Pet(id, pet.name(), pet.type());
//    }








    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public String getPassword(String username) throws DataAccessException {
        return "";
    }

    @Override
    public void clear() throws DataAccessException {

    }


//
//    public class MemoryUserDAO implements UserDAO{
//        private final Map<String, UserData> users = new HashMap<>();
//
//        public void createUser(UserData u) throws DataAccessException{
//            users.put(u.username(), u);
//        }
//
//        public UserData getUser(String username) throws DataAccessException{
//            return users.get(username);
//        }
//
//        public String getPassword(String username) throws DataAccessException{//my diagram did a getPassword, by username, but should PWs be hashed?
//            return users.get(username).password();
//        }
//
//        public void clear() throws DataAccessException{
//            users.clear();
//        }

}
