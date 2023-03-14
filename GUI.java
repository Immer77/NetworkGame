package game2023.game2023;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

public class GUI extends Application {

    public static final int size = 20;
    public static final int scene_height = size * 20 + 100;
    public static final int scene_width = size * 20 + 200;

    public static Image image_floor;
    public static Image image_wall;
    public static Image hero_right, hero_left, hero_up, hero_down;

    public static boolean gameReady = false;
    public static Player Peter;
    public static Player Dan;
    public static Player Rasmus;
    public static Player Abdulahi;
    public static List<Player> players = new ArrayList<>();

    private Label[][] fields;
    private TextArea scoreList;

    private String[] board = {    // 20x20
            "wwwwwwwwwwwwwwwwwwww",
            "w        ww        w",
            "w w  w  www w  w  ww",
            "w w  w   ww w  w  ww",
            "w  w               w",
            "w w w w w w w  w  ww",
            "w w     www w  w  ww",
            "w w     w w w  w  ww",
            "w   w w  w  w  w   w",
            "w     w  w  w  w   w",
            "w ww ww        w  ww",
            "w  w w    w    w  ww",
            "w        ww w  w  ww",
            "w         w w  w  ww",
            "w        w     w  ww",
            "w  w              ww",
            "w  w www  w w  ww ww",
            "w w      ww w     ww",
            "w   w   ww  w      w",
            "wwwwwwwwwwwwwwwwwwww"
    };

    // -------------------------------------------
    // | Maze: (0,0)              | Score: (1,0) |
    // |-----------------------------------------|
    // | boardGrid (0,1)          | scorelist    |
    // |                          | (1,1)        |
    // -------------------------------------------

    @Override
    public void start(Stage primaryStage) {
        try {

            //Establish connection

            Socket clientSocket = new Socket("localhost", 1026);
            //Socket clientSocket = new Socket("10.10.138.121", 1026);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());


            new RecieverThread(clientSocket).start();
            new SendThread(clientSocket).start();


            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));

            Text mazeLabel = new Text("Maze:");
            mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            Text scoreLabel = new Text("Score:");
            scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            scoreList = new TextArea();

            GridPane boardGrid = new GridPane();

            image_wall = new Image(getClass().getResourceAsStream("Image/wall4.png"), size, size, false, false);
            image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"), size, size, false, false);

            hero_right = new Image(getClass().getResourceAsStream("Image/heroRight.png"), size, size, false, false);
            hero_left = new Image(getClass().getResourceAsStream("Image/heroLeft.png"), size, size, false, false);
            hero_up = new Image(getClass().getResourceAsStream("Image/heroUp.png"), size, size, false, false);
            hero_down = new Image(getClass().getResourceAsStream("Image/heroDown.png"), size, size, false, false);

            fields = new Label[20][20];
            for (int j = 0; j < 20; j++) {
                for (int i = 0; i < 20; i++) {
                    switch (board[j].charAt(i)) {
                        case 'w':
                            fields[i][j] = new Label("", new ImageView(image_wall));
                            break;
                        case ' ':
                            fields[i][j] = new Label("", new ImageView(image_floor));
                            break;
                        default:
                            throw new Exception("Illegal field value: " + board[j].charAt(i));
                    }
                    boardGrid.add(fields[i][j], i, j);
                }
            }
            scoreList.setEditable(false);


            grid.add(mazeLabel, 0, 0);
            grid.add(scoreLabel, 1, 0);
            grid.add(boardGrid, 0, 1);
            grid.add(scoreList, 1, 1);

            Scene scene = new Scene(grid, scene_width, scene_height);
            primaryStage.setScene(scene);
            primaryStage.show();

            Peter = new Player("Peter", 9, 4, "up");
            players.add(Peter);
            fields[9][4].setGraphic(new ImageView(hero_up));

            Rasmus = new Player("Rasmus", 14, 15, "up");
            players.add(Rasmus);
            //fields[14][15].setGraphic(new ImageView(hero_up));

            Abdulahi = new Player("Abdulahi", 13, 14, "down");
            players.add(Abdulahi);
            //fields[13][14].setGraphic(new ImageView(hero_down));

