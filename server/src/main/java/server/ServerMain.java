//package server;
//
//import chess.*;
//
//public class ServerMain {
//    public static void main(String[] args) {
//        var server = new Server();
//        server.run(8080);
//    }
//}



package server;

public class ServerMain {
    public static void main(String[] args) {
        var port = 8080;
        Server server = new Server();
        server.run(port);

        System.out.println("♕ 240 Chess Server running on port " + port);
    }
}