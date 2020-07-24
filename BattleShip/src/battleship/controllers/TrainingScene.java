package battleship.controllers;

import battleship.BattleshipGame;
import battleship.resources.messages.Messages;
import battleship.resources.styles.Styles;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.Optional;

public class TrainingScene {
    static int sunkedShips;
    static Font globalFont;

    //Battle field objects
    @FXML
    static GridPane battlePlace;
    static Button[][] buttons;

    //Logger objects
    @FXML
    static ScrollPane textScrollPaneLog;
    static Label textlog;


    @FXML
    static GridPane rightPanel;
    //Information objects
    @FXML
    static Pane infoRow;
    static Label information;

    //Control UI objects
    @FXML
    static GridPane controlGrid;
    static Label horizontal;
    static Label vertical;
    static TextField x_text;
    static TextField y_text;
    static Button shoot;

    //Menu UI objects
    @FXML
    static GridPane menuGrid;
    static Button backToMenuButton;

    /**
     * This method should be used for initializing the battle scene
     */
    public static void init() {
        //Initial start variable
        if(globalFont == null) globalFont = new Font(12);
        sunkedShips = 0;

        Parent root = BattleshipGame.getTrainingRoot();

        //Part about battle square (map)
        if (buttons == null) {
            battlePlace = (GridPane) root.lookup("#battlePlace");
            installField();
        } else
            makeAllSeaClear();

        //Part about logging
        if (textlog == null) {
            textScrollPaneLog = (ScrollPane) root.lookup("#textScrollPaneLog");
            initialLoggerPlace();
        }
        textlog.setText("Морское сражение началось!");

        //Part about statistic information
        if(information == null){
            infoRow = (Pane)root.lookup("#infoRow");
            initialStatisticInformation();
        }
        information.setText(BattleshipGame.getTrainStatistic());

        //Part about control UI (shoot settings and shoot button)
        if(horizontal == null) {
            controlGrid = (GridPane)root.lookup("#controlGrid");
            rightPanel = (GridPane) root.lookup("#rightPanel");
            initialControllUI();
        }

        //Part about menu UI
        if(backToMenuButton == null) {
            menuGrid = (GridPane)root.lookup("#menuGrid");
            initialMenuUI();
        }
    }

