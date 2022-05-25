package com.example.vacationbookingapp.controllers;

import com.example.vacationbookingapp.DBUtils;
import com.example.vacationbookingapp.OfferSession;
import com.example.vacationbookingapp.UserSession;
import com.example.vacationbookingapp.models.OfferModel;
import com.example.vacationbookingapp.models.RequestModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class RequestsController implements Initializable {
    @FXML
    private Button logoutButton;

    @FXML
    private Button backButton;

    @FXML
    private Button acceptRequestButton;

    @FXML
    private Button rejectRequestButton;

    @FXML
    private TableView<RequestModel> table;

    @FXML
    private TableColumn<OfferModel, String> offeridCol;

    @FXML
    private TableColumn<OfferModel, String> useridCol;

    @FXML
    private TableColumn<OfferModel, String> titleCol;

    @FXML
    private TableColumn<OfferModel, String> priceCol;

    @FXML
    private TableColumn<OfferModel, String> startDateCol;

    @FXML
    private TableColumn<OfferModel, String> endDateCol;

    @FXML
    private TableColumn<OfferModel, String> firstNameCol;

    @FXML
    private TableColumn<OfferModel, String> lastNameCol;

    @FXML
    private TableColumn<OfferModel, String> statusCol;

    @FXML
    private Pane ratingPane;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    ObservableList<RequestModel> RequestsList = FXCollections.observableArrayList();

    UserSession userSession = UserSession.getInstance();
    OfferSession offerSession = OfferSession.getInstance();

    private int currOfferID = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        ratingPane.setVisible(false);


        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "login.fxml", "Login");
            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "homepage.fxml", "Homepage");
            }
        });

        acceptRequestButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(table.getSelectionModel().getSelectedItem() == null){
                    System.out.println("No entry selected.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("No request to accept has been selected.");
                    alert.show();
                }
                else{
                    try {
                        System.out.println("Accept boooking confirmation.");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Are you sure you want to accept this booking?");
                        alert.showAndWait();

                        if(alert.getResult() == ButtonType.OK){
                            DBUtils.changeRequestStatus(actionEvent, table.getSelectionModel().getSelectedItem().getBookingid(), "accepted");
                            refreshTable();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        rejectRequestButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(table.getSelectionModel().getSelectedItem() == null){
                    System.out.println("No entry selected.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("No request to reject has been selected.");
                    alert.show();
                }
                else{
                    try {
                        System.out.println("Reject boooking confirmation.");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Are you sure you want to reject this booking?");
                        alert.showAndWait();

                        if(alert.getResult() == ButtonType.OK){
                            DBUtils.changeRequestStatus(actionEvent, table.getSelectionModel().getSelectedItem().getBookingid(), "rejected");
                            refreshTable();
                        }
                    } catch (SQLException e) {
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

//            offeridCol.setCellValueFactory(new PropertyValueFactory<>("offerid"));
//            useridCol.setCellValueFactory(new PropertyValueFactory<>("userid"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void refreshTable() throws SQLException{
        try {
            RequestsList.clear();

//            ObservableList<RequestModel> auxRequestsList = FXCollections.observableArrayList();

            query = "SELECT * FROM bookings";
            connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                RequestsList.add(new RequestModel(
                        resultSet.getInt("bookingid"),
                        resultSet.getInt("offerid"),
                        resultSet.getInt("userid"),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        resultSet.getString("status")
                ));
            }

            if(RequestsList.size()>0){
                ArrayList<Integer> newOfferidList = new ArrayList<Integer>();
                ArrayList<String> titleList = new ArrayList<String>();
                ArrayList<String> startDateList = new ArrayList<String>();
                ArrayList<String> endDateList = new ArrayList<String>();
                ArrayList<String> priceList = new ArrayList<String>();

                query = "SELECT * from offers";
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                    newOfferidList.add(resultSet.getInt("offerid"));
                    titleList.add(resultSet.getString("title"));
                    startDateList.add(resultSet.getString("startDate"));
                    endDateList.add(resultSet.getString("endDate"));
                    priceList.add(resultSet.getString("price"));
                }

                for(int i=0; i<RequestsList.size(); i++){
                    int currOfferid = RequestsList.get(i).getOfferid();
                    for(int j=0; j<newOfferidList.size(); j++){
                        if(newOfferidList.get(j) == currOfferid){
                            RequestsList.set(i, new RequestModel(
                                    RequestsList.get(i).getBookingid(),
                                    RequestsList.get(i).getOfferid(),
                                    RequestsList.get(i).getUserid(),
                                    titleList.get(j),
                                    startDateList.get(j),
                                    endDateList.get(j),
                                    priceList.get(j),
                                    null,
                                    null,
                                    RequestsList.get(i).getStatus()
                            ));
                        }
                    }
                }

                query = "SELECT * FROM users";
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();

                ArrayList<Integer> newUseridList = new ArrayList<Integer>();
                ArrayList<String> firstNameList = new ArrayList<String>();
                ArrayList<String> lastNameList = new ArrayList<String>();

                while(resultSet.next()){
                    newUseridList.add(resultSet.getInt("userid"));
                    firstNameList.add(resultSet.getString("firstName"));
                    lastNameList.add(resultSet.getString("lastName"));
                }

                for(int i=0; i<RequestsList.size(); i++){
                    int currUserid = RequestsList.get(i).getUserid();
                    for(int j=0; j< newUseridList.size(); j++){
                        if(newUseridList.get(j) == currUserid){
                            RequestsList.set(i, new RequestModel(
                                    RequestsList.get(i).getBookingid(),
                                    RequestsList.get(i).getOfferid(),
                                    RequestsList.get(i).getUserid(),
                                    RequestsList.get(i).getTitle(),
                                    RequestsList.get(i).getStartDate(),
                                    RequestsList.get(i).getEndDate(),
                                    RequestsList.get(i).getPrice(),
                                    firstNameList.get(j),
                                    lastNameList.get(j),
                                    RequestsList.get(i).getStatus()
                            ));
                        }
                    }
                }

                table.setItems(RequestsList);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
