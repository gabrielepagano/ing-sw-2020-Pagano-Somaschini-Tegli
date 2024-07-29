package it.polimi.ingsw.network;

import it.polimi.ingsw.network.events.Event;

/**
 * <p> {@code Observer} is the Observer interface of the Observer&Observable pattern. </p>
 * <p> It has a method {@code update} that updates the {@code Observer} objects after the {@code Observable}
 * {@code notify} method. </p>
 */
public interface Observer {

    /**
     * This method is invoked after the {@code Observable} {@code notify} method.
     * @param arg the event that the {@code Observable} object that invoked the {@code notify} method passed as an argument
     */
    void update(Event arg);
}
