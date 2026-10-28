package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import model.UserData;
import model.AuthData;
import java.util.UUID;


public class UserServiceTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;

    @BeforeEach
    void initializeTests(){
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    void registerTest() throws DataAccessException{
        RegisterRequest request = new RegisterRequest("caleb", "mypassword", "caleb@byu.edu");
        RegisterResult result = userService.register(request);
        assertEquals("caleb", result.username());
        assertNotNull(result.authToken());

        UserData testUser = userDAO.getUser(request.username());
        assertEquals("caleb", testUser.username());
        assertEquals("mypassword", testUser.password());
        assertEquals("caleb@byu.edu", testUser.email());
        AuthData testUserAuthData = authDAO.getAuth(result.authToken());
        assertEquals("caleb", testUserAuthData.username());
    }

    @Test
    void registerDuplicateUsername() throws DataAccessException{
        RegisterRequest request = new RegisterRequest("caleb", "mypassword", "caleb@byu.edu");
        RegisterResult result = userService.register(request);

        assertThrows(AlreadyTakenException.class, () -> {
            RegisterRequest request2 = new RegisterRequest("caleb", "mypassword2", "caleb2@byu.edu");
            userService.register(request2);
        });
    }

    @Test
    void registerEmptyInfo() throws InvalidRequestException, DataAccessException, AlreadyTakenException{
        assertThrows(InvalidRequestException.class, () -> {
            RegisterRequest request = new RegisterRequest("", "", "");
            
        })
    }

//    @Test
//    public void simpleAssertionTest() {
//        assertEquals(200, 100 + 100);
//        assertTrue(100 == 2 * 50);
//        assertNotNull(new Object(), "Response did not return authentication String");
//        assertThrows(InvalidArgumentException.class, () -> {
//            throw new InvalidArgumentException();
//        });
//    }


}
