package battleship;

import battleship.ships.*;

import java.util.Random;

/**
 * This contains a 10x10 array of Ships, representing the "ocean," and some methods to manipulate it.
 */
public class Ocean {

    //Instance variables

    public Ship[][] ships;
    int shotsFired;
    int hitCount;
    int shipsSunk;
    int undamagedShips;

    /**
     * Create empty ocean
     */
    public Ocean() {
        undamagedShips = 10;
        ships = new Ship[10][10];
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                ships[i][j] = new EmptySea(i,j);
        shotsFired = 0;
        shipsSunk = 0;
        hitCount = 0;
    }

    //Getters

    /**
     * Getter for statistic
     * @return number of shots
     */
    public int getShotsFired()
    {
        return shotsFired;
    }

    /**
     * Getter for statistic
     * @return number of hits
     */
    public int getHitCount()
    {
        return hitCount;
    }

    /**
     * Getter for statistic
     * @return number of sunk ships
     */
    public int getShipsSunk()
    {
        return shipsSunk;
    }

    /**
     * Getter for statistic
     * @return number of fired ships
     */
    public int getFireShips() {
        return 10 - shipsSunk - undamagedShips;
    }

    /**
     * Getter for statistic
     * @return number of undamaged ships
     */
    public int getUndamagedShips() {
        return undamagedShips;
    }

    /**
     * @return all points of ocean
     */
    public Ship[][] getShipArray() {
        return ships;
    }

    //Methods

    public boolean isReady(){
        int num = 0;
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                if(!ships[i][j].getShipType().equals("EmptySea"))
                    num++;
        return num == 20;
    }


    /**
     * Randomly place
     *      1 Battleship,
     *      2 Destroyers,
     *      3 Cruisers,
     *      4 Submarines
     * in the ocean.
     */
    public void placeAllShipsRandomly() {
        deleteAllShips();
        Random rand = new Random();
        int row, column;
        boolean horizontal;
        Ship shipToPlace;

        for(int i = 0; i < 10; i++){
            do {
                row = rand.nextInt(10);
                column = rand.nextInt(10);
                horizontal = rand.nextBoolean();
                shipToPlace = new EmptySea();
                if(i == 0 ) shipToPlace = new Battleship(row, column, horizontal);
                if(i >= 1 && i <= 2) shipToPlace = new Destroyer(row, column, horizontal);
                if(i >= 3 && i <= 5) shipToPlace = new Cruiser(row, column, horizontal);
                if(i >= 6) shipToPlace = new Submarine(row, column, horizontal);
            } while (!shipToPlace.okToPlaceShipAt(row,column,horizontal,this));
            if(i == 0) shipToPlace.placeShipAt(row,column,horizontal,this);
            if(i >= 1 && i <= 2) shipToPlace.placeShipAt(row,column,horizontal,this);
            if(i >= 3 && i <= 5) shipToPlace.placeShipAt(row,column,horizontal,this);
            if(i >= 6) shipToPlace.placeShipAt(row,column,horizontal,this);

        }
    }

    /**
     * Delete all parts of ship from that ocean
     * @param row       row number of one of parts of chosen ship
     * @param column    column number of one of parts of chosen ship
     * @return          length of deleted ship
     */
    public int deleteShip(int row, int column) {
        int bowRow = ships[row][column].getBowRow();
        int bowColumn = ships[row][column].getBowColumn();
        int lenght = ships[row][column].GetLength();
        boolean isHorizontal = ships[row][column].isHorizontal();
        for(int i = 0; i < lenght; i++) {
            ships[bowRow + (isHorizontal ? 0 : i)][bowColumn + (isHorizontal ? i : 0)] =
                    new EmptySea(bowRow + (isHorizontal ? 0 : i), bowColumn + (isHorizontal ? i : 0));
        }
        return lenght;
    }

    /**
     * Delete all ships from that ocean
     */
    public void deleteAllShips() {
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                ships[i][j] = new EmptySea(i, j);
    }
    /**
     * Check, is occupied that point.
     * @param row       Row of this point
     * @param column    Column of this point
     * @return  is it occupied.
     *      {true}   - occupied
     *      {false}  - not occupied
     */
    boolean isOccupied(int row, int column) {
        return !ships[row][column].getShipType().equals("EmptySea");
    }

    /**
     * If that point occupied, destroy that ship.
     * If this is sea, point it as "shooted"
     * Refresh the statistic.
     * @param row       Row of shooting point
     * @param column    Column of shooting point
     * @return          Success of the shoot
     *      {true} - it was not sunk ship
     *      {false} - another situation
     */
    public int shootAt(int row, int column) {
        int firedParts = 0;
        for(int i = 0; i < ships[row][column].GetLength(); i++)
            if(ships[row][column].getHit()[i])
                firedParts++;
        if(firedParts == 0 && !ships[row][column].getShipType().equals("EmptySea"))
            undamagedShips--;
        boolean result = ships[row][column].shootAt(row,column);
        if(result && ships[row][column].isSunk()) {
            shipsSunk++;
        }
        if(result)hitCount++;
        if(!result) shotsFired++;
        return whatIsHere(row, column);
    }

    /**
     * Mark that ship as sea (mark of that ship begun true)
     * @param row       Row of chosen point
     * @param column    Column of chosen point
     */
    public void markAsSea(int row, int column) {
        ships[row][column].setMark(true);
    }

    /**
     * @return is sunk all ships
     *      {true}      - Game over
     *      {false}     - Not yet
     */
    public boolean isGameOver() {
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                if(!ships[i][j].isSunk() && isOccupied(i,j)) return false;
        return true;
    }

    /**
     * Print with good structure visible for player ocean
     */
    public void testprint() {
        System.out.print("  |");
        for(int i = 0; i < 10; i++)
            System.out.print(i + "|");
        System.out.println();
        for(int i = 0; i < 10; i++){
            System.out.print("|" + i + "|");
            for(int j = 0; j < 10; j++) {
                System.out.print(ships[i][j].toString() + "|");
            }
            System.out.println();
        }
    }

    /**
     *
     * @param x row
     * @param y column
     * @return -2 - marked empty sea
     *         -1 - shooted empty sea
     *          0 - not shooted empty sea
     *          1 - sunk ship
     *          2 - fire ship
     *          3 - not touched ship
     */
    public int whatIsHere(int x, int y) {
        if (ships[x][y].isMark() && ships[x][y].getShipType().equals("EmptySea"))
            return -2;
        if (ships[x][y].isSunk() && ships[x][y].getShipType().equals("EmptySea"))
            return -1;
        if(ships[x][y].getShipType().equals("EmptySea"))
            return 0;
        if(ships[x][y].situation(x,y) == 2)
            return 1;
        if(ships[x][y].situation(x,y) == 1)
            return 2;
        return 3;
    }

}