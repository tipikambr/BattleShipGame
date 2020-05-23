package battleship.controllers;

import battleship.BattleshipGame;
import battleship.connection.Client;
import battleship.connection.Launcher;
import battleship.connection.User;
import battleship.ships.Battleship;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.Scanner;

public class ClientDialogSimple {

    static User choosenUser;

    @FXML
    static GridPane mainGrid;
    static Label textStandart;
    static ComboBox name;
    @FXML
    static GridPane nameGrid;
    static TextField myName;

    @FXML
    static GridPane mainUI;
    static Button connectButton;
    static Button settingButton;
    static Button cancelButton;

    public static void init() {
        Parent root = BattleshipGame.getConnectSimpleRoot();
        if(mainGrid == null) {
            mainGrid = (GridPane) root.lookup("#mainGrid");
            mainUI = (GridPane) root.lookup("#mainUI");
            nameGrid = (GridPane) root.lookup("#nameGrid");
            initialField();
            initialLabel();
            initialUI();
        }
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
}
