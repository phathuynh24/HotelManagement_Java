package com.raven.model;

import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;
import java.text.DecimalFormat;

public class Model_Goods {
    private String id;
    private String name;
    private String unit;
    private int importPrice;
    private int sellPrice;
    private String description;
    private String status;

    // Constructors
    public Model_Goods() {
    }

    public Model_Goods(String id, String name, String unit, int importPrice, int sellPrice, String description, String status) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.importPrice = importPrice;
        this.sellPrice = sellPrice;
        this.description = description;
        this.status = status;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public int getImportPrice() {
        return importPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setImportPrice(int importPrice) {
        this.importPrice = importPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString method
    @Override
    public String toString() {
        return "Model_Goods{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", unit='" + unit + '\''
                + ", importPrice=" + importPrice
                + ", sellPrice=" + sellPrice
                + ", description='" + description + '\''
                + ", status='" + status + '\''
                + '}';
    }

    // toRowTable method
    public Object[] toRowTable(EventAction<Model_Goods> eventAction) {
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        return new Object[]{
                id,
                name,
                unit,
                df.format(importPrice),
                df.format(sellPrice),
                description,
                status,
                new ModelAction<>(this, eventAction, "action_all")
        };
    }
    
    public Object[] toRowTableInBooking() {
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        return new Object[]{
                name,
                df.format(sellPrice),
                unit + "/HH",
        };
    }
}
