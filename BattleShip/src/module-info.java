module BattleShip {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    opens battleship.controllers to javafx.fxml, javafx.graphics, javafx.controls;
    opens battleship;

}