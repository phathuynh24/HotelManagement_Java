package com.myproject.swings;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class GoodsTable extends JTable {

    public GoodsTable() {
        setShowHorizontalLines(true);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                TableHeader header = new TableHeader(o + "");
                header.setHorizontalAlignment(JLabel.CENTER);
                return header;
            }
        });
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean bln1, int i, int i1) {
                Component com = super.getTableCellRendererComponent(jtable, o, selected, bln1, i, i1);
                com.setBackground(Color.WHITE);
                setBorder(noFocusBorder);
                 if (selected) {
                    com.setForeground(new Color(15, 89, 140));
                    com.setFont(com.getFont().deriveFont(java.awt.Font.BOLD));
                } else {
                    com.setForeground(new Color(102, 102, 102));
                    com.setFont(com.getFont().deriveFont(java.awt.Font.PLAIN));
                }
                setHorizontalAlignment(JLabel.CENTER);
                return com;

            }
        });
    }

    public void addRow(Object[] row) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(row);
    }
}
