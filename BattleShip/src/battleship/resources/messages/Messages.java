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

    static {
        salagaAttentionMessages = new ArrayList<>();
        salagaAttentionMessages.add("К вам подходит весь израненый, побледневший помощник, и жалобным" +
                "голосом спрашивает вас: \"Вы действительно хотите " +
                "сдаться? Наши моряки рвутся в бой, корабль еще " +
                "на плаву. Может, ну это, продолжим сражение?\"");
        salagaAttentionMessages.add("Вы смотрите на Ваше еще судно, а ведь именно этот корабль еще " +
                "не знавал поражений... И мысли рискнуть, продолжить сражение, " +
                "уничтожить врага начинают появляться в вашей голове.\n" +
                "Отбросить мысли?");
    }

    static {
        disunderstandingAimMessages = new ArrayList<>();
        disunderstandingAimMessages.add("Ваш помощник смотрит на Вас непонимающим взглядом," +
                "после чего все-таки решается спросить:" +
                "\"Извините, а по каким координатам мы целимся?\"");
        disunderstandingAimMessages.add("Ваш помошник, как и все остальные на этом корабле смотрят на " +
                "вас в ожидании приказа. Помошник первый ретировался: " +
                "\"Капитан, вы не все координаты выстрела нам сообщили!\"");
    }

    static {
        shootInFireShipMessages = new ArrayList<>();
        shootInFireShipMessages.add("Ваш помощник подходит к Вам и предлагает " +
                "сместить мушку пушки на близлежайшие окресности: " +
                "Зачем делать дырку в дырявой части корабля, если можно в целой!");
        shootInFireShipMessages.add("Уже все готово к выстрелу. но вдруг в вашей голове " +
                "появляется образ вашего помошника. Из-за этого у Вас происходит прозрение и " +
                "вы понимаете, что сюда стрелять бессмысленно, и стоит выстрелить в другую часть корабля.");
    }
    static {
        shootInSunkShipMessages = new ArrayList<>();
        shootInSunkShipMessages.add("Ваш помощник бежит к Вам и кричит " +
                "так громко, что матросы прикрывают уши: " +
                "\"Стрелять в затонувшие корабли запрещено кодексом, одумайтесь!\"");
        shootInSunkShipMessages.add("К вам подходит совершенно серьезный помошник, и уверенно " +
                "заявляет: \"Капитан, мы не кровожадные убийцы. Если корабль уже " +
                "потоплен, то мы не будем добивать чудом выживших моряков.");
    }

    static {
        shootNotFirstTimeInSeaMessages = new ArrayList<>();
        shootNotFirstTimeInSeaMessages.add("Ваш помощник подходит к Вам и виноватым " +
                "голосом говорит, что люди отказываются стрелять в " +
                "чистое море, предлгая выбрать другую цель.");
        shootNotFirstTimeInSeaMessages.add("Ваш помошник подходит к Вам, с опаской " +
                "поглядывая на вашу цель, и слегка растеряным голосом " +
                "говорит: \"Стрельба в чистое море может " +
                "разгневать морского бога.");
    }

    static {
        shootInSeaMessages = new ArrayList<>();
        shootInSeaMessages.add("Пушечное ядро отправилось на дно морское, так и не попав по вражескому кораблю!");
        shootInSeaMessages.add("Знатно бабахнуло. Полетело хорошо. Брызг много. Вражеский корабль - не поврежден.");
    }

    static {
        shootInShipMessages = new ArrayList<>();
        shootInShipMessages.add("Корабль противника горит! Но все ещё не тонет.");
        shootInShipMessages.add("Четкое попадание по кораблю противника! Жаль, не уничтожен. А было бы красиво!");
    }

    static {
        destroyShipMessage = new ArrayList<>();
        destroyShipMessage.add("Корабль противника потоплен!");
        destroyShipMessage.add("Моряки вражеского корабля выбрасывают бочки рома. Значит, корабль пошел ко дну.");
    }

    static {
        understandMessage = new ArrayList<>();
        understandMessage.add("Ваш помошник внимательно смотрит на выбранную вами точку, после чего неуверенно произносит: \"Капитан, но там рядом корабль, значит в этой точке нет корабля. " +
                "Зачем на просто так тратить драгоценные ядра?");
        understandMessage.add("\"Капитан!\" - вы слышите крик с другой стороны палубы. Это " +
                "ваш помошник кричит, пытаясь сообщить, что выбранная цель " +
                "мало того, что не причинит вред врагу, так еще и даст " +
                "преимущество врагу в сражении с нами");
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
