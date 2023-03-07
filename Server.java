package game2023.game2023;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ArrayList<ServerThread> clients = new ArrayList<>();
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // En serversocket er en socket hvor serveren lytter på. (Her på port 1026)
        ServerSocket welcomeSocket = new ServerSocket(1026);


        // Hver gang der oprettes en connection.
        while (true){
            Socket connectionSocket = welcomeSocket.accept();
            ServerThread serverThread = new ServerThread(connectionSocket);
            clients.add(serverThread);
            serverThread.start();
        }

    }

    public static void sendMessage(String message){
        for(ServerThread s : clients){
            s.sendMessage(message);
            System.out.println("I serveren" + message);

        }
    }



}
