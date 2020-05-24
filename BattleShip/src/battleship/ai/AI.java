package battleship.ai;

import battleship.Ocean;
import battleship.resources.styles.Styles;
import battleship.ships.*;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    static Ocean myOcean;
    static int[] opponentShips = new int[4];
    static int[][] opponentOcean = new int[10][10];
    static Random rand = new Random();

    public static void startGameAI() {
        myOcean = new Ocean();
        generateField();
        for (int i = 0; i < 4; i++)
            opponentShips[i] = 4 - i;
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                opponentOcean[i][j] = 0;
    }

    private static void generateField() {
        Ship ship;
        myOcean = new Ocean();
        if (rand.nextInt(100) > 84) {
            //Четерыхпалубный не у края, тройки рядом
            if (rand.nextInt(100) > 50) {
                //Чеырехпалубный горизонтальный
                if (rand.nextInt(100) > 50) {
                    //Четырехпалубный сверху
                    int y = 2;
                    int x = rand.nextInt(6);

                    ship = new Battleship(y, x, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                    y = 0;
                    x = rand.nextInt(3);

                    ship = new Destroyer(y, x, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    x = 9 - 3 - rand.nextInt(3 - x);
                    ship = new Destroyer(y, x, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                } else {
                    //Четырехпалубный снизу
                    int y = 7;
                    int x = rand.nextInt(6);
                    ship = new Battleship(y, x, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                    y = 9;
                    x = rand.nextInt(3);

                    ship = new Destroyer(y, x, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    x = 9 - 3 - rand.nextInt(3 - x);
                    ship = new Destroyer(y, x, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                }
            } else {
                if (rand.nextInt(100) > 50) {
                    //Четырехпалубный справа
                    int y = rand.nextInt(6);
                    int x = 7;
                    ship = new Battleship(y, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                    x = 9;
                    y = rand.nextInt(3);

                    ship = new Destroyer(y, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    y = 9 - 3 - rand.nextInt(3 - y);
                    ship = new Destroyer(y, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                } else {
                    //Четырехпалубный слева
                    int y = rand.nextInt(6);
                    int x = 2;
                    ship = new Battleship(y, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                    x = 0;
                    y = rand.nextInt(3);

                    ship = new Destroyer(y, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    y = 9 - 3 - rand.nextInt(3 - y);
                    ship = new Destroyer(y, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                }
            }

            //Считаю количество двоек, которые могут быть где угодно
            int num = 0;
            while (rand.nextInt(100) > 65)
                num++;

            int unNum = 3 - num;

            //Ставлю двойки обязательно у границ
            while (unNum > 0)
                if (rand.nextInt(100) > 50)
                    if (rand.nextInt(100) > 50) {
                        //Cверху горизонтальная двойка
                        int y = 0;
                        int x = rand.nextInt(8);
                        ship = new Cruiser(y, x, true);
                        if (ship.okToPlaceShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean)) {
                            ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                            unNum--;
                        }
                    } else {
                        //Снизу горизонтальная двойка
                        int y = 9;
                        int x = rand.nextInt(8);
                        ship = new Cruiser(y, x, true);
                        if (ship.okToPlaceShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean)) {
                            ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                            unNum--;
                        }
                    }
                else if (rand.nextInt(100) > 50) {
                    //Справа вертикальная двойка
                    int y = rand.nextInt(8);
                    int x = 9;
                    ship = new Cruiser(y, x, false);
                    if (ship.okToPlaceShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean)) {
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                        unNum--;
                    }
                } else {
                    //Слева вертикальная двойка
                    int y = rand.nextInt(8);
                    int x = 0;
                    ship = new Cruiser(y, x, false);
                    if (ship.okToPlaceShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean)) {
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                        unNum--;
                    }
                }

            //Расставляю оставшиеся двойки случайно
            while (num > 0) {
                int x = rand.nextInt(10);
                int y = rand.nextInt(10);
                boolean isHorisonal = rand.nextBoolean();
                ship = new Cruiser(y, x, isHorisonal);
                if (ship.okToPlaceShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean)) {
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                    num--;
                }
            }

            //Случайная генерация единичек
            num = 4;
            while (num > 0) {
                int x = rand.nextInt(10);
                int y = rand.nextInt(10);
                ship = new Submarine(y, x, true);
                if (ship.okToPlaceShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean)) {
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                    num--;
                }
            }

        } else if (rand.nextInt(85) > 59) {
            //Куча-мала без единичек
            if (rand.nextInt(100) > 50)
                //Гортзонтальная
                if (rand.nextInt(100) > 50) {
                    //Куча-мала сверху
                    if (rand.nextInt(100) > 50) {
                        //Левая
                        int y = 0;
                        int x = 0;

                        ship = new Battleship(y, x, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Destroyer(y, x + 5, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Destroyer(y + 2, 0, false);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Cruiser(y, 9, false);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Cruiser(y + 2, x + 2, false);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Cruiser(y + 2, x + 4, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                    } else {
                        //Правая
                        int y = 0;
                        int x = 6;

                        ship = new Battleship(y, x, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Destroyer(y, x - 4, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Destroyer(y + 2, 9, false);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Cruiser(y, 0, false);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Cruiser(y + 2, x - 3, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Cruiser(y + 2, x, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                    }
                } else {
                    //Куча-мала снизу
                    if (rand.nextInt(100) > 50) {
                        //Левая
                        int y = 9;
                        int x = 0;

                        ship = new Battleship(y, x, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Destroyer(y, x + 5, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Destroyer(y - 4, 0, false);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Cruiser(y - 1, 9, false);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Cruiser(y - 3, x + 2, false);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Cruiser(y - 2, x + 4, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                    } else {
                        //Правая
                        int y = 9;
                        int x = 6;

                        ship = new Battleship(y, x, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Destroyer(y, x - 4, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Destroyer(y - 4, 9, false);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Cruiser(y - 1, 0, false);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Cruiser(y - 2, x, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                        ship = new Cruiser(y - 2, x - 4, true);
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                    }
                }
            else if (rand.nextInt(100) > 50) {
                //Куча-мала слева
                if (rand.nextInt(100) > 50) {
                    //Сверху
                    int x = 0;
                    int y = 0;

                    ship = new Battleship(y, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Destroyer(y + 5, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Destroyer(y, x + 2, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Cruiser(y + 2, x + 2, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Cruiser(y + 4, x + 2, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Cruiser(9, x, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                } else {
                    //Снизу
                    int x = 0;
                    int y = 9;

                    ship = new Battleship(y - 3, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Destroyer(2, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Destroyer(y, x + 2, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Cruiser(0, 0, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Cruiser(0, 2, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Cruiser(2, 2, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                }
            } else {
                //Куча-мала справа
                if (rand.nextInt(100) > 50) {
                    //Сверху
                    int x = 9;
                    int y = 0;

                    ship = new Battleship(y, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Destroyer(y + 5, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Destroyer(y, x - 4, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Cruiser(y + 2, x - 3, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Cruiser(y + 4, x - 3, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Cruiser(9, x - 1, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                } else {
                    //Снизу
                    int x = 9;
                    int y = 9;

                    ship = new Battleship(y - 3, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Destroyer(2, x, false);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Destroyer(y - 4, x - 4, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Cruiser(0, x - 4, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Cruiser(2, x - 3, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);

                    ship = new Cruiser(0, x - 1, true);
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                }
            }
            //Случайная генерация единичек
            int num = 4;
            while (num > 0) {
                int x = rand.nextInt(10);
                int y = rand.nextInt(10);
                ship = new Submarine(y, x, true);
                if (ship.okToPlaceShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean)) {
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                    num--;
                }
            }
        } else if (rand.nextInt(60) > 20) {
            //Все расставить по границам, кроме единичек
            int len;
            for (int i = 4; i > 1; i--) {
                len = 5 - i;
                while (len > 0) {
                    ship = borderShip(i);
                    if (ship.okToPlaceShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean)) {
                        ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                        len--;
                    }
                }
            }

            //Случайная генерация единичек
            int num = 4;
            while (num > 0) {
                int x = rand.nextInt(10);
                int y = rand.nextInt(10);
                ship = new Submarine(y, x, true);
                if (ship.okToPlaceShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean)) {
                    ship.placeShipAt(ship.getBowRow(), ship.getBowColumn(), ship.isHorizontal(), myOcean);
                    num--;
                }
            }
        } else {
            //Просто рандомно
            myOcean.placeAllShipsRandomly();
        }
    }

    private static Ship borderShip(int len) {
        if (rand.nextInt(100) < 25) {
            //Сверху
            switch (len) {
                case 1:
                    return new Submarine(0, rand.nextInt(10), true);
                case 2:
                    return new Cruiser(0, rand.nextInt(10), true);
                case 3:
                    return new Destroyer(0, rand.nextInt(10), true);
                case 4:
                    return new Battleship(0, rand.nextInt(10), true);
            }
        }
        if (rand.nextInt(75) < 25) {
            //Снизу
            switch (len) {
                case 1:
                    return new Submarine(9, rand.nextInt(10), true);
                case 2:
                    return new Cruiser(9, rand.nextInt(10), true);
                case 3:
                    return new Destroyer(9, rand.nextInt(10), true);
                case 4:
                    return new Battleship(9, rand.nextInt(10), true);
            }
        }
        if (rand.nextInt(50) < 25) {
            //Слева
            switch (len) {
                case 1:
                    return new Submarine(rand.nextInt(10), 0, false);
                case 2:
                    return new Cruiser(rand.nextInt(10), 0, false);
                case 3:
                    return new Destroyer(rand.nextInt(10), 0, false);
                case 4:
                    return new Battleship(rand.nextInt(10), 0, false);
            }
        }
        //Справа
        switch (len) {
            case 1:
                return new Submarine(rand.nextInt(10), 9, false);
            case 2:
                return new Cruiser(rand.nextInt(10), 9, false);
            case 3:
                return new Destroyer(rand.nextInt(10), 9, false);
            case 4:
                return new Battleship(rand.nextInt(10), 9, false);
        }
        return null;
    }

    public static int[] makeShoot() {
        Pair[] needToShoot = left();
        int x = needToShoot[0].x;
        int y = needToShoot[0].y;
        boolean isFound = false;
        int i = 0;
        int maxL = maxOpponentLen();

        Pair fired = isFired();
        if (fired != null) {
            int tx, ty;
            int max = 4 + rand.nextInt(30);
            int j = 0;
            while (j < max) {
                tx = fired.x;
                ty = fired.y;
                j++;
                while (tx >= 0 && opponentOcean[tx][ty] == 2) {
                    tx--;
                }
                if (tx >= 0 && opponentOcean[tx][ty] == 0) {
                    x = tx;
                    y = ty;
                }
                if (j >= max)
                    break;
                j++;

                tx = fired.x;
                ty = fired.y;
                while (tx <= 9 && opponentOcean[tx][ty] == 2) {
                    tx++;
                }
                if (tx <= 9 && opponentOcean[tx][ty] == 0) {
                    x = tx;
                    y = ty;
                }
                if (j >= max)
                    break;
                j++;

                tx = fired.x;
                ty = fired.y;
                while (ty <= 9 && opponentOcean[tx][ty] == 2) {
                    ty++;
                }
                if (ty <= 9 && opponentOcean[tx][ty] == 0) {
                    x = tx;
                    y = ty;
                }
                if (j >= max)
                    break;
                j++;

                tx = fired.x;
                ty = fired.y;
                while (ty >= 0 && opponentOcean[tx][ty] == 2) {
                    ty--;
                }
                if (ty >= 0 && opponentOcean[tx][ty] == 0) {
                    x = tx;
                    y = ty;
                }

            }

        } else {
            while (!isFound && i < 100) {
                i++;
                int t = rand.nextInt(needToShoot.length);
                x = needToShoot[t].x;
                y = needToShoot[t].y;

                switch (maxL) {
                    case 1:
                        isFound = true;
                        break;
                    case 2:
                        if (rand.nextInt(100) > 30)
                            if (canPlus(x, y, 2))
                                isFound = true;
                            else if (canLine(x, y, 2))
                                isFound = true;
                        break;
                    case 3:
                        if (rand.nextInt(100) > 50)
                            if (canPlus(x, y, 3))
                                isFound = true;
                            else if (canLine(x, y, 3))
                                isFound = true;
                        break;
                    case 4:
                        if (rand.nextInt(100) > 70)
                            if (canPlus(x, y, 4))
                                isFound = true;
                            else if (canLine(x, y, 4))
                                isFound = true;
                        break;
                    case 0:
                        System.out.println("WTF ERROR");
                        break;
                }

            }
        }
        int[] res = new int[2];
        res[0] = x;
        res[1] = y;
        return res;
    }

    public static boolean isDefeat() {
        return myOcean.isGameOver();
    }

    public static int[] getStatistic() {
        return new int[]{myOcean.getShotsFired(), myOcean.getHitCount(), myOcean.getUndamagedShips(), myOcean.getFireShips(), myOcean.getShipsSunk()};
    }

    public static void getResult(int x, int y, int type, int len) {
        opponentOcean[x][y] = type;
        if (type == 2) {
            if (x > 0 && y > 0)
                opponentOcean[x - 1][y - 1] = -2;
            if (x > 0 && y < 9)
                opponentOcean[x - 1][y + 1] = -2;
            if (x < 9 && y > 0)
                opponentOcean[x + 1][y - 1] = -2;
            if (x < 9 && y < 9)
                opponentOcean[x + 1][y + 1] = -2;
        }
        if (type == 1) {
            opponentShips[len - 1]--;
            int tx, ty;

            tx = x - 1;
            while (tx >= 0) {
                if (opponentOcean[tx][y] == 2) {
                    opponentOcean[tx][y] = 1;
                    if (y > 0)
                        opponentOcean[tx][y - 1] = -2;
                    if (y < 9)
                        opponentOcean[tx][y + 1] = -2;
                    tx--;
                } else {
                    opponentOcean[tx][y] = -2;
                    if (y > 0)
                        opponentOcean[tx][y - 1] = -2;
                    if (y < 9)
                        opponentOcean[tx][y + 1] = -2;
                    break;
                }
            }
            tx = x + 1;
            while (tx <= 9) {
                if (opponentOcean[tx][y] == 2) {
                    opponentOcean[tx][y] = 1;
                    if (y > 0)
                        opponentOcean[tx][y - 1] = -2;
                    if (y < 9)
                        opponentOcean[tx][y + 1] = -2;
                    tx++;
                } else {
                    opponentOcean[tx][y] = -2;
                    if (y > 0)
                        opponentOcean[tx][y - 1] = -2;
                    if (y < 9)
                        opponentOcean[tx][y + 1] = -2;
                    break;
                }
            }
            ty = y - 1;
            while (ty >= 0) {
                if (opponentOcean[x][ty] == 2) {
                    opponentOcean[x][ty] = 1;
                    if (x > 0)
                        opponentOcean[x - 1][ty] = -2;
                    if (x < 9)
                        opponentOcean[x + 1][ty] = -2;
                    ty--;
                } else {
                    opponentOcean[x][ty] = -2;
                    if (x > 0)
                        opponentOcean[x - 1][ty] = -2;
                    if (x < 9)
                        opponentOcean[x + 1][ty] = -2;
                    break;
                }
            }
            ty = y + 1;
            while (ty <= 9) {
                if (opponentOcean[x][ty] == 2) {
                    opponentOcean[x][ty] = 1;
                    if (x > 0)
                        opponentOcean[x - 1][ty] = -2;
                    if (x < 9)
                        opponentOcean[x + 1][ty] = -2;
                    ty++;
                } else {
                    opponentOcean[x][ty] = -2;
                    if (x > 0)
                        opponentOcean[x - 1][ty] = -2;
                    if (x < 9)
                        opponentOcean[x + 1][ty] = -2;
                    break;
                }
            }
        }
    }

    public static int getShoot(int x, int y) {
        return myOcean.shootAt(x, y);
    }

    private static boolean canPlus(int x, int y, int len) {
        int len_t = 1;
        int xt = x;
        int yt = y - 1;
        while (yt >= 0 && opponentOcean[xt][yt] == 0) {
            len_t++;
            yt--;
        }
        yt = y + 1;
        while (yt <= 9 && opponentOcean[xt][yt] == 0) {
            len_t++;
            yt++;
        }
        if (len_t < len)
            return false;

        len = 1;
        xt = x - 1;
        yt = y;
        while (xt >= 0 && opponentOcean[xt][yt] == 0) {
            len_t++;
            xt--;
        }
        xt = x + 1;
        while (xt <= 9 && opponentOcean[xt][yt] == 0) {
            len_t++;
            xt++;
        }
        if (len_t < len)
            return false;
        return true;

    }

    private static boolean canLine(int x, int y, int len) {
        int len_t = 1;
        int xt = x;
        int yt = y - 1;
        while (yt >= 0 && opponentOcean[xt][yt] == 0) {
            len_t++;
            yt--;
        }
        yt = y + 1;
        while (yt <= 9 && opponentOcean[xt][yt] == 0) {
            len_t++;
            yt++;
        }
        if (len_t >= len)
            return true;

        len = 1;
        xt = x - 1;
        yt = y;
        while (xt >= 0 && opponentOcean[xt][yt] == 0) {
            len_t++;
            xt--;
        }
        xt = x + 1;
        while (xt <= 9 && opponentOcean[xt][yt] == 0) {
            len_t++;
            xt++;
        }
        if (len_t >= len)
            return true;
        return false;

    }

    private static int maxOpponentLen() {
        for (int i = 3; i >= 0; i--)
            if (opponentShips[i] != 0)
                return i + 1;
        return 0;
    }

    private static int leftNum() {
        int left = 0;
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                if (opponentOcean[i][j] == 0)
                    left++;
        return left;
    }

    private static Pair isFired() {
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                if (opponentOcean[i][j] == 2)
                    return new Pair(i, j);
        return null;
    }

    private static Pair[] left() {
        Pair[] res = new Pair[leftNum()];
        int k = 0;
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                if (opponentOcean[i][j] == 0) {
                    res[k] = new Pair(i, j);
                    k++;
                }
        return res;
    }
}

class Pair {
    int x, y;

    Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
