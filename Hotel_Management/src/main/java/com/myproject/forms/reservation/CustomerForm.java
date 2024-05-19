package com.myproject.forms.reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerForm extends JFrame {
    private JTable customerTable;
    private DefaultTableModel customerTableModel;
    private JTextField nameField, phoneField, idField, emailField, addressField;
    private JCheckBox genderCheckBox;

    private JComboBox<String> customerComboBox;

    public CustomerForm(JComboBox<String> customerComboBox) {
        this.customerComboBox = customerComboBox;
        setTitle("Danh mục Khách hàng");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton closeButton = new JButton("Thoát");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Customer table
        String[] columnNames = {"HỌ TÊN", "ĐIỆN THOẠI", "GIỚI TÍNH", "CCCD/CMND", "EMAIL", "ĐỊA CHỈ"};
        customerTableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(customerTableModel);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Information panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 4));

        infoPanel.add(new JLabel("Tên"));
        nameField = new JTextField();
        infoPanel.add(nameField);

        infoPanel.add(new JLabel("Điện thoại"));
        phoneField = new JTextField();
        infoPanel.add(phoneField);

        infoPanel.add(new JLabel("CCCD/CMND"));
        idField = new JTextField();
        infoPanel.add(idField);

        infoPanel.add(new JLabel("Địa chỉ"));
        addressField = new JTextField();
        infoPanel.add(addressField);

        infoPanel.add(new JLabel("Giới tính"));
        genderCheckBox = new JCheckBox("Nam");
        infoPanel.add(genderCheckBox);

        infoPanel.add(new JLabel("Email"));
        emailField = new JTextField();
        infoPanel.add(emailField);

        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Button actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCustomer();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void addCustomer() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String id = idField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        boolean gender = genderCheckBox.isSelected();

        if (!name.isEmpty() && !phone.isEmpty()) {
            customerTableModel.addRow(new Object[]{name, phone, gender ? "Nam" : "Nữ", id, email, address});
            customerComboBox.addItem(name);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Tên và điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            customerTableModel.setValueAt(nameField.getText(), selectedRow, 0);
            customerTableModel.setValueAt(phoneField.getText(), selectedRow, 1);
            customerTableModel.setValueAt(genderCheckBox.isSelected() ? "Nam" : "Nữ", selectedRow, 2);
            customerTableModel.setValueAt(idField.getText(), selectedRow, 3);
            customerTableModel.setValueAt(emailField.getText(), selectedRow, 4);
            customerTableModel.setValueAt(addressField.getText(), selectedRow, 5);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            String name = (String) customerTableModel.getValueAt(selectedRow, 0);
            customerTableModel.removeRow(selectedRow);
            customerComboBox.removeItem(name);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        idField.setText("");
        emailField.setText("");
        addressField.setText("");
        genderCheckBox.setSelected(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerForm customerForm = new CustomerForm(new JComboBox<>());
            customerForm.setVisible(true);
        });
    }
}
