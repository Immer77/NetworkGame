package game2023.game2023;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GUI extends Application {

    public static final int size = 20;
    public static final int scene_height = size * 20 + 100;
    public static final int scene_width = size * 20 + 200;

    public static Image image_floor;
    public static Image image_wall;
    public static Image image_chest;
    public static Image hero_right, hero_left, hero_up, hero_down;
    public static Image fire_right, fire_left, fire_up, fire_down;
    public static Image fire_horizontal, fire_vertical;
    public static Image fire_wall_east, fire_wall_north, fire_wall_south, fire_wall_west;

    public static GridPane boardGrid = new GridPane();


    public static Player Peter;
    public static Player Dan;
    public static Player Rasmus;
    public static Player Abdulahi;
    public static List<Player> players = new ArrayList<>();

    private static Label[][] fields;
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
            new packageSpawner(clientSocket).start();


            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));

            Text mazeLabel = new Text("Maze:");
            mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            Text scoreLabel = new Text("Score:");
            scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            scoreList = new TextArea();



            image_wall = new Image(getClass().getResourceAsStream("Image/wall4.png"), size, size, false, false);
            image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"), size, size, false, false);
            image_chest = new Image(getClass().getResourceAsStream("Image/boxOfPoints.png"), size, size,false,false);

            hero_right = new Image(getClass().getResourceAsStream("Image/heroRight.png"), size, size, false, false);
            hero_left = new Image(getClass().getResourceAsStream("Image/heroLeft.png"), size, size, false, false);
            hero_up = new Image(getClass().getResourceAsStream("Image/heroUp.png"), size, size, false, false);
            hero_down = new Image(getClass().getResourceAsStream("Image/heroDown.png"), size, size, false, false);

            fire_down = new Image(getClass().getResourceAsStream("Image/fireDown.png"), size, size, false, false);
            fire_horizontal = new Image(getClass().getResourceAsStream("Image/fireHorizontal.png"), size, size, false, false);
            fire_left = new Image(getClass().getResourceAsStream("Image/fireLeft.png"), size, size, false, false);
            fire_right = new Image(getClass().getResourceAsStream("Image/fireRight.png"), size, size, false, false);
            fire_up = new Image(getClass().getResourceAsStream("Image/fireUp.png"), size, size, false, false);
            fire_vertical = new Image(getClass().getResourceAsStream("Image/fireVertical.png"), size, size, false, false);
            fire_wall_east = new Image(getClass().getResourceAsStream("Image/fireWallEast.png"), size, size, false, false);
            fire_wall_west = new Image(getClass().getResourceAsStream("Image/fireWallWest.png"), size, size, false, false);
            fire_wall_north = new Image(getClass().getResourceAsStream("Image/fireWallNorth.png"), size, size, false, false);
            fire_wall_south = new Image(getClass().getResourceAsStream("Image/fireWallSouth.png"), size, size, false, false);

            fields = new Label[20][20];
            updateBoard(boardGrid);
//            for (int j = 0; j < 20; j++) {
//                for (int i = 0; i < 20; i++) {
//                    switch (board[j].charAt(i)) {
//                        case 'w':
//                            fields[i][j] = new Label("", new ImageView(image_wall));
//                            break;
//                        case ' ':
//                            fields[i][j] = new Label("", new ImageView(image_floor));
//                            break;
//                        case 'x':
//                            fields[i][j] = new Label("", new ImageView(image_chest));
//                            break;
//                        default:
//                            throw new Exception("Illegal field value: " + board[j].charAt(i));
//                    }
//                    boardGrid.add(fields[i][j], i, j);
//                }
//            }
            scoreList.setEditable(false);


            grid.add(mazeLabel, 0, 0);
            grid.add(scoreLabel, 1, 0);
            grid.add(boardGrid, 0, 1);
            grid.add(scoreList, 1, 1);

            Scene scene = new Scene(grid, scene_width, scene_height);
            primaryStage.setScene(scene);
            primaryStage.show();

            Peter = new Player("Peter", "up");
            players.add(Peter);
