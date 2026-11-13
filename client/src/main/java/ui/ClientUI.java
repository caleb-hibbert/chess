package ui;
import client.ServerFacade;
import client.State;
import exception.ResponseException;
import model.AuthData;

import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;




public class ClientUI {
    private final ServerFacade facade;
    private State state = State.SIGNEDOUT;
    private String username = null;
    private AuthData authData;
    public static final String RESET_TEXT_COLOR_DEFAULT = "\u001B[0m";


    public ClientUI(ServerFacade facade){
        this.facade = facade;
    }

    public void run(){
        System.out.println("\nWelcome to 240 chess. Type Help to get started.\n");
        System.out.println(help());
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")){
            printPrompt();
            String input = scanner.nextLine().trim();

            try{
                result = eval(input);
                System.out.print(result);
            }
            catch (Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    public String eval(String input){
        try{
            String[] tokens = input.toLowerCase().split(" ");
            String operation;
            if (tokens.length > 0){
                operation = tokens[0];
            }
            else{
                operation = "help";
            }
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (operation){
                case "register" -> {
                    if (params.length != 3){
                        yield "Incorrect number of arguments entered for register - use the format \"Register <USERNAME> <PASSWORD> <EMAIL>\"";
                    }
                    yield register(params[0], params[1], params[2]);
                }
                case "login" -> {
                    if (params.length != 2){
                        yield "Incorrect number of arguments entered for login - use the format \"Login <USERNAME> <PASSWORD>\"";
                    }
                    yield login(params[0], params[1]);
                }
                case "help" -> help();
                case "quit" -> "quit";

                default -> help();
            };
        }
        catch (ResponseException ex){
            return ex.getMessage();
        }
    }







    public String register(String username, String password, String email) throws ResponseException {
        assertSignedOut();
        this.authData = facade.register(username, password, email);
        this.username = username;
        return String.format("You're signed as %s", username);
    }




    public String login(String username, String password){
        return "buh";
    }


    public String logout(){
        return "buh";
    }





    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    Register <USERNAME> <PASSWORD> <EMAIL> - Create an account
                    Login <USERNAME> <PASSWORD> - Play or watch chess games
                    Help - Get help with possible commands
                    Quit - Quit the chess program
                    
                    Select an option:
                    """;
        }
        return """
                Logout - Sign out of the Chess program
                List - List all chess games
                Create - Create a new chess game
                Join <WHITE|BLACK> <GAME ID> - Join a specific chess game
                Help - Get help with possible commands
                Quit - Quit the chess program
                """;
    }


    private void printPrompt() {
        if (state == State.SIGNEDOUT){
            System.out.print("\n" + RESET_TEXT_COLOR_DEFAULT +"[LOGGED_OUT] " + ">>> " + SET_TEXT_COLOR_GREEN);
        }
        else{
            System.out.print("\n" + RESET_TEXT_COLOR_DEFAULT +"[LOGGED_IN] " + ">>> " + SET_TEXT_COLOR_GREEN);
        }
    }


    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(401, "You must sign in");
        }
    }


    private void assertSignedOut() throws ResponseException {
        if (state == State.SIGNEDIN) {
            throw new ResponseException(403, "You need to log out before performing that operation");
        }
    }

}
