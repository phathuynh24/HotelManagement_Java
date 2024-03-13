package com.myproject.models;

import com.myproject.models.types.RoomType;

public class Model_RoomType {
    private RoomType typeName;
    private double pricePerNight;
    private int maxOccupancy;

    public Model_RoomType(RoomType typeName, double pricePerNight, int maxOccupancy) {
        this.typeName = typeName;
        this.pricePerNight = pricePerNight;
        this.maxOccupancy = maxOccupancy;
    }

    public RoomType getTypeName() {
        return typeName;
    }

    public void setTypeName(RoomType typeName) {
        this.typeName = typeName;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

//    public void displayInfo() {
//        System.out.println("Room type: " + typeName);
//        System.out.println("Price per night: $" + pricePerNight);
//        System.out.println("Max occupancy: " + maxOccupancy);
//    }
}