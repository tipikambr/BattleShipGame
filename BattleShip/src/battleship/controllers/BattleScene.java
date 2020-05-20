package battleship.controllers;

import battleship.BattleshipGame;
import battleship.Ocean;
import battleship.connection.Launcher;
import battleship.resources.messages.Messages;
import battleship.resources.styles.Styles;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.Optional;

public class BattleScene {
    static Boolean isOnline;
    static Boolean isMyTurn;
    static Boolean isReady = null;

    @FXML
    static GridPane myOcean;
    static Button[][] myOceanButtons;

    @FXML
    static GridPane opponentOcean;
    static Button[][] opponentOceanButtons;

    @FXML
    static GridPane chatGrid;
    @FXML
    static ScrollPane chatScrollPane;
    static Label textChat;
    @FXML
    static GridPane sendGrid;
    static TextField textMessage;
    static Button sendMessage;
    static Button defeat;

    @FXML
    static GridPane infoGrid;

    @FXML
    static ScrollPane textScrollPaneLog;
    static Label textlog;

    static Label textInfo;

    //
    //
    //  INITIAL PART
    //
    //

    public static void init(boolean isOnline, boolean isMyTurn) {
        BattleScene.isOnline = isOnline;
        BattleScene.isMyTurn = isMyTurn;
        if(isReady == null) BattleScene.isReady = false;
        Parent root = BattleshipGame.getBattleRoot();

        if(myOcean == null){
            myOcean = (GridPane) root.lookup("#myOcean");
            myOceanButtons = new Button[10][10];
        }
        installField(myOceanButtons, myOcean, true);

        if(opponentOcean == null) {
            opponentOcean = (GridPane) root.lookup("#opponentOcean");
            opponentOceanButtons = new Button[10][10];
            installField(opponentOceanButtons, opponentOcean, false);
        } else
            makeAllSeaClear(opponentOceanButtons);

        if(chatGrid == null) {
            chatGrid = (GridPane) root.lookup("#chatFrid");
            chatScrollPane = (ScrollPane) root.lookup("#chatScrollPane");
            sendGrid = (GridPane) root.lookup("#sendGrid");
            installChatUI();
        }

        if(infoGrid == null) {
            infoGrid = (GridPane) root.lookup("#infoGrid");
            textScrollPaneLog = (ScrollPane) root.lookup("#textScrollPaneLog");
            installInfoUI();
        }
        Launcher.send("",'S');
        Launcher.send("GET", 'I');
    }

    static void installField(Button[][] buttons, GridPane battlePlace, boolean isFriendly) {
        for(int i = 1; i < 11; i++) {
            Image img = new Image(TrainingScene.class.getResourceAsStream("/battleship/resources/numbers/"+ ((9 + i) % 10) + ".png"));
            ImageView numHor = new ImageView(img);
            ImageView numVer = new ImageView(img);
            numHor.setFitHeight(30);
            numHor.setFitWidth(30);
            numVer.setFitHeight(30);
            numVer.setFitWidth(30);
            battlePlace.add(numHor, i, 0);
            battlePlace.add(numVer, 0, i);
            for (int j = 1; j < 11; j++) {
                final int x = i - 1;
                final int y = j - 1;
                boolean isNeadToAdd = false;
                if(buttons[x][y] == null) {
                    isNeadToAdd = true;
                    buttons[x][y] = new Button();
                }
                buttonShipLogic(buttons[x][y], x, y, isFriendly);
                if(isNeadToAdd)
                    battlePlace.add(buttons[x][y], i, j);
            }

        }
    }

    static void buttonShipLogic(Button ship, int x, int y, boolean isFriendly){
        ship.setPrefWidth(10000);
        ship.setPrefHeight(10000);
        ship.focusedProperty().addListener((e, oldVal, newVal) ->{
            if(newVal)
                textMessage.requestFocus();
        });
        if(isFriendly)
            makeMySeaSquareTruth(ship, BattleshipGame.getMyOcean(), x, y);
        else {
            makeSeaClear(ship);
            ship.setOnAction(event -> shootAt(x, y));
        }
    }

