package it.polimi.ingsw.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a simple String logger. Strings are accumulated until getLastMessages() is invoked.
 * After that, any new String added to the logger via the add() method will overwrite the previous record
 */
public class Logger {

    private List<String> log;
    private boolean hasPrintedSinceLastAdd;
    private int index;

    /**
     * Creates a new empty logger
     */
    public Logger() {
        this.log = new ArrayList<>();
        this.hasPrintedSinceLastAdd = false;
        this.index = 0;
    }

    /**
     * Adds the specified String to the currently held record/log. {@code null} is ignored
     * @param message The string to be added to the log
     */
    public void add(String message) {
        if(message == null) {
            return;
        }
        log.add(message);
        if(hasPrintedSinceLastAdd) {
            index = log.size() - 1;
            hasPrintedSinceLastAdd = false;
        }
    }

    /**
     * Getter for the current log maintained by this logger. After the invocation of getLastMessages(),
     * any new String added to the logger via the add() method will overwrite the previous record
     * @return a list of String representing the currently held log
     */
    public List<String> getLastMessages() {
        hasPrintedSinceLastAdd = true;
        return log.subList(index, log.size());
    }

}
