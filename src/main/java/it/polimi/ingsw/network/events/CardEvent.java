package it.polimi.ingsw.network.events;

import it.polimi.ingsw.model.EGodPower;

/**
 * This {@code Event} is used to carry a specific god power/card, of type {@code EGodPower}
 */
public class CardEvent extends Event {

    private EGodPower card;

    /**
     * Creates a new {@CardEvent}, setting the god power it carries
     * @param card a {@code EGodPower} enum constant
     */
    public CardEvent(EGodPower card) {
        this.card = card;
    }

    /**
     * Getter for the card/god power carried by this event
     * @return the {@param card}
     */
    public EGodPower getCard() {
        return card;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CardEvent: " + card.toString();
    }

}
