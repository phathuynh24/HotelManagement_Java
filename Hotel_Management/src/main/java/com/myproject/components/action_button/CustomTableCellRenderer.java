package com.myproject.components.action_button;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    private int rowHeight;

    public CustomTableCellRenderer(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        table.setRowHeight(row, rowHeight);
        return component;
    }
}