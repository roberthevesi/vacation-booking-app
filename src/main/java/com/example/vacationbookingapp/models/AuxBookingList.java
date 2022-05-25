package com.example.vacationbookingapp.models;

public class AuxBookingList {
    Integer offerid, userid;

    public Integer getOfferid() {
        return offerid;
    }

    public void setOfferid(Integer offerid) {
        this.offerid = offerid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public AuxBookingList(Integer offerid, Integer userid) {
        this.offerid = offerid;
        this.userid = userid;
    }
}
