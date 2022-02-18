package edu.duke.yz723.battleship;

public abstract class PlacementRuleChecker<T> {

    private final PlacementRuleChecker<T> next;

    /**
     * a public constructor
     * @param next PlacementRuleChecker
     */
    public PlacementRuleChecker(PlacementRuleChecker<T> next) {
        this.next = next;
    }

    /**
     * Subclasses will override this method to specify how they check their own rule
     * @param theShip The ship you want to check the validation of its placement
     * @param theBoard The board with the size customized
     * @return true for valid; false for invalid placement
     */
    protected abstract String checkMyRule(Ship<T> theShip, Board<T> theBoard);

    /**
     * Handles chaining rules together, using tail recursion
     * @param theShip The ship you want to check the validation of its placement
     * @param theBoard The board with the size customized
     * @return null for valid; String for invalid.
     */
    public String checkPlacement (Ship<T> theShip, Board<T> theBoard) {
        //if we fail our own rule: stop the placement is not legal
        if (checkMyRule(theShip, theBoard) != null) {
            return checkMyRule(theShip,theBoard);
        }
        //otherwise, ask the rest of the chain.
        if (next != null) {
            return next.checkPlacement(theShip, theBoard);
        }
        //if there are no more rules, then the placement is legal
        return null;
    }
}
