package com.myproject.forms.account;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CheckboxCellHandler {

    // Renderer class for rendering checkboxes
    public static class CheckboxRenderer extends DefaultTableCellRenderer {
        private final JCheckBox checkbox;

        public CheckboxRenderer() {
            checkbox = new JCheckBox();
            checkbox.setHorizontalAlignment(JCheckBox.CENTER);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null) {
                checkbox.setSelected((boolean) value);
            }
            return checkbox;
        }
    }

    // Editor class for interacting with checkboxes
    public static class CheckboxEditor extends DefaultCellEditor {
        private final JCheckBox checkbox;

        public CheckboxEditor(JCheckBox checkbox) {
            super(checkbox);
            this.checkbox = checkbox;
            checkbox.setHorizontalAlignment(JCheckBox.CENTER);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value != null) {
                checkbox.setSelected((boolean) value);
            }
            return checkbox;
        }
    }
}
