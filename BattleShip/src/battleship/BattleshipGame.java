package battleship;

import battleship.connection.Launcher;
import battleship.connection.Port;
import battleship.connection.User;
import battleship.controllers.*;
import battleship.controllers.dialogs.ClientDialogSettings;
import battleship.controllers.dialogs.ClientDialogSimple;
import battleship.controllers.dialogs.ServerDialogSimple;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


/**
 * This is the "main" class, containing the main method and a variable of type oceans.
 */
public class BattleshipGame extends Application {
    final static Random random = new Random();

    final static String battleSceneURL = "/battleship/resources/scenes/battleScene.fxml";
    final static String mainSceneURL = "/battleship/resources/scenes/mainMenuScene.fxml";
    final static String trainSceneURL = "/battleship/resources/scenes/trainScene.fxml";
    final static String planSceneURL = "/battleship/resources/scenes/planOfShips.fxml";
    final static String connectSimpleDialogURL = "/battleship/resources/dialogs/client_dialog_simple.fxml";
    final static String connectSettingsDialogURL = "/battleship/resources/dialogs/client_dialog_settings.fxml";
    final static String hostSimpleDialogURL = "/battleship/resources/dialogs/server_dialog_simple.fxml";

    final static String connectionStreamInput = "info.ini";

    static ArrayList<User> users;
    static ArrayList<Port> ports;
    static String name;
    static int timeOut;
    static int timeWait;

    static Stage application;
    static Stage connectSimpleDialog;
    static Stage hostSimpleDialog;

    static Parent mainMenuRoot;
    static Parent trainingRoot;
    static Parent planningRoot;
    static Parent battleRoot;

    static Parent connectSimpleRoot;
    static Parent connectSettingRoot;
    static Parent hostSimpleRoot;

    static Ocean myOcean;
    static Ocean opponentOcean;

    static String format;

    public static void main(String[] args) {
        if(args.length > 0){
            format = args[0];
            if(!format.equals("server") && !format.equals("client")){
                System.out.println("Неверный входные пераметры.\n" +
                        "Его либо не должно быть, либо \"sever\", либо \"client\"\n" +
                        "Остановка приложения.");
                return;
            }

        }
        else
            format = "multiple";
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        readINIFile();

        application = new Stage();
        application.setMinWidth(900);
        application.setMinHeight(600);
        application.setMaxWidth(1920);
        application.setMaxHeight(1080);

        mainMenuRoot = FXMLLoader.load(getClass().getResource(mainSceneURL));
        trainingRoot = FXMLLoader.load(getClass().getResource(trainSceneURL));
        planningRoot = FXMLLoader.load(getClass().getResource(planSceneURL));
        battleRoot = FXMLLoader.load(getClass().getResource(battleSceneURL));


        connectSimpleDialog = new Stage();
        connectSimpleDialog.setResizable(false);
//        connectSimpleDialog.setMinWidth(400);
//        connectSimpleDialog.setMinHeight(200);
//        connectSimpleDialog.setMaxWidth(400);
//        connectSimpleDialog.setMaxHeight(200);

        connectSimpleRoot = FXMLLoader.load(getClass().getResource(connectSimpleDialogURL));
        connectSettingRoot = FXMLLoader.load(getClass().getResource(connectSettingsDialogURL));

        connectSimpleDialog.setTitle("Подключение");
        connectSimpleDialog.initModality(Modality.WINDOW_MODAL);
        connectSimpleDialog.initOwner(application);
        ClientDialogSimple.init();
        connectSimpleDialog.setScene(new Scene(connectSimpleRoot));

        hostSimpleDialog = new Stage();
        hostSimpleDialog.setResizable(false);

        hostSimpleRoot = FXMLLoader.load(getClass().getResource(hostSimpleDialogURL));

        hostSimpleDialog.setTitle("Создание сервера");
        hostSimpleDialog.initModality(Modality.WINDOW_MODAL);
        hostSimpleDialog.initOwner(application);
        ServerDialogSimple.init();
        hostSimpleDialog.setScene(new Scene(hostSimpleRoot));


        application.setTitle("Battle Ship");
        MainMenuScene.init();

        application.setScene(new Scene(mainMenuRoot));
        application.show();
    }

    static public void readINIFile() throws Exception {
        File f = new File(connectionStreamInput);
        ports = new ArrayList<>();
        users = new ArrayList<>();

        if(!f.exists()){
            f.createNewFile();
            User defaultUser = new User();
            timeOut = 120;
            timeWait = 20;
            updateINIFile(null, defaultUser, "Неизвестный игрок");
        }
        Scanner scanner = new Scanner(f, "windows-1251");
        String text;
        if(scanner.hasNext()){
            text = scanner.nextLine();
            if(text.charAt(0) != '#')
                throw new Exception(".ini file is incorrect");
        }

        while((text = scanner.nextLine()).charAt(0) != '#') {
            Port next = new Port();
            next.port = Integer.parseInt(text);
            ports.add(next);
        }

        while((text = scanner.nextLine()).charAt(0) != '#') {
            String[] info = text.split("#");
            User next = new User();
            next.setName(info[0]);
            next.setIp(info[1]);
            next.setPort(Integer.parseInt(info[2]));
            users.add(next);
        }
        timeOut = Integer.parseInt(scanner.nextLine());
        if(timeOut <= 0)
            timeOut = 120;

        text = scanner.nextLine();
        if(text.charAt(0) != '#')
            throw new Exception(".ini file is incorrect");
        timeWait = Integer.parseInt(scanner.nextLine());
        if(timeWait <= 0)
            timeWait = 20;

        text = scanner.nextLine();
        if(text.charAt(0) != '#')
            throw new Exception(".ini file is incorrect");
        name = scanner.nextLine();
        scanner.close();
    }

