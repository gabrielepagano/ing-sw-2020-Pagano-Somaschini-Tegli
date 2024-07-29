package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.EGodPower;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.events.MessageEvent;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ListUtilityTest {

    @Test
    public void illegalArgumentExceptionListToStringTest() {

       assertThrows(IllegalArgumentException.class, () -> {
           ListUtility.listToString(null);
       });

    }

    @Test
    public void godPower0ElementListToStringTest() {

        List<EGodPower> godPowerList = new ArrayList<>();

        String actualResult = ListUtility.listToString(godPowerList);
        String expectedResult = "";

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void godPower1ElementListToStringTest() {

        List<EGodPower> godPowerList = new ArrayList<>();
        godPowerList.add(EGodPower.ZEUS);

        String actualResult = ListUtility.listToString(godPowerList);
        String expectedResult = "ZEUS";

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void godPower5ElementListToStringTest() {

        List<EGodPower> godPowerList = new ArrayList<>();
        godPowerList.add(EGodPower.ATHENA);
        godPowerList.add(EGodPower.ATLAS);
        godPowerList.add(EGodPower.TRITON);
        godPowerList.add(EGodPower.APOLLO);
        godPowerList.add(EGodPower.ATLAS);

        String actualResult = ListUtility.listToString(godPowerList);
        String expectedResult = "ATHENA, ATLAS, TRITON, APOLLO, ATLAS";

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void messageEvent0ElementListToStringTest() {

        List<MessageEvent> messageEventList = new ArrayList<>();

        String actualResult = ListUtility.listToString(messageEventList);
        String expectedResult = "";

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void messageEvent4ElementListToStringTest() {

        List<MessageEvent> messageEventList = new ArrayList<>();
        messageEventList.add(new MessageEvent("a1"));
        messageEventList.add(new MessageEvent("B2"));
        messageEventList.add(new MessageEvent("c3"));
        messageEventList.add(new MessageEvent("D4"));

        String actualResult = ListUtility.listToString(messageEventList);
        String expectedResult = "MessageEvent: a1, MessageEvent: B2, MessageEvent: c3, MessageEvent: D4";

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void illegalArgumentExceptionShiftRightTest() {

        assertThrows(IllegalArgumentException.class, () -> {
           ListUtility.shiftRight(null, 7);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            ListUtility.shiftRight(null, -1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            ListUtility.shiftRight(null, 0);
        });

    }

    @Test
    public void emptyListShiftRightTest() {

        List<String> listToShift = new ArrayList<>();

        ListUtility.shiftRight(listToShift, 0);

        assertEquals(0, listToShift.size());

    }

    @Test
    public void shiftRightIntegerListTest() {

        List<Integer> listToShift = new ArrayList<>();
        listToShift.add(0);
        listToShift.add(1);
        listToShift.add(2);
        listToShift.add(3);
        listToShift.add(4);

        ListUtility.shiftRight(listToShift, 1);

        assertEquals(5, listToShift.size());
        assertEquals(4, listToShift.get(0));
        assertEquals(0, listToShift.get(1));
        assertEquals(1, listToShift.get(2));
        assertEquals(2, listToShift.get(3));
        assertEquals(3, listToShift.get(4));

        ListUtility.shiftRight(listToShift, 2);

        assertEquals(5, listToShift.size());
        assertEquals(2, listToShift.get(0));
        assertEquals(3, listToShift.get(1));
        assertEquals(4, listToShift.get(2));
        assertEquals(0, listToShift.get(3));
        assertEquals(1, listToShift.get(4));

        ListUtility.shiftRight(listToShift, -1);

        assertEquals(5, listToShift.size());
        assertEquals(3, listToShift.get(0));
        assertEquals(4, listToShift.get(1));
        assertEquals(0, listToShift.get(2));
        assertEquals(1, listToShift.get(3));
        assertEquals(2, listToShift.get(4));

        ListUtility.shiftRight(listToShift, 0);

        assertEquals(5, listToShift.size());
        assertEquals(3, listToShift.get(0));
        assertEquals(4, listToShift.get(1));
        assertEquals(0, listToShift.get(2));
        assertEquals(1, listToShift.get(3));
        assertEquals(2, listToShift.get(4));

        ListUtility.shiftRight(listToShift, 5);

        assertEquals(5, listToShift.size());
        assertEquals(3, listToShift.get(0));
        assertEquals(4, listToShift.get(1));
        assertEquals(0, listToShift.get(2));
        assertEquals(1, listToShift.get(3));
        assertEquals(2, listToShift.get(4));

        ListUtility.shiftRight(listToShift, -15);

        assertEquals(5, listToShift.size());
        assertEquals(3, listToShift.get(0));
        assertEquals(4, listToShift.get(1));
        assertEquals(0, listToShift.get(2));
        assertEquals(1, listToShift.get(3));
        assertEquals(2, listToShift.get(4));

        ListUtility.shiftRight(listToShift, 6);

        assertEquals(5, listToShift.size());
        assertEquals(2, listToShift.get(0));
        assertEquals(3, listToShift.get(1));
        assertEquals(4, listToShift.get(2));
        assertEquals(0, listToShift.get(3));
        assertEquals(1, listToShift.get(4));

    }

    @Test
    public void shiftRightStringListTest() {

        List<String> listToShift = new ArrayList<>();
        listToShift.add("A1");
        listToShift.add("B2");
        listToShift.add("C3");
        listToShift.add("D4");

        ListUtility.shiftRight(listToShift, 0);

        assertEquals(4, listToShift.size());
        assertEquals("A1", listToShift.get(0));
        assertEquals("B2", listToShift.get(1));
        assertEquals("C3", listToShift.get(2));
        assertEquals("D4", listToShift.get(3));

        ListUtility.shiftRight(listToShift, 1);

        assertEquals(4, listToShift.size());
        assertEquals("D4", listToShift.get(0));
        assertEquals("A1", listToShift.get(1));
        assertEquals("B2", listToShift.get(2));
        assertEquals("C3", listToShift.get(3));

        ListUtility.shiftRight(listToShift, -2);

        assertEquals(4, listToShift.size());
        assertEquals("B2", listToShift.get(0));
        assertEquals("C3", listToShift.get(1));
        assertEquals("D4", listToShift.get(2));
        assertEquals("A1", listToShift.get(3));

        ListUtility.shiftRight(listToShift, 5);

        assertEquals(4, listToShift.size());
        assertEquals("A1", listToShift.get(0));
        assertEquals("B2", listToShift.get(1));
        assertEquals("C3", listToShift.get(2));
        assertEquals("D4", listToShift.get(3));

        ListUtility.shiftRight(listToShift, -8);

        assertEquals(4, listToShift.size());
        assertEquals("A1", listToShift.get(0));
        assertEquals("B2", listToShift.get(1));
        assertEquals("C3", listToShift.get(2));
        assertEquals("D4", listToShift.get(3));

        ListUtility.shiftRight(listToShift, 3);

        assertEquals(4, listToShift.size());
        assertEquals("B2", listToShift.get(0));
        assertEquals("C3", listToShift.get(1));
        assertEquals("D4", listToShift.get(2));
        assertEquals("A1", listToShift.get(3));

        ListUtility.shiftRight(listToShift, -6);

        assertEquals(4, listToShift.size());
        assertEquals("D4", listToShift.get(0));
        assertEquals("A1", listToShift.get(1));
        assertEquals("B2", listToShift.get(2));
        assertEquals("C3", listToShift.get(3));

    }

    @Test
    public void shiftRightEGodPowerListTest() {

        List<EGodPower> listToShift = new ArrayList<>();
        listToShift.add(EGodPower.APOLLO);
        listToShift.add(EGodPower.ARTEMIS);
        listToShift.add(EGodPower.MINOTAUR);
        listToShift.add(EGodPower.PAN);

        ListUtility.shiftRight(listToShift, 0);

        assertEquals(4, listToShift.size());
        assertEquals(EGodPower.APOLLO, listToShift.get(0));
        assertEquals(EGodPower.ARTEMIS, listToShift.get(1));
        assertEquals(EGodPower.MINOTAUR, listToShift.get(2));
        assertEquals(EGodPower.PAN, listToShift.get(3));

        ListUtility.shiftRight(listToShift, 3);

        assertEquals(4, listToShift.size());
        assertEquals(EGodPower.ARTEMIS, listToShift.get(0));
        assertEquals(EGodPower.MINOTAUR, listToShift.get(1));
        assertEquals(EGodPower.PAN, listToShift.get(2));
        assertEquals(EGodPower.APOLLO, listToShift.get(3));

        ListUtility.shiftRight(listToShift, -6);

        assertEquals(4, listToShift.size());
        assertEquals(EGodPower.PAN, listToShift.get(0));
        assertEquals(EGodPower.APOLLO, listToShift.get(1));
        assertEquals(EGodPower.ARTEMIS, listToShift.get(2));
        assertEquals(EGodPower.MINOTAUR, listToShift.get(3));

        ListUtility.shiftRight(listToShift, 25);

        assertEquals(4, listToShift.size());
        assertEquals(EGodPower.MINOTAUR, listToShift.get(0));
        assertEquals(EGodPower.PAN, listToShift.get(1));
        assertEquals(EGodPower.APOLLO, listToShift.get(2));
        assertEquals(EGodPower.ARTEMIS, listToShift.get(3));

        ListUtility.shiftRight(listToShift, 2);

        assertEquals(4, listToShift.size());
        assertEquals(EGodPower.APOLLO, listToShift.get(0));
        assertEquals(EGodPower.ARTEMIS, listToShift.get(1));
        assertEquals(EGodPower.MINOTAUR, listToShift.get(2));
        assertEquals(EGodPower.PAN, listToShift.get(3));

        ListUtility.shiftRight(listToShift, 7);

        assertEquals(4, listToShift.size());
        assertEquals(EGodPower.ARTEMIS, listToShift.get(0));
        assertEquals(EGodPower.MINOTAUR, listToShift.get(1));
        assertEquals(EGodPower.PAN, listToShift.get(2));
        assertEquals(EGodPower.APOLLO, listToShift.get(3));

    }

    @Test
    public void shiftRightPositionListTest() {

        List<Position> listToShift = new ArrayList<>();
        List<Position> originalList = new ArrayList<>();
        listToShift.add(new Position(0,1));
        listToShift.add(new Position(0,2));
        listToShift.add(new Position(0,2));
        listToShift.add(new Position(0,3));
        for(Position p : listToShift) {
            originalList.add(p);
        }

        ListUtility.shiftRight(listToShift, 0);

        assertEquals(4, listToShift.size());
        assertSame(originalList.get(0), listToShift.get(0));
        assertSame(originalList.get(1), listToShift.get(1));
        assertSame(originalList.get(2), listToShift.get(2));
        assertSame(originalList.get(3), listToShift.get(3));

        ListUtility.shiftRight(listToShift, 1);

        assertEquals(4, listToShift.size());
        assertSame(originalList.get(3), listToShift.get(0));
        assertSame(originalList.get(0), listToShift.get(1));
        assertSame(originalList.get(1), listToShift.get(2));
        assertSame(originalList.get(2), listToShift.get(3));

        ListUtility.shiftRight(listToShift, 2);

        assertEquals(4, listToShift.size());
        assertSame(originalList.get(1), listToShift.get(0));
        assertSame(originalList.get(2), listToShift.get(1));
        assertSame(originalList.get(3), listToShift.get(2));
        assertSame(originalList.get(0), listToShift.get(3));

        ListUtility.shiftRight(listToShift, -5);

        assertEquals(4, listToShift.size());
        assertSame(originalList.get(2), listToShift.get(0));
        assertSame(originalList.get(3), listToShift.get(1));
        assertSame(originalList.get(0), listToShift.get(2));
        assertSame(originalList.get(1), listToShift.get(3));

        ListUtility.shiftRight(listToShift, -3);

        assertEquals(4, listToShift.size());
        assertSame(originalList.get(1), listToShift.get(0));
        assertSame(originalList.get(2), listToShift.get(1));
        assertSame(originalList.get(3), listToShift.get(2));
        assertSame(originalList.get(0), listToShift.get(3));

        ListUtility.shiftRight(listToShift, 8);

        assertEquals(4, listToShift.size());
        assertSame(originalList.get(1), listToShift.get(0));
        assertSame(originalList.get(2), listToShift.get(1));
        assertSame(originalList.get(3), listToShift.get(2));
        assertSame(originalList.get(0), listToShift.get(3));

        ListUtility.shiftRight(listToShift, 13);

        assertEquals(4, listToShift.size());
        assertSame(originalList.get(0), listToShift.get(0));
        assertSame(originalList.get(1), listToShift.get(1));
        assertSame(originalList.get(2), listToShift.get(2));
        assertSame(originalList.get(3), listToShift.get(3));

    }

}
