package game2023.game2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RecieveThread extends Thread {
    // Skal tage en socket så der kan oprettes flere forbindelser til serveren
    Socket connSocket;
    String clientSentence;

    public RecieveThread(Socket connSocket) {
        this.connSocket = connSocket;
    }

    public void run() {
        try {
            // Venter på at den kommer in from client før den kan sende den afsted til serveren
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
            // Når der er modtaget en besked får den sendt den afsted til serveren
            // Do the work and the communication with the client here
            while (true){
                clientSentence = inFromClient.readLine();
                System.out.println(clientSentence);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // do the work here
    }


}
