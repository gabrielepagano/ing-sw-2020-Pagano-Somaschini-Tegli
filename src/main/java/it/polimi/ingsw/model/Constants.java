package it.polimi.ingsw.model;

/**
 * The {@code Constants} class groups constants broadly used across the application
 */
public final class Constants {

    private Constants() {
        // Private constructor will prevent the instantiation of this class
    }

    /** Number of rows of the {@code Board} */
    public static final int ROWS = 5;

    /** Number of columns of the {@code Board} */
    public static final int COLUMNS = 5;

    /** Default IP to reach the server */
    public static final String DEFAULT_SERVER_IP = "localhost";

    /** Number of the port the server is listening to by default */
    public static final int DEFAULT_PORT = 12345;

    /**
     * ASCII art of Santorini title
     */
    public static final String SANTORINI_ASCII_ART = "          _____  ___   _   _ _____ ___________ _____ _   _ _____          \n"          +
                                                     "         /  ___|/ _ \\ | \\ | |_   _|  _  | ___ \\_   _| \\ | |_   _|         \n"      +
                                                     " ______  \\ `--./ /_\\ \\|  \\| | | | | | | | |_/ / | | |  \\| | | |    ______ \n"     +
                                                     "|______|  `--. \\  _  || . ` | | | | | | |    /  | | | . ` | | |   |______|\n"         +
                                                     "         /\\__/ / | | || |\\  | | | \\ \\_/ / |\\ \\ _| |_| |\\  |_| |_          \n"   +
                                                     "         \\____/\\_| |_/\\_| \\_/ \\_/  \\___/\\_| \\_|\\___/\\_| \\_/\\___/          ";

    /**
     * Error message shown server-side when ServerSocket#accept() launches
     * an IOException while waiting for new client connections
     */
    public static final String SERVER_CONNECTION_ERROR = "Errore di connessione del server (accept() di ServerSocket, errore di I/O mentre si attendeva la connessione)";

    /**
     * Warning message shown when, during WORKERSELECTION turn phase, the user
     * specifies a tile that does not contain one of his workers
     */
    public static final String NOT_VALID_WORKER_SELECTION_WARNING = "Sulla tile indicata non c'è un tuo lavoratore!";

    /**
     * Warning message shown when, during MOVE turn phase, the user
     * specifies a tile on which the currently selected worker cannot move on
     */
    public static final String NOT_VALID_MOVE_SELECTION_WARNING = "Tile non valida per muovere!";

    /**
     * Warning message shown when, during BUILD turn phase, the user
     * specifies a tile on which the currently selected worker cannot build
     */
    public static final String NOT_VALID_BUILD_SELECTION_WARNING = "Tile non valida per costruire!";

    /** Standard timeout for a network I/O operation (sendEvent/receiveEvent) */
    public static final int NORMAL_NETWORK_TIMEOUT_IN_SECONDS = 70;

    /** Long timeout for a network I/O operation (sendEvent/receiveEvent) */
    public static final int LONG_NETWORK_TIMEOUT_IN_SECONDS = 120;

    /** Time interval after which the pinger sends a new PingEvent */
    public static final int PING_TIMEOUT_IN_SECONDS = 10;

    /**
     * Ansi code that reset default colours
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /** Ansi code that sets background colour to yellow */
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[103m";

    /** Ansi code that sets background colour to red */
    public static final String ANSI_RED_BACKGROUND = "\u001B[91m";

    /** Ansi code that sets background colour to green */
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[92m";

    /** Ansi code that sets background colour to blue */
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[94m";

}
