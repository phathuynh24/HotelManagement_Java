package com.raven.form;

import com.raven.controller.InvoiceController;
import com.raven.form.reservation.Form_GroupBooking;
import com.raven.model.Model_Invoice;
import com.raven.swing.table.EventAction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class Form_Invoice extends javax.swing.JPanel {

    private InvoiceController invoiceController;
    private List<Model_Invoice> invoices;
    private EventAction<Model_Invoice> eventAction;
    private JFrame frame;

    public Form_Invoice() {
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
        invoiceController = new InvoiceController();
        invoices = invoiceController.getAllInvoices();

        eventAction = new EventAction<Model_Invoice>() {
            @Override
            public void delete(Model_Invoice invoice) {
                int option = JOptionPane.showConfirmDialog(frame, "Bạn có muốn xóa phiếu đặt phòng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    invoiceController.deleteInvoice(invoice.getId());
                    JOptionPane.showMessageDialog(frame, "Đã xóa phiếu đặt phòng");
                    loadDataAsync();
                }
            }

            @Override
            public void update(Model_Invoice invoice) {
                // Tạo JFrame
                frame = new JFrame("Cập nhật phiếu đặt phòng");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(1500, 800);
                EmptyBorder border = new EmptyBorder(10, 10, 10, 10);
                frame.setLayout(new BorderLayout());

                // Tạo panel
                Form_GroupBooking panel = new Form_GroupBooking("Update", invoice);
                panel.setBackground(Color.white);
                panel.setBorder(border);

                // Thêm panel vào JFrame
                frame.add(panel, BorderLayout.CENTER);
                frame.getContentPane().setBackground(new Color(242, 242, 242));

                // Hiển thị JFrame
                frame.setVisible(true);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        boolean swapSuccessful = panel.isUpdateSuccessful(); // Kiểm tra kết quả swap
                        if (swapSuccessful) {
                            loadDataAsync();
                        }
                    }
                });
            }
        };
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        for (Model_Invoice room : invoices) {
            table1.addRow(room.toRowTable(eventAction));
        }
    }

    private void attachSearchListener() {
        searchText1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchInvoices();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchInvoices();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchInvoices();
            }
        });
    }

    private void searchInvoices() {
        String keyword = searchText1.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0); // Clear table

        for (Model_Invoice invoice : invoices) {
            if (invoiceController.containsKeyword(invoice, keyword)) {
                model.addRow(invoice.toRowTable(eventAction));
            }
        }
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

        setPreferredSize(new java.awt.Dimension(1058, 741));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("sansserif", 1, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(76, 76, 76));
        jLabel5.setText("Thông tin Hóa Đơn");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Khách hàng", "Số khách", "Ngày đặt", "Checkin", "Checkout", "Thanh toán", "Ghi chú", "Trạng thái", "Thao tác"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table1.setShowGrid(false);
        table1.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(table1);
        if (table1.getColumnModel().getColumnCount() > 0) {
            table1.getColumnModel().getColumn(0).setPreferredWidth(40);
            table1.getColumnModel().getColumn(2).setPreferredWidth(30);
            table1.getColumnModel().getColumn(6).setPreferredWidth(50);
            table1.getColumnModel().getColumn(7).setPreferredWidth(60);
            table1.getColumnModel().getColumn(8).setPreferredWidth(60);
        }

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(searchText1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(693, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(searchText1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.raven.swing.SearchText searchText1;
    private com.raven.swing.table.Table table1;
    // End of variables declaration//GEN-END:variables
}
