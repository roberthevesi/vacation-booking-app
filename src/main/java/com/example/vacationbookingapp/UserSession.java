package com.example.vacationbookingapp;

public class UserSession {
    public String role;
    public Integer userid;

    private static final UserSession session = new UserSession();

    private UserSession(){
    }

    public static UserSession getInstance(){
        return session;
    }
}
