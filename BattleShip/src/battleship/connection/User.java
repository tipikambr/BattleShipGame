package battleship.connection;

public class User {
    public String name;
    public String ip;
    public int port;

    @Override
    public String toString(){
        return name;
    }
}
