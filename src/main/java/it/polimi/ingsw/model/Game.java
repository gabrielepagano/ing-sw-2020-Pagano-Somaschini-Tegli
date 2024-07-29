package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.EStartupPhase;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.network.Observable;
import it.polimi.ingsw.network.events.*;
import it.polimi.ingsw.utils.ListUtility;
import it.polimi.ingsw.view.RemoteView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents a single match. This is a Model wrapper class.
 */
public class Game extends Observable {

    // Questa lista è passata dal controller in fase di costruzione
    private List<Player> players;               // Tutti i giocatori che sono ancora in gioco
    private int nPlayers;                       // Numero di Player ancora in gioco (non hanno ancora perso)
    private Player currentPlayer;               // Il giocatore di turno
    private ETurnPhase turnPhase;
    private Player winner;                      // Eventuale vincitore della partita
    private Board board;                        // Plancia della partita
    private Player challenger;                  // Sceglie il primo giocatore
    private int completeTowers;
    private EStartupPhase startupPhase;
    private List<EGodPower> selectedCards;
    private List<EGodPower> playerCards;
    private Player firstPlayer;
    private boolean gameOver;

    // Constructor with Dependency Injection
    public Game(List<Player> players, Board board) {
        this.players = players;
        this.board = board;
        this.nPlayers = players.size();
        this.currentPlayer = players.get(0);
        this.winner = null;
        this.challenger = players.get(0);
        this.completeTowers = 0;
        this.turnPhase = ETurnPhase.WORKERSELECTION;
        this.startupPhase = EStartupPhase.PICKCHALLENGER;
        this.selectedCards = new ArrayList<>();
        this.playerCards = new ArrayList<>();
        this.gameOver = false;
    }

    /**
     * Manage the identification of the next current player.
     * Check for blocked workers and eventually declare the defeat of a stuck player.
     */
    public void changeTurn() {

        Player nextPlayer = getNextPlayer();

        Worker[] nextPlayerWorkers = nextPlayer.getWorkers();
        Playable nextPlayerCard = nextPlayer.getCard().getPlayable();

        List<Tile> firstWorkerPossibleMoves = nextPlayerCard.getTilesToMove(nextPlayerWorkers[0].getTile());
        List<Tile> secondWorkerPossibleMoves = nextPlayerCard.getTilesToMove(nextPlayerWorkers[1].getTile());

        if(firstWorkerPossibleMoves.isEmpty() && secondWorkerPossibleMoves.isEmpty()){
            // Message to client
            setLoser(nextPlayer);
            if(!isGameOver()) {
                changeTurn();
            }
        } else {
            setCurrentPlayer(nextPlayer);
            setTurnPhase(ETurnPhase.WORKERSELECTION);
            if(firstWorkerPossibleMoves.isEmpty() && !(secondWorkerPossibleMoves.isEmpty())) {
                setChanged();
                notify(new MessageEvent("You can move only one of your workers", true));
                nextPlayerCard.selectWorker(nextPlayerWorkers[1]);
                nextPlayerCard.setCanChangeWorkerSelection(false);
                // Già render() manda gli highlight
            } else if (secondWorkerPossibleMoves.isEmpty() && !(firstWorkerPossibleMoves.isEmpty())) {
                setChanged();
                notify(new MessageEvent("You can move only one of your workers", true));
                nextPlayerCard.selectWorker(nextPlayerWorkers[0]);
                nextPlayerCard.setCanChangeWorkerSelection(false);
                // Già render() manda gli highlight
            } else {
                setChanged();
                notify(new HighlightEvent(nextPlayerWorkers[0].getTile().getPosition(), nextPlayerWorkers[1].getTile().getPosition()));
                setChanged();
                notify(new TurnPhaseEvent(getTurnPhase()));
            }
        }

    }

