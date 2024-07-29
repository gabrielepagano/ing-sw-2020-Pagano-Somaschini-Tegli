package it.polimi.ingsw.network.events;

/**
 * This {@code Event} is used
 * <ul>
 *     <li>by the server, to ask the client a question with 2 possible answers, usually regarding the will
 *     to use a god power</li>
 *     <li>by che client, to answer the binary question</li>
 * </ul>
 */
public class ChoiceEvent extends Event {
    private final String question;
    private final String optionTrue;
    private final String optionFalse;
    private final boolean choice;

    /**
     * This is the constructor used by the server. It creates a new {@code ChoiceEvent}, setting the question and
     * the 2 possible answers
     * @param question a string representing the question to present to the client
     * @param optionTrue a string representing the first possible answer
     * @param optionFalse a string representing the second possible answer
     */
    public ChoiceEvent(String question, String optionTrue, String optionFalse){
        this.currentPlayerReserved = true;
        this.question = question;
        this.optionTrue = optionTrue;
        this.optionFalse = optionFalse;
        this.choice = false;
    }

    /**
     * This is the constructor used by the client. It creates a new {@code ChoiceEvent}, setting the answer
     * to a previously asked question
     * @param choice a boolean representing the answer to the previous question. {@code true} if the client
     *               chose the first answer, {@code false} if the client selected the second answer
     */
    public ChoiceEvent(boolean choice){
        this.choice = choice;
        this.question = null;
        this.optionTrue = null;
        this.optionFalse = null;
    }


    /**
     * Getter for the question associated with this event
     * @return the question asked.
     */
    public String getQuestion(){
        return this.question;
    }

    /**
     * Getter for the first possible answer associated with this event
     * @return the first possible answer
     */
    public String getOptionTrue() {
        return optionTrue;
    }

    /**
     * Getter for the second possible answer associated with this event
     *      * @return the second possible answer
     */
    public String getOptionFalse() {
        return optionFalse;
    }

    /**
     * Getter for the boolean flag representing the choice made by the user
     * @return the choice made by the user.
     */
    public boolean getChoice() {
        return choice;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ChoiceEvent: " + question + " " + optionTrue + "/" + optionFalse;
    }
}
