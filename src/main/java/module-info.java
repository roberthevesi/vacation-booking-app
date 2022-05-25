module com.example.vacationbookingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.vacationbookingapp to javafx.fxml;
    exports com.example.vacationbookingapp;
    exports com.example.vacationbookingapp.controllers;
    exports com.example.vacationbookingapp.models;
    opens com.example.vacationbookingapp.controllers to javafx.fxml;
}