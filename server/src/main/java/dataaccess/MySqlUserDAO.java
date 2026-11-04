package dataaccess;

import model.UserData;
import dataaccess.DatabaseManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.sql.*;
import com.google.gson.Gson;
import java.sql.SQLException;





public class MySqlUserDAO implements UserDAO {

    public MySqlUserDAO() throws DataAccessException {
        configureDatabase();
    }



    @Override
    public void createUser(UserData user) throws DataAccessException{
        String statement  = "INSERT INTO user (username, passwordHash, email) values (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()){

            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, user.username());
                ps.setString(2, user.password());
                ps.setString(3, user.email());

                ps.executeUpdate();
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to create user", ex);
        }
    }
    @Override
    public UserData getUser(String username) throws DataAccessException{
        String statement  = "SELECT username, passwordHash, email FROM user WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection()){

            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);

                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return new UserData(rs.getString("username"), rs.getString("passwordHash"), rs.getString("email"));
                    }
                }
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to retrieve user", ex);
        }
        return null;
    }


    @Override
    public String getPasswordHash(String username) throws DataAccessException {
        String statement  = "SELECT passwordHash FROM user WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection()){

            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);

                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return rs.getString("passwordHash");
                    }
                }
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to retrieve password hash", ex);
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        String statement  = "DELETE FROM user";

        try (Connection conn = DatabaseManager.getConnection()){

            try (PreparedStatement ps = conn.prepareStatement(statement)){
               ps.executeUpdate();
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to clear user data", ex);
        }
    }



    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS user (
        username VARCHAR(255) NOT NULL,
        passwordHash VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        PRIMARY KEY (username)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """
    };



    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("Unable to configure user database", ex);
        }
    }
}
