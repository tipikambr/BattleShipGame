package battleship.ships;

import battleship.Ocean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    Ship emptySea;
    Ship submarine;
    Ship cruiser;
    Ship destroyer;
    Ship battleship;

    @BeforeEach
    void setup() {
        emptySea = new EmptySea(1,1);
        submarine = new Submarine(1, 3, true);
        cruiser = new Cruiser(3,1, true);
        destroyer = new Destroyer(3,5,true);
        battleship = new Battleship(5,1,true);
    }

    @Test
    void constructorsTest() {
        Ship[] emptySea;
        Ship[] submarines;
        Ship[] cruisers;
        Ship[] destroyers;
        Ship[] battleships;

        System.out.println("==========CONSTRUCTORS TEST==========");
        System.out.print("EMPTY SEA - - - - -");
        emptySea = new Ship[2];
        emptySea[0] = new EmptySea();
        emptySea[1] = new EmptySea(3,5);

        assertEquals(emptySea[0].bowColumn, 0);
        assertEquals(emptySea[0].bowRow, 0);
        assertEquals(emptySea[0].length, 1);
        assertFalse(emptySea[0].hit[0]);
        assertEquals(emptySea[0].hit.length, 1);

        assertEquals(emptySea[1].bowRow, 3);
        assertEquals(emptySea[1].bowColumn, 5);
        assertEquals(emptySea[1].length, 1);
        assertFalse(emptySea[1].hit[0]);
        assertEquals(emptySea[1].hit.length, 1);

        System.out.println("OK");
        System.out.print("SUBMARINE - - - - -");

        submarines = new Ship[2];
        submarines[0] = new Submarine();
        submarines[1] = new Submarine(3,5, true);


        assertEquals(submarines[0].bowColumn, 0);
        assertEquals(submarines[0].bowRow, 0);
        assertEquals(submarines[0].length, 1);
        assertFalse(submarines[0].hit[0]);
        assertEquals(submarines[0].hit.length, 1);

        assertEquals(emptySea[1].bowColumn, 5);
        assertEquals(emptySea[1].bowRow, 3);
        assertEquals(emptySea[1].length, 1);
        assertFalse(emptySea[1].hit[0]);
        assertEquals(emptySea[1].hit.length, 1);

        System.out.println("OK");
        System.out.print("CRUISER - - - - -");

        cruisers = new Ship[3];
        cruisers[0] = new Cruiser();
        cruisers[1] = new Cruiser(3,5, true);
        cruisers[2] = new Cruiser(3,5, false);

        assertEquals(cruisers[0].bowColumn, 0);
        assertEquals(cruisers[0].bowRow, 0);
        assertEquals(cruisers[0].length, 2);
        assertFalse(cruisers[0].hit[0]);
        assertEquals(cruisers[0].hit.length, 2);

        assertEquals(cruisers[1].bowColumn, 5);
        assertEquals(cruisers[1].bowRow, 3);
        assertEquals(cruisers[1].length, 2);
        assertFalse(cruisers[1].hit[0]);
        assertEquals(cruisers[1].hit.length, 2);

        assertEquals(cruisers[2].bowColumn, 5);
        assertEquals(cruisers[2].bowRow, 3);
        assertEquals(cruisers[2].length, 2);
        assertFalse(cruisers[2].hit[0]);
        assertEquals(cruisers[2].hit.length, 2);

        System.out.println("OK");
        System.out.print("DESTROYER - - - - -");

        destroyers = new Ship[3];
        destroyers[0] = new Destroyer();
        destroyers[1] = new Destroyer(3,5, true);
        destroyers[2] = new Destroyer(3,5, false);

        assertEquals(destroyers[0].bowColumn, 0);
        assertEquals(destroyers[0].bowRow, 0);
        assertEquals(destroyers[0].length, 3);
        assertFalse(destroyers[0].hit[0]);
        assertEquals(destroyers[0].hit.length, 3);

        assertEquals(destroyers[1].bowColumn, 5);
        assertEquals(destroyers[1].bowRow, 3);
        assertEquals(destroyers[1].length, 3);
        assertFalse(destroyers[1].hit[0]);
        assertEquals(destroyers[1].hit.length, 3);

        assertEquals(destroyers[2].bowColumn, 5);
        assertEquals(destroyers[2].bowRow, 3);
        assertEquals(destroyers[2].length, 3);
        assertFalse(destroyers[2].hit[0]);
        assertEquals(destroyers[2].hit.length, 3);


        System.out.println("OK");
        System.out.print("BATTLESHIP - - - - -");


        battleships = new Ship[3];
        battleships[0] = new Battleship();
        battleships[1] = new Battleship(3,5, true);
        battleships[2] = new Battleship(3,5, false);

        assertEquals(battleships[0].bowColumn, 0);
        assertEquals(battleships[0].bowRow, 0);
        assertEquals(battleships[0].length, 4);
        assertFalse(battleships[0].hit[0]);
        assertEquals(battleships[0].hit.length, 4);

        assertEquals(battleships[1].bowColumn, 5);
        assertEquals(battleships[1].bowRow, 3);
        assertEquals(battleships[1].length, 4);
        assertFalse(battleships[1].hit[0]);
        assertEquals(battleships[1].hit.length, 4);

        assertEquals(battleships[2].bowColumn, 5);
        assertEquals(battleships[2].bowRow, 3);
        assertEquals(battleships[2].length, 4);
        assertFalse(battleships[2].hit[0]);
        assertEquals(battleships[2].hit.length, 4);

        System.out.println("OK");
    }

    @Test
    void getTypeTest() {
        System.out.println("==========GET TYPE TEST==========");
        System.out.print("EMPTY SEA - - - - -");

        assertEquals(emptySea.getShipType(), "EmptySea");
        assertEquals(emptySea.toString(), ".");
        emptySea.hit[0] = true;
        assertEquals(emptySea.toString(), "-");

        System.out.println("OK");
        System.out.print("SUBMARINE - - - - -");

        assertEquals(submarine.getShipType(), "Submarine");
        assertEquals(submarine.toString(), "S");
        submarine.hit[0] = true;
        assertEquals(submarine.toString(), "X");

        System.out.println("OK");
        System.out.print("CRUISER - - - - -");

        assertEquals(cruiser.getShipType(), "Cruiser");
        assertEquals(cruiser.toString(), "S");
        for(int i = 0; i < cruiser.GetLength(); i++)
            cruiser.hit[i] = true;
        assertEquals(cruiser.toString(), "X");

        System.out.println("OK");
        System.out.print("DESTROYER - - - - -");

        assertEquals(destroyer.getShipType(), "Destroyer");
        assertEquals(destroyer.toString(), "S");
        for(int i = 0; i < destroyer.GetLength(); i++)
            destroyer.hit[i] = true;
        assertEquals(destroyer.toString(), "X");

        System.out.println("OK");
        System.out.print("BATTLESHIP - - - - -");

        assertEquals(battleship.getShipType(), "Battleship");
        assertEquals(battleship.toString(), "S");
        for(int i = 0; i < battleship.GetLength(); i++)
            battleship.hit[i] = true;
        assertEquals(battleship.toString(), "X");

        System.out.println("OK");
    }

    @Test
    void gettersTest() {
        System.out.println("==========GETTERS TEST==========");
        System.out.print("EMPTY SEA - - - - -");

        assertEquals(emptySea.getBowColumn(), 1);
        assertEquals(emptySea.getBowRow(), 1);
        assertEquals(emptySea.getHit().length, 1);
        for(int i = 0; i < emptySea.getHit().length; i++)
            assertEquals(emptySea.getHit()[i], emptySea.hit[i]);
        assertFalse(emptySea.isHorizontal());
        assertFalse(emptySea.isMark());

        System.out.println("OK");
        System.out.print("SUBMARINE - - - - -");

        assertEquals(submarine.getBowColumn(), 3);
        assertEquals(submarine.getBowRow(), 1);
        assertEquals(submarine.getHit().length, 1);
        for(int i = 0; i < submarine.getHit().length; i++)
            assertEquals(submarine.getHit()[i], submarine.hit[i]);
        assertTrue(submarine.isHorizontal());
        assertFalse(submarine.isMark());

        System.out.println("OK");
        System.out.print("CRUISER - - - - -");

        assertEquals(cruiser.getBowColumn(), 1);
        assertEquals(cruiser.getBowRow(), 3);
        assertEquals(cruiser.getHit().length, 2);
        for(int i = 0; i < cruiser.getHit().length; i++)
            assertEquals(cruiser.getHit()[i], cruiser.hit[i]);
        assertTrue(cruiser.isHorizontal());
        assertFalse(cruiser.isMark());

        System.out.println("OK");
        System.out.print("DESTROYER - - - - -");

        assertEquals(destroyer.getBowColumn(), 5);
        assertEquals(destroyer.getBowRow(), 3);
        assertEquals(destroyer.getHit().length, 3);
        for(int i = 0; i < destroyer.getHit().length; i++)
            assertEquals(destroyer.getHit()[i], destroyer.hit[i]);
        assertTrue(destroyer.isHorizontal());
        assertFalse(destroyer.isMark());

        System.out.println("OK");
        System.out.print("BATTLESHIP - - - - -");

        assertEquals(battleship.getBowColumn(), 1);
        assertEquals(battleship.getBowRow(), 5);
        assertEquals(battleship.getHit().length, 4);
        for(int i = 0; i < battleship.getHit().length; i++)
            assertEquals(battleship.getHit()[i], battleship.hit[i]);
        assertTrue(battleship.isHorizontal());
        assertFalse(battleship.isMark());

        System.out.println("OK");
    }

    @Test
    void settersTest() {
        boolean ok;

        System.out.println("==========SETTERS TEST==========");
        System.out.print("EMPTY SEA - - - - -");
        try {
            ok = false;
            emptySea.setBowRow(-1);
        } catch (Exception e){ok = true;}
        assertTrue(ok);
        try {
            ok = false;
            emptySea.setBowColumn(50);
        } catch (Exception e){ok = true;}
        assertTrue(ok);
        emptySea.setBowColumn(0);
        emptySea.setBowRow(0);
        emptySea.setMark(true);
        emptySea.setHorizontal(true);
        assertEquals(emptySea.getBowColumn(), 0);
        assertEquals(emptySea.getBowRow(), 0);
        assertTrue(emptySea.mark);
        assertTrue(emptySea.horisontal);

        System.out.println("OK");
        System.out.print("SUBMARINE - - - - -");

        submarine.setBowColumn(0);
        submarine.setBowRow(0);
        submarine.setMark(true);
        submarine.setHorizontal(false);
        assertEquals(submarine.getBowColumn(), 0);
        assertEquals(submarine.getBowRow(), 0);
        assertTrue(submarine.mark);
        assertFalse(submarine.horisontal);


        System.out.println("OK");
        System.out.print("CRUISER - - - - -");

        cruiser.setBowColumn(0);
        cruiser.setBowRow(0);
        cruiser.setMark(true);
        cruiser.setHorizontal(false);
        assertEquals(cruiser.getBowColumn(), 0);
        assertEquals(cruiser.getBowRow(), 0);
        assertTrue(cruiser.mark);
        assertFalse(cruiser.horisontal);

        System.out.println("OK");
        System.out.print("DESTROYER - - - - -");

        destroyer.setBowColumn(0);
        destroyer.setBowRow(0);
        destroyer.setMark(true);
        destroyer.setHorizontal(false);
        assertEquals(destroyer.getBowColumn(), 0);
        assertEquals(destroyer.getBowRow(), 0);
        assertTrue(destroyer.mark);
        assertFalse(destroyer.horisontal);

        System.out.println("OK");
        System.out.print("BATTLESHIP - - - - -");

        battleship.setBowColumn(0);
        battleship.setBowRow(0);
        battleship.setMark(true);
        battleship.setHorizontal(false);
        assertEquals(battleship.getBowColumn(), 0);
        assertEquals(battleship.getBowRow(), 0);
        assertTrue(battleship.mark);
        assertFalse(battleship.horisontal);

        System.out.println("OK");
    }

    @Test
    void shootingTest() {
        System.out.println("==========SHOOTING TEST==========");
        System.out.print("EMPTY SEA - - - - -");

        emptySea.shootAt(1,1);
        assertTrue(emptySea.hit[0]);

        System.out.println("OK");
        System.out.print("SUBMARINE - - - - -");

        submarine.shootAt(1,3);
        assertTrue(submarine.hit[0]);

        System.out.println("OK");
        System.out.print("CRUISER - - - - -");

        cruiser.shootAt(3,2);
        assertFalse(cruiser.hit[0]);
        assertTrue(cruiser.hit[1]);

        System.out.println("OK");
        System.out.print("DESTROYER - - - - -");

        destroyer.shootAt(3,7);
        assertFalse(destroyer.hit[0]);
        assertFalse(destroyer.hit[1]);
        assertTrue(destroyer.hit[2]);

        System.out.println("OK");
        System.out.print("BATTLESHIP - - - - -");

        battleship.shootAt(5,2);
        assertFalse(battleship.hit[0]);
        assertTrue(battleship.hit[1]);
        assertFalse(battleship.hit[2]);
        assertFalse(battleship.hit[3]);

        System.out.println("OK");
    }

    @Test
    void situationTest() {
        System.out.println("==========SITUATION TEST==========");
        System.out.print("EMPTY SEA - - - - -");

        assertEquals(emptySea.situation(1, 1), 0);
        emptySea.shootAt(1,1);
        assertEquals(emptySea.situation(1, 1), 2);

        System.out.println("OK");
        System.out.print("SUBMARINE - - - - -");

        assertEquals(submarine.situation(1,3), 0);
        submarine.shootAt(1,3);
        assertEquals(submarine.situation(1,3), 2);

        System.out.println("OK");
        System.out.print("CRUISER - - - - -");

        assertEquals(cruiser.situation(3,2),0);
        cruiser.shootAt(3,2);
        assertEquals(cruiser.situation(3,2),1);

        System.out.println("OK");
        System.out.print("DESTROYER - - - - -");

        assertEquals(destroyer.situation(3,7), 0);
        destroyer.shootAt(3,7);
        assertEquals(destroyer.situation(3,7), 1);

        System.out.println("OK");
        System.out.print("BATTLESHIP - - - - -");

        assertEquals(battleship.situation(5,2), 0);
        battleship.shootAt(5,2);
        assertEquals(battleship.situation(5,2), 1);

        System.out.println("OK");
    }

    @Test
    void placeShipInOceanTest() {
        Ocean ocean = new Ocean();
        System.out.println("==========PLACE SHIP TEST==========");
        System.out.print("EMPTY SEA - - - - -");

        assertTrue(emptySea.okToPlaceShipAt(emptySea.bowRow, emptySea.bowColumn, emptySea.horisontal, ocean));
        assertTrue(emptySea.okToPlaceShipAt(emptySea.bowRow, emptySea.bowColumn, emptySea.horisontal, ocean));

        ocean.testprint();

        System.out.println("OK");
        System.out.print("SUBMARINE - - - - -");

        assertTrue(submarine.okToPlaceShipAt(submarine.bowRow, submarine.bowColumn, submarine.horisontal, ocean));
        submarine.placeShipAt(submarine.bowRow, submarine.bowColumn, submarine.horisontal, ocean);
        assertEquals(ocean.ships[submarine.bowRow][submarine.bowColumn], submarine);
        assertNotEquals(ocean.ships[submarine.bowRow + (submarine.isHorizontal()? 0 : 1)][submarine.bowColumn + (submarine.isHorizontal()? 1 : 0)], submarine);
        assertFalse(submarine.okToPlaceShipAt(submarine.bowRow, submarine.bowColumn, submarine.horisontal, ocean));

        ocean.testprint();

        System.out.println("OK");
        System.out.print("CRUISER - - - - -");

        assertTrue(cruiser.okToPlaceShipAt(cruiser.bowRow, cruiser.bowColumn, cruiser.horisontal, ocean));
        cruiser.placeShipAt(cruiser.bowRow, cruiser.bowColumn, cruiser.horisontal, ocean);
        assertEquals(ocean.ships[cruiser.bowRow][cruiser.bowColumn], cruiser);
        assertEquals(ocean.ships[cruiser.bowRow + (cruiser.isHorizontal()? 0 : 1)][cruiser.bowColumn + (cruiser.isHorizontal()? 1 : 0)], cruiser);
        assertNotEquals(ocean.ships[cruiser.bowRow + (cruiser.isHorizontal()? 0 : 2)][cruiser.bowColumn + (cruiser.isHorizontal()? 2 : 0)], cruiser);
        assertFalse(cruiser.okToPlaceShipAt(cruiser.bowRow, cruiser.bowColumn, cruiser.horisontal, ocean));

        ocean.testprint();

        System.out.println("OK");
        System.out.print("DESTROYER - - - - -");

        assertTrue(destroyer.okToPlaceShipAt(destroyer.bowRow, destroyer.bowColumn, destroyer.horisontal, ocean));
        destroyer.placeShipAt(destroyer.bowRow, destroyer.bowColumn, destroyer.horisontal, ocean);
        assertEquals(ocean.ships[destroyer.bowRow][destroyer.bowColumn], destroyer);
        assertEquals(ocean.ships[destroyer.bowRow + (destroyer.isHorizontal()? 0 : 1)][destroyer.bowColumn + (destroyer.isHorizontal()? 1 : 0)], destroyer);
        assertEquals(ocean.ships[destroyer.bowRow + (destroyer.isHorizontal()? 0 : 2)][destroyer.bowColumn + (destroyer.isHorizontal()? 2 : 0)], destroyer);
        assertNotEquals(ocean.ships[destroyer.bowRow + (destroyer.isHorizontal()? 0 : 3)][destroyer.bowColumn + (destroyer.isHorizontal()? 3 : 0)], destroyer);
        assertFalse(destroyer.okToPlaceShipAt(destroyer.bowRow, destroyer.bowColumn, destroyer.horisontal, ocean));

        ocean.testprint();

        System.out.println("OK");
        System.out.print("BATTLESHIP - - - - -");

        assertTrue(battleship.okToPlaceShipAt(battleship.bowRow, battleship.bowColumn, battleship.horisontal, ocean));
        battleship.placeShipAt(battleship.bowRow, battleship.bowColumn, battleship.horisontal, ocean);
        assertEquals(ocean.ships[battleship.bowRow][battleship.bowColumn], battleship);
        assertEquals(ocean.ships[battleship.bowRow + (battleship.isHorizontal()? 0 : 1)][battleship.bowColumn + (battleship.isHorizontal()? 1 : 0)], battleship);
        assertEquals(ocean.ships[battleship.bowRow + (battleship.isHorizontal()? 0 : 2)][battleship.bowColumn + (battleship.isHorizontal()? 2 : 0)], battleship);
        assertEquals(ocean.ships[battleship.bowRow + (battleship.isHorizontal()? 0 : 3)][battleship.bowColumn + (battleship.isHorizontal()? 3 : 0)], battleship);
        assertNotEquals(ocean.ships[battleship.bowRow + (battleship.isHorizontal()? 0 : 4)][battleship.bowColumn + (battleship.isHorizontal()? 4 : 0)], battleship);
        assertFalse(battleship.okToPlaceShipAt(battleship.bowRow, battleship.bowColumn, battleship.horisontal, ocean));

        ocean.testprint();

        System.out.println("OK");
    }
}