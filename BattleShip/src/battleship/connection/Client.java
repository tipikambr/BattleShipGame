package battleship.connection;

import battleship.BattleshipGame;
import battleship.controllers.PlanOfShips;
import javafx.application.Platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Client extends Connection {
    User user;

    public Client(User user, String  name) {
        this.user = new User();
        this.user.port = user.port;
        this.user.ip = user.ip;
        this.user.name = user.name;

        running = false;
        this.name = name;
    }

    @Override
    public void run() {
        running = true;
        Platform.runLater(() -> PlanOfShips.writeMessage("Системное: Попытка подключения"));

        if (!tryConnect()) {
            Platform.runLater(() -> PlanOfShips.writeMessage("Системное: Поиск остановлен."));
            stop();
            return;
        }

        work();
    }

    private boolean tryConnect(){
        while (true)
            try {
                if(!running ) return false;
                client = new Socket(user.ip, user.port);
                out = new PrintWriter(client.getOutputStream(), true, Charset.forName("windows-1251"));
                in = new Scanner(client.getInputStream(), "windows-1251");

                break;

            } catch (IOException e) {
            }

        Launcher.send(name, 'N');
        final ConcurrentLinkedDeque<String> listMessages = new ConcurrentLinkedDeque<String>();
        getMessagesinThread(listMessages, LocalTime.now().plusSeconds(BattleshipGame.getTimeOut()), 1);

        while(listMessages.size() == 0)
            if(!running) return false;

        if(listMessages.remove().charAt(0) != 'Y'){
            Platform.runLater(() -> {
                PlanOfShips.writeMessage("Системное: Сервер пытается восстановить связь, но не с вами.");
            });
            return false;
        }

        Platform.runLater(() -> {
            PlanOfShips.writeMessage("Системное: Вы подключены к игре.");
        });
        return true;
    }



    @Override
    public void stop() {
        try {
            running = false;
            if(in != null) in.close();
            if(out != null) out.close();
            if(client != null) client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