//            fields[9][4].setGraphic(new ImageView(hero_up));

            Rasmus = new Player("Rasmus", "up");
            players.add(Rasmus);
            fields[14][15].setGraphic(new ImageView(hero_up));

            Abdulahi = new Player("Abdulahi", "down");
            players.add(Abdulahi);
            //fields[13][14].setGraphic(new ImageView(hero_down));

            Dan = new Player("Dan", 7, 3, "down");
            players.add(Dan);
            fields[7][3].setGraphic(new ImageView(hero_down));

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                try {
                    switch (event.getCode()) {
                        case UP:
                            outToServer.writeBytes(("MOVE Dan 0 -1 up " + Dan.getXpos() + " " + Dan.getYpos() +"\n"));
                            playerMoved(Dan, 0, -1, "up", Dan.xpos, Dan.ypos);
                            outToServer.writeBytes(("POINT Dan " + Dan.getPoint() + "\n"));
                            break;
                        case DOWN:
                            outToServer.writeBytes(("MOVE Dan " + "0 +1" + " down " + Dan.getXpos() + " " + Dan.getYpos() + "\n"));
                            playerMoved(Dan, 0, +1, "down", Dan.xpos, Dan.ypos);
                            outToServer.writeBytes(("POINT Dan " + Dan.getPoint()+ "\n"));
                            break;
                        case LEFT:
                            outToServer.writeBytes(("MOVE Dan -1 0 left " + Dan.getXpos() + " " + Dan.getYpos() + " \n"));
                            playerMoved(Dan, -1, 0, "left", Dan.xpos, Dan.ypos);
                            outToServer.writeBytes(("POINT Dan " + Dan.getPoint()+ "\n"));
                            break;
                        case RIGHT:
                            outToServer.writeBytes(("MOVE Dan +1 0 right " + Dan.getXpos() + " " + Dan.getYpos() + " \n"));
                            playerMoved(Dan, +1, 0, "right", Dan.xpos, Dan.ypos);
                            outToServer.writeBytes("POINT Dan " + Dan.getPoint()+ "\n");
                            break;
                        case SPACE:
                            outToServer.writeBytes("SHOOT Dan " + Dan.getDirection() + " \n");
                            playerShoot(Dan);
                            outToServer.writeBytes("POINT Dan " + Dan.getPoint()+ "\n");
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

    public void updateBoard(GridPane gridBoard) throws Exception {
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
                    case 'x':
                        fields[i][j] = new Label("", new ImageView(image_floor));
                        break;
                    default:
                        throw new Exception("Illegal field value: " + board[j].charAt(i));
                }
                gridBoard.add(fields[i][j], i, j);
            }
        }


    }


    /*
    tjekker om der er en mur en den retning spilleren skyder
   Hvis der ikke er det, bliver der spawnet et skud.
   I while loopet søger den efter en mur,
   Indtil den finder en mur spawner den fire-horizontal/vertical.png
   når den rammer muren spawner den fireWall.png

   En spillers point bliver sat til 0 hvis man bliver skudt
     */
    public void playerShoot(Player player) {
        String playerDirection = player.direction;
        int playerX = player.getXpos();
        int playerY = player.getYpos();

        if (playerDirection.equals("right") && !(board[playerY].charAt(playerX + 1) == 'w')) {
            player.setShotFired(true);
            Player p = getPlayerAt(playerX +1, playerY);
            if (p != null && p.getPoint() > 0) {
                p.setPoint(0);
            }
            fields[playerX + 1][playerY].setGraphic(new ImageView(fire_right));

            int i = 2;
            while (board[playerY].charAt(playerX + i) != 'w') {

                p = getPlayerAt(playerX + i, playerY);
                if (p != null && p.getPoint() > 0) {
                    p.setPoint(0);
                }

                if (board[playerY].charAt(playerX + i + 1) == 'w') {

                    fields[playerX + i][playerY].setGraphic(new ImageView(fire_wall_east));
                } else {

                    fields[playerX + i][playerY].setGraphic(new ImageView(fire_horizontal));
                }

                i++;
            }
        }

        if (playerDirection.equals("left") && !(board[playerY].charAt(playerX - 1) == 'w')) {
            player.setShotFired(true);
            Player p = getPlayerAt(playerX -1, playerY);
            if (p != null && p.getPoint() > 0) {
                p.setPoint(0);
            }
            fields[playerX - 1][playerY].setGraphic(new ImageView(fire_left));

            int i = 2;
            while (board[playerY].charAt(playerX - i) != 'w') {

                p = getPlayerAt(playerX - i, playerY);
                if (p != null && p.getPoint() > 0) {
                    p.setPoint(0);
                }

                if (board[playerY].charAt(playerX - i - 1) == 'w') {
                    fields[playerX - i][playerY].setGraphic(new ImageView(fire_wall_west));
                } else {

                    fields[playerX - i][playerY].setGraphic(new ImageView(fire_horizontal));
                }

                i++;
            }
        }

        if (playerDirection.equals("up") && !(board[playerY - 1].charAt(playerX) == 'w')) {
            player.setShotFired(true);
            Player p = getPlayerAt(playerX, playerY - 1);
            if (p != null && p.getPoint() > 0) {
                p.setPoint(0);
            }
            fields[playerX][playerY - 1].setGraphic(new ImageView(fire_up));

            int i = 2;
            while (board[playerY - i].charAt(playerX) != 'w') {

                p = getPlayerAt(playerX, playerY - i);
                if (p != null && p.getPoint() > 0) {
                    p.setPoint(0);
                }

                if (board[playerY - i - 1].charAt(playerX) == 'w') {
                    fields[playerX][playerY - i].setGraphic(new ImageView(fire_wall_north));
                } else {

                    fields[playerX][playerY - i].setGraphic(new ImageView(fire_vertical));
                }

                i++;
            }
        }

        if (playerDirection.equals("down") && !(board[playerY + 1].charAt(playerX) == 'w')) {
            player.setShotFired(true);
            Player p = getPlayerAt(playerX, playerY + 1);
            if (p != null && p.getPoint() > 0) {
                p.setPoint(0);
            }
            fields[playerX][playerY + 1].setGraphic(new ImageView(fire_down));

            int i = 2;
            while (board[playerY + i].charAt(playerX) != 'w') {

                p = getPlayerAt(playerX, playerY + 1);
                if (p != null && p.getPoint() > 0) {
                    p.setPoint(0);
                }

                if (board[playerY + i + 1].charAt(playerX) == 'w') {
                    fields[playerX][playerY + i].setGraphic(new ImageView(fire_wall_south));
                } else {

                    fields[playerX][playerY + i].setGraphic(new ImageView(fire_vertical));
                }

                i++;
            }
        }
    }

    public void playerMoved(Player player, int delta_x, int delta_y, String direction, int xPos, int yPos) {
        String oldDirection = player.getDirection();
        int oldX = player.getXpos();
        int oldY = player.getYpos();
        player.direction = direction;
        player.setYpos(yPos);
        player.setXpos(xPos);
        Server.sendMessage(direction);
        int x = player.getXpos(), y = player.getYpos();

        if (board[y + delta_y].charAt(x + delta_x) == 'w') {
            player.addPoints(-1);
        } else {
            Player p = getPlayerAt(x + delta_x, y + delta_y);

            /*
            Fjern skud når man flytter sig
             */
            if (player.isShotFired()) {
                if (oldDirection.equals("right")) {
                    int i = 1;
                    while (board[oldY].charAt(oldX + i) != 'w') {
                        fields[oldX + i][oldY].setGraphic(new ImageView(image_floor));

                        i++;
                    }
                }
                if (oldDirection.equals("left")) {
                    int i = 1;
                    while (board[oldY].charAt(oldX - i) != 'w') {
                        fields[oldX - i][oldY].setGraphic(new ImageView(image_floor));

                        i++;
                    }
                }
                if (oldDirection.equals("up")) {
                    int i = 1;
                    while (board[oldY - i].charAt(oldX) != 'w') {
                        fields[oldX][oldY - i].setGraphic(new ImageView(image_floor));

                        i++;
                    }
                }
                if (oldDirection.equals("down")) {
                    int i = 1;
                    while (board[oldY + i].charAt(oldX) != 'w') {
                        fields[oldX][oldY + i].setGraphic(new ImageView(image_floor));

                        i++;
                    }
                }
                player.setShotFired(false);
            }

            if (p != null) {
                player.addPoints(10);
                p.addPoints(-10);

            } else {
                if(board[y + delta_y].charAt(x + delta_x) == 'x') {
                    player.addPoints(20);
                    fields[x][y].setGraphic(new ImageView(image_floor));
                    x += delta_x;
                    y += delta_y;
                }
                player.addPoints(1);

                fields[x][y].setGraphic(new ImageView(image_floor));
                x += delta_x;
                y += delta_y;

                if (direction.equals("right")) {
                    fields[x][y].setGraphic(new ImageView(hero_right));
                }

                if (direction.equals("left")) {
                    fields[x][y].setGraphic(new ImageView(hero_left));
                }

                if (direction.equals("up")) {
                    fields[x][y].setGraphic(new ImageView(hero_up));
                }

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


    class packageSpawner extends Thread{
        Socket connSocket;

        public packageSpawner(Socket connSocket){
            this.connSocket = connSocket;
        }

        public void run(){
            try {
                Random rand = new Random();
                while (true){
                    Thread.sleep(20000);
                    DataOutputStream spawnPackages = new DataOutputStream(connSocket.getOutputStream());
                    int x = rand.nextInt(18);
                    int y = rand.nextInt(18);
                    if(board[y].charAt(x) == ' '){
                        spawnPackages.writeBytes("Package " + x + " " + y +"\n");
                    }
                }


            }catch (IOException ie){

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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


                    if (clientInfo[0].equals("MOVE")) {
                        switch (clientInfo[1]) {
                            case "Peter" -> Platform.runLater(() -> playerMoved(Peter, Integer.parseInt(clientInfo[2]), Integer.parseInt(clientInfo[3]), clientInfo[4], Integer.parseInt(clientInfo[5]), Integer.parseInt(clientInfo[6])));
                            case "Rasmus" -> Platform.runLater(() -> playerMoved(Rasmus, Integer.parseInt(clientInfo[2]), Integer.parseInt(clientInfo[3]), clientInfo[4], Integer.parseInt(clientInfo[5]), Integer.parseInt(clientInfo[6])));
                            case "Abdulahi" -> Platform.runLater(() -> playerMoved(Abdulahi, Integer.parseInt(clientInfo[2]), Integer.parseInt(clientInfo[3]), clientInfo[4], Integer.parseInt(clientInfo[5]), Integer.parseInt(clientInfo[6])));
                        }
                    } else if (clientInfo[0].equals("POINT")) {
                        switch (clientInfo[1]) {
                            case "Peter" -> Platform.runLater(() -> Peter.setPoint(Integer.parseInt(clientInfo[2])));
                            case "Rasmus" -> Platform.runLater(() -> Rasmus.setPoint(Integer.parseInt(clientInfo[2])));
                            case "Abdulahi" -> Platform.runLater(() -> Abdulahi.setPoint(Integer.parseInt(clientInfo[2])));
                        }
                    } else if (clientInfo[0].equals("SHOOT")) {
                        switch (clientInfo[1]) {
                            case "Peter" -> Platform.runLater(() -> playerShoot(Peter));
                            case "Rasmus" -> Platform.runLater(() -> playerShoot(Rasmus));
                            case "Abdulahi" -> Platform.runLater(() -> playerShoot(Abdulahi));
                        }
                    }else if(clientInfo[0].equalsIgnoreCase("Package")){
                        Platform.runLater(() -> fields[Integer.parseInt(clientInfo[1])][Integer.parseInt(clientInfo[2])].setGraphic(new ImageView(image_chest)));

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            // do the work here
        }

    }


}

