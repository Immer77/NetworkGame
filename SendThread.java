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
            DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());

            while (true){
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));
                sentence = inFromClient.readLine();
                outToClient.writeBytes(sentence + "\n");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        // do the work here
    }
}