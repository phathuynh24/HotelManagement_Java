package com.raven.dialog;

import com.raven.controller.EmployeeController;
import com.raven.model.Model_Employee;
import com.raven.swing.ButtonHover;
import com.raven.utils.DataChangeListener;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class EmployeeDialog {

    private JTextField idField;
    private JTextField idCardField;  // Added field for ID Card
    private JTextField nameField;
    private JTextField phoneField;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private JComboBox<String> positionComboBox;
    private JTextField emailField;
    private JTextField dobField;
    private JTextField statusField;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JPasswordField reenterPasswordField; // Added field for re-entering password

    private final EmployeeController employeeController = new EmployeeController();
    private final Model_Employee employee;
    private JFrame frame;
    private ButtonHover saveButton;

    public EmployeeDialog(String purpose, Model_Employee employee, DataChangeListener dataChangeListener) {
        this.employee = employee;

        if (purpose.equals("Add")) {
            initUI("Thêm nhân viên");
            saveButton.addActionListener(e -> {
                if (validatePasswords()) {
                    saveEmployee();
                    JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công");
                    frame.dispose();
                    dataChangeListener.onDataChanged();
                }
            });
        } else if (purpose.equals("Update")) {
            initUI("Cập nhật nhân viên");
            loadData();
            saveButton.addActionListener(e -> {
                if (validatePasswords()) {
                    updateEmployee();
                    JOptionPane.showMessageDialog(null, "Cập nhật nhân viên thành công");
                    frame.dispose();
                    dataChangeListener.onDataChanged();
                }
            });
        }
    }

    private void loadData() {
        idField.setText(employee.getId());
        idCardField.setText(employee.getIdCard());  // Load ID Card data
        nameField.setText(employee.getName());
        phoneField.setText(employee.getPhone());
        if (employee.getGender().equalsIgnoreCase("Nam")) {
            maleRadioButton.setSelected(true);
        } else if (employee.getGender().equalsIgnoreCase("Nữ")) {
            femaleRadioButton.setSelected(true);
        }
        emailField.setText(employee.getEmail());
        dobField.setText(employee.getDob());
        statusField.setText(employee.getStatus());
        userNameField.setText(employee.getUserName());
        passwordField.setText(employee.getPassword());
        reenterPasswordField.setText(employee.getPassword()); // Load re-enter password with the same value
    }

    private void saveEmployee() {
        String id = idField.getText();
        String idCard = idCardField.getText();  // Get ID Card data
        String name = nameField.getText();
        String phone = phoneField.getText();
        String gender = maleRadioButton.isSelected() ? "Nam" : "Nữ";
        String email = emailField.getText();
        String dob = dobField.getText();
        String status = statusField.getText();
        String userName = userNameField.getText();
        String password = new String(passwordField.getPassword());
        String position = (String) positionComboBox.getSelectedItem();

        Model_Employee newEmployee = new Model_Employee(id, name, gender, phone, email, dob, status, userName, password, position, idCard);
        employeeController.addEmployee(newEmployee);
    }

    private void updateEmployee() {
        String id = idField.getText();
        String idCard = idCardField.getText();  // Get ID Card data
        String name = nameField.getText();
        String phone = phoneField.getText();
        String gender = maleRadioButton.isSelected() ? "Nam" : "Nữ";
        String email = emailField.getText();
        String dob = dobField.getText();
        String status = statusField.getText();
        String userName = userNameField.getText();
        String password = new String(passwordField.getPassword());
        String position = (String) positionComboBox.getSelectedItem();

        Model_Employee updatedEmployee = new Model_Employee(id, name, gender, phone, email, dob, status, userName, password, position, idCard);
        employeeController.updateEmployee(id, updatedEmployee);
    }

    private void initUI(String purposeName) {
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 13);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        frame = new JFrame(purposeName);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        idField = new JTextField(20);
        idCardField = new JTextField(20);  // Added field for ID Card
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        maleRadioButton = new JRadioButton("Nam");
        femaleRadioButton = new JRadioButton("Nữ");
        positionComboBox = new JComboBox<>(new String[]{"Quản lý", "Lễ tân"});
        emailField = new JTextField(20);
        dobField = new JTextField(20);
        statusField = new JTextField(20);
        userNameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        reenterPasswordField = new JPasswordField(20); // Added field for re-entering password

        if (purposeName.equals("Thêm nhân viên")) {
            idField.setText(employeeController.createId());
        }

        idField.setEditable(false);
        idField.setEnabled(false);

        // Format date of birth field
        dobField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;

                StringBuilder text = new StringBuilder(getText(0, getLength()));
                text.insert(offs, str);

                if (text.length() > 10) return;

                if (text.length() == 2 || text.length() == 5) {
                    text.append('/');
                }

                super.insertString(offs, str, a);
            }
        });

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

        JLabel label1 = new JLabel("Mã nhân viên:", SwingConstants.LEFT);
        label1.setFont(labelFont);

        JLabel label2 = new JLabel("Tên nhân viên:", SwingConstants.LEFT);
        label2.setFont(labelFont);

        JLabel label11 = new JLabel("CCCD:", SwingConstants.LEFT);  // Added label for ID Card
        label11.setFont(labelFont);

        JLabel label3 = new JLabel("Số điện thoại:", SwingConstants.LEFT);
        label3.setFont(labelFont);

        JLabel label4 = new JLabel("Giới tính:", SwingConstants.LEFT);
        label4.setFont(labelFont);

        JLabel label5 = new JLabel("Chức vụ:", SwingConstants.LEFT);
        label5.setFont(labelFont);

        JLabel label6 = new JLabel("Email:", SwingConstants.LEFT);
        label6.setFont(labelFont);

        JLabel label7 = new JLabel("Ngày sinh:", SwingConstants.LEFT);
        label7.setFont(labelFont);

        JLabel label8 = new JLabel("Trạng thái:", SwingConstants.LEFT);
        label8.setFont(labelFont);

        JLabel label9 = new JLabel("Tên đăng nhập:", SwingConstants.LEFT);
        label9.setFont(labelFont);

        JLabel label10 = new JLabel("Mật khẩu:", SwingConstants.LEFT);
        label10.setFont(labelFont);

        JLabel label12 = new JLabel("Nhập lại mật khẩu:", SwingConstants.LEFT);  // Added label for re-entering password
        label12.setFont(labelFont);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label1, gbc);

        gbc.gridx = 1;
        frame.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label2, gbc);

        gbc.gridx = 1;
        frame.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label11, gbc);  // Added label for ID Card

        gbc.gridx = 1;
        frame.add(idCardField, gbc);  // Added field for ID Card

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label3, gbc);

        gbc.gridx = 1;
        frame.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label4, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        frame.add(genderPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label5, gbc);

        gbc.gridx = 1;
        frame.add(positionComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label6, gbc);

        gbc.gridx = 1;
        frame.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label7, gbc);

        gbc.gridx = 1;
        frame.add(dobField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label8, gbc);

        gbc.gridx = 1;
        frame.add(statusField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label9, gbc);

        gbc.gridx = 1;
        frame.add(userNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label10, gbc);

        gbc.gridx = 1;
        frame.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        frame.add(label12, gbc);  // Added label for re-entering password

        gbc.gridx = 1;
        frame.add(reenterPasswordField, gbc);  // Added field for re-entering password

        gbc.gridx = 0;
        gbc.gridy = 12;
        frame.add(new JLabel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 13;
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
        idField.setPreferredSize(fieldDimension);
        idField.setFont(textFieldFont);
        idCardField.setPreferredSize(fieldDimension);  // Added field for ID Card
        idCardField.setFont(textFieldFont);  // Added field for ID Card
        nameField.setPreferredSize(fieldDimension);
        nameField.setFont(textFieldFont);
        phoneField.setPreferredSize(fieldDimension);
        phoneField.setFont(textFieldFont);
        positionComboBox.setPreferredSize(fieldDimension);
        positionComboBox.setFont(textFieldFont);
        emailField.setPreferredSize(fieldDimension);
        emailField.setFont(textFieldFont);
        dobField.setPreferredSize(fieldDimension);
        dobField.setFont(textFieldFont);
        statusField.setPreferredSize(fieldDimension);
        statusField.setFont(textFieldFont);
        userNameField.setPreferredSize(fieldDimension);
        userNameField.setFont(textFieldFont);
        passwordField.setPreferredSize(fieldDimension);
        passwordField.setFont(textFieldFont);
        reenterPasswordField.setPreferredSize(fieldDimension);
        reenterPasswordField.setFont(textFieldFont);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private boolean validatePasswords() {
        String password = new String(passwordField.getPassword());
        String reenterPassword = new String(reenterPasswordField.getPassword());

        if (!password.equals(reenterPassword)) {
            JOptionPane.showMessageDialog(frame, "Mật khẩu và nhập lại mật khẩu không khớp", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