    static public void updateINIFile(Port chosenPort, User chosenUser, String chosenName) {
        PrintWriter out = null;
        try{
            File f = new File(connectionStreamInput);
            out = new PrintWriter(f,"windows-1251");
            out.println("#port");

            if(chosenPort != null)
                out.println(chosenPort.port);
            for(Port p : ports)
                if(chosenPort == null || p.port != chosenPort.port)
                    out.println(p.port);
            out.println("#user");
            if(chosenUser != null)
                out.println(chosenUser.getName()+"#"+chosenUser.getIp() +"#"+chosenUser.getPort());
            for(User u : users)
                if(!u.equals(chosenUser))
                    out.println(u.getName()+"#"+u.getIp()+"#"+u.getPort());
            out.println("#timeout");
            out.println(timeOut);
            out.println("#timewait");
            out.println(timeWait);
            out.println("#name");
            if(chosenName != null)
                out.println(chosenName);
            else
                out.println(name);

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if(out != null)
                out.close();
        }
    }

    public static boolean canBeServer() {
        return !format.equals("client");
    }

    public static boolean canBeClient() {
        return !format.equals("server");
    }

    public static ArrayList<User> getUsers(){
        return users;
    }

    public static ArrayList<Port> getPorts() {
        return ports;
    }

    public static String getName(){
        return name;
    }

    public static int getTimeOut(){
        return timeOut;
    }

    public static int getTimeWait() {
        return timeWait;
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
    public static Parent getTrainingRoot() {
        return trainingRoot;
    }

    /**
     * @return root of planning ship position scene
     */
    public static Parent getPlanningRoot() {
        return planningRoot;
    }


    public static Parent getBattleRoot(){
        return battleRoot;
    }


    public static Parent getConnectSimpleRoot() {
        return connectSimpleRoot;
    }

    public static Parent getConnectSettingRoot(){
        return connectSettingRoot;
    }

    public static Parent getHostSimpleRoot() {
        return hostSimpleRoot;
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
    public static void startTrainGame() {
        opponentOcean = new Ocean();
        opponentOcean.placeAllShipsRandomly();
        TrainingScene.init();
        application.getScene().setRoot(trainingRoot);

    }

    public static void startSoloGame() {
        opponentOcean = new Ocean();
        opponentOcean.placeAllShipsRandomly();
        BattleScene.init(false, random.nextBoolean());
        application.getScene().setRoot(battleRoot);
    }

    public static void startMultipleGame() {
        Launcher.send("G",'S');
        BattleScene.init(true, !Launcher.isServer());
        application.getScene().setRoot(battleRoot);
    }

    /**
     * Setup planning ship position scene
     */
    public static void beginPlanBattlePlace(String param) {
        if(param.length() < 2 || param.charAt(1) != 'P')
            myOcean = new Ocean();
        PlanOfShips.init(param);
        application.getScene().setRoot(planningRoot);
    }

    public static void launchSimpleConnectDialog(){
        ClientDialogSimple.init();
        connectSimpleDialog.show();
        if(!connectSimpleDialog.getScene().equals(connectSimpleRoot))
            connectSimpleDialog.getScene().setRoot(connectSimpleRoot);
        connectSimpleDialog.sizeToScene();
    }

    public static void launchSettingsConnectDialog(User user){
        ClientDialogSettings.init(user);
        connectSimpleDialog.getScene().setRoot(connectSettingRoot);
        connectSimpleDialog.sizeToScene();
    }

    public static void closeSimpleConnectDialog(){
        connectSimpleDialog.close();
    }

    public static void launchSimpleHostDialog(){
        ServerDialogSimple.init();
        hostSimpleDialog.show();
    }

    public static void closeSimpleHostDialog(){
        hostSimpleDialog.close();
    }

    /**
     * @return game statistic
     */
    public static String getTrainStatistic(){
        return "Статистика:" +
                "\nСделано ходов: " + opponentOcean.getShotsFired()+
                "\nПопаданий: " + opponentOcean.getHitCount() +
                "\nКоличество целых кораблей: " + opponentOcean.getUndamagedShips() +
                "\nКоличество поврежденных кораблей: " + opponentOcean.getFireShips() +
                "\nКоличество уничтоженных кораблей: " + opponentOcean.getShipsSunk();
    }

    public static String convertStatistic(){
        return myOcean.getShotsFired() + " " + myOcean.getHitCount() + " " + myOcean.getUndamagedShips() + " " + myOcean.getFireShips() + " "
                + myOcean.getShipsSunk();
    }

    public static int[] statisticNumbers(){
        return new int[]{myOcean.getShotsFired(), myOcean.getHitCount(),  myOcean.getUndamagedShips(), myOcean.getFireShips(), myOcean.getShipsSunk()};
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
