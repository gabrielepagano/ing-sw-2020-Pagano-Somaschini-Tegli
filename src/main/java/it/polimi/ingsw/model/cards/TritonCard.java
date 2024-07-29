package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.*;

public class TritonCard extends Card{

    private boolean hasMovedOntoPerimetralTile;

    public TritonCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.TRITON;
        hasMovedOntoPerimetralTile = false;
    }

    /**
     * {@inheritDoc}
     * @param destinationTile  The {@code Tile} where the worker is going to be moved to
     */
    @Override
    public void move(Tile destinationTile) {
        if(tilesToMove.contains(destinationTile)) {
            hasMovedOntoPerimetralTile = destinationTile.isPerimetralTile();
        }
        super.move(destinationTile);
    }

    /**
     * This method overrides Card#setTurnPhase
     * {@inheritDoc}
     *
     *                                         -> MOVE -> GODPOWER ->>      if the player want to use Triton's godpower
     *                           ->> GODPOWER -
     *  -                       |              -> BUILD -> end              if the player doesn't want to use Triton's godpower
     * WORKERSELECTION -> MOVE -
     *                           -> BUILD -> end                            if the player can't use Triton's godpower
     *
     */
    @Override
    protected void setTurnPhase() {

        if(game.getTurnPhase() == ETurnPhase.MOVE) {

            if(hasMovedOntoPerimetralTile) {

                tilesToMove = decorator.getTilesToMove(selectedWorker.getTile());
                if(!(tilesToMove.isEmpty())) {

                    game.setTurnPhase(ETurnPhase.GODPOWER);

                    setChanged();
                    notify(new HighlightEvent(selectedWorker.getPosition()));

                    setChanged();
                    notify(new ChoiceEvent("You moved onto a perimetral tile, do you want to move again?", "Yes", "No"));

                    setChanged();
                    notify(new TurnPhaseEvent(ETurnPhase.GODPOWER));

                } else {
                    game.setTurnPhase();
                }

            } else {
                game.setTurnPhase();
            }

        }
        else if(game.getTurnPhase() == ETurnPhase.GODPOWER) {
            if(usedGodPower) {
                game.setTurnPhase(ETurnPhase.MOVE);
                startingTile = selectedWorker.getTile();

                setChanged();
                notify(new MessageEvent(owner.getNickname() + " decided to use his/her god power and will move again from " + selectedWorker.getPosition().toString()));
            } else {
                game.setTurnPhase(ETurnPhase.BUILD);

                setChanged();
                notify(new MessageEvent(owner.getNickname() + " decided NOT to use his/her god power"));
            }
        }
        else {
            super.setTurnPhase();
        }

    }

