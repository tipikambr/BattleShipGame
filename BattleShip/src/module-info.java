module BattleShip {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.media;

    opens battleship.controllers to javafx.fxml, javafx.graphics, javafx.controls, javafx.media;
    opens battleship;

}