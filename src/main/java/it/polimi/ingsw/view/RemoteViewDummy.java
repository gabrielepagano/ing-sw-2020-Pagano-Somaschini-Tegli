package it.polimi.ingsw.view;

import it.polimi.ingsw.network.events.Event;
import java.util.ArrayList;
import java.util.List;

public class RemoteViewDummy extends RemoteView {

    private List<Event> receivedEvents;

    public RemoteViewDummy(String nickname) {
        super(nickname);
        receivedEvents = new ArrayList<>();
    }

    @Override
    public void update(Event arg) {
        receivedEvents.add(arg);
    }

    public List<Event> getReceivedEvents(){
        return this.receivedEvents;
    }

    public Event getLastReceivedEvent(){
        if(receivedEvents.isEmpty()) {
            return null;
        }
        return this.receivedEvents.get(receivedEvents.size() - 1);
    }

    public void clearReceivedEvents(){
        this.receivedEvents.clear();
    }

}
