package battleship.controllers;

import battleship.BattleshipGame;
import battleship.connection.Launcher;
import battleship.resources.messages.Messages;
import battleship.resources.styles.Styles;
import battleship.ships.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.util.Optional;

public class PlanOfShips {
    static int choosenLen;
    static boolean vertival;
    static boolean isAdd;
    static boolean lookingForOpponent;
    static Ship addingShip;
    static Font globalFont;
    static int[] remainedNumShips;

    @FXML
    static GridPane fullscreenGrid;
    @FXML
    static GridPane imageLetsRest;
    static Image restImage;
    static ImageView restImageView;

    @FXML
    static GridPane controlShipsButtonPlace;
    static Button removeShipButton;
    static Button rotateShipButton;
    static Button removeAllShipsButton;
    static Button placeShipsRandomlyButton;

    @FXML
    static GridPane battlePlace;
    static Button[][] buttons;

    @FXML
    static StackPane addShipPane;
    @FXML
    static GridPane[] rightMenu;

    @FXML
    static GridPane okCancelGrid;
    static Button startButton;
    static Button hostButton;
    static Button connectButton;
    static Button cancelButton;

    static Image[][] shipsimg;
    static Image[] numbersImage;
    static ImageView[] remainedShips;
    static ImageView[] shipImg;


    @FXML
    static GridPane chatTable;
    @FXML
    static ScrollPane chatScrollPane;
    static Label chatText;
    @FXML
    static GridPane messageTable;
    static TextField messageText;
    static Button sendMessageButton;
    /**
     * This method should be used for initializing the planning scene
     */
    public static void init(String param){
        lookingForOpponent = false;
        choosenLen = 4;
        isAdd = true;
        vertival = false;
        addingShip = new Battleship();
        globalFont = Font.font(17);
        initialImages();
        for(int i = 0; i < 4; i++)
            remainedNumShips[i] = 4 - i;

        Parent root = BattleshipGame.getPlanningRoot();

        if(fullscreenGrid == null)
        {
            fullscreenGrid = (GridPane) root.lookup("#fullscreenGrid");
            fullscreenGrid.setOnMouseClicked(e -> {
                if(e.getButton() != MouseButton.SECONDARY) return;
                rotateShips();
            });
            imageLetsRest = (GridPane) root.lookup("#imageLetsRest");
            imageLetsRest.add(restImageView, 0, 2);

        }

        //Part about planning square (map)
        if (buttons == null) {
            battlePlace = (GridPane) root.lookup("#battlePlace");
            installField();
        }
        makeAllSeaClear();

        //Part about remove ship
        if(controlShipsButtonPlace == null) {
            controlShipsButtonPlace = (GridPane) root.lookup("#controlShipsButtonPlace");
            initialControlShipsButtons();
        }

        //Part about ships menu
        if(addShipPane == null) {
            addShipPane = (StackPane) root.lookup("#addShipPane");
            rightMenu = new GridPane[4];
            rightMenu[0] = (GridPane) root.lookup("#first");
            rightMenu[1] = (GridPane) root.lookup("#second");
            rightMenu[2] = (GridPane) root.lookup("#third");
            rightMenu[3] = (GridPane) root.lookup("#fourth");

            initialShipMenu();
        }
        for(int i = 0; i < 4; i++)
            updateRemainedNumOfShip(i);
        //Part about chat
        if (chatTable == null) {
            chatTable = (GridPane) root.lookup("#chatTable");
            chatScrollPane = (ScrollPane) root.lookup("#chatScrollPane");
            chatScrollPane.focusedProperty().addListener((e, oldVal, newVal) -> {
                if(newVal)
                    messageText.requestFocus();
            });
            messageTable = (GridPane) root.lookup("#messageTable");
            initialChatTableUI();
        }
        if(param != null && param.matches("S") && BattleScene.getChatLabel() != null){
            chatText.setText(BattleScene.getChatLabel().getText());
        } else
            chatText.setText("");


        messageText.setText("");


        //Part about buttons
        if (okCancelGrid == null) {
            okCancelGrid = (GridPane) root.lookup("#okCancelGrid");
            initialOkCancelButtons();
        }
        lostConnection();


    }

