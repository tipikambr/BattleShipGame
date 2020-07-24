package battleship.ships;

import battleship.Ocean;

import java.util.Objects;

/**
 * This describes characteristics common to all the ships.
 */
public abstract class Ship {


    int bowRow;
    int bowColumn;
    int length;
    boolean mark;
    boolean horisontal;
    boolean [] hit;

    //Getters

    /**
     * @return length of ship
     */
    public  int GetLength(){ return length;}

    /**
     * @return coordinate of bow this ship (row)
     */
    public int getBowRow()
    {
        return bowRow;
    }

    /**
     * @return coordinate of bow this ship (column)
     */
    public int getBowColumn()
    {
        return bowColumn;
    }

    /**
     * @return array with information about parts of ship.
     *      {true} -  destroyed part
     *      {false} - whole part
     */
    public boolean[] getHit()
    {
        return hit;
    }

    /**
     * @return position of ship.
     *      {true} - ship is horizontal
     *      {false} - ship is vertical
     */
    public boolean isHorizontal(){
        return horisontal;
    }

    public abstract String getShipType();

    //Setters

    /**
     * Setup the coordinate of bow this ship (row)
     * @param row must be >= 0 and <= 9.
     */
    void setBowRow(int row) {
        if(row < 0 || row > 9)
            throw new IllegalArgumentException();
        bowRow = row;
    }

    /**
     * Setup the coordinate of bow this ship (column)
     * @param column must be >= 0 and <= 9.
     */
    void setBowColumn(int column) {
        if(column < 0 || column > 9)
            throw new IllegalArgumentException();
        bowColumn = column;
    }

    /**
     * Setup the position of this ship
     * @param horizontal
     *      {true} - ship is horizontal
     *      {false} - ship is vertical
     */
    void setHorizontal(boolean horizontal) {
        this.horisontal = horizontal;
    }

    /**
     * Make that part of ship marked or unmarked
     * @param mark
     *      {true} - that place become marked
     *      {false} - that place become unmarked
     */
    public void setMark(boolean mark)
    {
        this.mark = mark;
    }
    //Methods

    /**
     * Check: is it possible to set ship in this place
     * @param row           coordinate of bow this ship (row)
     * @param column        coordinate of bow this ship (column)
     * @param horizontal    position of this ship
     * @param ocean         the ocean, where you want to place this ship
     * @return              is possible to place the ship
     *      {true} -    possible
     *      {false} -   impossible
     */
    public boolean okToPlaceShipAt(int row, int column, boolean horizontal, Ocean ocean) {
        if(this.GetLength() + (horizontal?column:row) > 10) return false;
        for(int i = -1; i <= this.GetLength(); i++)
            for(int j = -1; j <= 1; j++){
                if(row + (horizontal?j:i) < 0 || row + (horizontal?j:i) >= 10 ||
                        column + (horizontal?i:j) < 0 || column + (horizontal?i:j) >= 10) continue;
                if(!ocean.getShipArray()[row + (horizontal?j:i)][column + (horizontal?i:j)].getShipType().equals("EmptySea")){
                    return false;
                }
            }
        return true;
    }

    /**
     * Place this ship to the ocean.
     * @param row           coordinate of bow this ship (row)
     * @param column        coordinate of bow this ship (column)
     * @param horizontal    position of this ship
     * @param ocean         the ocean, where you want to place this ship
     */
    public void placeShipAt(int row, int column, boolean horizontal, Ocean ocean) {
        bowColumn = column;
        bowRow = row;
        this.horisontal = horizontal;
        for(int i = 0; i < this.GetLength(); i++)
            ocean.getShipArray()[row + (horizontal ? 0 : i)][column + (horizontal ? i : 0)] = this;

    }

    /**
     *
     * @param row       coordinate of point, which was fired.
     * @param column    coordinate of point, which was fired.
     * @return          is it hit
     *      {true} - hit
     *      {false} - not hit
     */
    public boolean shootAt(int row, int column) {
        if(this.isSunk()) return false;
        if (this.getShipType().equals("EmptySea")){
            this.hit[0] = true;
            return false;
        }
        this.hit[Math.abs(row-bowRow+column-bowColumn)] = true;
        return true;
    }

    /**
     * @return is that ship is sunk
     *      {true} - sunk
     *      {false} - not sunk
     */
    public boolean isSunk() {
        for(int i = 0; i < length; i++)
            if(!hit[i])
                return false;
        return  true;
    }

    /**
     * @return is marked that ship
     */
    public boolean isMark() {
        return mark;
    }

    /**
     * @param row       coordinate of interested point.
     * @param column    coordinate of interested point.
     * @return situation:
     *          {2} - ship is sunk
     *          {1} - that part of ship in fire
     *          {0} - else
     */
    public int situation(int row, int column) {
        if(isSunk())
            return 2;
        if (this.hit[Math.abs(row-bowRow+column-bowColumn)])
            return 1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return bowRow == ship.bowRow &&
                bowColumn == ship.bowColumn &&
                length == ship.length &&
                horisontal == ship.horisontal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bowRow, bowColumn, length, horisontal);
    }

    @Override
    public String toString() {
        return isSunk()?"X":"S";
    }
}
