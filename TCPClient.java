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
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        // Server IP og serverens port og så går man tilbage til server klassen hvor Accept metoden bliver kaldt

        Map<String, String> dns = new HashMap<>();
        dns.put("Peter", "localhost");
        dns.put("Rasmus","10.10.131.166");
        dns.put("dan", "10.10.138.2");



        System.out.println("Who do you wanna connect to?");
        Scanner connection = new Scanner(System.in);
        String key = connection.next();
        if(dns.containsKey(key)){
            try {
                System.out.println("Connected to: " + key + " On Address:" + dns.get(key));
                Socket clientSocket = new Socket(dns.get(key), 1026);
                // Input og outputstream
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Løser en linje som den sender ud
                sentence = inFromUser.readLine();
                // SKAL ALTID STARTE PÅ EN NY LINJE DERFOR \n writebytes skal bruges til at sende beskeden afsted
                outToServer.writeBytes(sentence + '\n');
                // Her står den så og venter på at serveren sender noget tilbage. inde i serverthread hvor den venter på at klienten har sendt noget
                modifiedSentence = inFromServer.readLine();
                System.out.println("FROM SERVER: " + modifiedSentence);

                new RecieveThread(clientSocket).start();
                new SendThread(clientSocket).start();
            }catch (ConnectException connectException){
                System.out.println("Couldn't connect to host " + key);
            }

        }else{
            System.out.println("Not in the DNS Table");
        }



    }
}


