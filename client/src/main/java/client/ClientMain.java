package client;

import chess.*;
import ui.ClientUI;



public class ClientMain {
    public static void main(String[] args) {
        int port = 8080;
        client.ServerFacade facade = new ServerFacade(port);
        ClientUI client = new ClientUI(facade);
        client.run();
    }
}
