package edu.duke.yz723.battleship;

public class SimpleShipDisplayInfo<T> implements ShipDisplayInfo<T> {
    public T myData;
    public T onHit;

    //constructor
     public SimpleShipDisplayInfo (T myData, T onHit){
         this.myData = myData;
         this.onHit = onHit;
    }

    @Override
    public T getInfo(Coordinate where, boolean hit) {
        if (hit){
            return onHit;
        }else{
            return myData;
        }
    }

}
