package battleship.ai;

import battleship.Ocean;
import battleship.resources.styles.Styles;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    static Ocean myOcean;
    static int[] opponentShips = new int[4];
    static int[][] opponentOcean = new int[10][10];
    static Random rand = new Random();

    public static void startGameAI(){
        myOcean = new Ocean();
        myOcean.placeAllShipsRandomly();
        for(int i = 0; i < 4; i++)
            opponentShips[i] = 4 - i;
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                opponentOcean[i][j] = 0;
    }

    public static int[] makeShoot(){
        Pair[] needToShoot = left();
        int x = needToShoot[0].x;
        int y = needToShoot[0].y;
        boolean isFound = false;
        int i = 0;
        int maxL = maxOpponentLen();

        Pair fired = isFired();
        if(fired != null){
            int tx, ty;
            int max = 4 + rand.nextInt(30);
            int j = 0;
            while(j < max){
                tx = fired.x;
                ty = fired.y;
                j++;
                while(tx >= 0 && opponentOcean[tx][ty] == 2){
                    tx--;
                }
                if(tx >= 0 && opponentOcean[tx][ty] == 0){
                    x = tx;
                    y = ty;
                }
                if(j >= max)
                    break;
                j++;

                tx = fired.x;
                ty = fired.y;
                while(tx <= 9 && opponentOcean[tx][ty] == 2){
                    tx++;
                }
                if(tx <= 9 && opponentOcean[tx][ty] == 0){
                    x = tx;
                    y = ty;
                }
                if(j >= max)
                    break;
                j++;

                tx = fired.x;
                ty = fired.y;
                while(ty <= 9 && opponentOcean[tx][ty] == 2){
                    ty++;
                }
                if(ty <= 9 && opponentOcean[tx][ty] == 0){
                    x = tx;
                    y = ty;
                }
                if(j >= max)
                    break;
                j++;

                tx = fired.x;
                ty = fired.y;
                while(ty >= 0 && opponentOcean[tx][ty] == 2){
                    ty--;
                }
                if(ty >= 0 && opponentOcean[tx][ty] == 0){
                    x = tx;
                    y = ty;
                }

            }

        } else {
            while(!isFound && i < 100){
                i++;
                int t = rand.nextInt(needToShoot.length);
                x = needToShoot[t].x;
                y = needToShoot[t].y;

                switch (maxL){
                    case 1:
                        isFound = true;
                        break;
                    case 2:
                        if(rand.nextInt(100) > 30)
                            if(canPlus(x,y,2))
                                isFound = true;
                            else
                            if(canLine(x,y,2))
                                isFound = true;
                        break;
                    case 3:
                        if(rand.nextInt(100) > 50)
                            if(canPlus(x,y,3))
                                isFound = true;
                            else
                            if(canLine(x,y,3))
                                isFound = true;
                        break;
                    case 4:
                        if(rand.nextInt(100) > 70)
                            if(canPlus(x,y,4))
                                isFound = true;
                         else
                            if(canLine(x,y,4))
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

    public static boolean isDefeat(){
        return myOcean.isGameOver();
    }

    public static int[] getStatistic(){
        return new int[] {myOcean.getShotsFired(),  myOcean.getHitCount(), myOcean.getUndamagedShips(), myOcean.getFireShips(), myOcean.getShipsSunk()};
    }

    public static void getResult(int x, int y, int type, int len){
        opponentOcean[x][y] = type;
        if(type == 2) {
            if(x > 0 && y > 0)
                opponentOcean[x-1][y-1] = -2;
            if(x > 0 && y < 9)
                opponentOcean[x-1][y+1] = -2;
            if(x < 9 && y > 0)
                opponentOcean[x+1][y-1] = -2;
            if(x < 9 && y < 9)
                opponentOcean[x+1][y+1] = -2;
        }
        if(type == 1) {
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

    public static int getShoot(int x, int y){
        return myOcean.shootAt(x,y);
    }

    private static boolean canPlus(int x, int y, int len){
        int len_t = 1;
        int xt = x;
        int yt = y - 1;
        while(yt >= 0 && opponentOcean[xt][yt] == 0){
            len_t++;
            yt--;
        }
        yt = y + 1;
        while(yt <= 9 && opponentOcean[xt][yt] == 0){
            len_t++;
            yt++;
        }
        if(len_t < len)
            return false;

        len = 1;
        xt = x - 1;
        yt = y;
        while(xt >= 0 && opponentOcean[xt][yt] == 0){
            len_t++;
            xt--;
        }
        xt = x + 1;
        while(xt <= 9 && opponentOcean[xt][yt] == 0){
            len_t++;
            xt++;
        }
        if(len_t < len)
            return false;
        return true;

    }

    private static boolean canLine(int x, int y, int len){
        int len_t = 1;
        int xt = x;
        int yt = y - 1;
        while(yt >= 0 && opponentOcean[xt][yt] == 0){
            len_t++;
            yt--;
        }
        yt = y + 1;
        while(yt <= 9 && opponentOcean[xt][yt] == 0){
            len_t++;
            yt++;
        }
        if(len_t >= len)
            return true;

        len = 1;
        xt = x - 1;
        yt = y;
        while(xt >= 0 && opponentOcean[xt][yt] == 0){
            len_t++;
            xt--;
        }
        xt = x + 1;
        while(xt <= 9 && opponentOcean[xt][yt] == 0){
            len_t++;
            xt++;
        }
        if(len_t >= len)
            return true;
        return false;

    }

    private static int maxOpponentLen(){
        for(int i = 3; i >= 0; i--)
            if(opponentShips[i] != 0)
                return i + 1;
        return 0;
    }

    private static int leftNum(){
        int left = 0;
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                if(opponentOcean[i][j] == 0)
                    left++;
        return left;
    }

    private static Pair isFired(){
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                if(opponentOcean[i][j] == 2)
                    return new Pair(i,j);
        return null;
    }

    private static Pair[] left(){
        Pair[] res = new Pair[leftNum()];
        int k = 0;
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                if(opponentOcean[i][j] == 0){
                    res[k] = new Pair(i,j);
                    k++;
                }
        return res;
    }
}

class Pair{
    int x, y;
    Pair(int x, int y){
        this.x = x;
        this.y = y;
    }
}
