package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.EStartupPhase;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.DisconnectedException;
import it.polimi.ingsw.network.events.*;
import it.polimi.ingsw.utils.ListUtility;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.view.View;

import java.io.PrintWriter;
import java.util.*;

import static it.polimi.ingsw.model.Constants.*;
import static java.lang.System.exit;

/**
 * This class displays the game's model objects so that the user can interact with them.
 * Also picks up and interpret the user inputs before sending them through the socket.
 */
public class ClientView extends View {
    private ClientConnection connection;                        //Client-side Socket wrapper
    private Logger logger;                                      //System messages manager

    private Scanner in;                                         //CLI reader
    private PrintWriter out;                                    //CLI writer

    private String nickname;                                    //Nickname of the player this class instance serve

    private int nPlayers;                                       //Number of players in this game
    private List<String> playersNicknames;                      //Nicknames of the players in this game
    private String currentPlayerNickname;                       //Nickname of the player currently playing
    private Board board;                                        //The copy of the actual game board used for its display
    private List<Position> positionsToHighlight;                //The positions to highlight on the board
    private Map<String, String> workersColors;
    private final String[] colors = {ANSI_RED_BACKGROUND, ANSI_GREEN_BACKGROUND, ANSI_BLUE_BACKGROUND};
    private final int td = 5;                                   //Board representation tile dimension in number of chars
    List<char[][]> characterTiles;                              //Board representation in chars
    private List<EGodPower> selectedCards;                      //The godcards that were picked by the challenger
    private List<EGodPower> playerCards;                        //The godcards that were picked by the challenger, with indexes that correspond to playersNicknames
    private EStartupPhase startupPhase;                         //The current phase of the game's initialization
    private ETurnPhase turnPhase;                               //The current phase ot the turn
    private String question;                                    //Blocking question we want the player to answer
    private List<String> choiceOptions;                         //Possible answers to the blocking question
    private boolean acceptedNickname;                           //Indicates if the nickname request was fulfilled
    private boolean acceptedNPlayers;                           //Indicates if the lobby size request was fulfilled
    private boolean windowOS;                                   //Indicates if the system CLI is running on Windows
    private boolean gameOver;                                   //Indicates if the game is still going

    public ClientView(ClientConnection connection) {
        this.connection = connection;
        in = new Scanner(System.in);
        out = new PrintWriter(System.out);

        connection.registerObserver(this);

        board = new Board();
        gameOver = false;
        turnPhase = null;
        positionsToHighlight = new ArrayList<>();
        workersColors = new HashMap<>();
        selectedCards = new ArrayList<>();
        playerCards = new ArrayList<>();
        choiceOptions = new ArrayList<>();
        playersNicknames = new ArrayList<>();
        logger = new Logger();
        acceptedNickname = false;
        acceptedNPlayers = false;
        windowOS = false;
        characterTiles = new ArrayList<>();
        redrawAllCharacterTiles();
    }

    private void init() throws DisconnectedException {
        connection.connectToServer();
        windowOS = determineIfWindowsOS();
        new Thread(connection).start();
    }

    /**
     * This is ran by the ClientApp thread, DOES NOT implement Runnable interface
     *
     * Routine method that continuously reads from stdin and send the inferred events into the socket
     *
     */
    public void run() {
        try {
            init();
        } catch (DisconnectedException e) {
            printMessage(e.getMessage());
            return;
        }

        printMessage("\n" +
                SANTORINI_ASCII_ART + "\n");
        printMessage("Insert your nickname: ");

        try {
            Event readEvent;
            do {
                readEvent = readEvent();
                if(readEvent != null) {
                    connection.sendEvent(readEvent);
                }
            } while(!(readEvent instanceof DisconnectedEvent) && !gameOver);

        } catch (DisconnectedException e) {
            printMessage("There was a connection error, ClientConnection#sendEvent() threw DisconnectedException!");
        } finally {
            in.close();
            out.close();
            connection.close();
        }
    }

