package com.raven.model;

import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;
import java.text.DecimalFormat;

public class Model_RoomType {

    private String id;
    private String name;
    private int price;
    private String description;
    private String type;

    // Constructor
    public Model_RoomType() {
    }

    public Model_RoomType(String id, String name, int price, String description, String type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.type = type;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public Object[] toRowTable(EventAction<Model_RoomType> eventAction) {
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        return new Object[]{id, name, type, df.format(price), description, new ModelAction<>(this, eventAction, "action_all")};
    }

    @Override
    public String toString() {
        return "Model_RoomType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
