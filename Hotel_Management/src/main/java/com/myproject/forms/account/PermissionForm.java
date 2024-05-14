package com.myproject.forms.account;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import com.myproject.models.account.Model_Account;
import com.myproject.models.account.Model_Permission;
import com.myproject.models.types.PermissionStatus;
import java.util.ArrayList;

public class PermissionForm extends JFrame {

    private DefaultTableModel leftTableModel;
    private DefaultTableModel rightTableModel;
    private List<Model_Account> userList;
    private final String[] permissionStatus = {PermissionStatus.FULL_ACCESS.getStatusString(),
        PermissionStatus.VIEW_ONLY.getStatusString(),
        PermissionStatus.LOCKED.getStatusString()};
    private int previousSelectedRow = -1;
    private boolean isChangePermission = false;
    private List<Model_Permission> unmodifiedPermission;

    public PermissionForm(List<Model_Account> userList) {
        this.userList = userList;
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

        for (Model_Account user : userList) {
            leftTableModel.addRow(new Object[]{user.getUsername(), user.getFullName()});
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
                userList.get(previousSelectedRow).updatePermissions(selectedRow, permissionStatus);
            }
        });
        rightTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));

        leftTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !leftTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRow = leftTable.getSelectedRow();
                if (selectedRow != -1 && selectedRow != previousSelectedRow) {
                    if (!isChangePermission) {
                        unmodifiedPermission = new ArrayList<>();
                        List<Model_Permission> permissions = userList.get(selectedRow).getPermissions();
                        for (Model_Permission permission : permissions) {
                            Model_Permission copy = new Model_Permission(permission.getName(), permission.getPermissionStatus());
                            unmodifiedPermission.add(copy);
                        }
                    }

                    if (previousSelectedRow != -1 && isChangePermission) {
                        int option = JOptionPane.showConfirmDialog(this, "Bạn có muốn cập nhật quyền hạn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                        leftTable.setRowSelectionInterval(previousSelectedRow, previousSelectedRow);
                        if (option == JOptionPane.YES_OPTION) {
                            updatePermissionsInMongoDB(userList.get(previousSelectedRow));
                        } else {
                            // Load lại dữ liệu cũ
                            userList.get(previousSelectedRow).setPermissions(unmodifiedPermission);
                            updateRightTable(userList.get(previousSelectedRow));
                        }
                        isChangePermission = false;
                    } else {
                        previousSelectedRow = selectedRow;
                        updateRightTable(userList.get(selectedRow));
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
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(leftPanel, gbc);

        gbc.gridx = 1;
        add(rightPanel, gbc);

        // Xử lý sự kiện đóng cửa sổ
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (previousSelectedRow != -1 && isChangePermission) {
                    int option = JOptionPane.showConfirmDialog(PermissionForm.this, "Bạn có muốn cập nhữat quyền hạn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        Model_Account previousSelectedUser = userList.get(previousSelectedRow);
                        updatePermissionsInMongoDB(previousSelectedUser);
                    }
                }
                super.windowClosing(e);
            }
        });
    }

    private void updateRightTable(Model_Account user) {
        rightTableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng bên phải
        for (Model_Permission permission : user.getPermissions()) {
            rightTableModel.addRow(new Object[]{permission.getName(), permission.getPermissionStatus()});
        }
    }

    private void updatePermissionsInMongoDB(Model_Account selectedUser) {
        selectedUser.updatePermissionsInMongoDB();
    }
}
