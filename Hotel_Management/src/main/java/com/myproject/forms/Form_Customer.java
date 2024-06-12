package com.raven.form;

import com.raven.controller.CustomerController;
import com.raven.dialog.CustomerDialog;
import com.raven.model.Model_Customer;
import com.raven.swing.table.EventAction;
import com.raven.utils.DataChangeListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class Form_Customer extends javax.swing.JPanel implements DataChangeListener {

    private CustomerController customerController;
    private List<Model_Customer> customers;
    private EventAction<Model_Customer> eventAction;
    private CustomerDialog customerDialog;

    public Form_Customer() {
        initComponents();
        table1.fixTable(jScrollPane1);
        setOpaque(false);
        loadDataAsync();
        attachSearchListener();
    }

    private void loadDataAsync() {
        // Tạo một luồng mới để tải dữ liệu
        Thread loadDataThread = new Thread(() -> {
            initData();
        });

        // Bắt đầu thực hiện luồng
        loadDataThread.start();
    }

    private void initData() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        
        customerController = new CustomerController();
        customers = customerController.getAllCustomers();

        eventAction = new EventAction<Model_Customer>() {
            @Override
            public void delete(Model_Customer customer) {
                customerController.deleteCustomer(customer.getIdCard());
                loadDataAsync();
                JOptionPane.showMessageDialog(null, "Xóa loại phòng thành công");
            }

            @Override
            public void update(Model_Customer customer) {
                customerDialog = new CustomerDialog("Update", customer, Form_Customer.this);
            }
        };

        for (Model_Customer customer : customers) {
            table1.addRow(customer.toRowTable(eventAction));
        }
    }

    private void attachSearchListener() {
        searchText1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchCustomers();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchCustomers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchCustomers();
            }
        });
    }

    private void searchCustomers() {
        String keyword = searchText1.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0); // Clear table

        for (Model_Customer customer : customers) {
            if (customerController.containsKeyword(customer, keyword)) {
                model.addRow(customer.toRowTable(eventAction));
            }
        }
    }

     @Override
    public void onDataChanged() {
        loadDataAsync();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new com.raven.swing.table.Table();
        jPanel1 = new javax.swing.JPanel();
        searchText1 = new com.raven.swing.SearchText();
        buttonHover1 = new com.raven.swing.ButtonHover();

        setPreferredSize(new java.awt.Dimension(1058, 741));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("sansserif", 1, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(76, 76, 76));
        jLabel5.setText("Thông tin Khách Hàng");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CCCD", "Tên khách hàng", "Giới tính", "SĐT", "Email", "Địa chỉ", "Thao tác"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table1.setShowGrid(false);
        table1.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(table1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        jPanel1.setOpaque(false);

        buttonHover1.setBorder(null);
        buttonHover1.setText("Thêm Khách Hàng");
        buttonHover1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        buttonHover1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonHover1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(searchText1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 549, Short.MAX_VALUE)
                .addComponent(buttonHover1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonHover1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchText1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 654, Short.MAX_VALUE)
                .addGap(31, 31, 31))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonHover1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonHover1ActionPerformed
      customerDialog = new CustomerDialog("Add", null, Form_Customer.this); 
    }//GEN-LAST:event_buttonHover1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.swing.ButtonHover buttonHover1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.raven.swing.SearchText searchText1;
    private com.raven.swing.table.Table table1;
    // End of variables declaration//GEN-END:variables
}