    static void installChatUI() {
        textChat = new Label(PlanOfShips.getChatLabel().getText());
        textChat.setPrefWidth(10000);
        textChat.setWrapText(true);
        chatScrollPane.setContent(textChat);

        sendMessage = new Button("Отправить");
        if(!isOnline)
            sendMessage.setDisable(true);
        sendMessage.prefWidth(74);
        sendMessage.minWidth(74);
        sendMessage.maxWidth(74);
        sendMessage.setOnAction((actionEvent) -> sendMessage());

        textMessage = new TextField();
        textMessage.textProperty().addListener((e, oldVal, newVal) -> {
            if(isOnline){
                if(newVal.length() == 2 && newVal.matches("\\d+")){
                    sendMessage.setText("Стрелять!");
                    sendMessage.setOnAction((actionEvent) -> shootAt(Integer.parseInt(newVal.substring(1,1)), Integer.parseInt(newVal.substring(1))));
                    if(!isMyTurn) sendMessage.setDisable(true);
                }
                if(oldVal.length() == 2 && oldVal.matches("\\d+")){
                    sendMessage.setText("Отправить");
                    sendMessage.setOnAction((actionEvent) -> sendMessage());
                    sendMessage.setDisable(false);
                }
            } else {

            }
        });

        defeat = new Button("Сдаться");
        defeat.setPrefWidth(74);
        defeat.setMinWidth(74);
        defeat.setMaxWidth(74);
        defeat.setOnAction(e -> {
            if(salagaAttention()){
                Launcher.send("SE " + BattleshipGame.convertStatistic(), 'D');
            }
        });

        sendGrid.add(textMessage,0,0);
        sendGrid.add(sendMessage,1,0);
        sendGrid.add(defeat, 3,0);
    }

    static void installInfoUI() {
        textInfo = new Label();
        textInfo.setWrapText(true);

        textlog = new Label();
        textlog.setPrefWidth(10000);
        textlog.setWrapText(true);
        textScrollPaneLog.setContent(textlog);

        infoGrid.add(textInfo,0,1);
        infoGrid.add(textlog,0,3);
    }

    public static Label getChatLabel(){
        return textChat;
    }

    //
    //
    //  CONNECTION PART
    //
    //

    static public void opponentReady(){
        isReady = true;
    }

    static public void setStatistic(int[] statistic){
        int[] opponentStatistic = BattleshipGame.statisticNumbers();
        String info = "Статистика: (Моя/Противника)" +
                "\nСделано ходов: " + statistic[0] + "/" + opponentStatistic[0] +
                "\nПопаданий: " + statistic[1] + "/" + opponentStatistic[1] +
                "\nКоличество целых кораблей: " + statistic[2] + "/" + opponentStatistic[2] +
                "\nКоличество поврежденных кораблей: " + statistic[3] + "/" + opponentStatistic[3] +
                "\nКоличество уничтоженных кораблей: " + statistic[4] + "/" + opponentStatistic[4];
        textInfo.setText(info);
    }

    static void shootAt(int x, int y){
        if(!isMyTurn) {
            messageNotMyTurn();
            return;
        }
        if(!isReady) {
            messageNotTimeYet();
            return;
        }

        Launcher.send(" " + x + " " + y, 'A');
        Launcher.send("GET", 'I');
        isMyTurn = false;
    }

    static public int getShoot(int x, int y){
        int type = BattleshipGame.getMyOcean().shootAt(x, y);
        makeMySeaSquareTruth(myOceanButtons[x][y], BattleshipGame.getMyOcean(), x, y);
        Launcher.send("GET", 'I');
        if(type != 1 && type != 2)
            isMyTurn = true;
        if(BattleshipGame.getMyOcean().isGameOver())
            Launcher.send("SF " + BattleshipGame.convertStatistic(), 'D');
        return type;
    }

    static void sendMessage(){
        String message = textMessage.getText();
        if(message != null && message.length() != 0){
            Launcher.sendMessage(message, 'M');
            writeMessage("Вы: " + message);
            textMessage.setText("");
        }
    }


    static public void writeMessage(String message){
        textChat.setText(textChat.getText() + message + "\n" );
        textChat.heightProperty().addListener(observable -> {
            chatScrollPane.setVvalue(1.0);
        });
    }

    //
    //
    //  BUTTONS STYLE
    //
    //

    static void makeMySeaSquareTruth(Button square, Ocean place, int x, int y){
        switch (place.whatIsHere(x,y)){
            case -2:
                square.setStyle(Styles.getNotShootedSeaStyle());
                break;
            case -1:
                square.setStyle(Styles.getShootedSeaStyle());
                break;
            case 0:
                square.setStyle(Styles.getNotShootedSeaStyle());
                break;
            case 1:
                square.setStyle(Styles.getSunkedShipStyle());
                sunkShip(myOceanButtons, x, y, false);
                break;
            case 2:
                square.setStyle(Styles.getFireShipStyle());
                break;
            case 3:
                square.setStyle(Styles.getMyShipStyle());
                break;
        }
    }

