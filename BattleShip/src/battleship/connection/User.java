package battleship.connection;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class User {

    private StringProperty name;
    private StringProperty ip;
    private IntegerProperty port;

    public User(){
        name = new SimpleStringProperty();
        ip = new SimpleStringProperty();
        port = new SimpleIntegerProperty();
        name.set("localhost");
        ip.set("127.0.0.1");
        port.set(7337);
    }

    public User(String name, String ip, int port){
        this.name = new SimpleStringProperty();
        this.ip = new SimpleStringProperty();
        this.port = new SimpleIntegerProperty();
        this.name.set(name);
        this.ip.set(ip);
        this.port.set(port);
    }

    public String getName(){
        return name.get();
    }

    public String getIp(){
        return ip.get();
    }

    public int getPort() {
        return port.get();
    }

    public void setName(String name){
        this.name.set(name);
    }

    public void setIp(String ip){
        this.ip.set(ip);
    }

    public void setPort(int port){
        this.port.set(port);
    }

    public StringProperty nameProperty() { return this.name; }

    public StringProperty ipProperty() { return this.ip; }

    public IntegerProperty portProperty() { return this.port; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.getName().equals(user.getName()) &&
                this.getIp().equals(user.getIp()) &&
                this.getPort() == (user.getPort());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ip, port);
    }

    @Override
    public String toString(){
        return name.get();
    }
}
