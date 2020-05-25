package battleship.controllers.dialogs;

import battleship.BattleshipGame;
import battleship.connection.Port;
import battleship.connection.User;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class ClientDialogSettings {

    @FXML
    static GridPane mainGrid;
    static TableView<User> table;

    @FXML
    static GridPane settingsGrid;

    @FXML
    static GridPane nameGrid;
    static TextField nameServer;

    @FXML
    static GridPane portIPGrid;
    static TextField[] ip = new TextField[4];
    static TextField port;

    @FXML
    static GridPane buttonsGrid;
    static Button back;
    static Button change;
    static Button add;

    public static void init(User user){
        Parent root = BattleshipGame.getConnectSettingRoot();

        if(mainGrid == null)
        {
            mainGrid = (GridPane) root.lookup("#mainGrid");

            settingsGrid = (GridPane) root.lookup("#settingsGrid");

            nameGrid = (GridPane) root.lookup("#nameGrid");
            portIPGrid = (GridPane) root.lookup("#portIPGrid");
            buttonsGrid = (GridPane) root.lookup("#buttonsGrid");

            initialTable();
            initialnameGrid();
            initialPortIPGrid();
            initialButtonsGrid();

            String[] ipSplit = user.getIp().split("\\.");

            nameServer.setText(user.getName());
            for(int i = 0; i < 4; i++)
                ip[i].setText(ipSplit[i]);
            port.setText(String.valueOf(user.getPort()));
            table.getItems().addAll(BattleshipGame.getUsers());
        }



        table.getSelectionModel().select(user);
    }

    static void initialTable(){
        table = new TableView<>();

        TableColumn<User, String> userName = new TableColumn<>("Имя сервера");
        userName.setMinWidth(180);
        userName.setMaxWidth(200);
        userName.setStyle("-fx-alignment: CENTER");
        userName.setCellValueFactory(data -> data.getValue().nameProperty());


        TableColumn<User, String> userIP = new TableColumn<>("IP адрес");
        userIP.setCellValueFactory(data -> data.getValue().ipProperty());
        userIP.setMinWidth(90);
        userIP.setMaxWidth(90);
        userIP.setStyle("-fx-alignment: CENTER");


        TableColumn<User, String> userPort = new TableColumn<>("Порт сервера");
        userPort.setCellValueFactory(data -> data.getValue().portProperty().asString());
        userPort.setMinWidth(100);
        userPort.setMaxWidth(100);
        userPort.setStyle("-fx-alignment: CENTER");

        TableColumn<User, Void> userDelete = new TableColumn<>("Удалить");
        userDelete.setCellFactory(new javafx.util.Callback<>() {
            @Override
            public TableCell<User, Void> call(TableColumn<User, Void> param) {
                return new TableCell<>(){
                    private Pane graphic;
                    {
                        Button btn = new Button("Удалить");
                        btn.setPrefWidth(90);
                        btn.setPrefHeight(30);
                        btn.setOnAction(e -> {
                            int id = getIndex();

                            table.getItems().remove(id);
                            BattleshipGame.getUsers().remove(id);
                            BattleshipGame.updateINIFile(null, null, null);
                        });

                        graphic = new Pane();
                        graphic.getChildren().add(btn);
                    }
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(graphic);
                        }
                    }
                };
            }

        });


        table.getColumns().addAll(userName, userIP, userPort, userDelete);
        table.setEditable(false);

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null)
                return;
            String[] ipSplit = newValue.getIp().split("\\.");

            nameServer.setText(newValue.getName());
            for(int i = 0; i < 4; i++)
                ip[i].setText(ipSplit[i]);
            port.setText(String.valueOf(newValue.getPort()));
        });

        mainGrid.add(table,0,0);
    }

    static void initialnameGrid(){
        nameServer = new TextField();
        nameServer.setPrefWidth(10000);
        nameServer.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() > 24)
                ((StringProperty)observable).setValue(newValue.substring(newValue.length() - 24));
        });

        nameGrid.add(nameServer, 1,0);
    }

    static void initialPortIPGrid() {
        for(int i = 0; i < 4; i++){
            ip[i] = new TextField();
            ip[i].setPrefWidth(10000);
            ip[i].textProperty().addListener((observable, oldValue, newValue) -> {
                try{
                    if(newValue.equals("")){
                        ((StringProperty)observable).setValue("0");
                        return;
                    }
                    if(oldValue.equals("0")) {
                        ((StringProperty) observable).setValue(newValue.substring(1));
                        return;
                    }
                    int partIP = Integer.parseInt(newValue);
                    if(partIP > 255)
                        ((StringProperty)observable).setValue(oldValue);
                    else
                        ((StringProperty)observable).setValue(newValue);
                } catch (Exception e){
                    ((StringProperty)observable).setValue(oldValue);
                }
            });

            portIPGrid.add(ip[i], i+1, 0);
        }

        port = new TextField();
        port.setPrefWidth(10000);
        port.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(newValue.equals("")){
                    ((StringProperty)observable).setValue("0");
                    return;
                }
                if(oldValue.equals("0")) {
                    ((StringProperty) observable).setValue(newValue.substring(1));
                    return;
                }
                int partIP = Integer.parseInt(newValue);
                if(partIP > 65535)
                    ((StringProperty)observable).setValue(oldValue);
                else
                    ((StringProperty)observable).setValue(newValue);
            } catch (Exception e){
                ((StringProperty)observable).setValue(oldValue);
            }
        });

        portIPGrid.add(port,7,0);
    }

    static void initialButtonsGrid(){
        back = new Button("Назад");
        back.setPrefWidth(10000);
        back.setFont(new Font(15));
        back.setOnAction(e -> BattleshipGame.launchSimpleConnectDialog());

        change = new Button("Изменить");
        change.setPrefWidth(10000);
        change.setFont(new Font(15));
        change.setOnAction(e -> {
            User changed = table.getSelectionModel().getSelectedItem();

            changed.setName(nameServer.getText());
            changed.setIp(ip[0].getText() + "." + ip[1].getText() + "." + ip[2].getText() + "." + ip[3].getText());
            changed.setPort(Integer.parseInt(port.getText()));

            BattleshipGame.updateINIFile(null, null, null);
        });

        add = new Button("Добавить");
        add.setPrefWidth(10000);
        add.setFont(new Font(15));
        add.setOnAction(e -> {
            User next = new User();
            next.setName(nameServer.getText().equals("")? "Новое подключение" : nameServer.getText());
            next.setIp(ip[0].getText() + "." + ip[1].getText() + "." + ip[2].getText() + "." + ip[3].getText());
            next.setPort(Integer.parseInt(port.getText()));
            for(User u : table.getItems())
                if(u.equals(next))
                    return;
            table.getItems().add(next);
            BattleshipGame.getUsers().add(next);
            BattleshipGame.updateINIFile(null, next, null);
        });

        buttonsGrid.add(back,0,0);
        buttonsGrid.add(change, 2,0);
        buttonsGrid.add(add,3,0);
    }

}
