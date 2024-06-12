package com.raven.dialog;

import com.raven.controller.FloorController;
import com.raven.model.Model_Floor;
import com.raven.swing.ButtonHover;
import com.raven.utils.DataChangeListener;

import javax.swing.*;
import java.awt.*;

public class FloorDialog {

    private JTextField codeField;
    private JTextField nameField;

    private final FloorController floorController = new FloorController();
    private final Model_Floor floor;
    private JFrame frame;
    private ButtonHover saveButton;

    public FloorDialog(String purpose, Model_Floor floor, DataChangeListener dataChangeListener) {
        this.floor = floor;

        if (purpose.equals("Add")) {
            initUI("Thêm tầng");
            saveButton.addActionListener(e -> {
                saveFloor();
                JOptionPane.showMessageDialog(null, "Thêm tầng thành công");
                frame.dispose();
                dataChangeListener.onDataChanged();
            });
        } else if (purpose.equals("Update")) {
            initUI("Cập nhật tầng");
            loadData();
            saveButton.addActionListener(e -> {
                updateFloor();
                JOptionPane.showMessageDialog(null, "Cập nhật tầng thành công");
                frame.dispose();
                dataChangeListener.onDataChanged();
            });
        }
    }

    private void loadData() {
        codeField.setText(floor.getId());
        nameField.setText(floor.getName());
    }

    private void saveFloor() {
        String code = codeField.getText();
        String name = nameField.getText();

        Model_Floor newFloor = new Model_Floor(code, name, 0);
        floorController.addFloor(newFloor);
    }

    private void updateFloor() {
        String code = codeField.getText();
        String name = nameField.getText();

        Model_Floor updatedFloor = new Model_Floor(code, name, 0);
        floorController.updateFloor(updatedFloor);
    }

    private void initUI(String purposeName) {
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 13);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        frame = new JFrame(purposeName);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        codeField = new JTextField(20);
        nameField = new JTextField(20);

        if (purposeName.equals("Thêm tầng")) {
            codeField.setText(floorController.createId());
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

        JLabel label1 = new JLabel("Mã tầng:", SwingConstants.LEFT);
        label1.setFont(labelFont);
        frame.add(label1, gbc);

        JLabel label2 = new JLabel("Tên tầng:", SwingConstants.LEFT);
        label2.setFont(labelFont);
        frame.add(label2, gbc);

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
        frame.add(new JLabel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
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

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
