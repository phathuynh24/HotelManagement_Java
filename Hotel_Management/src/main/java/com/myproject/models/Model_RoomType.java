package com.myproject.models;

import com.myproject.models.types.RoomType;
import java.text.DecimalFormat;

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
    
    public String getPricePerNightFormated() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(pricePerNight);
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
}