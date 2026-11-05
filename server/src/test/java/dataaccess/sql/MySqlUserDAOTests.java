package dataaccess.sql;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AlreadyTakenException;
import service.InvalidRequestException;
import service.RegisterRequest;
import service.RegisterResult;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


public class MySqlUserDAOTests{

    private UserDAO userDAO;

    @BeforeEach
    void initializeTests() throws DataAccessException {
        userDAO = new MySqlUserDAO();
        userDAO.clear();
    }

    @Test
    void createAndGetUserSuccess() throws DataAccessException{
        UserData user = new UserData("caleb", "myhashedpassword", "caleb@byu.edu");
        userDAO.createUser(user);
        UserData userData = userDAO.getUser(user.username());

        assertNotNull(userData);
        assertEquals("caleb", userData.username());
        assertEquals("myhashedpassword", userData.password());
        assertEquals("caleb@byu.edu", user.email());
    }

    @Test
    void registerDuplicateUsername() throws DataAccessException{
        UserData user = new UserData("caleb", "myhashedpassword", "caleb@byu.edu");
        userDAO.createUser(user);
        assertThrows(DataAccessException.class, ()-> userDAO.createUser(user));
    }



    @Test
    void getUserNotFound() throws DataAccessException{
        assertNull(userDAO.getUser("emptyUser"));
    }


    @Test
    void storeRetrieveHashedPassword() throws DataAccessException{
        UserData user = new UserData("caleb", "myhashedpassword", "caleb@byu.edu");
        userDAO.createUser(user);

        assertEquals("myhashedpassword", userDAO.getPasswordHash("caleb"));
    }


    @Test
    void failRetrieveHashedpassword() throws DataAccessException{
        UserData user = new UserData("caleb", "myhashedpassword", "caleb@byu.edu");
        userDAO.createUser(user);

        assertNull(userDAO.getPasswordHash("nonExistingUser"));
    }



    @Test
    void clearSuccess() throws DataAccessException{
        UserData user = new UserData("caleb", "myhashedpassword", "caleb@byu.edu");
        userDAO.createUser(user);

        assertNotNull(userDAO.getUser("caleb"));
        userDAO.clear();
        assertNull(userDAO.getUser("caleb"));
    }

}
