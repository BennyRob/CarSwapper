package com.example.carswapper;

public class Offer {

    int idFor;
    int idFrom;

    public Offer(int idFor, int idFrom){
        this.idFor = idFor;
        this.idFrom = idFrom;
    }

    public int getIdFor() {
        return idFor;
    }

    public void setIdFor(int idFor) {
        this.idFor = idFor;
    }

    public int getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(int idFrom) {
        this.idFrom = idFrom;
    }
}
