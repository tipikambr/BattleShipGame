package battleship.controllers;

import battleship.BattleshipGame;
import battleship.connection.Launcher;
import battleship.connection.Port;
import battleship.connection.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;

public class ServerDialogSimple {
     static Port port;

    @FXML
    static GridPane mainGrid;
    static Label textHost;
    static ComboBox<Port> portComboBox;


    @FXML
    static GridPane mainUI;
    static Button hostButton;
    static Button settingButton;
    static Button cancelButton;

    public static void init() {
        Parent root = BattleshipGame.getHostSimpleRoot();
        if(mainGrid == null) {
            mainGrid = (GridPane) root.lookup("#mainGrid");
            mainUI = (GridPane) root.lookup("#mainUI");
            initialLabel();
            initialUI();
        }
    }

    static void initialUI() {
        hostButton = new Button("Раздать");
        hostButton.setPrefWidth(10000);
        hostButton.setPrefHeight(10000);
        hostButton.wrapTextProperty().setValue(true);
        hostButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        settingButton = new Button("Настройки раздачи");
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
        mainUI.add(hostButton,4,0);
    }

    static void initialUIListners() {
        hostButton.setOnAction(e -> host());

        settingButton.setOnAction(e -> setting());

        cancelButton.setOnAction(e -> cancel());
    }

    private static void cancel() {
        BattleshipGame.closeSimpleHostDialog();
    }

    private static void setting() {
        //TODO
    }

    private static void host() {
        port = portComboBox.getValue();
        BattleshipGame.updateINIFile(port, null, null);
        Launcher.create(new Server(port), 's');
        PlanOfShips.clickedStartHost();
        BattleshipGame.closeSimpleHostDialog();
    }

    static void initialLabel() {
        textHost = new Label();
        textHost.setText("Вы хотите создать сервер с портом:\n");

        GridPane.setFillWidth(textHost, true);
        textHost.setFont(new Font(15));
        textHost.prefHeight(10000);
        textHost.prefWidth(10000);
        GridPane.setHalignment(textHost, HPos.CENTER);
        textHost.setWrapText(true);
        textHost.setAlignment(Pos.CENTER);
        textHost.setTextAlignment(TextAlignment.CENTER);
        mainGrid.add(textHost,0,0);

        ObservableList<Port> list = FXCollections.observableArrayList(BattleshipGame.getPorts());
        portComboBox = new ComboBox<>(list);
        portComboBox.setConverter(new StringConverter<Port>() {
            @Override
            public String toString(Port object) {
                if(object != null)
                    return object.toString();
                else
                    return "";
            }

            @Override
            public Port fromString(String string) {
                Port p = new Port();
                p.port = Integer.parseInt(string);
                return p;
            }
        });
        if(BattleshipGame.getPorts().size() != 0)
            portComboBox.setValue(BattleshipGame.getPorts().get(0));
        portComboBox.setEditable(true);
        portComboBox.getEditor().textProperty().addListener(((observable, oldValue, newValue) -> {
            newValue = newValue.replaceAll("\\D", "");
            if(newValue.length() > 5)
                newValue = newValue.substring(newValue.length() - 5);
            portComboBox.getEditor().setText(newValue);
        }));
        GridPane.setHalignment(portComboBox,HPos.CENTER);
        mainGrid.add(portComboBox,0,1);


    }

    public void setTextHost(int port){
        textHost.setText("Вы хотите создать сервер с портом:\n" + port);
    }
}
