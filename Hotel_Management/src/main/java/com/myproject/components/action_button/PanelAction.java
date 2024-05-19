package com.myproject.components.action_button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelAction extends javax.swing.JPanel {

    private boolean isEditButtonVisible = true;
    private boolean isDeleteButtonVisible = true;
    private boolean isViewButtonVisible = true;

    public PanelAction() {
        initComponents();
    }

    public void initEvent(TableActionEvent event, int row) {
        cmdEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                event.onEdit(row);
            }
        });
        cmdDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                event.onDelete(row);
            }
        });
        cmdView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                event.onView(row);
            }
        });
    }

    public void setEditButtonVisible(boolean visible) {
        isEditButtonVisible = visible;
        cmdEdit.setVisible(visible);
    }

    public void setDeleteButtonVisible(boolean visible) {
        isDeleteButtonVisible = visible;
        cmdDelete.setVisible(visible);
    }

    public void setViewButtonVisible(boolean visible) {
        isViewButtonVisible = visible;
        cmdView.setVisible(visible);
    }

    private void initComponents() {

        cmdEdit = new ActionButton();
        cmdDelete = new ActionButton();
        cmdView = new ActionButton();

        cmdEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/myproject/icons/edit.png")));
        cmdDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/myproject/icons/delete.png")));
        cmdView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/myproject/icons/view.png")));

        cmdEdit.setPreferredSize(new java.awt.Dimension(20, 20));
        cmdDelete.setPreferredSize(new java.awt.Dimension(20, 20));
        cmdView.setPreferredSize(new java.awt.Dimension(20, 20));
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cmdEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cmdDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cmdView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private ActionButton cmdDelete;
    private ActionButton cmdEdit;
    private ActionButton cmdView;
}