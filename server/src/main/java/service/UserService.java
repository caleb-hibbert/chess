package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;


public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }


    private void checkAuthData(String authToken) throws InvalidRequestException, UnauthorizedException, DataAccessException{
        if (authToken == null || authToken.isBlank()){
            throw new InvalidRequestException("Error: bad request");
        }
        if (authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
    }


    private void checkRegisterRequestData(RegisterRequest registerRequest) throws InvalidRequestException{
        if (registerRequest == null ||
                registerRequest.username() == null || registerRequest.username().isBlank() ||
                registerRequest.email() == null || registerRequest.email().isBlank() ||
                registerRequest.password() == null || registerRequest.password().isBlank()){
            throw new InvalidRequestException("Error: bad request");
        }
    }


    public RegisterResult register(RegisterRequest registerRequest) throws InvalidRequestException, DataAccessException, AlreadyTakenException{
        checkRegisterRequestData(registerRequest);
        UserData userData = userDAO.getUser(registerRequest.username());

        if (userData != null){
            throw new AlreadyTakenException("Error: already taken");//requested username already in use
        }
        String hashedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        userDAO.createUser(new UserData(registerRequest.username(), hashedPassword, registerRequest.email()));
        String authToken = generateToken();
        AuthData authData = new AuthData(authToken, registerRequest.username());
        authDAO.createAuth(authData);// generate authToken, associate w/ this user

        return new RegisterResult(registerRequest.username(),authToken);
    }


    private void checkLoginRequestData(LoginRequest loginRequest) throws InvalidRequestException{
        if (loginRequest == null ||
                loginRequest.username() == null || loginRequest.username().isBlank() ||
                loginRequest.password() == null || loginRequest.password().isBlank()){
            throw new InvalidRequestException("Error: bad request");
        }
    }


    public LoginResult login(LoginRequest loginRequest) throws InvalidRequestException, DataAccessException{
        checkLoginRequestData(loginRequest);

        UserData userData = userDAO.getUser(loginRequest.username());
        if (userData == null){
            throw new UnauthorizedException("Error: unauthorized");//username that's trying to be logged in to doesn't exist
        }
        String hashedPassword = userDAO.getPasswordHash(loginRequest.username());
        String providedClearTextPassword = loginRequest.password();

        if (BCrypt.checkpw(providedClearTextPassword, hashedPassword)){
            // ^ if provided un/pw match hashed db un/pw, they're good to go
            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, loginRequest.username());
            authDAO.createAuth(authData);// generate authToken, associate w/ this user
            return new LoginResult(loginRequest.username(), authToken);
        }
        else{
            throw new UnauthorizedException("Error: unauthorized");
        }
    }


    public void logout(LogoutRequest logoutRequest) throws InvalidRequestException, UnauthorizedException, DataAccessException{
        checkAuthData(logoutRequest.authToken());
        authDAO.deleteAuth(logoutRequest.authToken());
    }


    public void clear() throws DataAccessException{
        authDAO.clear();
        userDAO.clear();
    }


    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
