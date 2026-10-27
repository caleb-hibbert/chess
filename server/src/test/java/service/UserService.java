package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import model.AuthData;
import java.util.UUID;


public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }


    private void checkRegisterRequestData(RegisterRequest registerRequest) throws DataAccessException{
        if (registerRequest == null ||
                registerRequest.username() == null || registerRequest.username().isBlank() ||
                registerRequest.email() == null || registerRequest.email().isBlank() ||
                registerRequest.password() == null || registerRequest.password().isBlank()){
            throw new DataAccessException("The email, username, or password submitted was empty");//TODO - change to different exception type later
        }
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        checkRegisterRequestData(registerRequest);


        UserData userData = userDAO.getUser(registerRequest.username());
        if (userData != null){
            throw new DataAccessException("The requested username is already is use");//TODO - change to different exception type later
        }

        //if not in use, create user + add to db
        userDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        String authToken = generateToken();
        AuthData authData = new AuthData(authToken, registerRequest.username());
        authDAO.createAuth(authData);
        //generate authToken, associate w/ this user
        //return RegisterResult containing username and auth token
        return new RegisterResult(registerRequest.username(),authToken);



    }
//    public LoginResult login(LoginRequest loginRequest) {
//
//    }
//    public void logout(LogoutRequest logoutRequest) {
//
//    }


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}