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

        //if not in use, create user + add to db
        userDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
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

        if (loginRequest.password().equals(userDAO.getPassword(loginRequest.username()))){// if provided un/pw match db un/pw, they're good to go
            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, loginRequest.username());
            authDAO.createAuth(authData);// generate authToken, associate w/ this user
            return new LoginResult(loginRequest.username(), authToken);
        }
        else{
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

//    private void checkLogoutRequestData(LogoutRequest logoutRequest){
//        if (logoutRequest.authToken() == null ||
//                logoutRequest.authToken().isBlank()){
//            throw new InvalidRequestException("Error: bad request");
//        }
//    }
    private void checkAuthData(String authToken) throws InvalidRequestException, UnauthorizedException, DataAccessException{
        if (authToken == null || authToken.isBlank()){
            throw new InvalidRequestException("Error: bad request");

        }
        if (authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public void logout(LogoutRequest logoutRequest) throws InvalidRequestException, UnauthorizedException, DataAccessException{
        checkAuthData(logoutRequest.authToken());
//        checkLogoutRequestData(logoutRequest);
//
//        AuthData authData = authDAO.getAuth(logoutRequest.authToken());
//        if (authData == null){
//            throw new UnauthorizedException("Error: unauthorized");
//        }
        authDAO.deleteAuth(logoutRequest.authToken());

    }

    public void clear() throws DataAccessException{
        authDAO.clear();
        userDAO.clear();
    }


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}