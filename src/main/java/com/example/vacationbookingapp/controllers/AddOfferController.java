package com.example.vacationbookingapp.controllers;

import com.example.vacationbookingapp.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddOfferController implements Initializable {
    @FXML
    private Button addOfferButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField title;

    @FXML
    private TextField price;

    @FXML
    private TextField location;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField description;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "login.fxml", "Log In");
            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "homepage.fxml", "Welcome");
            }
        });

        addOfferButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if( // all fields are filled
                    !title.getText().trim().isEmpty() &&
                    !price.getText().trim().isEmpty() &&
                    !location.getText().trim().isEmpty() &&
                    startDate.getValue() != null &&
                    endDate.getValue() != null &&
                    !description.getText().trim().isEmpty())
                {
                    if(!DBUtils.isValidName(location.getText())){
                        System.out.println("Invalid location.");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid location.");
                        alert.show();
                    }
                    else{
                        DBUtils.addOffer(actionEvent, title.getText(), price.getText(), location.getText(), startDate.getValue().toString(), endDate.getValue().toString(), description.getText());
                        DBUtils.changeScene(actionEvent, "/com/example/login/homepage.fxml", "Homepage");
                    }
                }
                else
                { // not good :(
                    System.out.println("Please fill in all info.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to add offer.");
                    alert.show();
                }
            }
        });
    }
}
