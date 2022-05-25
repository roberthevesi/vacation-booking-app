package com.example.vacationbookingapp.controllers;

import com.example.vacationbookingapp.DBUtils;
import com.example.vacationbookingapp.OfferSession;
import com.example.vacationbookingapp.UserSession;
import com.example.vacationbookingapp.models.OfferModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {
    @FXML
    private Button logoutButton;

    @FXML
    private Button myBookingsButton;

    @FXML
    private Button requestsButton;

    @FXML
    private Button addOfferButton;

    @FXML
    private Button removeOfferButton;

    @FXML
    private Button viewOfferButton;

    @FXML
    private TableView<OfferModel> table;

    @FXML
    private TableColumn<OfferModel, String> titleCol;

    @FXML
    private TableColumn<OfferModel, String> priceCol;

    @FXML
    private TableColumn<OfferModel, String> locationCol;

    @FXML
    private TableColumn<OfferModel, String> startDateCol;

    @FXML
    private TableColumn<OfferModel, String> endDateCol;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    ObservableList<OfferModel> OfferList = FXCollections.observableArrayList();

    UserSession userSession = UserSession.getInstance();
    OfferSession offerSession = OfferSession.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(userSession.role.equals("user")){
            addOfferButton.setVisible(false);
            removeOfferButton.setVisible(false);
            requestsButton.setVisible(false);
        }else if(userSession.role.equals("agent")){
            myBookingsButton.setVisible(false);
        }

        loadData();

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "login.fxml", "Login");
            }
        });

        myBookingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "myBookings.fxml", "My Bookings");
            }
        });

        requestsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "requests.fxml", "Requests");
            }
        });

        addOfferButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "addOffer.fxml", "Add Offer");
            }
        });

        removeOfferButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                System.out.println(table.getSelectionModel().getSelectedItem());
                if(table.getSelectionModel().getSelectedItem() == null){
                    System.out.println("No entry selected.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("No offer to delete has been selected.");
                    alert.show();
                }
                else{
                    try {
                        System.out.println("Delete offer confirmation.");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Are you sure you want to delete this offer?");
                        alert.showAndWait();

                        if(alert.getResult() == ButtonType.OK){
                            DBUtils.removeOffer(actionEvent, table.getSelectionModel().getSelectedItem().getTitle());
                            refreshTable();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        viewOfferButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(table.getSelectionModel().getSelectedItem() == null){
                    System.out.println("No entry selected.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("No offer to view has been selected.");
                    alert.show();
                }
                else{
                    try{
                        String query = null;
                        Connection connection = null;
                        ResultSet resultSet = null;
                        PreparedStatement preparedStatement = null;

                        final String DOUBLE_QUOTES = "\"";
                        query = "SELECT offerid FROM offers WHERE title = " + DOUBLE_QUOTES + table.getSelectionModel().getSelectedItem().getTitle() + DOUBLE_QUOTES;
                        connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
                        preparedStatement = connection.prepareStatement(query);
                        resultSet = preparedStatement.executeQuery();

                        String str = null;
                        while(resultSet.next())
                            str = resultSet.getString("offerid");
                        offerSession.offerid = Integer.parseInt(str);
                        offerSession.title = table.getSelectionModel().getSelectedItem().getTitle();

                        DBUtils.changeScene(actionEvent, "viewOffer.fxml", "View Offer");
                    }
                    catch(SQLException e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void loadData(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
            refreshTable();

            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    public void refreshTable(){
        try {
            OfferList.clear();
            query = "SELECT * from offers";

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OfferList.add(new OfferModel(
                    resultSet.getInt("offerid"),
                    resultSet.getString("title"),
                    resultSet.getString("price"),
                    resultSet.getString("location"),
                    resultSet.getString("startDate"),
                    resultSet.getString("endDate"),
                    resultSet.getString("description"),
                    null,
                        null));
                table.setItems(OfferList);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
