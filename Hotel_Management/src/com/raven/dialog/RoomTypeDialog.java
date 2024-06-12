package com.raven.dialog;

import com.raven.controller.RoomTypeController;
import com.raven.model.Model_RoomType;
import com.raven.swing.ButtonHover;
import com.raven.utils.DataChangeListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RoomTypeDialog {

    private JTextField codeField;
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JComboBox<String> typeComboBox;
    private final RoomTypeController roomTypeController = new RoomTypeController();
    private final Model_RoomType roomType;
    private JFrame frame;
    private ButtonHover saveButton;

    public RoomTypeDialog(String purpose, Model_RoomType roomType, DataChangeListener dataChangeListener) {
        this.roomType = roomType;

        if (purpose.equals("Add")) {
            initUI("Thêm loại phòng");
            saveButton.addActionListener(e -> {
                saveTypeRoom();
                JOptionPane.showMessageDialog(null, "Thêm loại phòng thành công");
                frame.dispose();
                dataChangeListener.onDataChanged();
            });
        } else if (purpose.equals("Update")) {
            initUI("Cập nhật loại phòng");
            loadData();
            saveButton.addActionListener(e -> {
                updateTypeRoom();
                JOptionPane.showMessageDialog(null, "Cập nhật loại phòng thành công");
                frame.dispose();
                dataChangeListener.onDataChanged();
            });
        }
    }

    private void loadData() {
        codeField.setText(roomType.getId());
        nameField.setText(roomType.getName());
        descriptionField.setText(roomType.getDescription());
        priceField.setText(String.valueOf(roomType.getPrice()));
        typeComboBox.setSelectedItem(roomType.getType());
    }

    private void saveTypeRoom() {
        String code = codeField.getText();
        String name = nameField.getText();
        String description = descriptionField.getText();
        int price = Integer.parseInt(priceField.getText());
        String type = (String) typeComboBox.getSelectedItem();

        Model_RoomType newRoomType = new Model_RoomType(code, name, price, description, type);
        roomTypeController.addRoomType(newRoomType);
    }

    private void updateTypeRoom() {
        String id = roomType.getId();
        String name = nameField.getText();
        String description = descriptionField.getText();
        int price = Integer.parseInt(priceField.getText());
        String type = (String) typeComboBox.getSelectedItem();

        Model_RoomType updatedRoomType = new Model_RoomType(id, name, price, description, type);
        roomTypeController.updateRoomType(id, updatedRoomType);
    }

    private void initUI(String purposeName) {
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 13);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        frame = new JFrame(purposeName);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        codeField = new JTextField(20);
        nameField = new JTextField(20);
        descriptionField = new JTextField(20);
        priceField = new JTextField(20);
        typeComboBox = new JComboBox<>(new String[]{"SINGLE", "DOUBLE", "STANDARD", "SUITE", "TWIN", "DELUXE", "EXECUTIVE", "CONNECTINGv", "PENTHOUSE_SUITE", "HONEYMOON_SUITE"});
        typeComboBox.setEditable(true);

        codeField.setEditable(false);
        codeField.setEnabled(false);
        codeField.setText(roomTypeController.createId());

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

        JLabel label1 = new JLabel("Mã loại phòng:", SwingConstants.LEFT);
        label1.setFont(labelFont);
        frame.add(label1, gbc);

        JLabel label2 = new JLabel("Tên loại phòng:", SwingConstants.LEFT);
        label2.setFont(labelFont);
        frame.add(label2, gbc);

        JLabel label3 = new JLabel("Đơn giá:", SwingConstants.LEFT);
        label3.setFont(labelFont);
        frame.add(label3, gbc);

        JLabel label4 = new JLabel("Mô tả:", SwingConstants.LEFT);
        label4.setFont(labelFont);
        frame.add(label4, gbc);

        JLabel label5 = new JLabel("Loại phòng:", SwingConstants.LEFT);
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
        frame.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label4, gbc);

        gbc.gridx = 1;
        frame.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label5, gbc);

        gbc.gridx = 1;
        frame.add(typeComboBox, gbc);

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

        // Thiết lập kích thước và căn giữa cho JTextField và JComboBox
        Dimension fieldDimension = new Dimension(200, 30);
        codeField.setPreferredSize(fieldDimension);
        codeField.setFont(textFieldFont);
        nameField.setPreferredSize(fieldDimension);
        nameField.setFont(textFieldFont);
        priceField.setPreferredSize(fieldDimension);
        priceField.setFont(textFieldFont);
        descriptionField.setPreferredSize(fieldDimension);
        descriptionField.setFont(textFieldFont);
        typeComboBox.setPreferredSize(fieldDimension);
        typeComboBox.setFont(textFieldFont);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
