package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EGodPowerTest {

    @Test
    public void getCardListTest() {

        List<EGodPower> actualCardList;
        List<EGodPower> expectedCardList;

        // GodPowers compatible with 2 players
        actualCardList = EGodPower.getCardList(2);
        expectedCardList = Arrays.asList(EGodPower.values());
        assertTrue(actualCardList.containsAll(expectedCardList));

        // GodPowers compatible with 3 players
        actualCardList = EGodPower.getCardList(3);
        expectedCardList = new ArrayList<>();
        for (EGodPower god : EGodPower.values()) {
            expectedCardList.add(god);
        }
        expectedCardList.remove(EGodPower.CHRONUS);
        assertTrue(actualCardList.containsAll(expectedCardList));

        // Invalid number of players
        assertThrows(IllegalArgumentException.class, () -> EGodPower.getCardList(4));

        // Invalid number of players
        assertThrows(IllegalArgumentException.class, () -> EGodPower.getCardList(1));

        // Invalid number of players
        assertThrows(IllegalArgumentException.class, () -> EGodPower.getCardList(20));

    }

}
