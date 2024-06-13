package com.raven.dialog;

import com.raven.controller.CustomerController;
import com.raven.model.Model_Customer;
import com.raven.swing.ButtonHover;
import com.raven.utils.DataChangeListener;

import javax.swing.*;
import java.awt.*;

public class CustomerDialog {

    private JTextField idCardField;
    private JTextField nameField;
    private JTextField phoneField;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private JTextField addressField;
    private JTextField emailField;

    private final CustomerController customerController = new CustomerController();
    private final Model_Customer customer;
    private JFrame frame;
    private ButtonHover saveButton;

    public CustomerDialog(String purpose, Model_Customer customer, DataChangeListener dataChangeListener) {
        this.customer = customer;

        if (purpose.equals("Add")) {
            initUI("Thêm khách hàng");
            saveButton.addActionListener(e -> {
                if (validateFields()) {
                    saveCustomer();
                    JOptionPane.showMessageDialog(null, "Thêm khách hàng thành công");
                    frame.dispose();
                    dataChangeListener.onDataChanged();
                }
            });
        } else if (purpose.equals("Update")) {
            initUI("Cập nhật khách hàng");
            loadData();
            saveButton.addActionListener(e -> {
                if (validateFields()) {
                    updateCustomer();
                    JOptionPane.showMessageDialog(null, "Cập nhật khách hàng thành công");
                    frame.dispose();
                    dataChangeListener.onDataChanged();
                }
            });
        }
    }

    private void loadData() {
        idCardField.setText(customer.getIdCard());
        nameField.setText(customer.getName());
        phoneField.setText(customer.getPhone());
        if (customer.getGender().equalsIgnoreCase("Nam")) {
            maleRadioButton.setSelected(true);
        } else if (customer.getGender().equalsIgnoreCase("Nữ")) {
            femaleRadioButton.setSelected(true);
        }
        addressField.setText(customer.getAddress());
        emailField.setText(customer.getEmail());
    }

    private void saveCustomer() {
        String id = idCardField.getText();
        String name = nameField.getText();
        String phone = phoneField.getText();
        String gender = maleRadioButton.isSelected() ? "Nam" : "Nữ";
        String address = addressField.getText();
        String email = emailField.getText();

        Model_Customer newCustomer = new Model_Customer(id, name, gender, phone, email, address);
        customerController.addCustomer(newCustomer);
    }

    private void updateCustomer() {
        String id = idCardField.getText();
        String name = nameField.getText();
        String phone = phoneField.getText();
        String gender = maleRadioButton.isSelected() ? "Nam" : "Nữ";
        String address = addressField.getText();
        String email = emailField.getText();

        Model_Customer updatedCustomer = new Model_Customer(id, name, gender, phone, email, address);
        customerController.updateCustomer(id, updatedCustomer);
    }

    private boolean validateFields() {
        String id = idCardField.getText();
        String name = nameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String email = emailField.getText();

        if (id.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ tất cả các trường");
            return false;
        }

        if (!id.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "CCCD chỉ được chứa số");
            return false;
        }

        if (!name.matches("[\\p{L} ]+")) {
            JOptionPane.showMessageDialog(null, "Tên chỉ được chứa chữ cái");
            return false;
        }

        if (!phone.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ");
            return false;
        }

        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ");
            return false;
        }

        if (customerController.isCCCDExist(id)) {
            JOptionPane.showMessageDialog(null, "CCCD đã tồn tại");
            return false;
        }

        return true;
    }

    private void initUI(String purposeName) {
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 13);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        frame = new JFrame(purposeName);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        idCardField = new JTextField(20);
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        maleRadioButton = new JRadioButton("Nam");
        femaleRadioButton = new JRadioButton("Nữ");
        addressField = new JTextField(20);
        emailField = new JTextField(20);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);

        JPanel genderPanel = new JPanel();
        genderPanel.setOpaque(false);
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);

        saveButton = new ButtonHover();
        saveButton.setText("Lưu");
        saveButton.setPreferredSize(new Dimension(80, 30));
        saveButton.setBackground(new Color(242, 242, 242));

        ButtonHover cancelButton = new ButtonHover();
        cancelButton.setText("Hủy");
        cancelButton.setPreferredSize(new Dimension(80, 30));
        cancelButton.setBackground(new Color(242, 242, 242));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        cancelButton.addActionListener(e -> frame.dispose());

        frame.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);

        JLabel label1 = new JLabel("CCCD:", SwingConstants.LEFT);
        label1.setFont(labelFont);
        frame.add(label1, gbc);

        JLabel label2 = new JLabel("Tên khách hàng:", SwingConstants.LEFT);
        label2.setFont(labelFont);
        frame.add(label2, gbc);

        JLabel label3 = new JLabel("Số điện thoại:", SwingConstants.LEFT);
        label3.setFont(labelFont);
        frame.add(label3, gbc);

        JLabel label4 = new JLabel("Giới tính:", SwingConstants.LEFT);
        label4.setFont(labelFont);
        frame.add(label4, gbc);

        JLabel label5 = new JLabel("Địa chỉ:", SwingConstants.LEFT);
        label5.setFont(labelFont);
        frame.add(label5, gbc);

        JLabel label6 = new JLabel("Email:", SwingConstants.LEFT);
        label6.setFont(labelFont);
        frame.add(label6, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label1, gbc);

        gbc.gridx = 1;
        frame.add(idCardField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label2, gbc);

        gbc.gridx = 1;
        frame.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label3, gbc);

        gbc.gridx = 1;
        frame.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label4, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        frame.add(genderPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label5, gbc);

        gbc.gridx = 1;
        frame.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label6, gbc);

        gbc.gridx = 1;
        frame.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        frame.add(new JLabel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        frame.add(buttonPanel, gbc);

        // Thiết lập màu nền cho JFrame
        frame.getContentPane().setBackground(Color.WHITE);
        buttonPanel.setBackground(Color.WHITE);

        saveButton.setFont(buttonFont);
        cancelButton.setFont(buttonFont);

        // Thiết lập kích thước và căn giữa cho JTextField
        Dimension fieldDimension = new Dimension(200, 30);
        idCardField.setPreferredSize(fieldDimension);
        idCardField.setFont(textFieldFont);
        nameField.setPreferredSize(fieldDimension);
        nameField.setFont(textFieldFont);
        phoneField.setPreferredSize(fieldDimension);
        phoneField.setFont(textFieldFont);
        addressField.setPreferredSize(fieldDimension);
        addressField.setFont(textFieldFont);
        emailField.setPreferredSize(fieldDimension);
        emailField.setFont(textFieldFont);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
