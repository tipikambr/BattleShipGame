package battleship;

import battleship.ships.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OceanTest {
    Ocean ocean;

    Submarine[] submarines;
    Cruiser[] cruisers;
    Destroyer[] destroyers;
    Battleship battleship;
    @BeforeEach
    void setup() {
        ocean = new Ocean();
        submarines = new Submarine[4];
        submarines[0] = new Submarine(1,1, true);
        submarines[1] = new Submarine(1,3, true);
        submarines[2] = new Submarine(1,6, true);
        submarines[3] = new Submarine(1,8, true);
        cruisers = new Cruiser[3];
        cruisers[0] = new Cruiser(3,0, false);
        cruisers[1] = new Cruiser(3,2, false);
        cruisers[2] = new Cruiser(3,4, true);
        destroyers = new Destroyer[2];
        destroyers[0] = new Destroyer(5, 4, true);
        destroyers[1] = new Destroyer(3, 8, false);
        battleship = new Battleship(6, 1, false);
        for(int i = 0; i < 4; i++)
            submarines[i].placeShipAt(submarines[i].getBowRow(), submarines[i].getBowColumn(), submarines[i].isHorizontal(), ocean);
        for(int i = 0; i < 3; i++)
            cruisers[i].placeShipAt(cruisers[i].getBowRow(), cruisers[i].getBowColumn(), cruisers[i].isHorizontal(), ocean);
        for(int i = 0; i < 2; i++)
            destroyers[i].placeShipAt(destroyers[i].getBowRow(), destroyers[i].getBowColumn(), destroyers[i].isHorizontal(), ocean);
        battleship.placeShipAt(battleship.getBowRow(), battleship.getBowColumn(), battleship.isHorizontal(), ocean);
    }

    @Test
    void construcortsTest() {
        System.out.println("==========CONSTRUCTORS TEST==========");

        ocean = new Ocean();
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10 ;j++)
                assertEquals(ocean.ships[i][j].getShipType(), "EmptySea");
    }

    @Test
    void gettersTest() {
        System.out.println("==========GETTERS TEST==========");

        assertEquals(ocean.getShotsFired(), ocean.shotsFired);
        assertEquals(ocean.getFireShips(), 0);
        assertEquals(ocean.getHitCount(), ocean.hitCount);
        assertEquals(ocean.getShipsSunk(), ocean.shipsSunk);
        assertEquals(ocean.getUndamagedShips(), ocean.undamagedShips);
        assertEquals(ocean.getShipArray(), ocean.ships);
    }

    @Test
    void placeAllShipRandomlyTest() {
        System.out.println("==========GENERATING SEA TEST==========");

        for(int i = 0; i < 1000; i++)
        {
            int sea = 0;
            int sub = 0;
            int cru = 0;
            int des = 0;
            int bat = 0;
            int another = 0;
            ocean.placeAllShipsRandomly();
            for(int j = 0; j < 10; j++)
                for(int k = 0; k < 10; k++){
                    switch (ocean.getShipArray()[j][k].getShipType()){
                        case "EmptySea":
                            sea++;
                            break;
                        case "Submarine":
                            sub++;
                            break;
                        case "Cruiser":
                            cru++;
                            break;
                        case "Destroyer":
                            des++;
                            break;
                        case "Battleship":
                            bat++;
                            break;
                        default:
                            another++;
                            break;
                    }
                }
            assertEquals(sea, 80);
            assertEquals(sub, 4);
            assertEquals(cru, 6);
            assertEquals(des, 6);
            assertEquals(bat, 4);
            assertEquals(another, 0);
        }
    }

    @Test
    void DeleteTest() {
        System.out.println("==========DELETE ONE SHIP TEST==========");
        ocean.testprint();
        System.out.println();

        ocean.deleteShip(3,2);
        assertEquals(ocean.getShipArray()[3][2].getShipType(), "EmptySea");
        assertEquals(ocean.getShipArray()[4][2].getShipType(), "EmptySea");

        ocean.deleteShip(5,5);
        assertEquals(ocean.getShipArray()[5][5].getShipType(), "EmptySea");
        assertEquals(ocean.getShipArray()[5][4].getShipType(), "EmptySea");
        assertEquals(ocean.getShipArray()[5][6].getShipType(), "EmptySea");

        ocean.testprint();
        System.out.println();
        System.out.println("==========DELETE ALL TEST==========");

        ocean.deleteAllShips();
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                assertEquals(ocean.getShipArray()[i][j].getShipType(), "EmptySea");

        ocean.testprint();
        System.out.println();
    }

    @Test
    void whatHereTest() {
        System.out.println("==========IS OCCUPED TEST==========");

        assertTrue(ocean.isOccupied(1,1));
        assertTrue(ocean.isOccupied(9,1));
        assertTrue(ocean.isOccupied(4,0));
        assertTrue(ocean.isOccupied(8,1));
        assertTrue(ocean.isOccupied(5,5));

        assertFalse(ocean.isOccupied(0,0));
        assertFalse(ocean.isOccupied(9,9));
        assertFalse(ocean.isOccupied(2,2));

        System.out.println("==========WHAT IS HERE TEST==========");

        ocean.shootAt(1,1);
        ocean.shootAt(8,1);
        ocean.shootAt(0,0);
        ocean.markAsSea(0,1);

        assertEquals(ocean.whatIsHere(0,0), -1);
        assertEquals(ocean.whatIsHere(1,1), 1);
        assertEquals(ocean.whatIsHere(0,1), -2);
        assertEquals(ocean.whatIsHere(8,1), 2);
        assertEquals(ocean.whatIsHere(7,1), 3);
        assertEquals(ocean.whatIsHere(1,0), 0);

    }

    @Test
    void gameOverTest() {
        System.out.println("==========GAME OVER TEST==========");
        boolean ok;
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++) {
                ocean.shootAt(i, j);
                ok = true;
                for (int k = 0; k < 4; k++)
                    if (!submarines[k].isSunk())
                        ok = false;
                ;
                for (int k = 0; k < 3; k++)
                    if (!cruisers[k].isSunk())
                        ok = false;
                ;
                for (int k = 0; k < 2; k++)
                    if (!destroyers[k].isSunk())
                        ok = false;
                if(!battleship.isSunk())
                    ok = false;
                assertEquals(ocean.isGameOver(), ok);
            }
    }

    @Test
    void EmptyTest() {
        System.out.println("==========Empty TEST==========");

    }
}