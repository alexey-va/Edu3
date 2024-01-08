package org.example.other.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class Controller implements Initializable {

    @FXML
    private PasswordField password;
    @FXML
    private TextField login;
    @Setter
    private Stage stage;

    private double x, y;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            // Apply a different style if the password field is not empty
            if (!newValue.isEmpty() && !password.getStyleClass().contains("not-empty"))
                password.getStyleClass().add("not-empty");
            else if (newValue.isEmpty()) password.getStyleClass().remove("not-empty");

        });

        login.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                password.requestFocus();
                event.consume();
            }
        });

        password.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login.requestFocus();
                event.consume();
            }
        });
    }



    @SneakyThrows
    public void onLoginSubmit(ActionEvent actionEvent) {
        String login = this.login.getText();
        String password = this.password.getText();
        //if(login.equals("user") && password.equals("user")){
        URL url = getClass().getClassLoader().getResource("Tetris.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        Tetris controller = loader.getController();


        Scene scene = new Scene(root);
        controller.setScene(scene);
        controller.setupKeyHandlers();

/*        Rectangle rectangle = new Rectangle(600.0, 750.0);
        rectangle.setArcHeight(40);
        rectangle.setArcWidth(40);
        root.setClip(rectangle);
        scene.setFill(Color.TRANSPARENT);*/

        stage.setScene(scene);
        //}
    }


    public void close(ActionEvent actionEvent) {
        log.info("Closing the application");
        Dialog<ButtonType> dialog = new Dialog<>();
        ButtonType buttonTypeClose = new ButtonType("Закрыть");
        ButtonType buttonTypeCancel = new ButtonType("Отмена");
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeClose, buttonTypeCancel);
        StackPane content = new StackPane(new Label("Закрыть приложение?"));
        dialog.getDialogPane().setContent(content);
        dialog.initStyle(StageStyle.TRANSPARENT);

        dialog.getDialogPane().getScene().setFill(Color.TRANSPARENT);
        dialog.getDialogPane().setStyle(
                "    -fx-background-radius: 15px;\n" +
                        "    -fx-border-radius: 15px;\n" +
                        "    -fx-border-width: 1px;\n" +
                        "    -fx-border-color: rgba(0,0,0,0.15);\n" +
                        "-fx-padding: 8px");

        dialog.showAndWait().ifPresent(response -> {
            if (response == buttonTypeClose) {
                stage.close();
            }
        });
    }

    public void minimize(ActionEvent actionEvent) {
        log.info("Minimizing the application");
        stage.setIconified(true);
    }
}
