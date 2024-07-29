package it.polimi.ingsw.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    @Test
    public void loggerSimpleTest() {

        Logger logger = new Logger();

        assertEquals(0, logger.getLastMessages().size());
        assertEquals(0, logger.getLastMessages().size());
        assertEquals(0, logger.getLastMessages().size());

        logger.add("Stringa1");

        assertEquals(1, logger.getLastMessages().size());
        assertEquals("Stringa1", logger.getLastMessages().get(0));
        assertEquals(1, logger.getLastMessages().size());
        assertEquals("Stringa1", logger.getLastMessages().get(0));
        assertEquals(1, logger.getLastMessages().size());
        assertEquals("Stringa1", logger.getLastMessages().get(0));

        logger.add("Stringa2");

        assertEquals(1, logger.getLastMessages().size());
        assertEquals("Stringa2", logger.getLastMessages().get(0));
        assertEquals(1, logger.getLastMessages().size());
        assertEquals("Stringa2", logger.getLastMessages().get(0));
        assertEquals(1, logger.getLastMessages().size());
        assertEquals("Stringa2", logger.getLastMessages().get(0));

        // No getLastMessages() inbetween
        logger.add("Stringa3");
        logger.add("Stringa4");

        assertEquals(2, logger.getLastMessages().size());
        assertEquals("Stringa3", logger.getLastMessages().get(0));
        assertEquals("Stringa4", logger.getLastMessages().get(1));
        assertEquals(2, logger.getLastMessages().size());
        assertEquals("Stringa3", logger.getLastMessages().get(0));
        assertEquals("Stringa4", logger.getLastMessages().get(1));
        assertEquals(2, logger.getLastMessages().size());
        assertEquals("Stringa3", logger.getLastMessages().get(0));
        assertEquals("Stringa4", logger.getLastMessages().get(1));

        logger.add("A");
        logger.add("B");
        logger.add("C");
        logger.add("D");

        assertEquals(4, logger.getLastMessages().size());
        assertEquals("A", logger.getLastMessages().get(0));
        assertEquals("B", logger.getLastMessages().get(1));
        assertEquals("C", logger.getLastMessages().get(2));
        assertEquals("D", logger.getLastMessages().get(3));

        assertEquals(4, logger.getLastMessages().size());
        assertEquals("A", logger.getLastMessages().get(0));
        assertEquals("B", logger.getLastMessages().get(1));
        assertEquals("C", logger.getLastMessages().get(2));
        assertEquals("D", logger.getLastMessages().get(3));

    }

    @Test
    public void loggerNullMessageTest() {

        Logger logger = new Logger();

        assertEquals(0, logger.getLastMessages().size());
        assertEquals(0, logger.getLastMessages().size());

        logger.add(null);

        assertEquals(0, logger.getLastMessages().size());
        assertEquals(0, logger.getLastMessages().size());

        logger.add("A");
        logger.add("B");
        logger.add(null);

        assertEquals(2, logger.getLastMessages().size());
        assertEquals("A", logger.getLastMessages().get(0));
        assertEquals("B", logger.getLastMessages().get(1));
        assertEquals(2, logger.getLastMessages().size());
        assertEquals("A", logger.getLastMessages().get(0));
        assertEquals("B", logger.getLastMessages().get(1));
        assertEquals(2, logger.getLastMessages().size());
        assertEquals("A", logger.getLastMessages().get(0));
        assertEquals("B", logger.getLastMessages().get(1));

        logger.add(null);

        assertEquals(2, logger.getLastMessages().size());
        assertEquals("A", logger.getLastMessages().get(0));
        assertEquals("B", logger.getLastMessages().get(1));
        assertEquals(2, logger.getLastMessages().size());
        assertEquals("A", logger.getLastMessages().get(0));
        assertEquals("B", logger.getLastMessages().get(1));

        logger.add(null);
        logger.add(null);

        assertEquals(2, logger.getLastMessages().size());
        assertEquals("A", logger.getLastMessages().get(0));
        assertEquals("B", logger.getLastMessages().get(1));
        assertEquals(2, logger.getLastMessages().size());
        assertEquals("A", logger.getLastMessages().get(0));
        assertEquals("B", logger.getLastMessages().get(1));

        logger.add("C");

        assertEquals(1, logger.getLastMessages().size());
        assertEquals("C", logger.getLastMessages().get(0));
        assertEquals(1, logger.getLastMessages().size());
        assertEquals("C", logger.getLastMessages().get(0));
        assertEquals(1, logger.getLastMessages().size());
        assertEquals("C", logger.getLastMessages().get(0));

        logger.add(null);

        assertEquals(1, logger.getLastMessages().size());
        assertEquals("C", logger.getLastMessages().get(0));
        assertEquals(1, logger.getLastMessages().size());
        assertEquals("C", logger.getLastMessages().get(0));

    }

}