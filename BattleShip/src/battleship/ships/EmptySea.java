package battleship.ships;

/**
 * Describes a part of the ocean that doesn't have a ship in it.
 */
public class EmptySea
        extends Ship {

    /**
     * Create Empty sea in standart coordinates
     */
    public EmptySea() {
        length = 1;
        mark = false;
        hit = new boolean[1];
    }

    /**
     * Create Empty sea
     * @param column        coordinate of point (column)
     * @param row           coordinate of point(row)
     */
    public EmptySea(int row, int column) {
         length = 1;
        mark = false;
        hit = new boolean[1];
        bowRow = row;
        bowColumn =column;
    }


    @Override
    public String getShipType() {
        return "EmptySea";
    }

    @Override
    public String toString(){
        return isSunk()? "-":".";
    }
}
