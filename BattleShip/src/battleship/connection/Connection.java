package battleship.connection;

import battleship.BattleshipGame;
import battleship.controllers.BattleScene;
import battleship.controllers.PlanOfShips;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class Connection implements Runnable {
    protected Socket client;
    protected PrintWriter out;
    protected Scanner in;
    protected boolean running;

    String name;

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    void work() {
        Platform.runLater(PlanOfShips::getConnection);
        String sender = "P";        //Battle or Plan
        final ConcurrentLinkedDeque<String> listMessages = new ConcurrentLinkedDeque<String>();

        getMessagesinThread(listMessages, null, null);

        LocalTime end = LocalTime.now().plusSeconds(BattleshipGame.getTimeWait() / 2);
        LocalTime timeOut = LocalTime.now().plusSeconds(BattleshipGame.getTimeOut());
        int hungry = 0;
        while (running) {
            if(listMessages.size() > 0){
                String message = listMessages.remove();

                //Another app try to connect
                if(message.charAt(0) == 'N') {
                    Launcher.send("", 'Q');
                    continue;
                }

                end = LocalTime.now().plusSeconds(BattleshipGame.getTimeWait() / 2);
                timeOut = LocalTime.now().plusSeconds(BattleshipGame.getTimeOut());
                if(hungry == 2)
                {
                    Platform.runLater(() -> {
                        PlanOfShips.writeMessage("Системное: Соединение восстановленно!");
                    });
                }
                hungry = 0;

                switch (message.charAt(0)) {
                    //Message in chat
                    case 'M':   //MESSAGE
                        String finalMessage = message;
                        String finalSender = sender;
                        Platform.runLater(() -> {
                            if(finalSender.equals("P"))
                                PlanOfShips.writeMessage(finalMessage.substring(1));
                            if(finalSender.equals("B"))
                                BattleScene.writeMessage(finalMessage.substring(1));
                        });
                        break;

                        //It's mean, that you can shoot
                        //SG - start get
                        //SO - start ok
                    case 'S': //Start (game)
                        if(message.equals("SG")) {
                            BattleScene.opponentReady();
                            Launcher.send("O"  ,'S');
                        }
                        if(message.equals("SO"))
                            sender = "B";
                        break;

                        //Statistic messages:
                        // GET  -   You try to get opponent statistic
                        // Else -   Numbers about opponent statistic
                    case 'I': // Info (== statistic)
                        if(message.equals("IGET"))
                            Launcher.send(" " + BattleshipGame.convertStatistic(), 'I');
                        else {
                            int[] numbers = new int[5];
                            if (convertStringStatisticToArrayIntStatistic(message, numbers)) break;
                            Platform.runLater(() -> {
                                BattleScene.setStatistic(numbers);
                            });
                        }
                        break;
                        //Message about shoot (place, where opponent shoot) sample: A 1 1
                    case 'A':   //ATTACK
                        String[] shoot = message.split(" ");
                        if(shoot[0].length() != 1 || shoot[1].length() != 1 || shoot[1].matches("\\D") || shoot[2].length() != 1 || shoot[2].matches("\\D"))
                            continue;
                        int x = Integer.parseInt(shoot[1]);
                        int y = Integer.parseInt(shoot[2]);
                        Launcher.send(" " + BattleScene.getShoot(x, y) + " " + x + " " + y, 'R');
                        break;

                        //Message with result of shoot
                    case 'R':   //RESULT
                        String[] res = message.split(" ");
                        int type = Integer.parseInt(res[1]);
                        x = Integer.parseInt(res[2]);
                        y = Integer.parseInt(res[3]);
                        Platform.runLater(() -> {
                            BattleScene.makeInRealMyShoot(type, x, y);
                        });
                        break;

                        //When game over
                        //Second letter:
                        // S    -   your opponent is _surrender_. After that your statistic ("DF <statistic>")
                        // O    -   confirm the win. After that your statistic ("DO <statistic>")
                        //Third letter: (Only when get message about win)
                        // E    -   Early fail
                        // F    -   Final fail
                    case 'D':   //DEFEAT
                    {
                        if(message.charAt(1) == 'S'){
                            Launcher.send("O " + BattleshipGame.convertStatistic(), 'D');
                            int[] numbers = new int[5];
                            if (convertStringStatisticToArrayIntStatistic(message, numbers)) break;
                            Platform.runLater(() -> {
                                BattleScene.winMessage(numbers, message.charAt(2));
                                BattleshipGame.beginPlanBattlePlace("S");
                                PlanOfShips.getConnection();
                            });
                        } else
                        if(message.charAt(1) == 'O'){
                            int[] numbers = new int[5];
                            if (convertStringStatisticToArrayIntStatistic(message, numbers)) break;
                            Platform.runLater(() -> BattleScene.defeatMessage(numbers));
                        } else
                            break;
                        sender = "P";
                        break;
                    }
                        //Message to stop the connection. Messages:
                        // S - choosing the ship, opponent quit
                        // A - choosing the ship, opponent can quit
                        //ELSE - ERROR
                    case 'Q':   //QUIT
                        running = false;
                        if(message.length() == 2 && message.charAt(1) == 'S'){
                            Launcher.send("A", 'Q');
                            Platform.runLater(() -> PlanOfShips.writeMessage("Системное: Противник вышел из игры."));
                            Platform.runLater(PlanOfShips::lostConnection);
                            stop();
                            return;
                        }
                        if(message.length() == 2 && message.charAt(1) == 'A'){
                            Platform.runLater(() -> {
                                PlanOfShips.writeMessage("Системное: вы отключились от игры.");
                                PlanOfShips.lostConnection();
                            });
                            stop();
                            return;
                        }
                        errorExitMessage();
                        stop();
                        return;
                        //Test connection message
                    case 'T':   //TEST
                        Launcher.sendMessage("", 'O');
                        break;
                        //Answer on test message
                    case 'O':   //OK
                        break;
                }
            } else
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            if(end.isBefore(LocalTime.now())){
                if(hungry == 0) {
                    hungry = 1;
                    Launcher.sendMessage("",'T');
                    end = LocalTime.now().plusSeconds(BattleshipGame.getTimeWait());
                    continue;
                }
                if(hungry == 1){
                    hungry = 2;
                    String finalSender1 = sender;
                    Platform.runLater(() -> {
                        if(finalSender1 == "P")
                            PlanOfShips.writeMessage("Системное: Потеря соединения. Попытка восстановления");
                        else if (finalSender1 == "B")
                            BattleScene.writeMessage("Системное: Потеря соединения. Попытка восстановления");
                    });
                }

                if(timeOut.isBefore(LocalTime.now())) {
                    String finalSender2 = sender;
                    Platform.runLater(() -> {
                        if(finalSender2 == "B")
                            BattleScene.writeMessage("Системное: Восстановить связь не удалось.");

                        BattleshipGame.beginPlanBattlePlace("S");
                        PlanOfShips.writeMessage("Системное: Восстановить связь не удалось.");
                        PlanOfShips.lostConnection();
                    });
                    Launcher.stop();
                    return;
                }
            }
        }
    }

    private boolean convertStringStatisticToArrayIntStatistic(String message, int[] numbers) {
        String[] info = message.split(" ");
        try{
            for(int i = 0; i < 5; i++)
                numbers[i] = Integer.parseInt(info[i+1]);
        } catch (Exception e){
            return true;
        }
        return false;
    }

    void getMessagesinThread(ConcurrentLinkedDeque<String> listMessages, LocalTime timeWork, Integer numOfMessages) {
        Thread reader = new Thread(() -> {
            int readed = 0;
            while((running && (numOfMessages == null || readed < numOfMessages) && (timeWork == null || timeWork.isAfter(LocalTime.now()))))
                try{
                    if(in.hasNext()) {
                        listMessages.add(in.nextLine());
                        readed++;
                        if (listMessages.getFirst().charAt(0)=='Q')
                            return;

                    }
                } catch (Exception e){
                    return;//DNZOBDFSHIOGJIODFS;JHIDFSJ;VDFSJIULBHSFJDFS;OIGJIOEWJTHOWTRS
                }
        });
        reader.setDaemon(true);
        reader.start();
    }

    private void errorExitMessage(){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setTitle("Ошибка выхода");
            alert.setHeaderText("Приложение возвращено на главное меню из-за неизвестного способа выхода");
            alert.setContentText("И такое случается");
            alert.showAndWait();
            BattleshipGame.backToMenu();
        });
    }

    abstract void stop();
}
