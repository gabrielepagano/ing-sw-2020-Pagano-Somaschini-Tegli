package it.polimi.ingsw.network;

import it.polimi.ingsw.network.events.Event;

import java.util.List;
import java.util.ArrayList;

/**
 * <p> It is the {@code Observable} class of the Observer&Observable pattern. </p>
 * <p> It has: </p>
 * <ul>
 *     <li> A boolean {@param changed} that indicates if the {@code Observable} object has changed </li>
 *     <li> {@param observers}, an ArrayList of {@Observer}s </li>
 * </ul>
 */
public class Observable {
    private boolean changed = false;
    private List<Observer> observers = new ArrayList<>();


    /**
     * Registers an {@code Observer} to the {@code Observable} object adding it to the {@param observers} ArrayList.
     * @param o  the {@code Observer} which has to be added to the list
     */
    public synchronized void registerObserver(Observer o) {
        if (o == null)
            throw new IllegalArgumentException();
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    /**
     * Registers an list of {@code Observer}s to the {@code Observable} object calling for each {@code Observer} the
     * {@code registerObserver} method.
     * @param observers  the {@code Observer} list that has to be added
     */
    public synchronized void registerObservers(List<? extends Observer> observers) {
        if(observers == null) {
            throw new IllegalArgumentException();
        }
        for(Observer o : observers) {
            registerObserver(o);
        }
    }


    /**
     * Notifies the currently registered {@code Observer}s about an invoked event and update them calling the {@code update} method.
     * @param arg  the event which is invoked
     */
    public void notify(Event arg) {
        synchronized(this) {
            if (!changed) return;
            clearChanged();
            observers.forEach(observer -> observer.update(arg));
        }
    }

    /**
     * Sets the boolean {@code changed} as true.
     */
    public synchronized void setChanged() {
        changed = true;
    }

    /**
     * Sets the boolean {@code changed} as false.
     */
    protected synchronized void clearChanged() {
        changed = false;
    }
}