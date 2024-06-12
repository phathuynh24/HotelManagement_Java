package com.raven.model;

import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;

public class Model_Floor {
    private String id;
    private String name;
    private int totalRoom;

    public Model_Floor() {}
    
    public Model_Floor(String id, String name, int totalRoom) {
        this.id = id;
        this.name = name;
        this.totalRoom = totalRoom;
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

    public int getTotalRoom() {
        return totalRoom;
    }

    public void setTotalRoom(int totalRoom) {
        this.totalRoom = totalRoom;
    }
    
    public Object[] toRowTable(EventAction<Model_Floor> eventAction) {
        return new Object[]{id, name, totalRoom, new ModelAction<>(this, eventAction, "action_all")};
    }

    @Override
    public String toString() {
        return "Floor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", totalRoom=" + totalRoom +
                '}';
    }
}
