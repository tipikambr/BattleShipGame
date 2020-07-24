package battleship.ships;

/**
 * Describes a ship of length 2.
 */
public class Cruiser
        extends Ship {

    /**
     *  Create Cruiser with empty variables
     */
    public Cruiser() {
        length = 2;
        mark = false;
        hit = new boolean[length];
        for (int i = 0; i < length; i++)
            hit[i] = false;
    }

    /**
     * Create Cruiser
     * @param row           bow row
     * @param column        bow column
     * @param horizontal    is horizontal this ship
     */
    public Cruiser(int row,int column, boolean horizontal){
        length = 2;
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
        return "Cruiser";
    }
}
