package game2023.game2023;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        int count = 0;
        Socket[] sockets = new Socket[4];
        ArrayList<RecieveThread> recieves = new ArrayList<>();
        SendThread[] sends = new SendThread[4];
        // En serversocket er en socket hvor serveren lytter på. (Her på port 1026)
        ServerSocket welcomeSocket = new ServerSocket(1026);

        Socket socket = welcomeSocket.accept();
        RecieveThread recieveThread = new RecieveThread(socket);
        SendThread sendThread = new SendThread(socket);

        // Hver gang der oprettes en connection.
        while(true) {

            Socket connectionSocket = welcomeSocket.accept();
            RecieveThread recieveThread1 = new RecieveThread(connectionSocket);
            recieveThread1.start();
            recieves.add(recieveThread1);
        }
    }
}
