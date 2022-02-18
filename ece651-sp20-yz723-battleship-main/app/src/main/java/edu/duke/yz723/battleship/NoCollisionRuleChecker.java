package edu.duke.yz723.battleship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NoCollisionRuleChecker<T> extends InBoundsRuleChecker<T> {

    public NoCollisionRuleChecker(PlacementRuleChecker<T> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(Ship<T> theShip, Board<T> theBoard){
        for (Coordinate it : theShip.getCoordinates()){
            if(theBoard.whatIsAtForSelf(it) != null){
                return "That placement is invalid: the ship overlaps another ship.";
            }
        }
        return null;
    }



}
