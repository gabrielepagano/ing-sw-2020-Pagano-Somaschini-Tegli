package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.GameFactory;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZeusCardTest {

    static Game game;
    static Board board;
    //static List<RemoteView> remoteViews;
    //static List<Player> players;
    static Player currentPlayer;
    static Playable currentPlayerCard;

    @BeforeEach
    public void setup(){
        game = GameFactory.buildGameFromFile("gameZeus.txt", "board001.txt");
        board = game.getBoard();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();
    }

    @Test
    public void getTilesToBuild(){

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Check standard generation
        List<Tile> actualTilesToBuild =  currentPlayerCard.getTilesToBuild(board.getTile(1,2));
        List<Tile> expectedTilesToBuild = new ArrayList<>();
        expectedTilesToBuild.add(board.getTile(0,1));
        expectedTilesToBuild.add(board.getTile(0,2));
        expectedTilesToBuild.add(board.getTile(0,3));
        expectedTilesToBuild.add(board.getTile(1,1));
        expectedTilesToBuild.add(board.getTile(1,3));
        expectedTilesToBuild.add(board.getTile(2,1));
        expectedTilesToBuild.add(board.getTile(2,2));
        for (Tile t : expectedTilesToBuild) {
            assertTrue(actualTilesToBuild.contains(t));
        }
        assertFalse(actualTilesToBuild.contains(board.getTile(2,3)));

        //Check Zeus GODPOWER
        assertTrue(actualTilesToBuild.contains(board.getTile(1,2)));
    }

    @Test
    public void getTilesToBuildOnDome(){

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Check standard generation
        List<Tile> actualTilesToBuild =  currentPlayerCard.getTilesToBuild(board.getTile(4,1));
        List<Tile> expectedTilesToBuild = new ArrayList<>();
        expectedTilesToBuild.add(board.getTile(3,0));
        expectedTilesToBuild.add(board.getTile(3,1));
        expectedTilesToBuild.add(board.getTile(3,2));
        expectedTilesToBuild.add(board.getTile(4,0));
        expectedTilesToBuild.add(board.getTile(4,2));
        for (Tile t : expectedTilesToBuild) {
            assertTrue(actualTilesToBuild.contains(t));
        }

        //Zeus can't build a dome under himself
        assertFalse(actualTilesToBuild.contains(board.getTile(4,1)));
    }

}