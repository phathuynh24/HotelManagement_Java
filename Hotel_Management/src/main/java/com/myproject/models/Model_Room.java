package com.myproject.models;

import com.myproject.models.types.RoomStatus;

public class Model_Room {
    private String roomId;
    private Model_RoomType roomType;
    private RoomStatus status;
    private int capacity;

    public Model_Room(String roomId, Model_RoomType roomType, RoomStatus status, int capacity) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.status = status;
        this.capacity = capacity;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Model_RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(Model_RoomType roomType) {
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}