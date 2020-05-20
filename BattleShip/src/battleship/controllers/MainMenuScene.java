package battleship.controllers;

import battleship.BattleshipGame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.Optional;

public class MainMenuScene {


    @FXML
    static GridPane localGrid;
    @FXML
    static GridPane multGrid;
    @FXML
    static GridPane infoGrid;
    @FXML
    static GridPane exitGrid;
    static Button localGameButton;
    static Button multiplayerGameButton;
    static Button infoButton;
    static Button exitButton;

    /**
     * This method should be used for initializing the main menu scene
     */
    public static void init() {
        if(localGameButton == null) {
            Parent root = BattleshipGame.getMainMenuRoot();
            localGrid = (GridPane) root.lookup("#localGrid");
            multGrid = (GridPane)root.lookup("#multGrid");
            infoGrid = (GridPane)root.lookup("#infoGrid");
            exitGrid = (GridPane)root.lookup("#exitGrid");

            initialButtons();
        }
    }

    /**
     * Create buttons of menu and
     */
    static void initialButtons() {
        localGameButton = new Button("Одиночная игра");
        localGameButton.setPrefHeight(10000);
        localGameButton.setPrefWidth(10000);

        multiplayerGameButton = new Button("Мультиплеер");
        multiplayerGameButton.setPrefHeight(1000);
        multiplayerGameButton.setPrefWidth(1000);

        infoButton = new Button("Информация");
        infoButton.setPrefHeight(1000);
        infoButton.setPrefWidth(1000);

        exitButton = new Button("Выйти");
        exitButton.setPrefHeight(1000);
        exitButton.setPrefWidth(1000);

        localGameButton.setOnAction(e -> clickStartSoloGame());
        multiplayerGameButton.setOnAction(e -> clickMultiplayerGame());
        infoButton.setOnAction(e ->infoMessage());
        exitButton.setOnAction(e -> clickExitGame());

        localGrid.add(localGameButton, 0,0);
        multGrid.add(multiplayerGameButton, 0,0);
        infoGrid.add(infoButton, 0,0);
        exitGrid.add(exitButton, 0, 0);
    }

    /**
     * Launch training scene
     */
    static void clickStartSoloGame() {
        BattleshipGame.startTrainGame();
        helloMessage();
    }

    /**
     * Launch planning scene
     */
    static void clickMultiplayerGame() {
        BattleshipGame.beginPlanBattlePlace("");
    }

    /**
     * Alert message about author and app
     */
    static void infoMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Информационное сообщение");
        alert.setHeaderText("Приложение написано студентом группы БПИ184\n" +
                "Ухлином Михаилом Алексеевичем");
        alert.setContentText("Надеюсь, я не забыл потом обновить это сообшение, но если забыл, то как-то так :)\n" +
                "Приложение может быть как сервером или клиентом, так и выполнять обе функции. Параметры запуска:\n" +
                "\"server\" - сервер\n" +
                "\"client\" - клиент\n" +
                "Без параметров - и то, и другое сразу.\n" +
                "Чтобы добавить или убрать ненужные ip-адреса или порты, да и в целом посмотреть на сохраняемый файл - посмотрите файл info.ini\n" +
                "//TODO описать там все\n" +
                "У меня не было возможности проверить, возможно ли восстановить связь после потери интернета, если в комментариях напишите, работает она или нет - буду чень благодарен.");
        alert.showAndWait();
    }

    /**
     * Exit the game
     */
    static void clickExitGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Информационное сообщение");
        alert.setHeaderText("Вы действительно хотите закрыть игру?");

        Optional<ButtonType> option = alert.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    /**
     * Alert message in begin of game
     */
    static void helloMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Командир встал у руля!");
        alert.setHeaderText("Тем временем командир вражеского судна:");
        alert.setContentText("Они заряжают пушку. Зачем?\n" +
                "А-а.\n" +
                "Они будут стрелять!\n" +
                "Прибавить ходу!");
        alert.showAndWait();
    }
}
