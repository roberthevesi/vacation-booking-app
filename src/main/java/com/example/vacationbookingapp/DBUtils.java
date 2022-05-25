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
    private static UserSession userSession = UserSession.getInstance();

    private static OfferSession offerSession = OfferSession.getInstance();

    static final String DOUBLE_QUOTES = "\"";
    public static Stage stage;


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

    public static void signupUser(ActionEvent event, String firstName, String lastName, String phoneNumber, String emailAddress, String password, String role){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
            psCheckUserExists = connection.prepareStatement("SELECT * from users WHERE emailAddress = ?");
            psCheckUserExists.setString(1, emailAddress);
            resultSet = psCheckUserExists.executeQuery();

            // user already taken
            if(resultSet.isBeforeFirst()){
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username.");
                alert.show();
            }else{
                psInsert = connection.prepareStatement("INSERT INTO users (firstName, lastName, phoneNumber, emailAddress, password, role) VALUES (?, ?, ?, ?, ?, ?);");
                psInsert.setString(1, firstName);
                psInsert.setString(2, lastName);
                psInsert.setString(3, phoneNumber);
                psInsert.setString(4, emailAddress);
                psInsert.setString(5, password);
                psInsert.setString(6, role);
                psInsert.executeUpdate();


            }
        }
        catch(SQLException e){
            e.printStackTrace();
        } finally{
            if(resultSet != null){
                try{
                    resultSet.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(psCheckUserExists != null){
                try{
                    psCheckUserExists.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(psInsert != null){
                try{
                    psInsert.close();
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

    public static void addOffer(ActionEvent event, String title, String price, String location, String startDate, String endDate, String description){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
            psCheckUserExists = connection.prepareStatement("SELECT * from offers WHERE title = ?");
            psCheckUserExists.setString(1, title);
            resultSet = psCheckUserExists.executeQuery();

            // title already taken
            if(resultSet.isBeforeFirst()){
                System.out.println("Offer already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this title.");
                alert.show();
            }else{
                psInsert = connection.prepareStatement("INSERT INTO offers (title, price, location, startDate, endDate, description) VALUES (?, ?, ?, ?, ?, ?);");
                psInsert.setString(1, title);
                psInsert.setString(2, price);
                psInsert.setString(3, location);
                psInsert.setString(4, startDate);
                psInsert.setString(5, endDate);
                psInsert.setString(6, description);
                psInsert.executeUpdate();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        } finally{
            if(resultSet != null){
                try{
                    resultSet.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(psCheckUserExists != null){
                try{
                    psCheckUserExists.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(psInsert != null){
                try{
                    psInsert.close();
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

    public static void removeOffer(ActionEvent event, String title) throws SQLException {
        String query = null;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;


        query = "SELECT offerid FROM offers WHERE title = " + DOUBLE_QUOTES + title + DOUBLE_QUOTES;
        connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        String str = null;
        while(resultSet.next())
            str = resultSet.getString("offerid");
        offerSession.offerid = Integer.parseInt(str);

        query = "DELETE FROM offers WHERE offerid = " + offerSession.offerid;
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();

        query = "DELETE FROM bookings WHERE offerid = " + offerSession.offerid;
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }

    public static void changeRequestStatus(ActionEvent event, Integer bookingid, String status) throws SQLException{
        String query = null;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        query = "UPDATE bookings SET status = " + DOUBLE_QUOTES + status + DOUBLE_QUOTES + " WHERE bookingid = " + bookingid;
        connection = DriverManager.getConnection("jdbc:mysql://co-project-db.mysql.database.azure.com:3306/sefprojectdb", "robert@co-project-db", "SantJmek1337!");
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }

    public static boolean isValidEmail(String emailAddress){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (emailAddress == null)
            return false;
        return pat.matcher(emailAddress).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber){
        return phoneNumber.matches("[0-9]+");
    }

    public static boolean isValidName(String name){
        return name.matches("[a-zA-Z]+");
    }
}
