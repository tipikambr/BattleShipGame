package battleship.ships;

/**
 * Describes a ship of length 4.
 */
public class Battleship
        extends Ship {

    /**
     * Create empty Battleship
     */
    public Battleship(){
        length = 4;
        mark = false;
        hit = new boolean[length];
        for(int i = 0; i < length; i++)
            hit[i] = false;
    }

    /**
     * Create Battleship
     * @param row           bow row
     * @param column        bow column
     * @param horizontal    is horizontal this ship
     */
    public Battleship(int row, int column, boolean horizontal){
        length = 4;
          mark = false;
        hit = new boolean[length];
        for(int i = 0; i < length; i++)
            hit[i] = false;
        bowRow = row;
        bowColumn = column;
        setHorizontal(horizontal);
    }

    @Override
    public String getShipType() {
        return "Battleship";
    }
}
