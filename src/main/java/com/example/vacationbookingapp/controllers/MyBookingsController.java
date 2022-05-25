package com.example.vacationbookingapp.controllers;

import com.example.vacationbookingapp.DBUtils;
import com.example.vacationbookingapp.OfferSession;
import com.example.vacationbookingapp.UserSession;
import com.example.vacationbookingapp.models.AuxOfferModel;
import com.example.vacationbookingapp.models.OfferModel;
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
import java.util.ResourceBundle;




public class MyBookingsController implements Initializable {
    @FXML
    private Button logoutButton;

    @FXML
    private Button giveFeedbackButton;

    @FXML
    private Button backButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button submitButton;

    @FXML
    private Slider ratingSlider;

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

    @FXML
    private TableColumn<OfferModel, String> ratingCol;

    @FXML
    private TableColumn<OfferModel, String> statusCol;

    @FXML
    private Pane ratingPane;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    ObservableList<OfferModel> OfferList = FXCollections.observableArrayList();

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

        giveFeedbackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(table.getSelectionModel().getSelectedItem() == null){
                    System.out.println("No entry selected.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("No booking to give feedback to has been selected.");
                    alert.show();
                }
                else{
                    try{
                        connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");

                        final String DOUBLE_QUOTES = "\"";
                        query = "SELECT * FROM offers WHERE title = " + DOUBLE_QUOTES + table.getSelectionModel().getSelectedItem().getTitle() + DOUBLE_QUOTES;
                        System.out.println(query);

                        preparedStatement = connection.prepareStatement(query);
                        resultSet = preparedStatement.executeQuery();


                        if(resultSet.next())
                            currOfferID = resultSet.getInt("offerid");


                        query = "SELECT * FROM bookings WHERE userid = " + userSession.userid + " and offerid = " + currOfferID;
                        System.out.println(query);
                        connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
                        preparedStatement = connection.prepareStatement(query);
                        resultSet = preparedStatement.executeQuery();

                        int currRating = -1;
                        if(resultSet.next())
                            currRating = resultSet.getInt("rating");

                        if(currRating == -1){
                            System.out.println("Already rated.");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("You've already rated this booking.");
                            alert.show();
                        }else{
                            ratingPane.setVisible(true);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ratingPane.setVisible(false);
            }
        });

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("rating: " + (int)ratingSlider.getValue());
                try{
                    connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
                    query = "UPDATE bookings SET rating=" + (int)ratingSlider.getValue() + " WHERE offerid = " + currOfferID + " and userid = " + userSession.userid;
                    System.out.println(query);
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    refreshTable();
                }
                catch(SQLException e){
                    e.printStackTrace();
                } finally{
                    if(preparedStatement != null){
                        try{
                            preparedStatement.close();
                        }
                        catch(SQLException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection != null){
                        try{
                            connection.close();
                        }
                        catch(SQLException e){
                            e.printStackTrace();
                        }
                    }
                }

                ratingPane.setVisible(false);
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
            ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
            statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void refreshTable(){
        try {
            OfferList.clear();

            query = "SELECT * FROM bookings WHERE userid = " + userSession.userid;
            connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            ObservableList<AuxOfferModel> auxOfferList = FXCollections.observableArrayList();

            while(resultSet.next()){
                auxOfferList.add(new AuxOfferModel(
                        resultSet.getInt("offerid"),
                        resultSet.getInt("rating"),
                        resultSet.getString("status")
                        )
                );
            }

            if(auxOfferList.size()>0){
                String offerIDsStr = "(";
                for(int i=0; i<auxOfferList.size(); i++){
                    offerIDsStr += String.valueOf(auxOfferList.get(i).getId());
                    offerIDsStr += ", ";
                }
                offerIDsStr = offerIDsStr.substring(0, offerIDsStr.length()-2);
                offerIDsStr += ")";
                System.out.println(offerIDsStr);

                query = "SELECT * from offers WHERE offerid IN" + offerIDsStr;
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
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
                }

                for(int i=0; i<OfferList.size(); i++){
                    int currId = OfferList.get(i).getId();
                    for(int j=0; j<auxOfferList.size(); j++){
                        if(auxOfferList.get(j).getId() == currId)
                            OfferList.set(i, new OfferModel(
                                    OfferList.get(i).getId(),
                                    OfferList.get(i).getTitle(),
                                    OfferList.get(i).getPrice(),
                                    OfferList.get(i).getLocation(),
                                    OfferList.get(i).getStartDate(),
                                    OfferList.get(i).getEndDate(),
                                    OfferList.get(i).getDescription(),
                                    auxOfferList.get(j).getRating() + " stars",
                                    auxOfferList.get(j).getStatus())
                            );
                    }
                }
                table.setItems(OfferList);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
