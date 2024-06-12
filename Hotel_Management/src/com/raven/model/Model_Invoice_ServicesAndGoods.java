package com.raven.model;

import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;
import java.text.DecimalFormat;

public class Model_Invoice_ServicesAndGoods {

    private String roomName;
    private String name;
    private int quantity;
    private int unitPrice;
    private int totalPrice;
    private String unit;
    private String type;

    public Model_Invoice_ServicesAndGoods() {}
    
    public Model_Invoice_ServicesAndGoods(String roomName, String name, int quantity, int unitPrice, int totalPrice, String unit, String type) {
        this.roomName = roomName;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.unit = unit;
        this.type = type;
    }
    
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object[] toRowTable(EventAction<Model_Invoice_ServicesAndGoods> eventAction) {
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        return new Object[]{roomName, name, quantity, unit, df.format(unitPrice), df.format(totalPrice), type, new ModelAction<>(this, eventAction, "action_delete")};
    }
    
    @Override
    public String toString() {
        return "Model_Invoice_Services{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", unit='" + unit + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}