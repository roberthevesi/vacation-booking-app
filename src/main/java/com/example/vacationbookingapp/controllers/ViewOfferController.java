package com.example.vacationbookingapp.controllers;

import com.example.vacationbookingapp.DBUtils;
import com.example.vacationbookingapp.OfferSession;
import com.example.vacationbookingapp.UserSession;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ViewOfferController implements Initializable {
    @FXML
    private Button backButton;

    @FXML
    private Button bookOfferButton;

    @FXML
    private Button logoutButton;

    @FXML
    private javafx.scene.text.Text titleLabel;

    @FXML
    private javafx.scene.text.Text priceLabel;

    @FXML
    private javafx.scene.text.Text locationLabel;

    @FXML
    private javafx.scene.text.Text startDateLabel;

    @FXML
    private javafx.scene.text.Text endDateLabel;

    @FXML
    private javafx.scene.text.Text descriptionLabel;

    OfferSession offerSession = OfferSession.getInstance();
    UserSession userSession = UserSession.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String query = null;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        if(userSession.role.equals("agent")){
            bookOfferButton.setVisible(false);
        }

        try{
            query = "SELECT * FROM offers WHERE offerid = " + offerSession.offerid;
            System.out.println(query);
            connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){

                titleLabel.setText(resultSet.getString("title"));
                priceLabel.setText(resultSet.getString("price"));
                locationLabel.setText(resultSet.getString("location"));
                startDateLabel.setText(resultSet.getString("startDate"));
                endDateLabel.setText(resultSet.getString("endDate"));
                descriptionLabel.setText(resultSet.getString("description"));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "homepage.fxml", "Homepage");
            }
        });

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "login.fxml", "Log In");
            }
        });

        bookOfferButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Book offer confirmation.");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to book \"" + offerSession.title + "\"?");
                alert.showAndWait();

                System.out.println("offer: " + offerSession.offerid + " user: " + userSession.userid);

                if(alert.getResult() == ButtonType.OK){
                    DBUtils.addBooking(actionEvent, offerSession.offerid, userSession.userid, "pending");
                    DBUtils.changeScene(actionEvent, "homepage.fxml", "Homepage");
                }
            }
        });
    }
}
