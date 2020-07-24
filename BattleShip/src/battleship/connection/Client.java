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

    public Client(User user, String  myname) {
        this.user = new User(user.getName(), user.getIp(), user.getPort());
        running = false;
        this.myname = myname;
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
                client = new Socket(user.getIp(), user.getPort());
                client.setSoTimeout(BattleshipGame.getTimeOut() * 1000);
                out = new PrintWriter(client.getOutputStream(), true, Charset.forName("windows-1251"));
                in = new Scanner(client.getInputStream(), "windows-1251");

                break;

            } catch (IOException e) {
            }

        Launcher.send(myname, 'N');
        final ConcurrentLinkedDeque<String> listMessages = new ConcurrentLinkedDeque<String>();
        getMessagesInThread(listMessages, LocalTime.now().plusSeconds(BattleshipGame.getTimeOut()), 1);

        while(listMessages.size() == 0)
            if(!running) return false;

        opponentName = listMessages.remove();
        if(opponentName.charAt(0) != 'Y'){
            Platform.runLater(() -> {
                PlanOfShips.writeMessage("Системное: Сервер пытается восстановить связь, но не с вами.");
            });
            return false;
        }

        opponentName = opponentName.substring(1);
        Platform.runLater(() -> {
            PlanOfShips.writeMessage("Системное: Вы подключены к игре. Противник называет себя: " + opponentName);
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
