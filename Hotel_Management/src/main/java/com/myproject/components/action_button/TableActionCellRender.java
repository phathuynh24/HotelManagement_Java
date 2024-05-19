package com.myproject.components.action_button;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableActionCellRender extends DefaultTableCellRenderer {

    private boolean isEditButtonVisible = true;
    private boolean isDeleteButtonVisible = true;
    private boolean isViewButtonVisible = true;

    public void setButtonVisibility(boolean editVisible, boolean deleteVisible, boolean viewVisible) {
        isEditButtonVisible = editVisible;
        isDeleteButtonVisible = deleteVisible;
        isViewButtonVisible = viewVisible;
    }

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSelected, boolean hasFocus, int row, int column) {
        Component com = super.getTableCellRendererComponent(jtable, o, isSelected, hasFocus, row, column);
        
        PanelAction action = new PanelAction();
        action.setEditButtonVisible(isEditButtonVisible);
        action.setDeleteButtonVisible(isDeleteButtonVisible);
        action.setViewButtonVisible(isViewButtonVisible);
        
        if (isSelected == false && row % 2 == 0) {
            action.setBackground(Color.WHITE);
        } else {
            action.setBackground(com.getBackground());
        }
        return action;
    }
}