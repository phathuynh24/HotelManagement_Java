package com.raven.dialog;

import com.raven.controller.GoodsController;
import com.raven.model.Model_Goods;
import com.raven.swing.ButtonHover;
import com.raven.utils.DataChangeListener;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.Objects;

public class GoodsDialog {

    private JTextField codeField;
    private JTextField nameField;
    private JTextField unitField;
    private JTextField importPriceField;
    private JTextField sellPriceField;
    private JTextField descriptionField;
    private JTextField statusField;

    private final GoodsController goodsController = new GoodsController();
    private final Model_Goods goods;
    private JFrame frame;
    private ButtonHover saveButton;

    public GoodsDialog(String purpose, Model_Goods goods, DataChangeListener dataChangeListener) {
        this.goods = goods;

        if (purpose.equals("Add")) {
            initUI("Thêm hàng hóa");
            saveButton.addActionListener(e -> {
                if (validateInputs()) {
                    saveGoods();
                    JOptionPane.showMessageDialog(null, "Thêm hàng hóa thành công");
                    frame.dispose();
                    dataChangeListener.onDataChanged();
                }
            });
        } else if (purpose.equals("Update")) {
            initUI("Cập nhật hàng hóa");
            loadData();
            saveButton.addActionListener(e -> {
                if (validateInputs()) {
                    updateGoods();
                    JOptionPane.showMessageDialog(null, "Cập nhật hàng hóa thành công");
                    frame.dispose();
                    dataChangeListener.onDataChanged();
                }
            });
        }
    }

    private void loadData() {
        codeField.setText(goods.getId());
        nameField.setText(goods.getName());
        unitField.setText(goods.getUnit());
        importPriceField.setText(String.valueOf(goods.getImportPrice()));
        sellPriceField.setText(String.valueOf(goods.getSellPrice()));
        descriptionField.setText(goods.getDescription());
        statusField.setText(goods.getStatus());
    }

    private void saveGoods() {
        String code = codeField.getText();
        String name = nameField.getText();
        String unit = unitField.getText();
        int importPrice = Integer.parseInt(importPriceField.getText());
        int sellPrice = Integer.parseInt(sellPriceField.getText());
        String description = descriptionField.getText();
        String status = statusField.getText();

        Model_Goods newGoods = new Model_Goods(code, name, unit, importPrice, sellPrice, description, status);
        goodsController.addGoods(newGoods);
    }

    private void updateGoods() {
        String code = codeField.getText();
        String name = nameField.getText();
        String unit = unitField.getText();
        int importPrice = Integer.parseInt(importPriceField.getText());
        int sellPrice = Integer.parseInt(sellPriceField.getText());
        String description = descriptionField.getText();
        String status = statusField.getText();

        Model_Goods updatedGoods = new Model_Goods(code, name, unit, importPrice, sellPrice, description, status);
        goodsController.updateGoods(code, updatedGoods);
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
        importPriceField = new JTextField(20);
        sellPriceField = new JTextField(20);
        descriptionField = new JTextField(20);
        statusField = new JTextField(20);

        if (purposeName.equals("Thêm hàng hóa")) {
            codeField.setText(goodsController.createId());
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

        JLabel label1 = new JLabel("Mã hàng hóa:", SwingConstants.LEFT);
        label1.setFont(labelFont);
        frame.add(label1, gbc);

        JLabel label2 = new JLabel("Tên hàng hóa:", SwingConstants.LEFT);
        label2.setFont(labelFont);
        frame.add(label2, gbc);

        JLabel label3 = new JLabel("Đơn vị:", SwingConstants.LEFT);
        label3.setFont(labelFont);
        frame.add(label3, gbc);

        JLabel label4 = new JLabel("Giá nhập:", SwingConstants.LEFT);
        label4.setFont(labelFont);
        frame.add(label4, gbc);

        JLabel label5 = new JLabel("Giá bán:", SwingConstants.LEFT);
        label5.setFont(labelFont);
        frame.add(label5, gbc);

        JLabel label6 = new JLabel("Mô tả:", SwingConstants.LEFT);
        label6.setFont(labelFont);
        frame.add(label6, gbc);

        JLabel label7 = new JLabel("Trạng thái:", SwingConstants.LEFT);
        label7.setFont(labelFont);
        frame.add(label7, gbc);

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
        frame.add(importPriceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label5, gbc);

        gbc.gridx = 1;
        frame.add(sellPriceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label6, gbc);

        gbc.gridx = 1;
        frame.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label7, gbc);

        gbc.gridx = 1;
        frame.add(statusField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        frame.add(new JLabel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
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
        importPriceField.setPreferredSize(fieldDimension);
        importPriceField.setFont(textFieldFont);
        sellPriceField.setPreferredSize(fieldDimension);
        sellPriceField.setFont(textFieldFont);
        descriptionField.setPreferredSize(fieldDimension);
        descriptionField.setFont(textFieldFont);
        statusField.setPreferredSize(fieldDimension);
        descriptionField.setFont(textFieldFont);

        statusField.setFont(textFieldFont);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
private boolean validateInputs() {
    String code = codeField.getText();
    String name = nameField.getText();
    String unit = unitField.getText();
    String importPriceStr = importPriceField.getText();
    String sellPriceStr = sellPriceField.getText();
    String description = descriptionField.getText();
    String status = statusField.getText();

    // Kiểm tra các trường bắt buộc không được để trống
    if (code.isEmpty() || name.isEmpty() || unit.isEmpty() || importPriceStr.isEmpty()
            || sellPriceStr.isEmpty() || description.isEmpty() || status.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // Kiểm tra giá nhập là số nguyên dương
    if (!isPositiveInteger(importPriceStr)) {
        JOptionPane.showMessageDialog(frame, "Giá nhập phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // Kiểm tra giá bán là số nguyên dương
    if (!isPositiveInteger(sellPriceStr)) {
        JOptionPane.showMessageDialog(frame, "Giá bán phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // Kiểm tra trường Trạng thái chỉ chứa các ký tự chữ
    if (!isAlphaString(status)) {
        JOptionPane.showMessageDialog(frame, "Trạng thái chỉ được chứa chữ cái.", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

private boolean isAlphaString(String str) {
    return str.matches("[a-zA-Z]+");
}
}