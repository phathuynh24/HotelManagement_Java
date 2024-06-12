package com.raven.model;

import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;
import java.text.DecimalFormat;

public class Model_Service {
    private String id;
    private String name;
    private int price;
    private String unit;
    private String status;

    // Constructors
    public Model_Service(String id, String name, int price, String unit, String status) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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
    
    public Object[] toRowTable(EventAction<Model_Service> eventAction) {
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        return new Object[]{id, name, df.format(price), unit, status, new ModelAction<>(this, eventAction, "action_all")};
    }
    
    public Object[] toRowTableInBooking() {
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        return new Object[]{
                name,
                df.format(price),
                unit + "/DV",
        };
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