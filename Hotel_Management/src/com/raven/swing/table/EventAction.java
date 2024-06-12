package com.raven.swing.table;

public interface EventAction<T> {
    public void delete(T model);
    public void update(T model);
}
