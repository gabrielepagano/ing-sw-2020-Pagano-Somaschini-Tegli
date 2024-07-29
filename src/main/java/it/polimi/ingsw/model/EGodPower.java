package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Represent a godcard or a godpower
 */
public enum EGodPower {

    APOLLO("Your Worker may\n" +
            "move into an opponent Worker’s\n" +
            "space by forcing their Worker to\n" +
            "the space yours just vacated."),

    ARTEMIS("Your Worker may\n" +
            "move one additional time, but not\n" +
            "back to its initial space."),

    ATHENA("If one of your\n" +
            "Workers moved up on your last\n" +
            "turn, opponent Workers cannot\n" +
            "move up this turn."),

    ATLAS("Your Worker may\n" +
            "build a dome at any level."),

    CHRONUS(false, "You also win\n" +
            "when there are at least five\n" +
            "Complete Towers on the board."),

    DEMETER("Your Worker may\n" +
            "build one additional time, but not\n" +
            "on the same space."),

    HEPHAESTUS("Your Worker may\n" +
            "build one additional block (not\n" +
            "dome) on top of your first block"),

    HERA("An opponent\n" +
            "cannot win by moving into a\n" +
            "perimeter space."),

    HESTIA("Your Worker may\n" +
            "build one additional time, but this\n" +
            "cannot be on a perimeter space."),

    MINOTAUR("Your Worker may\n" +
            "move into an opponent Worker’s\n" +
            "space, if their Worker can be\n" +
            "forced one space straight backwards to an\n" +
            "unoccupied space at any level."),

    PAN("You also win if\n" +
            "your Worker moves down two or\n" +
            "more levels."),

    PROMETHEUS("If your Worker does\n" +
            "not move up, it may build both\n" +
            "before and after moving."),

    TRITON("Each time your\n" +
            "Worker moves into a perimeter\n" +
            "space, it may immediately move\n" +
            "again."),

    ZEUS("Your Worker may\n" +
            "build a block under itself.");

    private boolean compatibleWith3Players;
    private String powerInfo;

    // All God Powers are compatible with a 2-player game
    EGodPower(String powerInfo) {
        this(true, powerInfo);
    }

    // Some God Powers (namely, Chronus) cannot be used in a 3-player game
    EGodPower(boolean compatibleWith3Players, String powerInfo) {
        this.compatibleWith3Players = compatibleWith3Players;
        this.powerInfo = powerInfo;
    }

    public boolean isCompatibleWith3Players() {
        return compatibleWith3Players;
    }

    public static EGodPower parseGodPower(String s) {
        return EGodPower.valueOf(s.toUpperCase());
    }

    public String getPowerInfo() {
        return this.powerInfo;
    }

    /**
     * Returns all godcards (names) compatible with this amount of players
     *
     * @param nPlayers Number of players
     * @return all compatible godcards
     */
    public static List<EGodPower> getCardList(int nPlayers) {
        if(nPlayers != 2 && nPlayers != 3) {
            throw new IllegalArgumentException("Invalid number of players");
        }
        List<EGodPower> cardList = new ArrayList<>(asList(values()));
        if(nPlayers == 3) {
            cardList.removeIf(el -> !(el.isCompatibleWith3Players()));
        }
        return cardList;
    }

    /**
     * Used to build actual cards instances from their names
     *
     * @param god God name
     * @param owner Player that will be associated with the card
     * @param game Contextual game
     * @return godcard instance
     */
    public static Card getGodCard(EGodPower god, Player owner, Game game) {
        if(god == null || owner == null || game == null) {
            throw new IllegalArgumentException("Invalid argument");
        }
        switch(god){
            case APOLLO:
                return new ApolloCard(owner, game);
            case ARTEMIS:
                return new ArtemisCard(owner, game);
            case ATHENA:
                return new AthenaCard(owner, game);
            case ATLAS:
                return new AtlasCard(owner, game);
            case DEMETER:
                return new DemeterCard(owner, game);
            case HEPHAESTUS:
                return new HephaestusCard(owner, game);
            case MINOTAUR:
                return new MinotaurCard(owner, game);
            case PAN:
                return new PanCard(owner, game);
            case ZEUS:
                return new ZeusCard(owner, game);
            case TRITON:
                return new TritonCard(owner, game);
            case HESTIA:
                return new HestiaCard(owner, game);
            case PROMETHEUS:
                return new PrometheusCard(owner, game);
            case CHRONUS:
                return new ChronusCard(owner, game);
            case HERA:
                return new HeraCard(owner, game);
            default:
                return null;
        }
    }

}

