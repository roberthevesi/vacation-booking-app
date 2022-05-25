package com.example.vacationbookingapp.controllers;

import com.example.login.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddAgentController implements Initializable {
    @FXML
    private Button addAgentButton;

    @FXML
    private Button backButton;

    @FXML
    private Button logoutButton;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField emailAddress;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addAgentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if( // all fields are filled
                    !firstName.getText().trim().isEmpty() &&
                    !lastName.getText().trim().isEmpty() &&
                    !phoneNumber.getText().trim().isEmpty() &&
                    !emailAddress.getText().trim().isEmpty() &&
                    !password.getText().trim().isEmpty() &&
                    !confirmPassword.getText().trim().isEmpty() &&
                    password.getText().compareTo(confirmPassword.getText()) == 0)
                {
                    if(!DBUtils.isValidName(firstName.getText()) || !DBUtils.isValidName(lastName.getText())){
                        System.out.println("Invalid name.");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid name.");
                        alert.show();
                    }
                    else if(!DBUtils.isValidPhoneNumber(phoneNumber.getText())){
                        System.out.println("Invalid phone number.");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid phone number.");
                        alert.show();
                    }
                    else if(!DBUtils.isValidEmail(emailAddress.getText())){
                        System.out.println("Invalid email address.");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid email address.");
                        alert.show();
                    }
                    else{
                        DBUtils.signupUser(actionEvent, firstName.getText(), lastName.getText(), phoneNumber.getText(), emailAddress.getText(), password.getText(), "agent");
                        DBUtils.changeScene(actionEvent, "/com/example/login/adminHomepage.fxml", "Admin Homepage");
                    }
                }
                else if( // passwords don't match
                    !firstName.getText().trim().isEmpty() &&
                    !lastName.getText().trim().isEmpty() &&
                    !phoneNumber.getText().trim().isEmpty() &&
                    !emailAddress.getText().trim().isEmpty() &&
                    !password.getText().trim().isEmpty() &&
                    !confirmPassword.getText().trim().isEmpty() &&
                    password.getText().compareTo(confirmPassword.getText()) != 0)
                {
                    System.out.println("Passwords don't match.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Passwords don't match.");
                    alert.show();
                }
                else
                { // not good :(
                    System.out.println("Please fill in all info.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up.");
                    alert.show();
                }
            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "adminHomepage.fxml", "Admin Center");
            }
        });

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "login.fxml", "Log In");
            }
        });
    }
}
