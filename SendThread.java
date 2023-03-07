package game2023.game2023;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SendThread extends Thread{
    Socket connSocket;
    String sentence;

    public SendThread(Socket connSocket) {
        this.connSocket = connSocket;
    }

    public void run() {
        try {
            // Her er 2 streams

            // Når der er modtaget en besked får den sendt den afsted til serveren

            DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());

            while (true){
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));
                sentence = inFromClient.readLine();
                outToClient.writeBytes(sentence + "\n");
            }
            // Do the work and the communication with the client here
            // The following two lines are only an example


        } catch (IOException e) {
            e.printStackTrace();
        }
        // do the work here
    }
}
