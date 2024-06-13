package com.raven.dialog;

import com.raven.controller.RoomController;
import com.raven.model.Model_Room;
import com.raven.swing.ButtonHover;
import com.raven.utils.DataChangeListener;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.Objects;

public class RoomDialog {

    private JTextField codeField;
    private JTextField nameField;
    private JTextField capacityField;
    private JComboBox<String> typeComboBox;
    private JComboBox<String> floorComboBox;

    private final RoomController roomController = new RoomController();
    private final Model_Room room;
    private JFrame frame;
    private ButtonHover saveButton;

    public RoomDialog(String purpose, Model_Room room, DataChangeListener dataChangeListener) {
        this.room = room;

        if (purpose.equals("Add")) {
            initUI("Thêm phòng");
            saveButton.addActionListener(e -> {
                if (validateInputs()) {
                    saveRoom();
                    JOptionPane.showMessageDialog(null, "Thêm phòng thành công");
                    frame.dispose();
                    dataChangeListener.onDataChanged();
                }
            });
        } else if (purpose.equals("Update")) {
            initUI("Cập nhật phòng");
            loadData();
            saveButton.addActionListener(e -> {
                if (validateInputs()) {
                    updateRoom();
                    JOptionPane.showMessageDialog(null, "Cập nhật phòng thành công");
                    frame.dispose();
                    dataChangeListener.onDataChanged();
                }
            });
        }
    }

    private void loadData() {
        codeField.setText(room.getId());
        nameField.setText(room.getName());
        capacityField.setText(String.valueOf(room.getCapacity()));
        floorComboBox.setSelectedItem(room.getFloor());
        typeComboBox.setSelectedItem(room.getType());
    }

    private void saveRoom() {
        String code = codeField.getText();
        String name = nameField.getText();
        int capacity = Integer.parseInt(capacityField.getText());
        String floor = Objects.requireNonNull(floorComboBox.getSelectedItem()).toString();
        String type = Objects.requireNonNull(typeComboBox.getSelectedItem()).toString();

        Model_Room newRoom = new Model_Room(code, name, type, floor, capacity);
        roomController.addRoom(newRoom);
    }

    private void updateRoom() {
        String code = codeField.getText();
        String name = nameField.getText();
        int capacity = Integer.parseInt(capacityField.getText());
        String floor = Objects.requireNonNull(floorComboBox.getSelectedItem()).toString();
        String type = Objects.requireNonNull(typeComboBox.getSelectedItem()).toString();

        Model_Room updatedRoom = new Model_Room(code, name, type, floor, capacity);
        roomController.updateRoom(code, updatedRoom);
    }

    private void initUI(String purposeName) {
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 13);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        frame = new JFrame(purposeName);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        codeField = new JTextField(20);
        nameField = new JTextField(20);
        capacityField = new JTextField(20);
        typeComboBox = new JComboBox<>(new String[]{"SINGLE", "DOUBLE", "STANDARD", "SUITE", "TWIN", "DELUXE", "EXECUTIVE", "CONNECTING", "PENTHOUSE_SUITE", "HONEYMOON_SUITE"});
        floorComboBox = new JComboBox<>(new String[]{"Tầng 1", "Tầng 2", "Tầng 3", "Tầng 4", "Tầng 5"});

        if (purposeName.equals("Thêm phòng")) {
            floorComboBox.addActionListener(e -> codeField.setText(roomController.createId((String) floorComboBox.getSelectedItem())));
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

        JLabel label1 = new JLabel("Mã phòng:", SwingConstants.LEFT);
        label1.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label1, gbc);

        gbc.gridx = 1;
        frame.add(codeField, gbc);

        JLabel label2 = new JLabel("Tên phòng:", SwingConstants.LEFT);
        label2.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label2, gbc);

        gbc.gridx = 1;
        frame.add(nameField, gbc);

        JLabel label3 = new JLabel("Sức chứa:", SwingConstants.LEFT);
        label3.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label3, gbc);

        gbc.gridx = 1;
        frame.add(capacityField, gbc);

        JLabel label4 = new JLabel("Tầng:", SwingConstants.LEFT);
        label4.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label4, gbc);

        gbc.gridx = 1;
        frame.add(floorComboBox, gbc);

        JLabel label5 = new JLabel("Loại phòng:", SwingConstants.LEFT);
        label5.setFont(labelFont);
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

        // Thiết lập màu nền cho JFrame và JPanel
        frame.getContentPane().setBackground(Color.WHITE);
        buttonPanel.setBackground(Color.WHITE);

        saveButton.setFont(buttonFont);
        cancelButton.setFont(buttonFont);

        // Thiết lập kích thước và căn giữa cho các thành phần
        Dimension fieldDimension = new Dimension(200, 30);
        codeField.setPreferredSize(fieldDimension);
        codeField.setFont(textFieldFont);
        nameField.setPreferredSize(fieldDimension);
        nameField.setFont(textFieldFont);
        capacityField.setPreferredSize(fieldDimension);
        capacityField.setFont(textFieldFont);
        floorComboBox.setPreferredSize(fieldDimension);
        floorComboBox.setFont(textFieldFont);
        typeComboBox.setPreferredSize(fieldDimension);
        typeComboBox.setFont(textFieldFont);

        // Thiết lập ràng buộc cho capacityField chỉ nhập số
        capacityField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;
                if (str.matches("[0-9]*")) {
                    super.insertString(offs, str, a);
                }
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private boolean validateInputs() {
        if (nameField.getText().isEmpty() || capacityField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Vui lòng điền đầy đủ thông tin cho tên phòng và sức chứa.");
            return false;
        }

        try {
            Integer.parseInt(capacityField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Sức chứa phòng phải là một số nguyên.");
            return false;
        }

        return true;
    }
}
