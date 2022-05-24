package com.example.vacationbookingapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.regex.Pattern;

public class DBUtils {
    public static Stage stage;
    private static UserSession userSession = UserSession.getInstance();

    public static void changeScene(ActionEvent event, String fxmlFile, String title){
        Parent root = null;

        try{
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }

        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 1280, 720));
    }
    public static void loginUser(ActionEvent event, String emailAddress, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
            preparedStatement = connection.prepareStatement("SELECT password, role, userid FROM users WHERE emailAddress = ?");
            preparedStatement.setString(1, emailAddress);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("User not found.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect.");
                alert.show();
            }else{
                while(resultSet.next()){
                    String retrievedPassword = resultSet.getString("password");
                    if(retrievedPassword.equals(password)){
                        String retrievedRole = resultSet.getString("role");
                        Integer retrievedID = resultSet.getInt("userid");

                        userSession.role = retrievedRole;
                        userSession.userid = retrievedID;

                        if(retrievedRole.equals("admin")){
                            changeScene(event, "/com/example/login/adminHomepage.fxml", "Welcome Admin");
                        }
                        else if(retrievedRole.equals("agent")){
                            changeScene(event, "/com/example/login/homepage.fxml", "Welcome Agent");
                        }
                        else if(retrievedRole.equals("user")){
                            changeScene(event, "/com/example/login/homepage.fxml", "Welcome");
                        }
                    }else{
                        System.out.println("Passwords did not match.");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect.");
                        alert.show();
                    }
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            if(resultSet != null){
                try{
                    resultSet.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
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
    }

}
