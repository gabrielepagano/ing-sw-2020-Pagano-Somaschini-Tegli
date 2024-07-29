package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardFactoryTest {

    @Test
    public void buildBoardWithoutWorkersFromFile() {

        Board actualResult = BoardFactory.buildBoardWithoutWorkersFromFile("board001.txt");
        int[][] levels = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 0, 2, 0},
                {0, 3, 0, 0, 3}
        };
        boolean[][] domes = {
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, true},
        };

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                Tile actualTile = actualResult.getTile(r, c);
                assertEquals(levels[r][c], actualTile.getLevel().getHeight());
                assertEquals(domes[r][c], actualTile.isDomed());
                assertNull(actualTile.getWorker());
            }
        }
    }

}
