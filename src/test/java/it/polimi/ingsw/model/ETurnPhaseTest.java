package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ETurnPhaseTest {

    @Test
    public void nextCircularTest() {

        ETurnPhase turnPhase = ETurnPhase.WORKERSELECTION;

        turnPhase = turnPhase.nextCircular();
        assertEquals(ETurnPhase.MOVE, turnPhase);

        turnPhase = turnPhase.nextCircular();
        assertEquals(ETurnPhase.BUILD, turnPhase);

        turnPhase = turnPhase.nextCircular();
        assertEquals(ETurnPhase.WORKERSELECTION, turnPhase);

        turnPhase = turnPhase.nextCircular();
        assertEquals(ETurnPhase.MOVE, turnPhase);

        turnPhase = turnPhase.nextCircular();
        assertEquals(ETurnPhase.BUILD, turnPhase);

    }

}
