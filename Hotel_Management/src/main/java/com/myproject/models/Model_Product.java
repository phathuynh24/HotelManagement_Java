package com.myproject.models;

public class Model_Product {
    private String id;
    private String name;
    private double purchasePrice;
    private double sellingPrice;
    private String unit;
    private int currentQuantity;
    private String status;

    // Constructors

    public Model_Product(String id, String name, double purchasePrice, double sellingPrice, String unit, int currentQuantity) {
        this.id = id;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.unit = unit;
        this.currentQuantity = currentQuantity;
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

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
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
        return "Product ID: " + id +
                "\nName: " + name +
                "\nPurchase Price: " + purchasePrice +
                "\nSelling Price: " + sellingPrice +
                "\nUnit: " + unit +
                "\nCurrent Quantity: " + currentQuantity + 
                "\nStatus: " + status;
    }
}