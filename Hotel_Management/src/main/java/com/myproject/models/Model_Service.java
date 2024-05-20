package com.myproject.models;

public class Model_Service {
    private String id;
    private String name;
    private double price;
    private String unit;
    private String status;

    // Constructors

    public Model_Service(String id, String name, double price, String unit, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.status = status;
    }

    // Getter and Setter methods

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    // toString method

    @Override
    public String toString() {
        return "Service ID: " + id +
                "\nName: " + name +
                "\nPrice: " + price +
                "\nUnit: " + unit +
                "\nStatus: " + status;
    }
}