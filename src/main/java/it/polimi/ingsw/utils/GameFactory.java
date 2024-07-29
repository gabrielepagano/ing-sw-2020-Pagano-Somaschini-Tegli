package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.RemoteViewDummy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Factory class for Game, used for Dependency Injection when creating a new game
 */
public class GameFactory {

    private GameFactory() {
        // Private constructor will prevent the instantiation of this class
    }

    public static Game buildEmptyGameFromRemoteViewsList(List<RemoteView> remoteViews) {

        // Build players list
        List<Player> players = new ArrayList<>();
        for(RemoteView rv : remoteViews) {
            Player p = new Player(rv);
            players.add(p);
            for(Worker w : p.getWorkers()) {
                w.registerObservers(remoteViews);
            }
        }

        // Build empty board
        Board board = BoardFactory.buildEmptyBoardFromRemoteViewsList(remoteViews);

        Game game = new Game(players, board);
        game.registerObservers(remoteViews);

        return game;

    }

    public static Game buildGameFromFile(String gameResourcePath, String boardResourcePath) {

        ClassLoader classLoader = GameFactory.class.getClassLoader();
        Scanner resourceScanner = new Scanner(classLoader.getResourceAsStream(gameResourcePath));

        // First line: list of nicknames
        String nicknamesString = resourceScanner.nextLine();
        String[] nicknames = nicknamesString.split(",");

        // Build a list of RemoteViewDummy based on nicknames list
        List<RemoteViewDummy> remoteViewDummies = new ArrayList<>();
        for(String nickname : nicknames) {
            remoteViewDummies.add(new RemoteViewDummy(nickname));
        }

        // Build list of players
        List<Player> players = new ArrayList<>();
        for(RemoteView rv : remoteViewDummies) {
            Player p = new Player(rv);
            players.add(p);
            for(Worker w : p.getWorkers()) {
                w.registerObservers(remoteViewDummies);
            }
        }

        int nPlayers = players.size();

        // Build board from file
        Board board = BoardFactory.buildBoardWithoutWorkersFromFile(boardResourcePath);
        board.registerObserversOnTiles(remoteViewDummies);

        // Place workers
        for(int i = 0; i < nPlayers; i++) {
            Worker[] workers = players.get(i).getWorkers();
            for(int n = 0; n < 2; n++) {
                String coords = resourceScanner.nextLine();
                String[] coordsArray = coords.split(",");
                int row = Integer.parseInt(coordsArray[0]);
                int col = Integer.parseInt(coordsArray[1]);
                workers[n].setTile(board.getTile(row, col));
            }
        }

        String cardsString = resourceScanner.nextLine();
        String[] cardNames = cardsString.split(",");

        List<EGodPower> cardList = new ArrayList<>();
        for(String cardName : cardNames) {
            cardList.add(EGodPower.parseGodPower(cardName));
        }

        Game game = new Game(players, board);

        game.registerObservers(remoteViewDummies);

        for(int i = 0; i < nPlayers; i++) {
            game.chooseCard(players.get(i), cardList.get(i));
        }

        for(Player p : players) {
            p.getCard().setup();
        }

        game.setCurrentPlayer(players.get(nPlayers - 1));
        game.changeTurn();

        for(RemoteViewDummy rvd : remoteViewDummies) {
            rvd.clearReceivedEvents();
        }

        return game;

    }

}
