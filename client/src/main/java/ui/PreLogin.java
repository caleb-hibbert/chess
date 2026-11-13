package ui;
import client.ServerFacade;
import client.State;
import exception.ResponseException;

import java.util.Scanner;

public class PreLogin {
    private final ServerFacade facade;
    private final Scanner scanner;
    private State state = State.SIGNEDOUT;

    public PreLogin(ServerFacade facade){
        this.facade = facade;
        this.scanner = new Scanner(System.in);
    }

    public void run(){
        boolean running = true;
        while (running){
            String input = scanner.nextLine().trim();

            switch (input){
                case "Register" -> register();
                case "Login" -> login();
                case "Help" -> help();
                case "Quit" -> quit();
                default -> System.out.println("Invalid input");
            }
        }
    }






    private void printOptions(){
        System.out.println("\nWelcome to 240 chess. Type Help to get started.\n");
        System.out.println("Register <USERNAME> <PASSWORD> <EMAIL> - Create an account");
        System.out.println("Login <USERNAME> <PASSWORD> - Play or watch chess games");
        System.out.println("Help - Get help with possible commands");
        System.out.println("Quit - Quit the chess program");
    }


    private void login(){
        System.out.println("Username: ");
        String username = scanner.nextLine().trim();
        System.out.println("Password: ");
        String password = scanner.nextLine().trim();
    }


    private void register(){

    }


    private void help(){

    }


    private void quit(){

    }











    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(401, "You must sign in");
        }
    }

}
