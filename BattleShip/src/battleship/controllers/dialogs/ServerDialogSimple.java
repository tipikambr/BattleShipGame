package battleship.controllers.dialogs;

import battleship.BattleshipGame;
import battleship.connection.Launcher;
import battleship.connection.Port;
import battleship.connection.Server;
import battleship.controllers.PlanOfShips;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.StringConverter;




public class ServerDialogSimple {
     static Port port;
    @FXML
    static GridPane mainGrid;
    static Label textHost;
    static ComboBox<Port> portComboBox;

    @FXML
    static GridPane grid_name;
    static TextField name;

    @FXML
    static GridPane mainUI;
    static Button hostButton;
    static Button cancelButton;

    public static void init() {
        try {
            BattleshipGame.readINIFile();
        } catch (Exception e) {
            errorReadINIDileMessage();
        }
        Parent root = BattleshipGame.getHostSimpleRoot();
        if(mainGrid == null) {
            mainGrid = (GridPane) root.lookup("#mainGrid");
            mainUI = (GridPane) root.lookup("#mainUI");
            grid_name = (GridPane) root.lookup("#grid_name");
            initialLabel();
            initialUI();
            initialField();
        }
        ObservableList<Port> list = FXCollections.observableArrayList(BattleshipGame.getPorts());
        portComboBox.setItems(list);
        if(list.size() != 0)
            portComboBox.setValue(list.get(0));
    }

    static void initialUI() {
        hostButton = new Button("Раздать");
        hostButton.setPrefWidth(10000);
        hostButton.setPrefHeight(10000);
        hostButton.wrapTextProperty().setValue(true);
        hostButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        cancelButton = new Button("Отменить");
        cancelButton.setPrefWidth(10000);
        cancelButton.setPrefHeight(10000);
        cancelButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        cancelButton.wrapTextProperty().setValue(true);;

        initialUIListners();

        mainUI.add(cancelButton,0,0);
        mainUI.add(hostButton,4,0);
    }

    static void initialUIListners() {
        hostButton.setOnAction(e -> host());

        cancelButton.setOnAction(e -> cancel());
    }

    private static void cancel() {
        BattleshipGame.closeSimpleHostDialog();
    }

    private static void initialField(){
        name = new TextField();
        name.setFont(new Font(15));
        name.setText(BattleshipGame.getName());

        name.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.length() > 24)
                name.setText(newValue.substring(newValue.length() - 24));
        }));

        grid_name.add(name, 1,0);
    }

    private static void host() {
        port = portComboBox.getValue();
        BattleshipGame.updateINIFile(port, null, name.getText());
        Launcher.create(new Server(port, name.getText()), 's');             //TODO choose your name
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
                p.port = 0;
                if(string.equals("")) return p;
                p.port = Integer.parseInt(string);
                return p;
            }
        });

        portComboBox.setCellFactory(new Callback<ListView<Port>, ListCell<Port>>() {
            @Override
            public ListCell<Port> call(ListView<Port> param) {
                return new ListCell<Port>(){
                    private GridPane graphic;
                    {
                        Label label = new Label();
                        label.textProperty().bind(this.itemProperty().asString());
                        label.setMaxWidth(10000);
                        label.setOnMouseClicked(event -> portComboBox.hide());

                        Button closeButton = new Button("X");
                        closeButton.setOnAction(event -> {
                            Port p = getItem();
                            BattleshipGame.getPorts().remove(p);
                            portComboBox.getItems().remove(p);
                            portComboBox.hide();
                        });
                        closeButton.setMinWidth(25);
                        closeButton.setMaxWidth(25);
                        closeButton.setMinHeight(25);
                        closeButton.setMaxHeight(25);

                        graphic = new GridPane();
                        ColumnConstraints column1 = new ColumnConstraints();
                        column1.setPercentWidth(75);
                        ColumnConstraints column2 = new ColumnConstraints();
                        column2.setMaxWidth(20);
                        column2.setMinWidth(20);
                        graphic.getColumnConstraints().addAll(column1, column2);
                        RowConstraints row = new RowConstraints();
                        row.setMinHeight(25);
                        row.setMaxHeight(25);
                        graphic.getRowConstraints().addAll(row);
                        graphic.add(label, 0,0);
                        graphic.add(closeButton,1,0);
//                        graphic.setHgrow(label, Priority.ALWAYS);
                        graphic.setMinHeight(0);
                        graphic.setMaxHeight(20);
                        graphic.setAlignment(Pos.CENTER);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    }
                    @Override
                    protected void updateItem(Port item, boolean empty) {
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

        portComboBox.setSkin(new ComboBoxListViewSkin<Port>(portComboBox){
            {
                setHideOnClick(false);
            }
        });


        if(BattleshipGame.getPorts().size() != 0)
            portComboBox.setValue(BattleshipGame.getPorts().get(0));

        portComboBox.setVisibleRowCount(4);
        portComboBox.setEditable(true);
        portComboBox.getEditor().textProperty().addListener(((observable, oldValue, newValue) -> {
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
        }));
        GridPane.setHalignment(portComboBox,HPos.CENTER);
        mainGrid.add(portComboBox,0,1);


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
