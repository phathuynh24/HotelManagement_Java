package com.raven.model;

import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;
import java.text.DecimalFormat;

public class Model_Invoice_Rooms {

    private String id;
    private String name;
    private int capacity;
    private String type;
    private String floor;
    private int pricePerNight;
    private int numberOfNights;
    private int totalRoomPrice;

    public Model_Invoice_Rooms() {}
    
    public Model_Invoice_Rooms(String id, String name, int capacity, String type, String floor, int pricePerNight, int numberOfNights, int totalRoomPrice) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.floor = floor;
        this.pricePerNight = pricePerNight;
        this.numberOfNights = numberOfNights;
        this.totalRoomPrice = totalRoomPrice;
    }
    
    public Model_Invoice_Rooms(Model_Room room, int pricePerNight, int numberOfNights, int totalRoomPrice) {
        this.id = room.getId();
        this.name = room.getName();
        this.capacity = room.getCapacity();
        this.type = room.getType();
        this.floor = room.getFloor();
        this.pricePerNight = pricePerNight;
        this.numberOfNights = numberOfNights;
        this.totalRoomPrice = totalRoomPrice;
    }

    public String getCode() {
        return id;
    }

    public void setCode(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public int getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(int pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public int getTotalRoomPrice() {
        return totalRoomPrice;
    }

    public void setTotalRoomPrice(int totalRoomPrice) {
        this.totalRoomPrice = totalRoomPrice;
    }
    
    public Object[] toRowTable(EventAction<Model_Invoice_Rooms> eventAction) {
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        return new Object[]{name, type, df.format(pricePerNight), capacity, floor, new ModelAction<>(this, eventAction, "action_delete")};
    }

    @Override
    public String toString() {
        return "Model_Invoice_Rooms{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", type='" + type + '\'' +
                ", floor='" + floor + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", numberOfNights=" + numberOfNights +
                ", totalRoomPrice=" + totalRoomPrice +
                '}';
    }
}