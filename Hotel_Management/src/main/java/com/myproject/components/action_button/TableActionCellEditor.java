package com.myproject.components.action_button;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class TableActionCellEditor extends DefaultCellEditor {

    private TableActionEvent event;
    private boolean isEditButtonVisible;
    private boolean isDeleteButtonVisible;
    private boolean isViewButtonVisible;

    public TableActionCellEditor(TableActionEvent event) {
        super(new JCheckBox());
        this.event = event;
    }

    public void setButtonVisibility(boolean isEditButtonVisible, boolean isDeleteButtonVisible, boolean isViewButtonVisible) {
        this.isEditButtonVisible = isEditButtonVisible;
        this.isDeleteButtonVisible = isDeleteButtonVisible;
        this.isViewButtonVisible = isViewButtonVisible;
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int row, int column) {
        PanelAction action = new PanelAction();
        action.initEvent(event, row);
        action.setBackground(jtable.getSelectionBackground());
        action.setEditButtonVisible(isEditButtonVisible);
        action.setDeleteButtonVisible(isDeleteButtonVisible);
        action.setViewButtonVisible(isViewButtonVisible);
        return action;
    }
}