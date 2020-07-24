package battleship.ships;

/**
 * Describes a ship of length 3.
 */
public class Destroyer
    extends  Ship {

    /**
     * Create empty Destroyer
     */
    public Destroyer(){
        length = 3;
        mark = false;
        hit = new boolean[length];
        for(int i = 0; i < length; i++)
            hit[i] = false;
    }

    /**
     * Create Destroyer
     * @param row           bow row
     * @param column        bow column
     * @param horizontal    is horizontal this ship
     */
    public Destroyer(int row, int column, boolean horizontal){
        length = 3;
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
        return "Destroyer";
    }
}
