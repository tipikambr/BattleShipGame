package battleship.connection;


import battleship.BattleshipGame;
import battleship.controllers.PlanOfShips;
import javafx.application.Platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server extends Connection{
    private ServerSocket server;
    Port port;

    public Server(Port port){
        this.port = port;
    }

    @Override
    public void run() {
        running = true;
        if (!start())
            return;

        if(!getConnection()){
            Launcher.stop();
            return;
        };

        work();

    }

    private boolean start() {
        try {
            server = new ServerSocket(port.port, 1);

            Platform.runLater(() -> {
                try {
                    PlanOfShips.writeMessage("Системное: Сервер запущен. IP-адрес: " + Inet4Address.getLocalHost().getHostAddress() + " Порт: " + server.getLocalPort());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            });
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean getConnection(){
        try {
            client = server.accept();

            out = new PrintWriter(client.getOutputStream(), true, Charset.forName("windows-1251"));
            in = new Scanner(client.getInputStream(), "windows-1251");

            final ConcurrentLinkedDeque<String> listMessages = new ConcurrentLinkedDeque<String>();
            getMessagesinThread(listMessages, LocalTime.now().plusSeconds(BattleshipGame.getTimeOut()), 1);

            while(listMessages.size() == 0)
                if(!running) return false;

            name = listMessages.remove();
            if(name.charAt(0) != 'N')
            {
                Platform.runLater(() -> {
                    PlanOfShips.writeMessage("Системное: Что-то пошло не так");
                });
                return false;
            }

            name = name.substring(1);

            out.println("Y");

            Platform.runLater(() -> {
                PlanOfShips.writeMessage("Системное: Соединение установлено. Ваш противник " + name);
            });
            return true;
        } catch (IOException e) {
            Platform.runLater(() -> {
                PlanOfShips.writeMessage("Системное: Сервер остановлен ");
            });
            return false;
        }


    }


    @Override
    public void stop() {
        try {
            running = false;
            if(in != null) in.close();
            if (out != null) out.close();
            if (client != null) client.close();
            if (server != null) server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
