package com.example.vacationbookingapp;

public class OfferSession {
    public Integer offerid;
    public String title;

    private static final OfferSession session = new OfferSession();

    private OfferSession(){
    }

    public static OfferSession getInstance(){
        return session;
    }
}