            Dan = new Player("Dan", 7, 3, "down");
            players.add(Dan);
            //fields[7][3].setGraphic(new ImageView(hero_down));

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                try {
                    switch (event.getCode()) {
                        case UP:
                            outToServer.writeBytes(("Peter 0 -1 up " + Peter.getXpos() + " " + Peter.getYpos() +"\n"));
                            playerMoved(Peter, 0, -1, "up", Peter.xpos, Peter.ypos);
                            break;
                        case DOWN:
                            outToServer.writeBytes(("Peter " + "0 +1" + " down " + Peter.getXpos() + " " + Peter.getYpos() + "\n"));
                            playerMoved(Peter, 0, +1, "down", Peter.xpos, Peter.ypos);
                            break;
                        case LEFT:
                            outToServer.writeBytes(("Peter -1 0 left " + Peter.getXpos() + " " + Peter.getYpos() + " \n"));
                            playerMoved(Peter, -1, 0, "left", Peter.xpos, Peter.ypos);
                            break;
                        case RIGHT:
                            outToServer.writeBytes(("Peter +1 0 right " + Peter.getXpos() + " " + Peter.getYpos() + " \n"));
                            playerMoved(Peter, +1, 0, "right", Peter.xpos, Peter.ypos);
                            break;
                        default:
                            break;

                    }

                } catch (IOException ioException) {

                }

            });

            // Setting up standard players


            scoreList.setText(getScoreList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playerMoved(Player player, int delta_x, int delta_y, String direction, int xPos, int yPos) {
        player.direction = direction;
        player.setYpos(yPos);
        player.setXpos(xPos);
        Server.sendMessage(direction);
        int x = player.getXpos(), y = player.getYpos();

        if (board[y + delta_y].charAt(x + delta_x) == 'w') {
            player.addPoints(-1);
        } else {
            Player p = getPlayerAt(x + delta_x, y + delta_y);
            if (p != null) {
                player.addPoints(10);
                p.addPoints(-10);
            } else {
                player.addPoints(1);

                fields[x][y].setGraphic(new ImageView(image_floor));
                x += delta_x;
                y += delta_y;

                if (direction.equals("right")) {
                    fields[x][y].setGraphic(new ImageView(hero_right));
                }
                ;
                if (direction.equals("left")) {
                    fields[x][y].setGraphic(new ImageView(hero_left));
                }
                ;
                if (direction.equals("up")) {
                    fields[x][y].setGraphic(new ImageView(hero_up));
                }
                ;
                if (direction.equals("down")) {
                    fields[x][y].setGraphic(new ImageView(hero_down));
                }
                ;


                player.setXpos(x);
                player.setYpos(y);
            }
        }
        scoreList.setText(getScoreList());
    }

    public String getScoreList() {
        StringBuffer b = new StringBuffer(100);
        for (Player p : players) {
            b.append(p + "\r\n");
        }
        return b.toString();
    }

    public Player getPlayerAt(int x, int y) {
        for (Player p : players) {
            if (p.getXpos() == x && p.getYpos() == y) {
                return p;
            }
        }
        return null;
    }


    class RecieverThread extends Thread {
        Socket connSocket;
        String clientSentence;

        public RecieverThread(Socket connSocket) {
            this.connSocket = connSocket;
        }

        public void run() {
            try {
                // Venter på at den kommer in from client før den kan sende den afsted til serveren
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
                // Når der er modtaget en besked får den sendt den afsted til serveren
                // Do the work and the communication with the client here
                while (true) {

                    clientSentence = inFromClient.readLine();
                    System.out.println(clientSentence);
                    String[] clientInfo = clientSentence.split(" ");
                    switch (clientInfo[0]) {
                        case "Dan" -> Platform.runLater(() -> playerMoved(Dan, Integer.parseInt(clientInfo[1]), Integer.parseInt(clientInfo[2]), clientInfo[3], Integer.parseInt(clientInfo[4]), Integer.parseInt(clientInfo[5])));
                        case "Rasmus" -> Platform.runLater(() -> playerMoved(Rasmus, Integer.parseInt(clientInfo[1]), Integer.parseInt(clientInfo[2]), clientInfo[3], Integer.parseInt(clientInfo[4]), Integer.parseInt(clientInfo[5])));
                        case "Abdulahi" -> Platform.runLater(() -> playerMoved(Abdulahi, Integer.parseInt(clientInfo[1]), Integer.parseInt(clientInfo[2]), clientInfo[3], Integer.parseInt(clientInfo[4]), Integer.parseInt(clientInfo[5])));
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            // do the work here
        }

    }


}

