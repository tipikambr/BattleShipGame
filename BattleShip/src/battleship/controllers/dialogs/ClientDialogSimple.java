package battleship.controllers.dialogs;

import battleship.BattleshipGame;
import battleship.connection.Client;
import battleship.connection.Launcher;
import battleship.connection.Port;
import battleship.connection.User;
import battleship.controllers.PlanOfShips;
import battleship.ships.Battleship;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.Scanner;

public class ClientDialogSimple {

    static User choosenUser;

    @FXML
    static GridPane mainGrid;
    static Label textStandart;
    static ComboBox<User> name;
    @FXML
    static GridPane nameGrid;
    static TextField myName;

    @FXML
    static GridPane mainUI;
    static Button connectButton;
    static Button settingButton;
    static Button cancelButton;

    public static void init() {
        try {
            BattleshipGame.readINIFile();
        } catch (Exception e) {
            errorReadINIDileMessage();
        }
        Parent root = BattleshipGame.getConnectSimpleRoot();
        if(mainGrid == null) {
            mainGrid = (GridPane) root.lookup("#mainGrid");
            mainUI = (GridPane) root.lookup("#mainUI");
            nameGrid = (GridPane) root.lookup("#nameGrid");
            initialField();
            initialLabel();
            initialUI();
        }
        ObservableList<User> list = FXCollections.observableArrayList(BattleshipGame.getUsers());
        name.setItems(list);
        if(list.size() != 0)
            name.setValue(list.get(0));
    }

    static void initialField(){
        myName = new TextField();
        myName.setFont(new Font(15));
        myName.setText(BattleshipGame.getName());

        myName.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.length() > 24)
                myName.setText(newValue.substring(newValue.length() - 24));
        }));

        nameGrid.add(myName, 1,0);
    }

    static void initialUI() {
        connectButton = new Button("Подключиться");
        connectButton.setPrefWidth(10000);
        connectButton.setPrefHeight(10000);
        connectButton.wrapTextProperty().setValue(true);
        connectButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        settingButton = new Button("Настройки подключения");
        settingButton.setPrefWidth(10000);
        settingButton.setPrefHeight(10000);
        settingButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        settingButton.wrapTextProperty().setValue(true);
        cancelButton = new Button("Отменить");
        cancelButton.setPrefWidth(10000);
        cancelButton.setPrefHeight(10000);
        cancelButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        cancelButton.wrapTextProperty().setValue(true);

        initialUIListners();

        mainUI.add(cancelButton,0,0);
        mainUI.add(settingButton,2,0);
        mainUI.add(connectButton,4,0);
    }

    static void initialUIListners() {
        connectButton.setOnAction(e -> connect());

        settingButton.setOnAction(e -> setting());

        cancelButton.setOnAction(e -> cancel());
    }

    private static void cancel() {
        BattleshipGame.closeSimpleConnectDialog();
    }

    private static void setting() {
        BattleshipGame.updateINIFile(null, name.getValue(), myName.getText());
        BattleshipGame.launchSettingsConnectDialog((User) name.getValue());
    }

    private static void connect() {
        choosenUser = (User) name.getValue();
        BattleshipGame.updateINIFile(null, choosenUser, myName.getText());
        Launcher.create(new Client(choosenUser, myName.getText()), 'c');
        PlanOfShips.clickedStartConnect();
        BattleshipGame.closeSimpleConnectDialog();
    }

    static void initialLabel() {
        textStandart = new Label();
        textStandart.setText("Вы хотите подключиться к пользователю:");

        GridPane.setFillWidth(textStandart, true);
        textStandart.setFont(new Font(15));
        textStandart.prefHeight(10000);
        textStandart.prefWidth(10000);
        GridPane.setHalignment(textStandart, HPos.CENTER);
        textStandart.setWrapText(true);
        textStandart.setAlignment(Pos.CENTER);
        textStandart.setTextAlignment(TextAlignment.CENTER);
        mainGrid.add(textStandart,0,0);


        ObservableList<User> list = FXCollections.observableArrayList(BattleshipGame.getUsers());
        name = new ComboBox<User>(list);
        name.setPrefWidth(10000);
        name.setEditable(false);
        if(BattleshipGame.getUsers().size() != 0)
            name.setValue(BattleshipGame.getUsers().get(0));
        GridPane.setHalignment(name, HPos.CENTER);


        mainGrid.add(name,0,1);
    }

    private static void errorReadINIDileMessage(){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Ошибка чтения файла");
        alert.setHeaderText("Настройки невозможны из-за повреждения файла");
        alert.setContentText("Восстановите пожалуйста файл");
        alert.showAndWait();
        BattleshipGame.beginPlanBattlePlace("SP");
    }
}
