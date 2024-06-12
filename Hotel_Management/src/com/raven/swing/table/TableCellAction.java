package com.raven.swing.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class TableCellAction extends DefaultCellEditor {

    private ModelAction data;

    public TableCellAction() {
        super(new JCheckBox());
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int i, int i1) {
        data = (ModelAction) o;
        String type = data.getType();

        Component cell;
        if (null == type) {
            cell = new Action_All(data);
        } else {
            switch (type) {
                case "action_all":
                    cell = new Action_All(data);
                    break;
                case "action_delete":
                    cell = new Action_Delete(data);
                    break;
                default:
                    cell = new Action_All(data);
                    break;
            }
        }

        cell.setBackground(new Color(239, 244, 255));
        return cell;
    }

    //  This method to pass data to cell render when focus lose in cell
    @Override
    public Object getCellEditorValue() {
        return data;
    }
}
