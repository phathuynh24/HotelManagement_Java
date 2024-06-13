package com.raven.dialog;

import com.raven.controller.ServiceController;
import com.raven.model.Model_Service;
import com.raven.swing.ButtonHover;
import com.raven.utils.DataChangeListener;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ServiceDialog {

    private JTextField codeField;
    private JTextField nameField;
    private JTextField unitField;
    private JTextField priceField;
    private JTextField statusField;

    private final ServiceController serviceController = new ServiceController();
    private final Model_Service service;
    private JFrame frame;
    private ButtonHover saveButton;

    public ServiceDialog(String purpose, Model_Service service, DataChangeListener dataChangeListener) {
        this.service = service;

        if (purpose.equals("Add")) {
            initUI("Thêm dịch vụ");
            saveButton.addActionListener(e -> {
                if (validateInputs()) {
                    saveService();
                    JOptionPane.showMessageDialog(null, "Thêm dịch vụ thành công");
                    frame.dispose();
                    dataChangeListener.onDataChanged();
                }
            });
        } else if (purpose.equals("Update")) {
            initUI("Cập nhật dịch vụ");
            loadData();
            saveButton.addActionListener(e -> {
                if (validateInputs()) {
                    updateService();
                    JOptionPane.showMessageDialog(null, "Cập nhật dịch vụ thành công");
                    frame.dispose();
                    dataChangeListener.onDataChanged();
                }
            });
        }
    }

    private void loadData() {
        codeField.setText(service.getId());
        nameField.setText(service.getName());
        unitField.setText(service.getUnit());
        priceField.setText(String.valueOf(service.getPrice()));
        statusField.setText(service.getStatus());
    }

    private void saveService() {
        String code = codeField.getText();
        String name = nameField.getText();
        String unit = unitField.getText();
        int price = Integer.parseInt(priceField.getText());
        String status = statusField.getText();

        Model_Service newService = new Model_Service(code, name, price, unit, status);
        serviceController.addService(newService);
    }

    private void updateService() {
        String code = codeField.getText();
        String name = nameField.getText();
        String unit = unitField.getText();
        int price = Integer.parseInt(priceField.getText());
        String status = statusField.getText();

        Model_Service updatedService = new Model_Service(code, name, price, unit, status);
        serviceController.updateService(code, updatedService);
    }

    private void initUI(String purposeName) {
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 13);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        frame = new JFrame(purposeName);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        codeField = new JTextField(20);
        nameField = new JTextField(20);
        unitField = new JTextField(20);
        priceField = new JTextField(20);
        statusField = new JTextField(20);

        if (purposeName.equals("Thêm dịch vụ")) {
            codeField.setText(serviceController.createId());
        }

        codeField.setEditable(false);
        codeField.setEnabled(false);

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

        JLabel label1 = new JLabel("Mã dịch vụ:", SwingConstants.LEFT);
        label1.setFont(labelFont);
        frame.add(label1, gbc);

        JLabel label2 = new JLabel("Tên dịch vụ:", SwingConstants.LEFT);
        label2.setFont(labelFont);
        frame.add(label2, gbc);

        JLabel label3 = new JLabel("Đơn vị:", SwingConstants.LEFT);
        label3.setFont(labelFont);
        frame.add(label3, gbc);

        JLabel label4 = new JLabel("Giá:", SwingConstants.LEFT);
        label4.setFont(labelFont);
        frame.add(label4, gbc);

        JLabel label5 = new JLabel("Trạng thái:", SwingConstants.LEFT);
        label5.setFont(labelFont);
        frame.add(label5, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label1, gbc);

        gbc.gridx = 1;
        frame.add(codeField, gbc);

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
        frame.add(unitField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label4, gbc);

        gbc.gridx = 1;
        frame.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label5, gbc);

        gbc.gridx = 1;
        frame.add(statusField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        frame.add(new JLabel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
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
        codeField.setPreferredSize(fieldDimension);
        codeField.setFont(textFieldFont);
        nameField.setPreferredSize(fieldDimension);
        nameField.setFont(textFieldFont);
        unitField.setPreferredSize(fieldDimension);
        unitField.setFont(textFieldFont);
        priceField.setPreferredSize(fieldDimension);
        priceField.setFont(textFieldFont);
        statusField.setPreferredSize(fieldDimension);
        statusField.setFont(textFieldFont);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private boolean validateInputs() {
        String code = codeField.getText();
        String name = nameField.getText();
        String unit = unitField.getText();
        String priceStr = priceField.getText();
        String status = statusField.getText();

        // Kiểm tra các trường bắt buộc không được để trống
        if (isEmptyOrWhitespace(code) || isEmptyOrWhitespace(name) || isEmptyOrWhitespace(unit)
                || isEmptyOrWhitespace(priceStr) || isEmptyOrWhitespace(status)) {
            JOptionPane.showMessageDialog(frame, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra giá là số nguyên dương
        if (!isPositiveInteger(priceStr)) {
            JOptionPane.showMessageDialog(frame, "Giá phải là số nguyên dương.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean isPositiveInteger(String str) {
        try {
            int num = Integer.parseInt(str);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isEmptyOrWhitespace(String str) {
        return str == null || str.trim().isEmpty();
    }
}
