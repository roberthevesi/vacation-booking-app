package com.example.vacationbookingapp.controllers;
import com.example.vacationbookingapp.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private TextField emailAddress;

    @FXML
    private PasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.loginUser(actionEvent, emailAddress.getText(), password.getText());
            }
        });

        signupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "/com/example/login/signup.fxml", "Sign up");
            }
        });

        while(true) {
            try {
                URL testURL = new URL("http://www.google.com");
                URLConnection connection = testURL.openConnection();
                connection.connect();
                break;
            } catch (MalformedURLException e) {
                System.out.println("No internet connection.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No internet connection has been found. Please connect to the internet and try again.");
                alert.showAndWait();
            } catch (IOException e) {
                System.out.println("No internet connection.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No internet connection has been found. Please connect to the internet and try again.");
                alert.showAndWait();
            }
        }
    }
}
