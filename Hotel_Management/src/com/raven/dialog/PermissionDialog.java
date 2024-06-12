package com.raven.dialog;

import com.raven.controller.EmployeeController;
import com.raven.model.Model_Employee;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import com.raven.model.Model_Permission;
import com.raven.utils.PermissionStatus;
import java.util.ArrayList;

public class PermissionDialog extends JFrame {

    private DefaultTableModel leftTableModel;
    private DefaultTableModel rightTableModel;
    private List<Model_Employee> employees;
    private final String[] permissionStatus = {PermissionStatus.FULL_ACCESS.getStatusString(),
        PermissionStatus.LOCKED.getStatusString()};
    private int previousSelectedRow = -1;
    private boolean isChangePermission = false;
    private List<Model_Permission> unmodifiedPermission;
    private final EmployeeController employeeController = new EmployeeController();
    private JButton saveButton;

    public PermissionDialog(List<Model_Employee> userList) {
        this.employees = userList;
        initComponents();
    }

    private void initComponents() {
        setTitle("Phân quyền user");
        setSize(800, 400);
        setLocationRelativeTo(null);

        String[] leftColumnNames = {"Tài khoản", "Họ tên"};
        leftTableModel = new DefaultTableModel(null, leftColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable leftTable = new JTable(leftTableModel);

        for (Model_Employee user : employees) {
            leftTableModel.addRow(new Object[]{user.getUserName(), user.getName()});
        }

        String[] rightColumnNames = {"Chức năng", "Quyền"};
        rightTableModel = new DefaultTableModel(null, rightColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        JTable rightTable = new JTable(rightTableModel);

        JComboBox<String> comboBox = new JComboBox<>(permissionStatus);
        comboBox.addActionListener(e -> {
            int selectedRow = rightTable.getSelectedRow();
            if (selectedRow != -1) {
                // Lấy thông tin trạng thái quyền hạn từ bảng bên phải
                isChangePermission = true;
                String permissionStatus = (String) rightTableModel.getValueAt(selectedRow, 1);
                employees.get(previousSelectedRow).updatePermissions(selectedRow, permissionStatus);
            }
        });
        rightTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));

        leftTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !leftTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRow = leftTable.getSelectedRow();
                if (selectedRow != -1 && selectedRow != previousSelectedRow) {
                    if (!isChangePermission) {
                        unmodifiedPermission = new ArrayList<>();
                        List<Model_Permission> permissions = employees.get(selectedRow).getPermissions();
                        for (Model_Permission permission : permissions) {
                            Model_Permission copy = new Model_Permission(permission.getName(), permission.getPermissionStatus());
                            unmodifiedPermission.add(copy);
                        }
                    }

                    if (previousSelectedRow != -1 && isChangePermission) {
                        int option = JOptionPane.showConfirmDialog(this, "Bạn có muốn cập nhật quyền hạn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                        leftTable.setRowSelectionInterval(previousSelectedRow, previousSelectedRow);
                        if (option == JOptionPane.YES_OPTION) {
                            updatePermissionsInMongoDB(employees.get(previousSelectedRow));
                        } else {
                            // Load lại dữ liệu cũ
                            employees.get(previousSelectedRow).setPermissions(unmodifiedPermission);
                            updateRightTable(employees.get(previousSelectedRow));
                        }
                        isChangePermission = false;
                    } else {
                        previousSelectedRow = selectedRow;
                        updateRightTable(employees.get(selectedRow));
                    }
                }
            }
        });

        leftTable.setPreferredScrollableViewportSize(new Dimension(400, 300));
        rightTable.setPreferredScrollableViewportSize(new Dimension(400, 300));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JScrollPane(leftTable), BorderLayout.CENTER);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Danh sách tài khoản"));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JScrollPane(rightTable), BorderLayout.CENTER);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Danh sách quyền hạn"));

        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        add(rightPanel, gbc);

        // Thêm nút "Lưu"
        saveButton = new JButton("Lưu");
        saveButton.addActionListener(e -> {
            int selectedRow = leftTable.getSelectedRow();
            if (selectedRow != -1 && isChangePermission) {
                Model_Employee previousSelectedUser = employees.get(previousSelectedRow);
                updatePermissionsInMongoDB(previousSelectedUser);
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.SOUTH;
        add(saveButton, gbc);

        // Xử lý sự kiện khi đóng cửa sổ
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isChangePermission) {
                    int option = JOptionPane.showConfirmDialog(PermissionDialog.this, "Bạn có muốn cập nhật quyền hạn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        updatePermissionsInMongoDB(employees.get(previousSelectedRow));
                    }
                }
                dispose();
            }
        });
    }

    private void updateRightTable(Model_Employee selectedUser) {
        rightTableModel.setRowCount(0);
        List<Model_Permission> permissions = selectedUser.getPermissions();
        for (Model_Permission permission : permissions) {
            rightTableModel.addRow(new Object[]{permission.getName(), permission.getPermissionStatus()});
        }
    }

    private void updatePermissionsInMongoDB(Model_Employee selectedUser) {
        employeeController.updatePermissions(selectedUser.getId(), selectedUser.convertPermissionsToMap());
        isChangePermission = false;
    }

    
}