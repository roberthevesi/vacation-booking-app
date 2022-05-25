package com.example.vacationbookingapp.controllers;

import com.example.login.DBUtils;
import com.example.login.models.AgentModel;
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

public class AdminHomepageController implements Initializable {
    @FXML
    private Button logoutButton;

    @FXML
    private Button addAgentButton;

    @FXML
    private Button deleteAgentButton;

    @FXML
    private TableView<AgentModel> table;

    @FXML
    private TableColumn<AgentModel, Integer> useridCol;

    @FXML
    private TableColumn<AgentModel, String> firstNameCol;

    @FXML
    private TableColumn<AgentModel, String> lastNameCol;

    @FXML
    private TableColumn<AgentModel, String> phoneNumberCol;

    @FXML
    private TableColumn<AgentModel, String> emailAddressCol;

    @FXML
    private TableColumn<AgentModel, String> roleCol;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    AgentModel agent = null;

    ObservableList<AgentModel> AgentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();


        addAgentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "addAgent.fxml", "Add Agent");
            }
        });

        deleteAgentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                System.out.println(table.getSelectionModel().getSelectedItem());
                if(table.getSelectionModel().getSelectedItem() == null){
                    System.out.println("No entry selected.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("No user to delete has been selected.");
                    alert.show();
                }
                else if(table.getSelectionModel().getSelectedItem().getUserid() == 1){
                    System.out.println("Can't delete admin.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Cannot delete admin user.");
                    alert.show();
                }
                else{
                    try{
                        System.out.println("Delete user confirmation.");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Are you sure you want to delete this user?");
                        alert.showAndWait();

                        if(alert.getResult() == ButtonType.OK){
                            query = "DELETE FROM users WHERE userid = " + table.getSelectionModel().getSelectedItem().getUserid();
                            preparedStatement = connection.prepareStatement(query);
                            preparedStatement.executeUpdate();

                            refreshTable();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

            }
        });

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "login.fxml", "Log In");
            }
        });
    }

    public void loadData(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
            refreshTable();

            useridCol.setCellValueFactory(new PropertyValueFactory<>("userid"));
            firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            emailAddressCol.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
            roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void refreshTable(){
        try {
            AgentList.clear();
            query = "SELECT * from users";

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                AgentList.add(new AgentModel(
                        resultSet.getInt("userid"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("emailAddress"),
                        resultSet.getString("role")));
                table.setItems(AgentList);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
