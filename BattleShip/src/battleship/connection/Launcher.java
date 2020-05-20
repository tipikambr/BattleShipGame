package battleship.connection;


public class Launcher {
    static Thread work;
    static Connection runnablePart;
    static char type;

    /**
     * Launch server or client
     * @param runnable - what to launch
     * @param type
     *      c - client
     *      s - server
     */
    public static void create(Connection runnable, char type){
        Launcher.type = type;

        if(type != 'c' && type != 's')
            return;

        runnablePart = runnable;
        work = new Thread(runnable);
        work.setDaemon(true);
        work.start();

    }

    public static boolean isServer(){
        return type == 's';
    }

    public static void sendMessage(String message, char type){
        runnablePart.sendMessage(type + runnablePart.name + ": " + message);
    }

    public static void send(String message, char type){
        runnablePart.sendMessage(type + message);
    }

    public static void stop(){
        runnablePart.stop();
    }
}
