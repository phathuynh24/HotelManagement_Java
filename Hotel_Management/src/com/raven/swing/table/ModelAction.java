package com.raven.swing.table;

public class ModelAction<T> {

    private T model;
    private EventAction<T> event;
    private String type;

    public ModelAction(T model, EventAction<T> event, String type) {
        this.model = model;
        this.event = event;
        this.type = type;
    }

    public ModelAction() {
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public EventAction<T> getEvent() {
        return event;
    }

    public void setEvent(EventAction<T> event) {
        this.event = event;
    }
    
     public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
