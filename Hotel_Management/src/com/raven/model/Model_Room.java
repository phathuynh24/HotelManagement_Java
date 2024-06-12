package com.raven.model;

import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;
import java.text.DecimalFormat;

public class Model_Room {
    private String id;
    private String name;
    private String type;
    private String floor;
    private int capacity;

    public Model_Room() {}
    
    public Model_Room(String id, String name, String type, String floor, int capacity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.floor = floor;
        this.capacity = capacity;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public Object[] toRowTable(EventAction<Model_Room> eventAction) {
        return new Object[]{id, name, type, floor, capacity, new ModelAction<>(this, eventAction, "action_all")};
    }
    
    public Object[] toRowTableGroupBooking(int price) {
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        return new Object[]{name, type, df.format(price), capacity, floor};
    }

    @Override
    public String toString() {
        return "Model_Room{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", floor='" + floor + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}