    static public void makeInRealMyShoot(int res, int x, int y){
        switch (res){
            case -2:
            case -1:
                settingStyleButton(opponentOceanButtons[x][y], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                break;
            case 0:
                settingStyleButton(opponentOceanButtons[x][y], Styles.getNotShootedSeaStyle(), Styles.getExtractedNotShootedSeaStyle());
                break;
            case 1:
                settingStyleButton(opponentOceanButtons[x][y], Styles.getSunkedShipStyle(), Styles.getExtractedSunkedShipStyle());
                sunkShip(opponentOceanButtons, x,y, true);
                isMyTurn = true;
                break;
            case 2:
                settingStyleButton(opponentOceanButtons[x][y], Styles.getFireShipStyle(), Styles.getExtractedFireShipStyle());
                shootedInFireShip(opponentOceanButtons, x,y);
                isMyTurn = true;
                break;
            case 3:
                settingStyleButton(opponentOceanButtons[x][y], Styles.getMyShipStyle(), Styles.getMyShipStyle());
                break;

        }
    }

    static void shootedInFireShip(Button[][] ocean, int x, int y) {
        if(x > 0 && y > 0)
            settingStyleButton(ocean[x-1][y-1], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());

        if(x > 0 && y < 9)
            settingStyleButton(ocean[x-1][y+1], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());

        if(x < 9 && y > 0)
            settingStyleButton(ocean[x+1][y-1], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());

        if(x < 9 && y < 9)
            settingStyleButton(ocean[x+1][y+1], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
    }

    static void sunkShip(Button[][] ocean, int x, int y, boolean mark) {
        int tx, ty;

        tx = x - 1;
        while(tx >= 0)
            if(ocean[tx][y].getStyle().equals(Styles.getFireShipStyle()) || ocean[tx][y].getStyle().equals(Styles.getExtractedFireShipStyle())){
                settingStyleButton(ocean[tx][y], Styles.getSunkedShipStyle(), Styles.getExtractedSunkedShipStyle());
                if(mark) {
                    if (y > 0)
                        settingStyleButton(ocean[tx][y - 1], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                    if (y < 9)
                        settingStyleButton(ocean[tx][y + 1], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                }
                tx--;
            } else {
                if(mark){
                    settingStyleButton(ocean[tx][y], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                    if(y > 0)
                        settingStyleButton(ocean[tx][y-1], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                    if(y < 9)
                        settingStyleButton(ocean[tx][y+1], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                }
                break;
           }
        tx = x + 1;
        while(tx <= 9)
            if(ocean[tx][y].getStyle().equals(Styles.getFireShipStyle()) || ocean[tx][y].getStyle().equals(Styles.getExtractedFireShipStyle())){
                settingStyleButton(ocean[tx][y], Styles.getSunkedShipStyle(), Styles.getExtractedSunkedShipStyle());
                if(mark) {
                    if (y > 0)
                        settingStyleButton(ocean[tx][y - 1], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());

                    if (y < 9)
                        settingStyleButton(ocean[tx][y + 1], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                }
                tx++;
            } else {
                if(mark) {
                    settingStyleButton(ocean[tx][y], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                    if (y > 0)
                        settingStyleButton(ocean[tx][y - 1], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                    if (y < 9)
                        settingStyleButton(ocean[tx][y + 1], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                }
                break;
            }

        ty = y - 1;
        while(ty >= 0)
            if(ocean[x][ty].getStyle().equals(Styles.getFireShipStyle()) || ocean[x][ty].getStyle().equals(Styles.getExtractedFireShipStyle())){
                settingStyleButton(ocean[x][ty], Styles.getSunkedShipStyle(), Styles.getExtractedSunkedShipStyle());
                if(mark) {
                    if (x > 0)
                        settingStyleButton(ocean[x - 1][ty], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                    if (x < 9)
                        settingStyleButton(ocean[x + 1][ty], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                }
                ty--;
            } else {
                if(mark) {
                    settingStyleButton(ocean[x][ty], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                    if (x > 0)
                        settingStyleButton(ocean[x - 1][ty], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                    if (x < 9)
                        settingStyleButton(ocean[x + 1][ty], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                }
                break;
            }

        ty = y + 1;
        while(ty <= 9)
            if(ocean[x][ty].getStyle().equals(Styles.getFireShipStyle()) || ocean[x][ty].getStyle().equals(Styles.getExtractedFireShipStyle())){
                settingStyleButton(ocean[x][ty], Styles.getSunkedShipStyle(), Styles.getExtractedSunkedShipStyle());
                if(mark) {
                    if (x > 0)
                        settingStyleButton(ocean[x - 1][ty], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                    if (x < 9)
                        settingStyleButton(ocean[x + 1][ty], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                }
                ty++;
            } else {
                if(mark) {
                    settingStyleButton(ocean[x][ty], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                    if (x > 0)
                        settingStyleButton(ocean[x - 1][ty], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                    if (x < 9)
                        settingStyleButton(ocean[x + 1][ty], Styles.getShootedSeaStyle(), Styles.getExtractedShootedSeaStyle());
                }
                break;
            }

    }

    static void settingStyleButton(Button square, String usualStyle, String aimStyle){
        square.setStyle(usualStyle);
        square.setOnMouseEntered(event -> square.setStyle(aimStyle));
        square.setOnMouseExited(event -> square.setStyle(usualStyle));
    }

    static void makeSeaClear(Button btn) {
        btn.setStyle(Styles.getNotShootedSeaStyle());
        btn.setOnMouseEntered(event -> btn.setStyle(Styles.getExtractedNotShootedSeaStyle()));
        btn.setOnMouseExited(event -> btn.setStyle(Styles.getNotShootedSeaStyle()));
    }

    /**
     *  Mark remove ship button and unmark chosen ship in right menu
     */
    static void makeAllSeaClear(Button[][] buttons) {
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                makeSeaClear(buttons[i][j]);
    }


    //
    //
    // MESSAGES
    //
    //

    static boolean salagaAttention() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Обнаружен салага");
        alert.setHeaderText("Вы действительно хотите сдаться?");
        alert.setContentText(Messages.SalagaAttentionMessages());

        Optional<ButtonType> option = alert.showAndWait();

        return option.isPresent() && option.get() == ButtonType.OK;
    }

    static void messageNotMyTurn(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Невозможно выстрелить");
        alert.setHeaderText("Сейчас не ваш ход");
        alert.setContentText(Messages.NotNyTurnMessages());
        alert.showAndWait();
    }

    static void messageNotTimeYet(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Невозможно выстрелить");
        alert.setHeaderText("Противник еще не расствил корабли");
        alert.setContentText(Messages.NotTimeYetMessages());
        alert.showAndWait();
    }

    static public void winMessage(int[] statistic, char type) {
        int[] opponentStatistic = BattleshipGame.statisticNumbers();
        String info = "Статистика:" +
                "\nСделано ходов: " + statistic[0] + "/" + opponentStatistic[0] +
                "\nПопаданий: " + statistic[1] + "/" + opponentStatistic[1] +
                "\nКоличество целых кораблей: " + statistic[2] + "/" + opponentStatistic[2] +
                "\nКоличество поврежденных кораблей: " + statistic[3] + "/" + opponentStatistic[3] +
                "\nКоличество уничтоженных кораблей: " + statistic[4] + "/" + opponentStatistic[4];

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Победа!");
        alert.setHeaderText(type == 'E' ? "Вы победили!\nПротивник бежал с поля боя." : "Вы победили!\nНо противник сражался до конца.");
        alert.setContentText(info);
        alert.showAndWait();
    }

    static public void defeatMessage(int[] statistic) {
        int[] opponentStatistic = BattleshipGame.statisticNumbers();
        String info = "Статистика:" +
                "\nСделано ходов: " + statistic[0] + "/" + opponentStatistic[0] +
                "\nПопаданий: " + statistic[1] + "/" + opponentStatistic[1] +
                "\nКоличество целых кораблей: " + statistic[2] + "/" + opponentStatistic[2] +
                "\nКоличество поврежденных кораблей: " + statistic[3] + "/" + opponentStatistic[3] +
                "\nКоличество уничтоженных кораблей: " + statistic[4] + "/" + opponentStatistic[4];

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Поражение!");
        alert.setHeaderText("Противник оказался слишком силен для вас на этот раз.");
        alert.setContentText(info);
        alert.showAndWait();
    }
}
