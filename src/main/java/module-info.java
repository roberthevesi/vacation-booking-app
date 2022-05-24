module com.example.vacationbookingapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.vacationbookingapp to javafx.fxml;
    exports com.example.vacationbookingapp;
}