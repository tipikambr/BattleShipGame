package battleship.ships;

/**
 * Describes a ship of length 1.
 */
public class Submarine
        extends Ship
{
    /**
     * Create empty Submarine
     */
    public Submarine() {
        length = 1;
        mark = false;
        hit = new boolean[length];
        hit[0] = false;
    }

    /**
     * Create Submarine
     * @param row           bow row
     * @param column        bow column
     * @param horizontal    is horizontal this ship
     */
    public Submarine(int row, int column, boolean horizontal){
        length = 1;
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
        return "Submarine";
    }

}