    /**
     * Manage connection to the server by manually handing nickname and lobby size settings.
     * After the connection phase, filter any message not directed to this client ({@code Event#currentPlayerReserved})
     *
     * {@inheritDoc}
     *
     * @param arg The event representing details of the received notification
     */
    @Override
    public void update(Event arg) {
        if(!acceptedNickname) {
            if(arg instanceof SetNicknameEvent) {
                setNickname(((SetNicknameEvent) arg).getAcceptedNickname());
            } else if(arg instanceof ErrorEvent) {
                printMessage(((ErrorEvent) arg).getWarning());
                printMessage("Insert your nickname: ");
            }
        } else if(!acceptedNPlayers) {
            if(arg instanceof SetNPlayersEvent) {
                setNPlayers(((SetNPlayersEvent) arg).getAcceptedNPlayers());
            } else if(arg instanceof ErrorEvent) {
                printMessage(((ErrorEvent) arg).getWarning());
                printMessage("Insert the number of players (2 or 3): ");
            }
        }
        else if(!arg.isReserved() || nickname == null || (arg.isReserved() && nickname.equals(currentPlayerNickname))) {
            super.update(arg);
        }
    }

    /**
     * Read user input from CLI
     * @return User input
     */
    public String readLine() {
        return in.nextLine();
    }

    /**
     * Write on CLI
     * @param message System response as a string
     */
    @Override
    public void printMessage(String message) {
        out.println(message);
        out.flush();
    }

    /**
     * {@inheritDoc}
     *
     * @param message The string representing the textual message to be printed
     * @param currentPlayerReserved Specify if the message is reserved for the current player
     */
    @Override
    public void printMessage(String message, boolean currentPlayerReserved){
        printMessage(message);
    }

    /**
     * {@inheritDoc}
     *
     * @param message The string representing the textual message to be printed
     */
    @Override
    public void logMessage(String message) {
        logger.add(message);
    }

    /**
     * {@inheritDoc}
     *
     * @param message The string representing the textual message to be printed
     * @param currentPlayerReserved Specify if the message is reserved for the current player
     */
    @Override
    public void logMessage(String message, boolean currentPlayerReserved) {
        logMessage(message);
    }

    /**
     * Write an empty line on CLI
     */
    public void printEmptyLine() {
        out.println();
        out.flush();
    }

    /**
     * {@inheritDoc}
     *
     * @return The event read from the input source
     */
    @Override
    public Event readEvent() {
        String inputLine = readLine();

        // Check for quit command
        if(inputLine.equalsIgnoreCase("QUIT")) {
            return new DisconnectedEvent("I quit");
        }

        // Check for a choice option
        if(choiceOptions.size() >= 2) {
            if (inputLine.equalsIgnoreCase(choiceOptions.get(0))) {
                choiceOptions.clear();
                return new ChoiceEvent(true);
            } else if (inputLine.equalsIgnoreCase(choiceOptions.get(1))) {
                choiceOptions.clear();
                return new ChoiceEvent(false);
            }
        }
        // Check for a player's nickname
        for(String nick : playersNicknames) {
            if(nick.equals(inputLine)) {
                return new PlayerNicknameEvent(nick);
            }
        }

        // Check for a god power
        try{
            return new CardEvent(EGodPower.parseGodPower(inputLine));
        } catch (IllegalArgumentException ignored) {
            //
        }

        // Check for a valid position on the board
        try {
            String[] words = inputLine.replaceAll("\\s", "").split(",");
            int row = Integer.parseInt(words[0]);
            int col = Integer.parseInt(words[1]);
            return new ActionEvent(new Position(row, col));
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            // Ignore it
        }

        // Check for a description command
        String[] words = inputLine.trim().split(" ");
        if(words.length == 2) {
            if(words[0].equalsIgnoreCase("DESCRIPTION")) {
                try {
                    printMessage(EGodPower.parseGodPower(words[1]).getPowerInfo());
                } catch (IllegalArgumentException e) {
                    // Ignore it
                }
            }
        }

        // Default behaviour
        return new MessageEvent(inputLine);
    }

