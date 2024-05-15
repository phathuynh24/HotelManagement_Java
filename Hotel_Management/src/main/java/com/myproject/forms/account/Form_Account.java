package com.myproject.forms.account;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.myproject.commons.Constants;
import com.myproject.models.account.Model_Account;
import com.myproject.models.account.Model_Permission;
import com.myproject.models.types.PermissionStatus;
import com.myproject.swings.ScrollBar;
import com.myproject.swings.SearchText;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class Form_Account extends javax.swing.JPanel {

    private List<Model_Account> userList = new ArrayList<>();
    private DefaultTableModel tableModel;
    private Add_Edit_Account addAndEditForm;
    private JTextField searchField;
    private DefaultTableModel leftTableModel;
    private DefaultTableModel rightTableModel;
    private final String[] permissionTypes = {PermissionStatus.FULL_ACCESS.getStatusString(),
        PermissionStatus.VIEW_ONLY.getStatusString(),
        PermissionStatus.LOCKED.getStatusString()};

    public Form_Account() {
        initComponents();
        fetchDataFromMongoDB();
    }

    public void fetchDataFromMongoDB() {
        try (MongoClient mongoClient = MongoClients.create(Constants.MONGODB_URI)) {
            // Chọn cơ sở dữ liệu
            MongoDatabase database = mongoClient.getDatabase(Constants.DATABASE_NAME);
            // Chọn bảng
            MongoCollection<Document> collection = database.getCollection(Constants.COLLECTION_USER);
            FindIterable<Document> cursor = collection.find();
            tableModel = (DefaultTableModel) table.getModel();
            // Clear data
            tableModel.setRowCount(0);
            userList.clear();

            for (Document document : cursor) {
                String value0 = document.getObjectId("_id").toString();
                String value1 = document.getString("username");
                String value2 = document.getString("fullName");
                String value3 = document.getString("password");

                Model_Account user = new Model_Account(value0, value1, value2, value3);

                // Lấy thông tin permissions từ document
                Document permissionsDocument = (Document) document.get("permissions");
                for (Map.Entry<String, Object> entry : permissionsDocument.entrySet()) {
                    String permissionName = entry.getKey();
                    String permissionStatus = (String) entry.getValue();

                    // Tạo đối tượng Model_Permission và thêm vào Model_Account
                    Model_Permission permission = new Model_Permission(permissionName, permissionStatus);
                    user.addPermission(permission);
                }

                addUserToListAndTable(user);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void addUserToListAndTable(Model_Account user) {
        // Thêm người dùng vào danh sách
        userList.add(user);
        // Thêm dòng mới vào bảng
        tableModel.addRow(new Object[]{false, user.getUsername(), user.getFullName()});
    }

    public void updateUserToListAndTable(Model_Account updatedUser, int rowIndex) {
        // Cập nhật thông tin người dùng trong danh sách
        userList.set(rowIndex, updatedUser);

        // Cập nhật thôngupdateUserToListAndTable tin người dùng trong bảng
        table.setValueAt(updatedUser.getUsername(), rowIndex, 1);
        table.setValueAt(updatedUser.getUsername(), rowIndex, 1);
        table.setValueAt(updatedUser.getFullName(), rowIndex, 2);
        table.repaint();
    }

    public void deleteUserDataInMongoDB(String userId) {
        try (MongoClient mongoClient = MongoClients.create(Constants.MONGODB_URI)) {
            // Chọn cơ sở dữ liệu
            MongoDatabase database = mongoClient.getDatabase(Constants.DATABASE_NAME);
            // Chọn bảng
            MongoCollection<Document> collection = database.getCollection(Constants.COLLECTION_USER);

            // Tạo điều kiện để xác định người dùng cần xóa
            Bson filter = Filters.eq("_id", new ObjectId(userId));

            // Thực hiện xóa người dùng
            DeleteResult deleteResult = collection.deleteOne(filter);
            if (deleteResult.getDeletedCount() == 1) {
                System.out.println("Xóa người dùng thành công!");
            } else {
                System.out.println("Không tìm thấy người dùng để xóa.");
            }
        } catch (Exception ex) {
            System.err.println("Lỗi khi xóa người dùng: " + ex.getMessage());
        }
    }

    private void editUserDataInMongoDB() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Kiểm tra nếu là double click
                    int column = table.columnAtPoint(e.getPoint()); // Lấy cột được click
                    if (column != 0) { // Kiểm tra xem cột được click có phải là cột đầu tiên không
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            Model_Account selectedUser = userList.get(selectedRow);

                            // Hiển thị form chỉnh sửa thông tin người dùng
                            if (addAndEditForm == null || !addAndEditForm.isVisible()) {
                                // Nếu form thêm chưa được mở hoặc đã bị đóng, thì tạo một form mới
                                addAndEditForm = new Add_Edit_Account(Form_Account.this, selectedUser, selectedRow, true);
                            } else {
                                // Nếu form thêm đã được mở, đưa nó ra phía trước
                                addAndEditForm.toFront();
                            }
                            addAndEditForm.setVisible(true);
                        }
                    }
                }
            }
        });
    }

    private void initComponents() {

        searchField = new SearchText();
        panelBorder1 = new com.myproject.swings.PanelBorder();
        spTable = new javax.swing.JScrollPane();
        table = new com.myproject.swings.Table();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1.setBackground(new java.awt.Color(28, 181, 224));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setBackground(new java.awt.Color(28, 181, 224));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setBackground(new java.awt.Color(28, 181, 224));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));

        setPreferredSize(new java.awt.Dimension(476, 349));

        panelBorder1.setBackground(new java.awt.Color(255, 255, 255));

        spTable.setBorder(null);

        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Xóa", "Tài khoản", "Họ tên"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean[]{
                true, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        spTable.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMinWidth(40);
            table.getColumnModel().getColumn(0).setPreferredWidth(40);
            table.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        spTable.setVerticalScrollBar(new ScrollBar());
        spTable.getVerticalScrollBar().setBackground(Color.WHITE);
        spTable.getViewport().setBackground(Color.WHITE);
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);

        int checkboxColumnIndex = 0; // Chỉ số cột chứa checkbox
        // Sử dụng CheckboxRenderer để hiển thị checkbox trong cột
        table.getColumnModel().getColumn(checkboxColumnIndex).setCellRenderer(new CheckboxCellHandler.CheckboxRenderer());
        // Sử dụng CheckboxEditor để tương tác với checkbox trong cột
        table.getColumnModel().getColumn(checkboxColumnIndex).setCellEditor(new CheckboxCellHandler.CheckboxEditor(new JCheckBox()));

        // Cho phép click vào hàng muốn chỉnh sửa thông tin
        editUserDataInMongoDB();

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
                panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelBorder1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(spTable, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelBorder1Layout.setVerticalGroup(
                panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                                .addContainerGap(20, Short.MAX_VALUE)
                                .addComponent(spTable, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))
        );

        jButton1.setText("Thêm user");
        jButton1.addActionListener((java.awt.event.ActionEvent evt) -> {
            jButton1ActionPerformed(evt);
        });

        jButton2.setText("Phân quyền");
        jButton2.addActionListener((java.awt.event.ActionEvent evt) -> {
            jButton2ActionPerformed(evt);
        });

        jButton3.setText("Xóa user");
        jButton3.addActionListener((java.awt.event.ActionEvent evt) -> {
            jButton3ActionPerformed(evt);
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)
                                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addContainerGap())
        );

        jPanel1.setPreferredSize(new java.awt.Dimension(920, jPanel1.getPreferredSize().height));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (addAndEditForm == null || !addAndEditForm.isVisible()) {
            // Nếu form thêm chưa được mở hoặc đã bị đóng, thì tạo một form mới
            addAndEditForm = new Add_Edit_Account(this, null, -1, false);
        } else {
            // Nếu form thêm đã được mở, đưa nó ra phía trước
            addAndEditForm.toFront();
        }
        addAndEditForm.setVisible(true);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        // Hiển thị form mới khi click vào nút "Phân quyền user"
        SwingUtilities.invokeLater(() -> {
            new PermissionForm(userList).setVisible(true);
        });
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        // Lấy các hàng đã chọn
        List<Integer> selectedRows = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            Boolean selected = (Boolean) table.getValueAt(i, 0);
            if (selected != null && selected) {
                selectedRows.add(i);
            }
        }

        // Xóa các hàng đã chọn
        Collections.reverse(selectedRows);
        // Đảo ngược danh sách các hàng đã chọn để tránh lỗi khi xóa
        for (int rowIndex : selectedRows) {
            String userId = userList.get(rowIndex).getUserId();
            userList.remove(rowIndex);
            tableModel.removeRow(rowIndex);
            deleteUserDataInMongoDB(userId);
        }
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private com.myproject.swings.PanelBorder panelBorder1;
    private javax.swing.JScrollPane spTable;
    private com.myproject.swings.Table table;
}