    public Board getBoard() {
        return this.board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setChallenger(Player challenger) {
        this.challenger = challenger;
        setCurrentPlayer(challenger);
        setChanged();
        notify(new SetChallengerEvent(challenger.getNickname()));
        setStartupPhase(EStartupPhase.PICKCARDS);
    }

    public Player getChallenger() {
        return this.challenger;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        setChanged();
        notify(new SetCurrentPlayerEvent(currentPlayer));
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Card getCurrentPlayerCard() {
        return currentPlayer.getCard();
    }

    public ETurnPhase getTurnPhase() {
        return turnPhase;
    }

    public void setTurnPhase(ETurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    public void setTurnPhase() {
        setTurnPhase(this.turnPhase.nextCircular());
    }

    public EStartupPhase getStartupPhase() {
        return startupPhase;
    }

    public void setStartupPhase(EStartupPhase startupPhase) {
        this.startupPhase = startupPhase;

        setChanged();
        notify(new StartupPhaseEvent(startupPhase));
    }

    public void setWinner(Player winner) {
        this.winner = winner;
        this.gameOver = true;
        setChanged();
        notify(new MessageEvent("The winner is... " + winner.getNickname() + "!"));
        setChanged();
        notify(new GameOverEvent());
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Remove a player that has been defeated from the game by:
     * <ul>
     *     <li>Removing his/her worker</li>
     *     <li>Calling Card specific removal routine</li>
     *     <li>Shrieking the players list</li>
     * </ul>
     *
     * @param loser The player that has lost
     */
    public void setLoser(Player loser) {
        if(players.contains(loser)) {

            Player previousPlayer = players.get((players.indexOf(loser) - 1 + players.size()) % players.size());
            players.remove(loser);

            // Clean the board from loser workers
            Worker w0 = loser.getWorkers()[0];
            Worker w1 = loser.getWorkers()[1];
            Tile w0Tile = w0.getTile();
            Tile w1Tile = w1.getTile();
            w0.setTile(null);
            w1.setTile(null);

            loser.getCard().destroy();

            setChanged();
            notify(new BoardEvent(w0Tile, w1Tile));

            setChanged();
            notify(new LoseEvent(loser.getNickname()));

            // If there is only one player remaining, don't bother changing turn
            if (players.size() == 1) {
                setWinner(players.get(0));
                return;
            }

            if (loser == currentPlayer) {
                currentPlayer = previousPlayer;
                changeTurn();
            }

        }
    }

    public int getCompleteTowers() {
        return this.completeTowers;
    }

    public void buildCompleteTower() {
        completeTowers++;
    }

    /**
     * Notify start of the game
     */
    public void startGame() {
        setChanged();
        notify(new StartGameEvent(getPlayerNicknamesList()));
    }

    public List<EGodPower> getPlayerCards() {
        return playerCards;
    }

    /**
     * Creates and set the chosen {@code Card} to the {@code Player}
     * @param owner The player that will be linked to the card
     * @param card The god related to the card
     */
    public void chooseCard(Player owner, EGodPower card) {
        if(owner == null || card == null) {
            throw new IllegalArgumentException();
        }
        owner.setCard(EGodPower.getGodCard(card, owner, this));
        setChanged();
        notify(new CardEvent(card));
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
        setChanged();
        notify(new SetFirstPlayerEvent(firstPlayer.getNickname()));
    }

    /**
     * Place the {@code Worker} to the specified {@code Tile}. This is not a movement.
     *
     * @param player The player that owns the worker
     * @param workerId Worker's unique ID
     * @param tileToPlaceWorker The {@code Tile} where the worker will be placed onto
     */
    public void placeWorker(Player player, int workerId, Tile tileToPlaceWorker) {
        if(player == null || (workerId != 0 && workerId != 1) || tileToPlaceWorker == null) {
            throw new IllegalArgumentException();
        }
        Worker workerToPlace = player.getWorkers()[workerId];
        workerToPlace.setTile(tileToPlaceWorker);
        setChanged();
        notify(new BoardEvent(tileToPlaceWorker));
    }

    /**
     * Chose the challenger amongst the players. The choice is done randomly.
     */
    public void pickChallenger() {
        Random r = new Random();
        int i = r.nextInt(players.size());
        setChallenger(players.get(i));
    }

    /**
     * The challenger is choosing which card will be present in this game.
     * We check the validity of the choice and communicate it.
     * @param selectedCard A chosen {@code Card}
     */
    public void pickCards(EGodPower selectedCard){
        if(selectedCard == null) {
            setChanged();
            notify(new ErrorEvent("Card cannot be null"));
            return;
        }
        if (!(selectedCards.contains(selectedCard))) {
            if (players.size() == 2 || selectedCard.isCompatibleWith3Players()) {
                selectedCards.add(selectedCard);
                setChanged();
                notify(new CardEvent(selectedCard));
                if (selectedCards.size() == players.size()) {
                    setCurrentPlayer(getNextPlayer());
                    setStartupPhase(EStartupPhase.DEALCARDS);
                }
            }
            else {
                setChanged();
                notify(new ErrorEvent("This card is not compatible with 3 players!"));
            }
        }
        else {
            setChanged();
            notify(new ErrorEvent("This card has already been chosen!"));
        }
    }

    /**
     * Each player is choosing a {@code Card},from those chosen by the challenger, that will be their own during the game.
     * We check the validity of the choice and communicate it.
     * @param selectedCard A chosen {@code Card}
     */
    public void dealCards(EGodPower selectedCard){
        if(selectedCard == null) {
            setChanged();
            notify(new ErrorEvent("Card cannot be null"));
            return;
        }
        if (selectedCards.contains(selectedCard)) {
            chooseCard(getCurrentPlayer(), selectedCard);
            selectedCards.remove(selectedCard);
            playerCards.add(selectedCard);
            setCurrentPlayer(getNextPlayer());
            if (selectedCards.size() == 1) {
                chooseCard(getCurrentPlayer(), selectedCards.get(0));

                playerCards.add(selectedCards.get(0));
                // Shift playerCards, so that the cards' order matches the players' order
                ListUtility.shiftRight(playerCards, players.indexOf(challenger) + 1);
                selectedCards.clear();
                for(Player p : players){
                    p.getCard().setup();
                }

                setStartupPhase(EStartupPhase.PICKFIRSTPLAYER);
            }
        }
        else if (playerCards.contains(selectedCard)) {
            setChanged();
            notify(new ErrorEvent("Somebody already chose that card!"));
        }
        else {
            setChanged();
            notify(new ErrorEvent("Choose a card from the ones selected by the challenger!"));
        }
    }

    /**
     * The challenger is indicating which player will start first.
     * @param nickname The first player indicated by the challenger
     */
    public void pickFirstPlayer(String nickname){
        if(nickname == null) {
            setChanged();
            notify(new ErrorEvent("The nick cannot be null"));
            return;
        }
        for (Player player : players) {
            if (player.getNickname().equals(nickname)) {
                setFirstPlayer(player);
                setCurrentPlayer(firstPlayer);
                setStartupPhase(EStartupPhase.PLACEFIRSTWORKER);
                return;
            }
        }
        setChanged();
        notify(new ErrorEvent("The nick you indicated does not belong to any player in this game!"));
    }

    /**
     * Each player has to place their {@code Worker} on the {@code Board} before the game can start.
     * @param placement The position on the board where the worker will be placed.
     */
    public void placeFirstWorker(Position placement){
        if(placement == null) {
            setChanged();
            notify(new ErrorEvent("The position cannot be null"));
            return;
        }
        Tile tileToPlaceWorker = getBoard().getTile(placement);

        if (!(tileToPlaceWorker.hasWorker())) {
            placeWorker(getCurrentPlayer(), 0, tileToPlaceWorker);
            setStartupPhase(EStartupPhase.PLACESECONDWORKER);
        }
        else {
            setChanged();
            notify(new ErrorEvent("The tile you chose is already occupied!"));
        }
    }

    /**
     * Each player has to place their {@code Worker} on the {@code Board} before the game can start.
     * @param placement The position on the board where the worker will be placed.
     */
    public void placeSecondWorker(Position placement){
        Tile tileToPlaceWorker = getBoard().getTile(placement);

        if (!(tileToPlaceWorker.hasWorker())) {
            placeWorker(getCurrentPlayer(), 1, tileToPlaceWorker);

            Player nextPlayer = getNextPlayer();
            if (nextPlayer == firstPlayer) {
                setStartupPhase(EStartupPhase.GAMESTARTED);
                changeTurn();
            } else {
                setCurrentPlayer(nextPlayer);
                setStartupPhase(EStartupPhase.PLACEFIRSTWORKER);
            }

        }
        else {
            setChanged();
            notify(new ErrorEvent("The tile you chose is already occupied!"));
        }
    }

    private Player getNextPlayer() {
        int nPlayersAtTheMoment = players.size();
        int nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % nPlayersAtTheMoment;
        return players.get(nextPlayerIndex);
    }

    public List<RemoteView> getRemoteViews() {
        List<RemoteView> remoteViews = new ArrayList<>();
        for(Player p : players) {
            remoteViews.add(p.getRemoteView());
        }
        return remoteViews;
    }

    public List<String> getPlayerNicknamesList() {
        return players.stream().map(Player::getNickname).collect(Collectors.toList());
    }

    /**
     * Kill a game after detecting a disconnection.
     */
    public void abort() {
        setChanged();
        notify(new ErrorEvent("A disconnection took place. The game is cancelled!", false));
        setChanged();
        notify(new GameOverEvent());
    }

    public List<EGodPower> getSelectedCards() {
        return selectedCards;
    }

}
