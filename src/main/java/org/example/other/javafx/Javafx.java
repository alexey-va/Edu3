package org.example.other.javafx;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.log4j.Log4j2;

import java.net.URL;

@Log4j2
public class Javafx extends Application {

    private double x, y;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage asd) throws Exception {
        URL url = getClass().getClassLoader().getResource("Main.fxml");
        String css = getClass().getClassLoader().getResource("app.css").toExternalForm();

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        Controller controller = loader.getController();



        Scene scene = new Scene(root);
        Stage stage = new Stage();
        controller.setStage(stage);
        stage.initStyle(StageStyle.TRANSPARENT);

        Rectangle rectangle = new Rectangle(815.0, 465.0);
        rectangle.setArcHeight(40);
        rectangle.setArcWidth(40);
        root.setClip(rectangle);
        scene.setFill(Color.TRANSPARENT);

        scene.setOnMousePressed(event -> {
            x = stage.getX() -  event.getScreenX();
            y = stage.getY() -  event.getScreenY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + x);
            stage.setY(event.getScreenY() + y);
        });

        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
}