//      EXPERIMENTAL FEATURE
//    /**
//     * This method overrides Card.move()
//     * Triton's godpower allows him to move an undefined number of times as long as he stays on the board's perimeter.
//     * In order to fasten the process, we simulate all moves necessary to get to the the designed destination instead of asking the player
//     *  what he wants to do tile by tile.
//     * All reachable perimetral tiles are included in this.tilesToMove, but also in this.perimetralMoves* in such order that we can recreate
//     *  the worker movement to his destination. We can't do this procedure in a single step, since we may skip some other condition that gets triggered
//     *  along the way.
//     *
//     * @param destinationTile The tile where the worker is going to be moved to
//     */
//    @Override
//    public void move(Tile destinationTile) {
//        this.validateMoves();
//
//        if(tilesToMove.contains(destinationTile)){
//            setCanChangeWorkerSelection(false);
//
//            if(super.getTilesToMove(startingTile).contains(destinationTile)){
//                fastMove(destinationTile);
//            }
//            else{
//                for(List<Tile> perimeterRoute: perimetralMovesClock){
//                    if(perimeterRoute.contains(destinationTile)){
//                        if(!super.getTilesToMove(startingTile).contains(perimeterRoute.get(0))){
//                            this.move(perimeterRoute.get(0));
//                            game.setTurnPhase(ETurnPhase.MOVE);
//                        }
//                        else {
//                            fastMove(perimeterRoute.get(0));
//                        }
//
//                        for(int i = 1; !perimeterRoute.get(i - 1).equals(destinationTile); i++){
//                            fastMove(perimeterRoute.get(i));
//                        }
//                        break;
//                    }
//                }
//
//                for(List<Tile> perimeterRoute: perimetralMovesAntiClock){
//                    if(perimeterRoute.contains(destinationTile)){
//                        if(!super.getTilesToMove(startingTile).contains(perimeterRoute.get(0))){
//                            this.move(perimeterRoute.get(0));
//                            game.setTurnPhase(ETurnPhase.MOVE);
//                        }
//                        else {
//                            fastMove(perimeterRoute.get(0));
//                        }
//
//                        for(int i = 1; !perimeterRoute.get(i - 1).equals(destinationTile); i++){
//                            fastMove(perimeterRoute.get(i));
//                        }
//                        break;
//                    }
//                }
//            }
//
//            setTurnPhase();
//            render();
//        }
//        else{
//            super.move(destinationTile);
//        }
//    }
//
//    /**
//     * This method overrides Card.getTilesToMove()
//     * First of all we check if Triton can reach any perimetral tile using general movement.
//     * If so, we generate all perimetrals move he could take and add it to list.
//     * Otherwise we just return the standard set of movements.
//     *
//     * @param tile The tile where the worker is currently placed
//     * @return All possible moves also considering Triton's godpower
//     */
//    @Override
//    public List<Tile> getTilesToMove(Tile tile) {
//        this.startingTile = tile;
//        boolean canReachPerimeter = false;
//
//        List<Tile> result = super.getTilesToMove(tile);
//        for (Tile t : result) {
//            if (t.isPerimetralTile()) {
//                canReachPerimeter = true;
//                break;
//            }
//        }
//
//        if(canReachPerimeter) {
//            perimetralMovesClock = this.getPerimetralTilesToMove(true);
//            perimetralMovesAntiClock = this.getPerimetralTilesToMove(false);
//
//            for(List<Tile> perimeterRoute: perimetralMovesClock){
//                for(Tile t : perimeterRoute){
//                    if(!result.contains(t)) {
//                        result.add(t);
//                    }
//                }
//            }
//
//            for(List<Tile> perimeterRoute: perimetralMovesAntiClock){
//                for(Tile t : perimeterRoute){
//                    if(!result.contains(t)) {
//                        result.add(t);
//                    }
//                }
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * This method is used to generate the perimetral moves Triton can take from its starting position, rotating
//     *  in a clockwise or anticlockwise way.
//     * For each perimetral tile that can be reached from the starting position, we check the next perimentral one
//     * in clockwise/anticlockwise order:
//     *  -> If we can't move to it or its contained in Triton general movement then we stop gathering moves
//     *  -> Otherwise we iterate on the tile
//     *
//     * The code also check various sub-cases regarding board corners.
//     *
//     * @param clockwise Indicates if the tiles will be gathered in clockwise or in anticlockwise order
//     * @return All tile reachable by subsequent perimetral moves (following the rotation sense)
//     */
//    private List<List<Tile>> getPerimetralTilesToMove(boolean clockwise){
//        Board board = game.getBoard();
//
//        List<List<Tile>> perimetralMoves = new ArrayList<>();
//        List<Tile> closePerimetrals = super.getTilesToMove(startingTile);
//        closePerimetrals.removeIf(t -> !t.isPerimetralTile());
//        closePerimetrals.add(startingTile);
//
//        for(Tile perimetral: closePerimetrals){
//            if(perimetral.equals(startingTile)){
//                continue;
//            }
//            Tile next = board.getPerimetralNext(perimetral, clockwise);
//
//            if(canMove(perimetral, next) && !closePerimetrals.contains(next)){
//                perimetralMoves.add(new ArrayList<>());
//                int pos = perimetralMoves.size() - 1;
//
//                perimetralMoves.get(pos).add(perimetral);
//                perimetralMoves.get(pos).add(next);
//
//                List<List<Tile>> routes = routePerimetrals(next, clockwise);
//                if(routes.size() > 0){
//                    if (!routes.get(0).isEmpty()) {
//                        perimetralMoves.get(pos).addAll(routes.get(0));
//                    }
//                }
//                if(routes.size() > 1){
//                    if (!routes.get(1).isEmpty()) {
//                        perimetralMoves.add(new ArrayList<>());
//                        perimetralMoves.get(pos + 1).addAll(routes.get(1));
//                    }
//                }
//            }
//            //CASE: Corner tile being part of general movement
//            else if(!canMove(perimetral, next) && perimetral.isCornerTile()){
//                Tile previous = board.getPerimetralPrevious(perimetral, clockwise);
//
//                if(canMove(previous, next)){
//                    perimetralMoves.add(new ArrayList<>());
//                    int pos = perimetralMoves.size() - 1;
//
//                    perimetralMoves.get(pos).add(perimetral);
//                    perimetralMoves.get(pos).add(previous);
//                    perimetralMoves.get(pos).add(next);
//
//                    List<List<Tile>> routes = routePerimetrals(next, clockwise);
//                    if(routes.size() > 0){
//                        if (!routes.get(0).isEmpty()) {
//                            perimetralMoves.get(pos).addAll(routes.get(0));
//                        }
//                    }
//                    if(routes.size() > 1){
//                        if (!routes.get(1).isEmpty()) {
//                            perimetralMoves.add(new ArrayList<>());
//                            perimetralMoves.get(pos + 1).addAll(routes.get(1));
//                        }
//                    }
//                }
//            }
//        }
//
//        //Debug prints
//        /*
//        System.out.println(">> perimetralsMovesClockwise(" + clockwise + ") : ");
//        for(List<Tile> route : perimetralMoves){
//            System.out.println(ListUtility.listToString(route));
//        }*/
//
//
//
//        return perimetralMoves;
//    }
//
//    private List<List<Tile>> routePerimetrals(Tile current, boolean clockwise){
//        Board board = game.getBoard();
//
//        List<Tile> closePerimetrals = super.getTilesToMove(startingTile);
//        closePerimetrals.removeIf(t -> !t.isPerimetralTile());
//        closePerimetrals.add(startingTile);
//
//        boolean deadEnd;
//        List<Tile> routeLinear = new ArrayList<>();
//        List<Tile> routeBranch = new ArrayList<>();
//        do{
//            deadEnd = true;
//            Tile next = board.getPerimetralNext(current, clockwise);
//            if(next.isCornerTile()){
//                Tile angular = next;
//                Tile diagonal = board.getPerimetralNext(next, clockwise);
//
//                //CASE: The tiles goes all the way around and the angular/diagonal is contained in closePerimetrals
//                if(closePerimetrals.contains(angular) && closePerimetrals.contains(diagonal)){
//                    //idle, no action needed
//                }
//                else if(closePerimetrals.contains(angular) && canMove(current, diagonal)){
//                    routeLinear.add(diagonal);
//                }
//                else if(closePerimetrals.contains(diagonal) && canMove(current, angular)){
//                    routeLinear.add(angular);
//                }
//
//                //CASE: The current tile can move to the corner; the corner can move to the diagonal -> base case
//                else if(canMove(current, angular) && canMove(angular, diagonal)){
//                    routeLinear.add(angular);
//                    routeLinear.add(diagonal);
//
//                    current = diagonal;
//                    deadEnd = false;
//                }
//                //CASE: The current tile can only move to the diagonal -> diagonal is the next move
//                else if(!canMove(current, angular) && canMove(current, diagonal)){
//                    if(!canMove(diagonal, angular)){
//                        routeLinear.add(diagonal);
//
//                        current = diagonal;
//                        deadEnd = false;
//                    }
//                    //SUBCASE: The diagonal tile can move to the corner -> branch route (current -> diagonal -> corner -> STOP)
//                    else {
//                        routeLinear.add(diagonal);
//                        routeBranch.add(diagonal);
//                        routeBranch.add(angular);
//
//                        current = diagonal;
//                        deadEnd = false;
//                    }
//                }
//                //CASE: The current tile can only move to the corner -> corner is the next and final move
//                else if(canMove(current, angular) && !canMove(angular, diagonal)){
//                    routeLinear.add(angular);
//                }
//            }
//            //BASE CASE
//            else if(canMove(current, next) && !closePerimetrals.contains(next)){
//                routeLinear.add(next);
//
//                current = next;
//                deadEnd = false;
//            }
//        } while(!deadEnd);
//
//        List<List<Tile>> result = new ArrayList<>();
//        if(!routeLinear.isEmpty()) {
//            result.add(routeLinear);
//        }
//        if(!routeBranch.isEmpty()) {
//            result.add(routeBranch);
//        }
//
//        return result;
//    }
//
//
//    /**
//     * Before we can safely move Triton's worker, we need to make sure no other card have destroyed the
//     * moves continuity on the perimeters (i.e. Athena)
//     *
//     */
//    private void validateMoves(){
//        for(List<Tile> perimeterRoute: perimetralMovesClock){
//            for(int i = 0; i < perimeterRoute.size(); i++){
//                if(!tilesToMove.contains(perimeterRoute.get(i))){
//                    //Corner not accessible anymore, but can still move past it
//                    if(perimeterRoute.get(i).isCornerTile()
//                        && tilesToMove.contains(game.getBoard().getPerimetralNext(perimeterRoute.get(i), true) )){
//                        tilesToMove.remove(perimeterRoute.get(i));
//                    }
//                    else {
//                        tilesToMove.removeAll(perimeterRoute.subList(i, perimeterRoute.size()));
//                        break;
//                    }
//                }
//            }
//        }
//
//        for(List<Tile> perimeterRoute: perimetralMovesAntiClock){
//            for(int i = 0; i < perimeterRoute.size(); i++){
//                if(!tilesToMove.contains(perimeterRoute.get(i))){
//                    //Corner not accessible anymore, but can still move past it
//                    if(perimeterRoute.get(i).isCornerTile()
//                            && tilesToMove.contains(game.getBoard().getPerimetralNext(perimeterRoute.get(i), false) )){
//                        tilesToMove.remove(perimeterRoute.get(i));
//                    }
//                    else {
//                        tilesToMove.removeAll(perimeterRoute.subList(i, perimeterRoute.size()));
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//
//    /**
//     * Given two Tiles, this method determines if it's possible to move between them.
//     *
//     * @param start The tile where the movement starts
//     * @param destination The tile where the movement ends
//     * @return Answer the question "is the inferred movement possible?"
//     */
//    private boolean canMove(Tile start,Tile destination){
//        if(Math.abs(destination.getRow() - start.getRow()) > 1) {
//            return false;
//        }
//        else if(Math.abs(destination.getColumn() - start.getColumn()) > 1) {
//            return false;
//        }
//        else if(destination.getLevel().getHeight() > start.getLevel().getHeight() + 1
//                || destination.getWorker() != null
//                || destination.isDomed()) {
//            return false;
//        }
//        else {
//            return true;
//        }
//    }
//
//
//    private void fastMove(Tile destinationTile){
//        selectedWorker.moveWorker(destinationTile);
//        // Hera can suppress the win condition
//        decorator.checkWinCondition();
//    }

}