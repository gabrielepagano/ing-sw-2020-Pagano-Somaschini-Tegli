package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.RemoteViewDummy;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static it.polimi.ingsw.model.Constants.COLUMNS;
import static it.polimi.ingsw.model.Constants.ROWS;

/**
 * Factory class for Board, used for Dependency Injection when creating a new game
 */
public class BoardFactory {

    private BoardFactory() {
        // Private constructor will prevent the instantiation of this class
    }

    public static Board buildBoardWithoutWorkersFromFile(String resourcePath) {

        Board boardWithoutWorkers = new Board();

        ClassLoader classLoader = BoardFactory.class.getClassLoader();
        Scanner resourceScanner = new Scanner(classLoader.getResourceAsStream(resourcePath));

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {

                Tile currentTile = boardWithoutWorkers.getTile(r, c);
                int level = 0;
                boolean isDomed = false;

                if(resourceScanner.hasNextLine()) {
                    String line = resourceScanner.nextLine();
                    level = line.charAt(0) - '0';
                    isDomed = line.charAt(1) == 'D';
                }

                currentTile.setLevel(ETileLevel.values()[level]);
                currentTile.setDomed(isDomed);

            }
        }

        return boardWithoutWorkers;
    }

    public static void generateRandomBoard(){
        File randomBoard = new File("boardRand.txt");
        String p = randomBoard.getAbsolutePath().replaceAll("boardRand.txt", "");
        Path PATH = Paths.get(p + "\\src\\test\\resources\\boardRand.txt");

        try (BufferedWriter out = new BufferedWriter(new FileWriter(PATH.toString()))) {
            Random rand = new Random();

            int tileLevel;
            for(int i = 0; i < ROWS*COLUMNS; i++) {
                tileLevel = rand.nextInt(4);

                switch (tileLevel){
                    case 0:
                        out.write("0-");
                        out.newLine();
                        break;
                    case 1:
                        out.write("1-");
                        out.newLine();
                        break;
                    case 2:
                        out.write("2-");
                        out.newLine();
                        break;
                    case 3:
                        int dome = rand.nextInt(100);
                        if(dome < 66) {
                            out.write("3-");
                            out.newLine();
                        }
                        else {
                            out.write("3D");
                            out.newLine();
                        }
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Player> generatePlayers(int nPlayers){
        List<Player> testPlayers = new ArrayList<>();

        for(int i = 0; i < nPlayers; i++){
            testPlayers.add(new Player(new RemoteViewDummy("Bot" + i)));
        }

        return testPlayers;
    }

    public static Board buildEmptyBoardFromRemoteViewsList(List<RemoteView> remoteViews) {
        Board board = new Board();
        board.registerObserversOnTiles(remoteViews);
        return board;
    }

}