    /**
     * Create sea buttons and setup their methods
     */
    static void installField() {
        buttons = new Button[10][10];
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
                buttons[x][y] = new Button();
                buttons[x][y].setPrefWidth(10000);
                buttons[x][y].setPrefHeight(10000);
                makeSeaClear(buttons[x][y]);
                buttons[x][y].setOnMouseClicked(e -> {
                    if(e.getButton() != MouseButton.PRIMARY) return;
                    Shoot(x, y);
                });
                buttons[x][y].focusedProperty().addListener((observableValue,  aBoolean,  t1) -> x_text.requestFocus());
                battlePlace.add(buttons[x][y], i, j);
            }
        }
    }

    /**
     * Initial logger place
     */
    static void initialLoggerPlace() {
        textlog = new Label();
        textlog.setPrefWidth(320);
        textlog.setWrapText(true);
        textScrollPaneLog.setContent(textlog);

    }

    /**
     * Initial statistic place
     */
    static void initialStatisticInformation() {
        information = new Label();
        information.setWrapText(true);
        infoRow.getChildren().add(information);
        infoRow.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            globalFont = Font.font(Math.min(newValue.getHeight()/10, newValue.getWidth() / 17));
            information.setFont(globalFont);
        });

    }

    /**
     * Initial control UI pane
     */
    static void initialControllUI() {
        horizontal = new Label("Введите координату Х:");
        vertical = new Label("Введите координату Y:");
        x_text = new TextField();
        y_text = new TextField();
        shoot = new Button("Стреляяяяяй!!!");

        shoot.setPrefHeight(10000);
        shoot.setPrefWidth(10000);

        controlGrid.add(horizontal, 0, 0);
        controlGrid.add(vertical, 0, 1);
        controlGrid.add(x_text, 1, 0);
        controlGrid.add(y_text, 1, 1);
        rightPanel.add(shoot, 0, 2);

        initialUILisrners();
    }

    /**
     * Install listeners for control UI
     */
    static void initialUILisrners() {
        x_text.textProperty().addListener((observableValue, s, t1) -> {
            if(t1 == null || t1.length() != 1 ||  t1.charAt(0) < '0' || t1.charAt(0) > '9')
            {
                x_text.setText("");
                return;
            }
            y_text.requestFocus();
        });
        x_text.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(t1)
                x_text.setText("");
        });
        y_text.textProperty().addListener((observableValue, s, t1) -> {
            if(t1 == null || t1.length() != 1 ||  t1.charAt(0) < '0' || t1.charAt(0) > '9')
            {
                y_text.setText("");
                return;
            }
            shoot.requestFocus();
        });
        y_text.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(t1)
                y_text.setText("");
        });

        shoot.setOnAction(e -> {
            if (isCorrectNum(x_text.getText()) && isCorrectNum(y_text.getText())) {
                int x = Integer.parseInt(x_text.getText());
                int y = Integer.parseInt(y_text.getText());
                Shoot(x, y);
            } else
                DisunderstandingAim();
            x_text.requestFocus();
        });

        controlGrid.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            horizontal.setFont(globalFont);
            vertical.setFont(globalFont);
            x_text.setFont(globalFont);
            y_text.setFont(globalFont);
        });
        addLisinerIsCorrectNumber(x_text);
        addLisinerIsCorrectNumber(y_text);
    }

    /**
     * Initial menu UI pane
     */
    static void initialMenuUI() {
        backToMenuButton = new Button("Сдаться.");
        backToMenuButton.setPrefWidth(10000);
        backToMenuButton.setPrefHeight(10000);
        backToMenuButton.setOnAction(e -> {
            if(SalagaAttention()) {
                BattleshipGame.backToMenu();
            }
        });
        menuGrid.add(backToMenuButton,0,1);
    }

    /**
     * Install listeners for correcting textfields
     * @param field, where should be installed listener
     */
    static void addLisinerIsCorrectNumber(TextField field) {
        field.focusedProperty().addListener((observableValue,  aBoolean,  t1) -> {
            if (!t1)
                if(isCorrectNum(field.getText()))
                    field.setStyle(Styles.getCorrectTextFieldStyle());
                else
                    field.setStyle(Styles.getIncorrectTextFieldStyle());
            else
                field.setStyle("");
        });
    }

    /**
     * Check, is string is digit
     * @param str checking string
     * @return
     *      {true}  -   correct string
     *      {false} -   incorrect string
     */
    static boolean isCorrectNum(String str) {
        try {
            int num = Integer.parseInt(str);
            return num >= 0 && num <= 9;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Make shoot in point
     * @param x row number of point
     * @param y column number of point
     */
    static void Shoot(int x, int y) {
        switch (BattleshipGame.getOpponentOcean().whatIsHere(x,y))
        {
            case -2:
                UnderstandPlace();
                break;
            case -1:
            {
                ShootNotFirstTimeInSea();
                break;
            }
            case 0:
            {
                BattleshipGame.getOpponentOcean().shootAt(x,y);
                textlog.setText("Выстрел по координатам (" + x + ";" + y + ")\n" +
                        Messages.ShootInSeaMessages() +
                        "\n________________________________________________________________\n" +
                        textlog.getText());
                makeSeaShooted(buttons[x][y]);
                break;
            }
            case 1:
            {
                ShootInSunkShip();
                break;
            }
            case 2:
            {
                ShootInFireShip();
                break;
            }
            case 3:
            {
                BattleshipGame.getOpponentOcean().shootAt(x,y);

                if(x - 1 >= 0 && y - 1 >= 0)
                    makeSeaUnderstand(x - 1, y - 1);
                if(x + 1 <= 9 && y - 1 >= 0)
                    makeSeaUnderstand(x + 1, y - 1);
                if(x - 1 >= 0 && y + 1 <= 9)
                    makeSeaUnderstand(x - 1, y + 1);
                if(x + 1 <= 9 && y + 1 <= 9)
                    makeSeaUnderstand(x + 1, y + 1);

                if(BattleshipGame.getOpponentOcean().whatIsHere(x,y) == 1)
                    textlog.setText("Выстрел по координатам (" + x + ";" + y + ")\n" +
                            Messages.DestroyShipMessage() +
                            "\n________________________________________________________________\n" +
                            textlog.getText());
                else
                    textlog.setText("Выстрел по координатам (" + x + ";" + y + ")\n" +
                            Messages.ShootInShipMessages() +
                            "\n________________________________________________________________\n" +
                            textlog.getText());
                makeShipFire(buttons[x][y]);
                supposedPlaces(x,y);
            break;
            }
        }
        if(sunkedShips == 10)
            WinGame();
        information.setText(BattleshipGame.getTrainStatistic());
    }

    /**
     * Alert message, that that place already shooted
     */
    static void ShootNotFirstTimeInSea() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Отмена выстрела");
        alert.setHeaderText("Сюда нет смысла стрелять");
        alert.setContentText(Messages.ShootNotFirstTimeInSeaMessages());
        alert.showAndWait();
    }

    /**
     * Alert message, that place is empty sea and players understand that
     */
    static void UnderstandPlace() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Отмена выстрела");
        alert.setHeaderText("Сюда нет смысла стрелять");
        alert.setContentText(Messages.UnderstandMessage());
        alert.showAndWait();
    }

    /**
     * Alert message, that ship is already sunk
     */
    static void ShootInSunkShip() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Отмена выстрела");
        alert.setHeaderText("Сюда нельзя стрелять!");
        alert.setContentText(Messages.ShootInSunkShipMessages());
        alert.showAndWait();
    }

    /**
     * Alert message, that you win the game
     */
    static void WinGame() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Корабль ещё на плаву!");
        alert.setHeaderText("Мы победили в этой перестрелке!");
        alert.setContentText(BattleshipGame.getResultStatistic());
        alert.showAndWait();
        BattleshipGame.backToMenu();
    }

    /**
     * Alert message, that you shoot in already fire part of ship
     */
    static void ShootInFireShip() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Отмена выстрела");
        alert.setHeaderText("Нет смысла стрелять по этой части корабля!");
        alert.setContentText(Messages.ShootInFireShipMessages());
        alert.showAndWait();
    }

    /**
     * Alert message, that written coordinates is incorrect
     */
    static void DisunderstandingAim() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("!$#@%}{^#@!#@");
        alert.setHeaderText("Неверно выбрана цель");
        alert.setContentText(Messages.DisunderstandingAimMessages());
        alert.showAndWait();
    }

    /**
     * Alert message, that mean you try to surrender
     * @return
     *      {true}  -   you agree to be a snotty
     *      {false} -   battle continue
     */
    static boolean SalagaAttention() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Обнаружен салага");
        alert.setHeaderText("Вы действительно хотите сдаться?");
        alert.setContentText(Messages.SalagaAttentionMessages());

        Optional<ButtonType> option = alert.showAndWait();

        return option.isPresent() && option.get() == ButtonType.OK;
    }

    /**
     * Mark near places as empty sea
     * @param x row number of middle point
     * @param y column number of middle point
     */
    static void supposedPlaces(int x, int y) {
        if(BattleshipGame.getOpponentOcean().whatIsHere(x,y) == 1) {
            sunkedShips++;
            int i;

            if (x - 1 >= 0) {
                makeSeaUnderstand(x - 1, y);
                if (y - 1 >= 0)
                    makeSeaUnderstand(x - 1, y - 1);
                if (y + 1 <= 9)
                    makeSeaUnderstand(x - 1, y + 1);
            }
            if (y - 1 >= 0)
                makeSeaUnderstand(x, y - 1);
            if (y + 1 <= 9)
                makeSeaUnderstand(x, y + 1);
            for (i = 0; x + i <= 9 && BattleshipGame.getOpponentOcean().whatIsHere(x + i, y) == 1; i++) {
                makeShipSunk(buttons[x+i][y]);
                if (y - 1 >= 0)
                    makeSeaUnderstand(x + i, y - 1);
                if (y + 1 <= 9)
                    makeSeaUnderstand(x + i, y + 1);
            }
            if (x + i <= 9) {
                makeSeaUnderstand(x + i, y);
                if (y - 1 >= 0)
                    makeSeaUnderstand(x + i, y - 1);
                if (y + 1 <= 9)
                    makeSeaUnderstand(x + i, y + 1);
            }

            i = 1;
            if (x - 1 >= 0 && BattleshipGame.getOpponentOcean().whatIsHere(x - 1, y) == 1) {
                for (; x - i >= 0 && BattleshipGame.getOpponentOcean().whatIsHere(x - i, y) == 1; i++) {
                    makeShipSunk(buttons[x - i][y]);
                    if (y - 1 >= 0)
                        makeSeaUnderstand(x - i, y - 1);
                    if (y + 1 <= 9)
                        makeSeaUnderstand(x - i, y + 1);
                }
                if (x - i >= 0) {
                    makeSeaUnderstand(x - i, y);
                    if (y - 1 >= 0)
                        makeSeaUnderstand(x - i, y - 1);
                    if (y + 1 <= 9)
                        makeSeaUnderstand(x - i, y + 1);
                }
            }
            i = 1;
            if (y - 1 >= 0 && BattleshipGame.getOpponentOcean().whatIsHere(x, y - 1) == 1) {
                for (; y - i >= 0 && BattleshipGame.getOpponentOcean().whatIsHere(x, y - i) == 1; i++) {
                    makeShipSunk(buttons[x][y - i]);
                    if (x - 1 >= 0)
                        makeSeaUnderstand(x - 1, y - i);
                    if (x + 1 <= 9)
                        makeSeaUnderstand(x + 1, y - i);
                }
                if (y - i >= 0) {
                    makeSeaUnderstand(x, y - i);
                    if (x - 1 >= 0)
                        makeSeaUnderstand(x - 1, y - i);
                    if (x + 1 <= 9)
                        makeSeaUnderstand(x + 1, y - i);
                }
            }
            i = 1;
            if (y + 1 <= 9 && BattleshipGame.getOpponentOcean().whatIsHere(x, y + 1) == 1) {
                for (; y + i <= 9 && BattleshipGame.getOpponentOcean().whatIsHere(x, y + i) == 1; i++) {
                    makeShipSunk(buttons[x][y + i]);
                    if (x - 1 >= 0)
                        makeSeaUnderstand(x - 1, y + i);
                    if (x + 1 <= 9)
                        makeSeaUnderstand(x + 1, y + i);
                }
                if (y + i <= 9) {
                    makeSeaUnderstand(x, y + i);
                    if (x - 1 >= 0)
                        makeSeaUnderstand(x - 1, y + i);
                    if (x + 1 <= 9)
                        makeSeaUnderstand(x + 1, y + i);
                }
            }
        }
    }

    /**
     * Mark that button as fired ship
     * @param btn - new fired part of ship
     */
    static void makeShipFire(Button btn) {
        btn.setStyle(Styles.getFireShipStyle());
        btn.setOnMouseEntered(ent -> btn.setStyle(Styles.getExtractedFireShipStyle()));
        btn.setOnMouseExited(ent -> btn.setStyle(Styles.getFireShipStyle()));
    }

    /**
     * Mark that button as sunked ship
     * @param btn - new sunked part of ship
     */
    static void makeShipSunk(Button btn) {
        btn.setStyle(Styles.getSunkedShipStyle());
        btn.setOnMouseEntered(ent -> btn.setStyle(Styles.getExtractedSunkedShipStyle()));
        btn.setOnMouseExited(ent -> btn.setStyle(Styles.getSunkedShipStyle()));
    }

    /**
     * Mark button as understand
     * @param x - row number of button
     * @param y - column number off button
     */
    static void makeSeaUnderstand(int x, int y) {
        if(BattleshipGame.getOpponentOcean().whatIsHere(x,y) == -1) return;
        buttons[x][y].setStyle(Styles.getShootedSeaStyle());
        buttons[x][y].setOnMouseEntered(event -> buttons[x][y].setStyle(Styles.getExtractedShootedSeaStyle()));
        buttons[x][y].setOnMouseExited(event -> buttons[x][y].setStyle(Styles.getShootedSeaStyle()));
        BattleshipGame.getOpponentOcean().markAsSea(x,y);
    }

    /**
     * Mark that button as shooted sea
     * @param btn - new empty understand sea
     */
    static void makeSeaShooted(Button btn) {
        btn.setStyle(Styles.getShootedSeaStyle());
        btn.setOnMouseEntered(event -> btn.setStyle(Styles.getExtractedShootedSeaStyle()));
        btn.setOnMouseExited(event -> btn.setStyle(Styles.getShootedSeaStyle()));
    }

    /**
     * setup style of unknown sea
     * @param btn - new empty sea
     */
    static void makeSeaClear(Button btn) {
        btn.setStyle(Styles.getNotShootedSeaStyle());
        btn.setOnMouseEntered(event -> btn.setStyle(Styles.getExtractedNotShootedSeaStyle()));
        btn.setOnMouseExited(event -> btn.setStyle(Styles.getNotShootedSeaStyle()));
    }

    /**
     *  Mark remove ship button and unmark chosen ship in right menu
     */
    static void makeAllSeaClear() {
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                makeSeaClear(buttons[i][j]);
    }
}
