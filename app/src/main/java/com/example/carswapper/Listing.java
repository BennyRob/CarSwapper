package com.example.carswapper;

import android.graphics.Bitmap;

public class Listing {

    private int idNumber;
    private String user;
    private String year;
    private String make;
    private String model;
    private String value;
    private String miles;





    private Bitmap photo;

    public Listing(int idNumber, String user, String year, String make, String model, String value, String miles, Bitmap photo){
        this.idNumber = idNumber;
        this.user = user;
        this.year = year;
        this.make = make;
        this.model = model;
        this.value = value;
        this.miles = miles;
        this.photo = photo;
    }

    public int getIdNumber() { return idNumber; }

    public void setIdNumber(int idNumber) { this.idNumber = idNumber; }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getYear() { return year; }

    public void setYear(String year) { this.year = year; }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) { this.value = value; }

    public String getMiles() { return miles; }

    public void setMiles(String miles) { this.miles = miles; }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

}
