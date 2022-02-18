package edu.duke.yz723.battleship;

import java.util.Random;

public class RandomGenerator {

    public Coordinate getRandomCoordination(int rowMax, int columnMax){
       return new Coordinate(randGenerator(0,rowMax), randGenerator(0,columnMax));
    }

    public Placement getRandomPlacement(int rowMax, int columnMax){
        Coordinate c = getRandomCoordination(rowMax, columnMax);
        char ch1 = 'A';
        char ch2 = 'T';
        char orien = (char)(ch1 + Math.random() * (ch2 - ch1 +1));
        return new Placement(c,orien);
    }
    private int randGenerator(int min, int max){
        return min + (int)(Math.random())*(max - min + 1);
    }

    public char getRandChar(){
        Random rand = new Random();

        if (randGenerator(0,2) == 0){
            return 'F';
        }
        if (randGenerator(0,2) == 1){
            return 'S';
        }
        if (randGenerator(0,2) == 2) {
            return 'M';
        }
        return 'F';
    }


}