    /**
     * {@inheritDoc}
     *
     * @param tilesToUpdate The list of {@code Tile} that have been updated
     */
    @Override
    public void updateBoard(List<Tile> tilesToUpdate) {
        for(Tile t : tilesToUpdate) {
            board.setTile(t);
            characterTiles.set(5 * t.getRow() + t.getColumn(), drawCharacterTile(t));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param positionsToHighlight The list of {@code Position} that have to be highlighted
     */
    @Override
    public void highlightTiles(List<Position> positionsToHighlight) {
        this.positionsToHighlight = positionsToHighlight;
    }

    /**
     * {@inheritDoc}
     *
     * @param question The string representing the question to be asked
     * @param opTrue The string representing the textual answer for {@code true}
     * @param opFalse The string representing the textual answer for {@code false}
     */
    @Override
    public void askChoice(String question, String opTrue, String opFalse) {
        this.question = question;
        choiceOptions.add(opTrue);
        choiceOptions.add(opFalse);
    }

    /**
     * {@inheritDoc}
     *
     * @param warning The string representing the textual message to be printed
     * @param currentPlayerReserved Specify if the message is reserved for the current player
     */
    @Override
    public void printError(String warning, boolean currentPlayerReserved){
        out.println(warning);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameOver() {
        this.gameOver = true;
        connection.close();
        if(startupPhase == EStartupPhase.GAMESTARTED) {
            printBoard();
        }
        List<String> loggedMessages = logger.getLastMessages();
        for(String message : loggedMessages) {
            printMessage(message);
        }
        exit(0);
    }

    /**
     * {@inheritDoc}
     * @param newCurrentPlayerNick Current player's nickname
     */
    @Override
    public void setCurrentPlayer(String newCurrentPlayerNick) {
        this.currentPlayerNickname = newCurrentPlayerNick;

        if(nickname.equals(newCurrentPlayerNick)){
            printMessage(">> It's your turn!");
        }
        else {
            printMessage(">> " + newCurrentPlayerNick + " is now playing");
        }
    }

    /**
     * {@inheritDoc}
     * @param nickname Challenger's nickname
     */
    @Override
    public void setChallenger(String nickname) {
        this.currentPlayerNickname = nickname;
        printMessage(currentPlayerNickname + " is the challenger");
    }

    /**
     * {@inheritDoc}
     * @param acceptedNickname Client's nickname as double check
     */
    @Override
    public void setNickname(String acceptedNickname) {
        this.nickname = acceptedNickname;
        this.currentPlayerNickname = acceptedNickname;
        this.acceptedNickname = true;
        printMessage("Insert the number of players (2 or 3): ");
    }

    /**
     * {@inheritDoc}
     * @param loserNickname Losing player's nickname
     */
    @Override
    public void setLoser(String loserNickname) {

        printMessage(loserNickname + " lost");

        playerCards.remove(playersNicknames.indexOf(loserNickname));
        playersNicknames.remove(loserNickname);

        if(!(currentPlayerNickname.equals(loserNickname)) && playersNicknames.size() > 1) {
            printPlayers();
            printBoard();
            printInstructions();
        }

    }

    /**
     * {@inheritDoc}
     * @param acceptedNPlayers Lobby size specified by the client as a double check
     */
    @Override
    public void setNPlayers(int acceptedNPlayers) {
        this.nPlayers = acceptedNPlayers;
        this.acceptedNPlayers = true;
        printMessage(">> Waiting for other players to connect, please stand-by...");
    }

    /**
     * {@inheritDoc}
     * @param selectedCard God relative the chosen card
     */
    @Override
    public void setSelectedCard(EGodPower selectedCard) {

        if(startupPhase == EStartupPhase.PICKCARDS) {
            selectedCards.add(selectedCard);
            printMessage("Challenger picked " + selectedCard.toString());
        } else if (startupPhase == EStartupPhase.DEALCARDS) {
            playerCards.add(selectedCard);
            printMessage(currentPlayerNickname + " picked " + selectedCard.toString());
        }

    }

    /**
     * {@inheritDoc}
     * @param startupPhase Match initialization phase
     */
    @Override
    public void setStartupPhase(EStartupPhase startupPhase) {
        this.startupPhase = startupPhase;
        switch(this.startupPhase) {
            case PICKCHALLENGER:
                printMessage("Set PICKCHALLENGER");
                break;

            case PICKCARDS:

                printMessage(">> The CHALLENGER will reveal which GODS summoned you here!");
                if(nickname.equals(currentPlayerNickname)) {
                    printMessage(">> The available GOD CARDS are: " + ListUtility.listToString(EGodPower.getCardList(playersNicknames.size())));
                    printMessage(">> Choose " + playersNicknames.size() + " cards");
                }
                break;

            case DEALCARDS:
                if(nickname.equals(currentPlayerNickname)) {
                    printMessage(">> Choose a card");
                }
                break;

            case PICKFIRSTPLAYER:
                ListUtility.shiftRight(playerCards, playersNicknames.indexOf(currentPlayerNickname) + 1);

                printPlayers();

                printMessage(">> The CHALLENGER is choosing who will GO FIRST!");
                if(nickname.equals(currentPlayerNickname)) {
                    printMessage(">> Who should play FIRST?");
                }
                break;

            case PLACEFIRSTWORKER:
                printPlayers();
                printBoard();
                if(nickname.equals(currentPlayerNickname)) {
                    printMessage(">> Select the tile where you want to PLACE your FIRST worker");
                }
                else{
                    printMessage(">> " + currentPlayerNickname + " is choosing...");
                }
                break;

            case PLACESECONDWORKER:
                printPlayers();
                printBoard();
                if(nickname.equals(currentPlayerNickname)) {
                    printMessage(">> Select the tile where you want to PLACE your SECOND worker");
                }
                else{
                    printMessage(">> " + currentPlayerNickname + " is choosing...");
                }
                break;

            case GAMESTARTED:
                printMessage(">> The game is starting!");
                break;
        }
    }

    /**
     * {@inheritDoc}
     * @param turnPhase Turn phase currently taking place
     */
    @Override
    public void setTurnPhase(ETurnPhase turnPhase) {
        this.turnPhase = turnPhase;
        printPlayers();
        printBoard();
        List<String> loggedMessages = logger.getLastMessages();
        for(String message : loggedMessages) {
            printMessage(message);
        }
        printInstructions();
        this.positionsToHighlight.clear();
    }

    /**
     * Prints turnphase-specific information to the client.
     * This info is different for the player actually playing this turn vs the "spectators"
     *
     */
    public void printInstructions() {
        if(startupPhase == EStartupPhase.GAMESTARTED) {
            switch (this.turnPhase) {
                case WORKERSELECTION:
                    if(nickname.equals(currentPlayerNickname)) {
                        printMessage("++ SELECT one of your workers");
                    } else {
                        printMessage("++ The current player is selecting a worker");
                    }
                    break;
                case MOVE:
                    if(nickname.equals(currentPlayerNickname)) {
                        printMessage("++ Select the tile where you want to MOVE");
                    } else {
                        printMessage("++ The current player is choosing where to move his/her worker");
                    }
                    break;
                case BUILD:
                    if(nickname.equals(currentPlayerNickname)) {
                        printMessage("++ Select the tile where you want to BUILD");
                    } else {
                        printMessage("++ The current player is choosing where to build");
                    }
                    break;
                case GODPOWER:
                    if(nickname.equals(currentPlayerNickname)) {
                        printMessage(question);
                        printMessage("Choose between " + choiceOptions.get(0) + " and " + choiceOptions.get(1));
                    } else {
                        printMessage("++ The current player is deciding whether to use his/her god power or not");
                    }
                    break;
            }
        }
    }

    /**
     * {@inheritDoc}
     * @param playersNicknames The list of {@code Player} nicknames
     */
    @Override
    public void setPlayersNicknames(List<String> playersNicknames){
        this.playersNicknames = playersNicknames;
        for(int i = 0; i < this.playersNicknames.size(); i++) {
            workersColors.put(this.playersNicknames.get(i), colors[i]);
        }
        printMessage(">> Player(s) found!");
        printMessage(">> The players are: " + ListUtility.listToString(playersNicknames));
    }

    /**
     * Procedure to print the game {@code Board} on the terminal.
     * The {@code Tile} representations as chars is stored in {@code ClientView#characterTiles} in crescent order of row,column.
     * We cycle through rows, printing 5 adjacent cells simultaneously by chaining theirs char representation of the same line.
     * If a tile as a {@code Worker} on top, we print the pawn with the color relative to the {@code Player} owner of that worker.
     *
     */
    private void printBoard() {
        if(windowOS){
            printBoardOnWindows();
            return;
        }

        out.println("     0       1       2       3       4   ");
        for(int r = 0; r < ROWS; r++){
            for(int line = 0; line < td; line++){
                if(line == td/2) {
                    out.print(r + " ");
                } else {
                    out.print("  ");
                }

                for(int c = 0; c < COLUMNS; c++){
                    //Determine if the tile has to be highlighted
                    if(positionsToHighlight.contains(new Position(r,c))) {
                        out.print(ANSI_YELLOW_BACKGROUND);
                    }

                    if(board.getTile(r,c).getWorker() != null && line == td/2){
                        String owner = board.getTile(r,c).getWorker().getOwnerNickname();

                        int i;
                        for(i = 0; i <= td/2; i++){
                            out.print(characterTiles.get(5*r + c)[line][i]);
                        }

                        out.print(workersColors.get(owner));
                        out.print(characterTiles.get(5*r + c)[line][td/2 + 1]);

                        if(!positionsToHighlight.contains(new Position(r,c))) {
                            out.print(ANSI_RESET);
                        }

                        for(i = td/2 + 2; i <= td + 1; i++){
                            out.print(characterTiles.get(5*r + c)[line][i]);
                        }

                    }
                    else {
                        out.print(characterTiles.get(5*r + c)[line]);
                    }

                    out.print(ANSI_RESET);

                    if(c < COLUMNS - 1) {
                        out.print("|");
                    }
                }

                out.println();
            }
            out.println("  -------+-------+-------+-------+-------");
        }
        out.flush();
    }

    /**
     * This method prints the local game {@code Board} to the terminal, in a way that is optimized for Windows OS
     */
    private void printBoardOnWindows() {

        // Table heading
        out.println("     0       1       2       3       4   ");
        // Table row
        for(int r = 0; r < ROWS; r++){
            // Cell line
            for(int line = 0; line < td; line++){
                if(line == td/2) {
                    out.print(r + " ");
                } else {
                    out.print("  ");
                }
                for(int c = 0; c < COLUMNS; c++){

                    boolean toHighlight = positionsToHighlight.contains(new Position(r,c));
                    char[] tileLine = characterTiles.get(5*r + c)[line];

                    if(toHighlight && (line == 0 || line == td - 1)){
                        out.print('*');
                    }
                    else {
                        out.print(tileLine[0]);
                    }

                    for(int i = 1; i <= td; i++){
                        out.print(tileLine[i]);
                    }

                    if(toHighlight && (line == 0 || line == td - 1)){
                        out.print('*');
                    }
                    else {
                        out.print(tileLine[td - 1]);
                    }

                    if(c < COLUMNS - 1) {
                        out.print("|");
                    }
                }
                out.println();
            }
            out.println("  -------+-------+-------+-------+-------");
        }
        out.flush();

    }

    private void redrawAllCharacterTiles() {

        characterTiles.clear();
        for(int r = 0; r < ROWS; r++) {
            for(int c = 0; c < COLUMNS; c++) {
                characterTiles.add(drawCharacterTile(board.getTile(r,c)));
            }
        }

    }

    /**
     * Given a {@code Tile}, this method produces a {@code char} matrix that represents the textual representation
     * of that tile
     * @param tileToDraw the tile of which to produce the textual representation
     * @return a {@code char} matrix representing the textual version of the specified tile
     */
    private char[][] drawCharacterTile(Tile tileToDraw) {

        char[][] cell = new char[td][td + 2];

        // Fills the cell with whitespace chars
        for(int r = 0; r < ROWS; r++) {
            Arrays.fill(cell[r], ' ');
        }

        int height = tileToDraw.getLevel().getHeight();
        boolean hasWorker = tileToDraw.getWorker() != null;
        boolean isDomed = tileToDraw.isDomed();

        // Draws the building level
        for(int r = 1; r < td - 1; r++) {
            for(int c = 1; c < td + 1; c++) {
                switch (height){
                    case 0:
                        cell[r][c] = ' ';
                        break;

                    case 1:
                        if(r == 2 || (c >= 2 && c <= 4)) {
                            cell[r][c] = ' ';
                        }
                        else {
                            cell[r][c] = '1';
                        }
                        break;

                    case 2:
                        if(r == 2 || c == 2 || c == 4) {
                            cell[r][c] = '2';
                        }
                        else {
                            cell[r][c] = ' ';
                        }
                        break;

                    case 3:
                        cell[r][c] = '3';
                        break;
                }
            }
        }

        // Clears the central space of a tile
        cell[td/2][td/2] = ' ';
        cell[td/2][td/2 + 1] = ' ';
        cell[td/2][td/2 + 2] = ' ';

        // Draws a worker, if there is one
        if(hasWorker) {
            cell[td/2][td/2 + 1] = 'W';
        }

        // Draws a dome, if there is one
        if(isDomed) {
            cell[td/2][td/2 + 1] = 'D';
        }

        return cell;

    }

    /**
     * This method prints to the terminal an overlay with the nicknames of the players currently in game together with
     * the card/god power they chose
     */
    private void printPlayers(){
        for(int i = 0; i < playersNicknames.size(); i++){
            if(playersNicknames.get(i).equals(currentPlayerNickname)){
                printMessage(" -> " + playersNicknames.get(i) + ", the champion of " + playerCards.get(i));
            }
            else{
                printMessage("    " + playersNicknames.get(i) + ", the champion of " + playerCards.get(i));
            }
        }
    }

    /**
     * This method determines if the OS on which the program is running is some version of Windows
     * @return
     */
    private boolean determineIfWindowsOS() {

        String os = System.getProperty("os.name");

        if(os == null) {
            return false;
        }

        return os.toUpperCase().contains("WINDOWS");

    }

}
