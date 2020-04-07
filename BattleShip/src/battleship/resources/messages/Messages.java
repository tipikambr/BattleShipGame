package battleship.resources.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Messages {
    private static Random rand;

    private static ArrayList<String> salagaAttentionMessages;
    private static ArrayList<String> disunderstandingAimMessages;
    private static ArrayList<String> shootInFireShipMessages;
    private static ArrayList<String> shootInSunkShipMessages;
    private static ArrayList<String> shootNotFirstTimeInSeaMessages;
    private static ArrayList<String> shootInSeaMessages;
    private static ArrayList<String> shootInShipMessages;
    private static ArrayList<String> destroyShipMessage;
    private static ArrayList<String> understandMessage;


    static {
        rand = new Random();
    }

    //Salaga constructor
    static {
        salagaAttentionMessages = new ArrayList<>();
        salagaAttentionMessages.add("К вам подходит весь израненый, побледневший помощник, и жалобным" +
                "голосом спрашивает вас: \"Вы действительно хотите " +
                "сдаться? Наши моряки рвутся в бой, корабль еще " +
                "на плаву. Может, ну это, продолжим сражение?\"");
    }

    //Disunderstanding message
    static {
        disunderstandingAimMessages = new ArrayList<>();
        disunderstandingAimMessages.add("Ваш помощник смотрит на Вас непонимающим взглядом," +
                "после чего все-таки решается спросить:" +
                "\"Извините, а по каким координатам мы целимся?\"");
    }

    static {
        shootInFireShipMessages = new ArrayList<>();
        shootInFireShipMessages.add("Ваш помощник подходит к Вам и предлагает " +
                "сместить мушку пушки на близлежайшие окресности: " +
                "Зачем делать дырку в дырявой части корабля, если можно в целой!");
    }
    static {
        shootInSunkShipMessages = new ArrayList<>();
        shootInSunkShipMessages.add("Ваш помощник бежит к Вам и кричит " +
                "так громко, что матросы прикрывают уши: " +
                "\"Стрелять в затонувшие корабли запрещено кодексом, одумайтесь!\"");
    }

    static {
        shootNotFirstTimeInSeaMessages = new ArrayList<>();
        shootNotFirstTimeInSeaMessages.add("Ваш помощник подходит к Вам и виноватым " +
                "голосом говорит, что люди отказываются стрелять в " +
                "чистое море, предлгая выбрать другую цель.");
    }

    static {
        shootInSeaMessages = new ArrayList<>();
        shootInSeaMessages.add("Пушечное ядро отправилось на дно морское, так и не попав по вражескому кораблю!");
    }

    static {
        shootInShipMessages = new ArrayList<>();
        shootInShipMessages.add("Корабль противника горит! Но не тонет.");
    }

    static {
        destroyShipMessage = new ArrayList<>();
        destroyShipMessage.add("Корабль противника потоплен!");
    }

    static {
        understandMessage = new ArrayList<>();
        understandMessage.add("Ваш помошник внимательно смотрит на выбранную вами точку, после чего неуверенно произносит: \"Капитан, но там рядом корабль, значит в этой точке нет корабля. " +
                "Зачем на просто так тратить драгоценные ядра?");
    }

    public static String SalagaAttentionMessages() {return salagaAttentionMessages.get(rand.nextInt(salagaAttentionMessages.size()));}
    public static String DisunderstandingAimMessages() {return disunderstandingAimMessages.get(rand.nextInt(disunderstandingAimMessages.size()));}
    public static String ShootInFireShipMessages() {return shootInFireShipMessages.get(rand.nextInt(shootInFireShipMessages.size()));}
    public static String ShootInSunkShipMessages() {return shootInSunkShipMessages.get(rand.nextInt(shootInSunkShipMessages.size()));}
    public static String ShootNotFirstTimeInSeaMessages() {return shootNotFirstTimeInSeaMessages.get(rand.nextInt(shootNotFirstTimeInSeaMessages.size()));}
    public static String ShootInSeaMessages() {return shootInSeaMessages.get(rand.nextInt(shootInSeaMessages.size()));}
    public static String ShootInShipMessages() {return shootInShipMessages.get(rand.nextInt(shootInShipMessages.size()));}
    public static String DestroyShipMessage() {return destroyShipMessage.get(rand.nextInt(destroyShipMessage.size()));}
    public static String UnderstandMessage() {return understandMessage.get(rand.nextInt(understandMessage.size()));}

}
