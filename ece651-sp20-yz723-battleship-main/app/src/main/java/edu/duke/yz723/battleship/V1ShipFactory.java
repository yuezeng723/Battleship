package edu.duke.yz723.battleship;

public class V1ShipFactory implements AbstractShipFactory<Character>{
    protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name){
        if (where.orientation == 'V') {
            Ship<Character> ship = new RectangleShip<Character>(name, where.getCoordinate(), w, h, letter, '*');
            return ship;
        }
        else {
            Ship<Character> ship = new RectangleShip<Character>(name, where.getCoordinate(), h, w, letter, '*');
            return ship;
        }
    }
    @Override
    public Ship<Character> makeSubmarine(Placement where) {
        return createShip(where, 1, 2, 's', "Submarine");
    }

    @Override
    public Ship<Character> makeBattleship(Placement where) {
        return createShip(where, 1, 4, 'b', "Battleship");
    }

    @Override
    public Ship<Character> makeCarrier(Placement where) {
        return createShip(where, 1, 6, 'c', "Carrier");
    }

    @Override
    public Ship<Character> makeDestroyer(Placement where) {
        return createShip(where, 1, 3, 'd', "Destroyer");
    }
}
