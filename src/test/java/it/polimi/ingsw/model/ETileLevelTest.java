package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ETileLevelTest {

    @Test
    public void getHeightTest() {
        assertEquals(0, ETileLevel.GROUND.getHeight());
        assertEquals(1, ETileLevel.LEVEL1.getHeight());
        assertEquals(2, ETileLevel.LEVEL2.getHeight());
        assertEquals(3, ETileLevel.LEVEL3.getHeight());
    }

    @Test
    public void getNextValueTest() {
        assertEquals(ETileLevel.LEVEL1, ETileLevel.GROUND.getNextValue());
        assertEquals(ETileLevel.LEVEL2, ETileLevel.LEVEL1.getNextValue());
        assertEquals(ETileLevel.LEVEL3, ETileLevel.LEVEL2.getNextValue());

        // Check repeated invocations on LEVEL3
        assertEquals(ETileLevel.LEVEL3, ETileLevel.LEVEL3.getNextValue());
        assertEquals(ETileLevel.LEVEL3, ETileLevel.LEVEL3.getNextValue());
    }

}
