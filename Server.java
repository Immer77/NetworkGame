package game2023.game2023;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        int count = 0;
        Socket[] sockets = new Socket[4];
        // En serversocket er en socket hvor serveren lytter på. (Her på port 1026)
        ServerSocket welcomeSocket = new ServerSocket(1026);


        // Hver gang der oprettes en connection.

        while(true) {
            sockets[count] = welcomeSocket.accept();
            count++;
            for (int i = 0; i < count; i++) {
                new RecieveThread(sockets[i]).start();
                new SendThread(sockets[i]).start();
            }
        }
    }
}
