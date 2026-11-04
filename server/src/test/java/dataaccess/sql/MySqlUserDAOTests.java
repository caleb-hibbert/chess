package dataaccess.sql;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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



}
