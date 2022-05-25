package com.example.vacationbookingapp.models;

public class AuxOfferModel {
    Integer id, rating;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AuxOfferModel(Integer id, Integer rating, String status) {
        this.id = id;
        this.rating = rating;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }


}
