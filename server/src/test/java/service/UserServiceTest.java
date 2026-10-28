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
    void registerSuccessTest() throws DataAccessException{
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
    void registerEmptyInfo() throws InvalidRequestException, AlreadyTakenException{
//        assertThrows(InvalidRequestException.class, () -> {
//            RegisterRequest request = new RegisterRequest("", "", "");
//
//        })
        assertAll(() -> assertThrows(InvalidRequestException.class, () -> {
            RegisterRequest request = new RegisterRequest("", "mypassword", "caleb@byu.edu");
            userService.register(request);
            }),
            () -> assertThrows(InvalidRequestException.class, () -> {
                RegisterRequest request = new RegisterRequest("caleb", "", "caleb@byu.edu");
                userService.register(request);
            }),
            () -> assertThrows(InvalidRequestException.class, () ->{
                RegisterRequest request = new RegisterRequest("caleb", "mypassword", "");
                userService.register(request);
            })
        );
    }

    @Test
    void loginSuccessTest() throws InvalidRequestException, AlreadyTakenException, DataAccessException{
        RegisterRequest registerRequest = new RegisterRequest("caleb", "mypassword", "caleb@byu.edu");
        RegisterResult registerResult = userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("caleb", "mypassword");
    }

}
