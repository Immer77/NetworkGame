package game2023.game2023;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TCPClient {

    public static void main(String argv[]) throws Exception {
        String sentence;
        String modifiedSentence;


        try {
            Socket clientSocket = new Socket("localhost", 1026);
            //new ServerThread(clientSocket).start();
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            // Input og outputstream

            // Løser en linje som den sender ud
            sentence = inFromUser.readLine();
            // SKAL ALTID STARTE PÅ EN NY LINJE DERFOR \n writebytes skal bruges til at sende beskeden afsted
            outToServer.writeBytes(sentence + '\n');
            // Her står den så og venter på at serveren sender noget tilbage. inde i serverthread hvor den venter på at klienten har sendt noget
            modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);
            new SendThread(clientSocket).start();

        } catch (ConnectException connectException) {

        }

    }


}
