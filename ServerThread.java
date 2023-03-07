package game2023.game2023;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread {
    // Skal tage en socket så der kan oprettes flere forbindelser til serveren
    Socket socket;
    String clientSentence;


    public ServerThread(Socket connSocket) throws IOException {
        this.socket = connSocket;
    }

    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true){
                clientSentence = inFromClient.readLine();
                System.out.println(clientSentence);
                //outToClient.writeBytes(clientSentence + '\n' );
                //sendMessageToServer(clientSentence);
                //Kald serverMetode
                Server.sendMessage(clientSentence);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // do the work here
    }

    public void sendMessage(String message){
        try {
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            outToClient.writeBytes(message + "\n");

        }catch (IOException io){

        }
    }

}
