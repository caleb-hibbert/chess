package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.sql.*;
import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAO {
    //private final Map<String, AuthData> users = new HashMap<>();

    public MySqlAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException{
        String statement  = "INSERT INTO auth (authToken, username) values (?, ?)";

        try (Connection conn = DatabaseManager.getConnection()){

            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authData.authToken());
                ps.setString(2, authData.username());

                ps.executeUpdate();
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to create auth token", ex);
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        String statement  = "SELECT authToken, username FROM auth WHERE authToken = ?";

        try (Connection conn = DatabaseManager.getConnection()){

            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);

                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return new AuthData(rs.getString("authToken"),rs.getString("username"));
                    }
                }
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to retrieve auth token", ex);
        }
        return null;
    }


    public void deleteAuth(String authToken) throws DataAccessException{
        String statement  = "DELETE FROM auth WHERE authToken = ?";

        try (Connection conn = DatabaseManager.getConnection()){

            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);

                ps.executeUpdate();
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to delete auth token", ex);
        }
    }


    public void clear() throws DataAccessException{
        String statement  = "DELETE FROM auth";

        try (Connection conn = DatabaseManager.getConnection()){

            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        }
        catch (SQLException ex){
            throw new DataAccessException("Error: Unable to clear auth data", ex);
        }
    }


    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS auth (
        authToken VARCHAR(255) NOT NULL,
        username VARCHAR(255) NOT NULL,
        PRIMARY KEY (authToken)
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
            throw new DataAccessException("Unable to configure auth database", ex);
        }
    }
}
