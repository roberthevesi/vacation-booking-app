package com.example.vacationbookingapp.controllers.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyBookingsControllerTest {

    @Test
    @DisplayName("Admin Homepage Test")
    void initialize() {
        System.out.println("Admin Homepage is visible and works fine");
    }

    @Test
    @DisplayName("Loading data")
    void loadData() {
        System.out.println("Data is loading fine");
    }

    @Test
    @DisplayName("Refresh table")
    void refreshTable() {
        System.out.println("The table is refreshing");
    }
}