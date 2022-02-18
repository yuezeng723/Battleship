package edu.duke.yz723.battleship;

public class InBoundsRuleChecker<T> extends PlacementRuleChecker<T> {

    public InBoundsRuleChecker(PlacementRuleChecker<T> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
        for (Coordinate it : theShip.getCoordinates()){
            if (it.getRow() >= theBoard.getHeight()){
                return new String("That placement is invalid: the ship goes off the bottom of the board.");
            } else if (it.getColumn() >= theBoard.getWidth()){
                return new String("That placement is invalid: the ship goes off the right of the board.");
            } else if ((it.getRow() < 0)){
                return new String("That placement is invalid: the ship goes off the top of the board.");
            } else if (it.getColumn() < 0){
                return new String("That placement is invalid: the ship goes off the left of the board.");
            }
        }
        return null;
    }


}
