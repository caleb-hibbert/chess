package dataaccess.sql;


import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AlreadyTakenException;
import service.InvalidRequestException;
import service.RegisterRequest;
import service.RegisterResult;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlAuthDAOTests {
    private AuthDAO authDAO;
    private UserDAO userDAO;


    @BeforeEach
    void initializeTests() throws DataAccessException {
        authDAO = new MySqlAuthDAO();
        userDAO = new MySqlUserDAO();
        authDAO.clear();
        userDAO.clear();
    }


    @Test
    void createGetAuthSuccess() throws DataAccessException {
        AuthData authData = new AuthData("totallyRealAuthToken", "caleb");
        authDAO.createAuth(authData);

        assertNotNull(authDAO.getAuth("totallyRealAuthToken"));
        assertNull(authDAO.getAuth("fsdasdf"));
    }

    @Test
    void deleteAuthSuccess() throws DataAccessException{
        AuthData authData = new AuthData("totallyRealAuthToken", "caleb");
        authDAO.createAuth(authData);

        assertNotNull(authDAO.getAuth("totallyRealAuthToken"));
        authDAO.deleteAuth("totallyRealAuthToken");
        assertNull(authDAO.getAuth("totallyRealAuthToken"));
    }


    @Test
    void clearSuccess() throws DataAccessException{
        AuthData authData = new AuthData("totallyRealAuthToken", "caleb");
        authDAO.createAuth(authData);

        assertNotNull(authDAO.getAuth("totallyRealAuthToken"));
        authDAO.clear();
        assertNull(authDAO.getAuth("totallyRealAuthToken"));
    }





}
