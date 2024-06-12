package com.raven.utils;

import com.raven.controller.RoomTypeController;
import com.raven.model.Model_RoomType;

public class RoomPrice {
    private final RoomTypeController roomTypeController = new RoomTypeController();
    private java.util.List<Model_RoomType> roomTypeList;    
    
    public RoomPrice() {
        roomTypeList = roomTypeController.getAllRoomTypes();
    }
    
    public int getPrice(String type) {
        for (Model_RoomType rt : roomTypeList) {
            if (rt.getType().equals(type)) {
                return rt.getPrice();
            }
        }
        return 0;
    }
}
