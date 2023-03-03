package game2023.game2023;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // En serversocket er en socket hvor serveren lytter på. (Her på port 1026)
        ServerSocket welcomeSocket = new ServerSocket(1026);


        // Hver gang der oprettes en connection.
        Socket connectionSocket = welcomeSocket.accept();
        new RecieveThread(connectionSocket).start();
        new SendThread(connectionSocket).start();

    }

}