    static void initialChatTableUI() {
        chatText = new Label();
        chatText.setPrefWidth(10000);
        chatText.setWrapText(true);
        chatScrollPane.setFitToWidth(true);
        chatScrollPane.setContent(chatText);

        messageText = new TextField();
        messageText.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if(code.equals(KeyCode.ENTER) && !sendMessageButton.isDisable())
                sendMessage();
        });
        sendMessageButton = new Button("Отправить");
        sendMessageButton.setOnAction(e -> sendMessage());
        messageTable.add(messageText,0,0);
        messageTable.add(sendMessageButton,1,0);
    }

    static void sendMessage(){
        String message = messageText.getText();
        if(message != null && message.length() != 0){
            Launcher.sendMessage(message, 'M');
            writeMessage("Вы: " + message);
            messageText.setText("");
        }
    }

    public static Label getChatLabel(){
        return chatText;
    }

    /**
     * Create new ship with chosen length
     */
    static void installNewShip() {
        switch (choosenLen)
        {
            case 1:
                addingShip = new Submarine();
                break;
            case 2:
                addingShip = new Cruiser();
                break;
            case 3:
                addingShip = new Destroyer();
                break;
            case 4:
                addingShip= new Battleship();
                break;
        }
    }

    /**
     * Setup/Remove ship in/from chosen point
     * @param x - row of chosen point
     * @param y - column of chosen point
     */
    static void doThingWithShip(int x, int y) {
        if(isAdd)
            setShip(x,y);
        else
            removeShip(x,y);
    }

    /**
     * Setup ship in chosen point
     * @param x - row of chosen point
     * @param y - column of chosen point
     */
    static void setShip(int x, int y){
        if(remainedNumShips[choosenLen - 1] > 0 && addingShip.okToPlaceShipAt(x, y ,!vertival, BattleshipGame.getMyOcean())) {
            addingShip.placeShipAt(x, y, !vertival, BattleshipGame.getMyOcean());

            for (int k = 0; k < choosenLen; k++)
                buttons[x + (vertival ? k : 0)][y + (vertival ? 0 : k)].setStyle(Styles.getMyShipStyle());

            remainedNumShips[choosenLen - 1]--;
            updateRemainedNumOfShip(choosenLen - 1);
            for(int i = 0; remainedNumShips[choosenLen - 1] == 0 && i < 4; i++)
                choosenLen = (choosenLen + 2) % 4 + 1;

            chooseLenShip(choosenLen);
            if(remainedNumShips[choosenLen - 1] == 0) {
                rightMenu[choosenLen - 1].setStyle(Styles.getPaneBordersStyle());
            }
            installNewShip();
            if(BattleshipGame.getMyOcean().isReady())
                startButton.setDisable(false);
        }
    }

    /**
     * Remove ship from chosen point
     * @param x - row of chosen point
     * @param y - column of chosen point
     */
    static void removeShip(int x, int y){
        if(BattleshipGame.getMyOcean().whatIsHere(x, y) != 3) return;

        for(int i = x; i < 10 && BattleshipGame.getMyOcean().whatIsHere(i, y) == 3; i++)
            buttons[i][y].setStyle(Styles.getNotShootedSeaStyle());
        for(int i = x - 1; i >= 0 && BattleshipGame.getMyOcean().whatIsHere(i, y) == 3; i--)
            buttons[i][y].setStyle(Styles.getNotShootedSeaStyle());
        for(int i = y; i < 10 && BattleshipGame.getMyOcean().whatIsHere(x, i) == 3; i++)
            buttons[x][i].setStyle(Styles.getNotShootedSeaStyle());
        for(int i = y; i >= 0 && BattleshipGame.getMyOcean().whatIsHere(x, i) == 3; i--)
            buttons[x][i].setStyle(Styles.getNotShootedSeaStyle());

        int length = BattleshipGame.getMyOcean().deleteShip(x,y);
        remainedNumShips[length - 1]++;
        updateRemainedNumOfShip(length - 1);
        buttons[x][y].setStyle(Styles.getExtractedNotShootedSeaStyle());
        startButton.setDisable(true);
    }

    /**
     * Update number in table of ship (right menu)
     * @param i length of ship - 1
     */
    static void updateRemainedNumOfShip(int i) {
        remainedShips[i].setImage(numbersImage[remainedNumShips[i]]);
    }

    /**
     * Create sea buttons and setup their methods
     */
    static void installField() {
        buttons = new Button[10][10];
        for (int i = 1; i < 11; i++) {
            ImageView numHor = new ImageView(numbersImage[(9 + i) % 10]);
            ImageView numVer = new ImageView(numbersImage[(9 + i) % 10]);
            numHor.setFitHeight(30);
            numHor.setFitWidth(30);
            numVer.setFitHeight(30);
            numVer.setFitWidth(30);
            battlePlace.add(numHor, i, 0);
            battlePlace.add(numVer, 0, i);
            for (int j = 1; j < 11; j++) {
                final int x = i - 1;
                final int y = j - 1;
                Button btn = new Button();
                buttons[x][y] = btn;
                btn.setPrefWidth(10000);
                btn.setPrefHeight(10000);
                buttons[x][y].focusedProperty().addListener((e, oldVal, newVal) -> {
                    if(newVal)
                        messageText.requestFocus();
                });
                btn.setOnMouseClicked(event -> {
                    if(event.getButton() == MouseButton.PRIMARY)
                        doThingWithShip(x,y);
                    if(event.getButton() == MouseButton.SECONDARY)
                        rotateShips(x,y);
                });
                buttons[x][y].setOnMouseEntered(event -> mouseEntered(x, y));
                buttons[x][y].setOnMouseExited(event -> mouseExited(x,y));
                battlePlace.add(btn, i, j);
            }
        }


    }

    /**
     * Delete all ships from sea
     */
    static void clearSea() {
        startButton.setDisable(true);
        BattleshipGame.getMyOcean().deleteAllShips();
        makeAllSeaClear();
        for(int i = 0; i < 4; i++) {
            remainedNumShips[i] = 4 - i;
            updateRemainedNumOfShip(i);
        }
    }

    /**
     * What to do, if mouse enter in button
     * @param x row number of this button
     * @param y column number of this button
     */
    static void mouseEntered(int x, int y){
        if(isAdd) {
            if (remainedNumShips[choosenLen - 1] > 0) {
                if (addingShip.okToPlaceShipAt(x, y, !vertival, BattleshipGame.getMyOcean())) {
                    for (int i = 0; i < choosenLen; i++)
                        buttons[x + (vertival ? i : 0)][y + (vertival ? 0 : i)].setStyle(Styles.getExtractedNotShootedSeaStyle());

                    return;
                }
                for (int i = 0; i < choosenLen && x + (vertival ? i : 0) < 10 && y + (vertival ? 0 : i) < 10; i++)
                    buttons[x + (vertival ? i : 0)][y + (vertival ? 0 : i)].setStyle(Styles.getImposibleAddShipStyle());
            }
            return;
        }
        if(BattleshipGame.getMyOcean().whatIsHere(x,y) == 3)
            makrThatShip(x,y, Styles.getDeleteShipStyle());
        else
            buttons[x][y].setStyle(Styles.getExtractedNotShootedSeaStyle());

    }

    /**
     * What to do, if mouse exit from button
     * @param x row number of this button
     * @param y column number of this button
     */
    static void mouseExited(int x, int y) {
        if(!isAdd && BattleshipGame.getMyOcean().whatIsHere(x, y) == 3) {
            makrThatShip(x, y, Styles.getMyShipStyle());
            return;
        }

        int len = isAdd ? choosenLen : BattleshipGame.getMyOcean().getShipArray()[x][y].GetLength();

        for(int i = 0; i < len && x + (vertival ? i : 0) < 10 && y + (vertival ? 0 : i) < 10; i++)
            switch (BattleshipGame.getMyOcean().whatIsHere(x +(vertival ? i : 0),y + (vertival ? 0 : i))) {
                case 0:
                    buttons[x + (vertival ? i : 0)][y + (vertival ? 0 : i)].setStyle(Styles.getNotShootedSeaStyle());
                    break;
                case 3:
                    buttons[x + (vertival ? i : 0)][y + (vertival ? 0 : i)].setStyle(Styles.getMyShipStyle());
                    break;
                default:
                    buttons[x + (vertival ? i : 0)][y + (vertival ? 0 : i)].setStyle(Styles.getErrorStyle());
                    break;
        }
    }

    /**
     * Set style on chosen ship
     * @param x row number of part of this ship
     * @param y column number of part of this ship
     * @param style string style in format fxml
     */
    static void makrThatShip(int x, int y, String style){
        for (int i = x; i < 10 && BattleshipGame.getMyOcean().whatIsHere(i, y) == 3; i++)
            buttons[i][y].setStyle(style);
        for (int i = x - 1; i >= 0 && BattleshipGame.getMyOcean().whatIsHere(i, y) == 3; i--)
            buttons[i][y].setStyle(style);
        for (int i = y; i < 10 && BattleshipGame.getMyOcean().whatIsHere(x, i) == 3; i++)
            buttons[x][i].setStyle(style);
        for (int i = y; i >= 0 && BattleshipGame.getMyOcean().whatIsHere(x, i) == 3; i--)
            buttons[x][i].setStyle(style);
    }

    /**
     * Initial all images and image view
     */
    static void initialImages(){
        if(shipsimg == null) {
            shipsimg = new Image[4][2];
            numbersImage = new Image[10];
            remainedShips = new ImageView[4];
            shipImg = new ImageView[4];
            remainedNumShips = new int[4];

            restImage = new Image(PlanOfShips.class.getResourceAsStream("/battleship/resources/images/sea.jpeg"));
            restImageView = new ImageView(restImage);


            for(int i = 0; i < 4; i++){
                shipsimg[i][0] = new Image(PlanOfShips.class.getResourceAsStream("/battleship/resources/ships/shipl" + (i + 1) + "h.png"));
                shipsimg[i][1] = new Image(PlanOfShips.class.getResourceAsStream("/battleship/resources/ships/shipl" + (i + 1) + "v.png"));
            }

            for(int i = 0; i < 10; i++)
                numbersImage[i] = new Image(PlanOfShips.class.getResourceAsStream("/battleship/resources/numbers/"+i+".png"));

            for(int i = 0; i < 4; i++) {
                remainedShips[i] = new ImageView(numbersImage[4 - i]);
                remainedShips[i].setFitWidth(66);
                remainedShips[i].setFitHeight(66);

                shipImg[i] = new ImageView(shipsimg[i][vertival ? 0 : 1]);
                shipImg[i].setFitHeight(66);
                shipImg[i].setFitWidth(66);
            }
        }
    }

    /**
     * Initial buttons continue and remove to main menu
     */
    static void initialOkCancelButtons() {
        cancelButton = new Button("Вернуться в меню");
        cancelButton.setPrefWidth(10000);
        cancelButton.setPrefHeight(10000);
        cancelButton.setFont(globalFont);;
        cancelButton.setOnAction(e -> confirmCancel());

        startButton = new Button("Оффлайн сражение!");
        startButton.setFont(globalFont);
        startButton.setPrefWidth(10000);
        startButton.setPrefHeight(10000);
        startButton.setDisable(true);
        startButton.setOnAction(e -> confirmSolo());

        hostButton = new Button("Ожидать противника!");
        hostButton.setFont(globalFont);
        hostButton.setPrefWidth(10000);
        hostButton.setPrefHeight(10000);
        hostButton.setOnAction(e -> confirmHost());

        connectButton = new Button("Найти противника!");
        connectButton.setFont(globalFont);
        connectButton.setPrefWidth(10000);
        connectButton.setPrefHeight(10000);
        connectButton.setOnAction(e -> confirmConnect());


        okCancelGrid.add(cancelButton,2,2);
        okCancelGrid.add(startButton,2,3);
        okCancelGrid.add(hostButton,2,4);
        okCancelGrid.add(connectButton,2,5);
    }

    /**
     * What to do, if pressed button remove to main menu
     * @return
     *      {true}  -   remove to main menu
     *      {false} -   don't remove
     */
    static void confirmCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Подтверждение действия");
        alert.setHeaderText("Вы действительно хотите выйти в меню?");
        alert.setContentText("Расставленные корабли будут удалены с поля");

        Optional<ButtonType> option = alert.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK)
            BattleshipGame.backToMenu();
    }

    /**
     * Player want to play multiplayer :c
     */
    static void confirmSolo() {
        BattleshipGame.startSoloGame();
    }

    static void confirmMultiple() {
        BattleshipGame.startMultipleGame();
    }

    static void confirmHost() {
        BattleshipGame.launchSimpleHostDialog();
    }

    static void confirmConnect() {
        BattleshipGame.launchSimpleConnectDialog();
    }

    static void confirmStop(){
        lookingForOpponent = false;
        clickedStop();
        Launcher.stop();
    }

    static void confirmDisconnect(){
        lookingForOpponent = false;
        writeMessage("Системное: вы отключились от игры.");
        lostConnection();
        Launcher.send("S",'Q');
        Launcher.stop();
    }

    static public void clickedStartHost(){
        lookingForOpponent = true;
        startButton.setDisable(true);

        cancelButton.setDisable(true);

        if(BattleshipGame.canBeServer()) {
            hostButton.setText("Прекратить ожидание");
            hostButton.setOnAction(e -> confirmStop());
            hostButton.setDisable(false);
        }

        if(BattleshipGame.canBeClient())
            connectButton.setDisable(true);

        sendMessageButton.setDisable(true);
    }

    static public void clickedStartConnect(){
        lookingForOpponent = true;
        startButton.setDisable(true);

        cancelButton.setDisable(true);

        if(BattleshipGame.canBeServer())
            hostButton.setDisable(true);

        if(BattleshipGame.canBeClient()) {
            connectButton.setText("Прекратить поиск");
            connectButton.setOnAction(e -> confirmStop());
            connectButton.setDisable(false);
        }

        sendMessageButton.setDisable(true);
    }

    static public void clickedStop(){
        lookingForOpponent = false;
        if(BattleshipGame.getMyOcean().isReady())
            startButton.setDisable(false);
        else
            startButton.setDisable(true);

        cancelButton.setDisable(false);

        if(BattleshipGame.canBeServer()) {
            hostButton.setText("Ожидать противника!");
            hostButton.setOnAction(e -> confirmHost());
            hostButton.setDisable(false);
        }

        if(BattleshipGame.canBeClient()) {
            connectButton.setText("Найти противника!");
            connectButton.setOnAction(e -> confirmConnect());
            connectButton.setDisable(false);
        }

        sendMessageButton.setDisable(true);
    }

    static public void getConnection(){
        lookingForOpponent = false;
        startButton.setText("Онлайн сражение!");
        startButton.setOnAction(e -> confirmMultiple());
        if(BattleshipGame.getMyOcean().isReady())
            startButton.setDisable(false);
        else
            startButton.setDisable(true);

        cancelButton.setText("Отключиться");
        cancelButton.setOnAction(e -> confirmDisconnect());
        cancelButton.setDisable(false);

        if(BattleshipGame.canBeServer())
            hostButton.setDisable(true);

        if(BattleshipGame.canBeClient())
            connectButton.setDisable(true);

        sendMessageButton.setDisable(false);
    }

    static public void lostConnection() {
        lookingForOpponent = false;
        startButton.setText("Оффлайн сражение!");
        startButton.setOnAction(e -> confirmSolo());
        if(BattleshipGame.getMyOcean().isReady())
            startButton.setDisable(false);
        else
            startButton.setDisable(true);

        cancelButton.setText("Вернуться в меню");
        cancelButton.setOnAction(e -> confirmCancel());
        cancelButton.setDisable(false);

        if(BattleshipGame.canBeServer()) {
            hostButton.setText("Ожидать противника!");
            hostButton.setOnAction(e -> confirmHost());
            hostButton.setDisable(false);
        } else
            hostButton.setDisable(true);

        if(BattleshipGame.canBeClient()) {
            connectButton.setText("Найти противника!");
            connectButton.setOnAction(e -> confirmConnect());
            connectButton.setDisable(false);
        } else
            connectButton.setDisable(true);

        sendMessageButton.setDisable(true);
    }

    static public void writeMessage(String message){
        chatText.setText(chatText.getText() + message + "\n" );
        chatText.heightProperty().addListener(observable -> {
            chatScrollPane.setVvalue(1.0);
        });
    }


    /**
     * Initial controls of doing
     */
    static void initialControlShipsButtons() {
        removeShipButton = new Button("Вернуть корабль в \"руку\"");
        removeShipButton.setPrefWidth(10000);
        removeShipButton.setPrefHeight(10000);
        removeShipButton.setFont(globalFont);
        removeShipButton.setOnAction(e -> chooseRemoveShip());

        rotateShipButton = new Button("Повернуть корабли в \"руке\"");
        rotateShipButton.setPrefWidth(100000);
        rotateShipButton.setPrefHeight(100000);
        rotateShipButton.setFont(globalFont);
        rotateShipButton.setOnMouseClicked(e -> rotateShips());

        removeAllShipsButton = new Button("Очистить поле");
        removeAllShipsButton.setPrefWidth(10000);
        removeAllShipsButton.setPrefHeight(10000);
        removeAllShipsButton.setFont(globalFont);
        removeAllShipsButton.setOnAction(e -> clearSea());

        placeShipsRandomlyButton = new Button("Расставить корабли случайно");
        placeShipsRandomlyButton.setPrefWidth(10000);
        placeShipsRandomlyButton.setPrefHeight(10000);
        placeShipsRandomlyButton.setFont(globalFont);
        placeShipsRandomlyButton.setOnAction(e -> placeAllShipsRandomply());

        controlShipsButtonPlace.add(removeShipButton, 0,0);
        controlShipsButtonPlace.add(rotateShipButton, 0, 2);
        controlShipsButtonPlace.add(removeAllShipsButton,0, 4);
        controlShipsButtonPlace.add(placeShipsRandomlyButton,0,6);
    }

    static void placeAllShipsRandomply(){
        BattleshipGame.getMyOcean().placeAllShipsRandomly();
        makeAllSeaClear();
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                if(BattleshipGame.getMyOcean().whatIsHere(i,j) == 3)
                   buttons[i][j].setStyle(Styles.getMyShipStyle());
        for(int i = 0; i < 4; i++){
            remainedNumShips[i] = 0;
            updateRemainedNumOfShip(i);
        }
        if(!lookingForOpponent)
            startButton.setDisable(false);
    }

    /**
     * Rotate ships in deck
     */
    static void rotateShips() {
        vertival = !vertival;
        for(int i = 0; i < 4; i++)
            shipImg[i].setImage(shipsimg[i][vertival? 0 : 1]);
    }

    /**
     * rotate ships in deck and swimming ship
     * @param x bow row number of swimming ship
     * @param y bow column number of swimming ship
     */
    static void rotateShips(int x, int y) {
        mouseExited(x, y);
        vertival = !vertival;
        mouseEntered(x,y);
        for(int i = 0; i < 4; i++)
            shipImg[i].setImage(shipsimg[i][vertival? 0 : 1]);
    }

    /**
     * setup style of clear sea in chosen point
     * @param x - row number of chosen point
     * @param y - column number of chosen point
     */
    static void makeSeaClear(int x, int y) {
        buttons[x][y].setStyle(Styles.getNotShootedSeaStyle());
    }

    /**
     * setup style of clear sea in each element of sea
     */
    static void makeAllSeaClear() {
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                makeSeaClear(i, j);
    }

    /**
     * Initial right menu with table of ships
     */
    static void initialShipMenu() {
        //Add header
        Label addShip = new Label("Добавить корабль");
        addShipPane.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            addShip.setFont(globalFont);
        });
        addShipPane.getChildren().add(addShip);

        //Add assortment  of ships
        for(int i = 0; i < 4; i++) {
            final int lenShip = i + 1;
            shipImg[i].setImage(shipsimg[i][vertival? 0 : 1]);
            rightMenu[i].add(shipImg[i], 2, 0);
            rightMenu[i].add(remainedShips[i], 0,0);
            rightMenu[i].setStyle(Styles.getPaneBordersStyle());
            rightMenu[i].setOnMouseClicked(e -> chooseLenShip(lenShip));
        }
        rightMenu[choosenLen - 1].setStyle(Styles.getExtractedPaneBordersStyle());
    }

    /**
     * Mark chosen ship in right menu and unmark remove ship button
     * @param lenShip length of chosen ship
     */
    static void chooseLenShip(int lenShip) {
        if(remainedNumShips[lenShip - 1] == 0) return;
        choosenLen = lenShip;
        isAdd = true;

        installNewShip();

        removeShipButton.setStyle("");
        for(int i = 0; i < 4; i++)
            rightMenu[i].setStyle(Styles.getPaneBordersStyle());
        rightMenu[lenShip - 1].setStyle(Styles.getExtractedPaneBordersStyle());
    }

    /**
     *  Mark remove ship button and unmark chosen ship in right menu
     */
    static void chooseRemoveShip(){
        isAdd = false;
        addingShip = new EmptySea();
        removeShipButton.setStyle(Styles.getExtractedPaneBordersStyle());
        for(int i = 0; i < 4; i++)
            rightMenu[i].setStyle(Styles.getPaneBordersStyle());
    }
}

