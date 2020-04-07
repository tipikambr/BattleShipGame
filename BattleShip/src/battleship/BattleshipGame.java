package battleship;

import battleship.controllers.TrainingScene;
import battleship.controllers.MainMenuScene;
import battleship.controllers.PlanOfShips;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.text.DecimalFormat;


/**
 * This is the "main" class, containing the main method and a variable of type oceans.
 */
public class BattleshipGame extends Application {
    static Stage application;
    static Parent  mainMenuRoot;
    static Parent battleRoot;
    static Parent planningRoot;
    static Ocean myOcean;
    static Ocean opponentOcean;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        application = new Stage();
        application.setMinWidth(700);
        application.setMinHeight(600);
        application.setMaxWidth(1920);
        application.setMaxHeight(1080);

        mainMenuRoot = FXMLLoader.load(getClass().getResource("resources/scenes/mainMenuScene.fxml"));
        battleRoot = FXMLLoader.load(getClass().getResource("resources/scenes/battleScene.fxml"));
        planningRoot = FXMLLoader.load(getClass().getResource("resources/scenes/planOfShips.fxml"));

        application.setTitle("Battle Ship");
        MainMenuScene.init();

        application.setScene(new Scene(mainMenuRoot));
        application.show();
    }

    /**
     * @return linq of player ocean
     */
    public static Ocean getMyOcean() {
        return myOcean;
    }

    /**
     * @return linq of opponent's ocean
     */
    public static Ocean getOpponentOcean() {
        return opponentOcean;
    }

    /**
     * @return root of main menu scene
     */
    public static Parent getMainMenuRoot() {
        return mainMenuRoot;
    }

    /**
     * @return root of training battle scene
     */
    public static Parent getBattleRoot() {
        return battleRoot;
    }

    /**
     * @return root of planning ship position scene
     */
    public static Parent getPlanningRoot() {
        return planningRoot;
    }

    /**
     * setup main menu scene
     */
    public static void backToMenu() {
        MainMenuScene.init();
        application.getScene().setRoot(mainMenuRoot);
    }

    /**
     * Setup training game scene
     */
    public static void startSoloGame() {
        opponentOcean = new Ocean();
        opponentOcean.placeAllShipsRandomly();
        TrainingScene.init();
        application.getScene().setRoot(battleRoot);

    }

    /**
     * Setup planning ship position scene
     */
    public static void beginPlanBattlePlace() {
        myOcean = new Ocean();
        PlanOfShips.init();
        application.getScene().setRoot(planningRoot);
    }

    /**
     * @return game statistic
     */
    public static String getGameStatistic(){
        return "Статистика:" +
                "\nСделано ходов: " + opponentOcean.getShotsFired()+
                "\nПопаданий: " + opponentOcean.getHitCount() +
                "\nКоличество целых кораблей: " + opponentOcean.getUndamagedShips() +
                "\nКоличество поврежденных кораблей: " + opponentOcean.getFireShips() +
                "\nКоличество уничтоженных кораблей: " + opponentOcean.getShipsSunk();
    }

    /**
     * @return information about results of game
     */
    public static String getResultStatistic(){
        DecimalFormat update = new DecimalFormat("###.##");
        return "Статистика:" +
                "\nСделано ходов: " + opponentOcean.getShotsFired()+
                "\nПроцент попаданий: " + update.format( 20.0 / (opponentOcean.getShotsFired() + 20.0));
    }

}
