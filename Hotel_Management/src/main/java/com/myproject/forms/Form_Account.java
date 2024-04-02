package com.myproject.forms;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.myproject.swings.ScrollBar;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class Form_Account extends javax.swing.JPanel {

    private List<User> userList = new ArrayList<>();
    private DefaultTableModel tableModel;
    private AddUserForm addUserForm;
    private EditUserForm editUserForm;

    public Form_Account() {
        initComponents();
        customize();
        fetchDataFromMongoDB();
        displayUser();
    }

    public void fetchDataFromMongoDB() {
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/")) {
            // Chọn cơ sở dữ liệu
            MongoDatabase database = mongoClient.getDatabase("Hotel_Management");
            // Chọn bảng
            MongoCollection<Document> collection = database.getCollection("User");
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
                User user = new User(value0, value1, value2, value3);
                addUserToListAndTable(user);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void addUserToListAndTable(User user) {
        // Thêm người dùng vào danh sách
        userList.add(user);
        // Thêm dòng mới vào bảng
        tableModel.addRow(new Object[]{false, user.getUsername(), user.getFullName()});
    }

    public void updateUserToListAndTable(User updatedUser, int rowIndex) {
        // Cập nhật thông tin người dùng trong danh sách
        userList.set(rowIndex, updatedUser);

        // Cập nhật thôngupdateUserToListAndTable tin người dùng trong bảng
        table.setValueAt(updatedUser.getUsername(), rowIndex, 1);
        table.setValueAt(updatedUser.getUsername(), rowIndex, 1);
        table.setValueAt(updatedUser.getFullName(), rowIndex, 2);
        table.repaint();
    }

    public void deleteUserDataInMongoDB(String userId) {
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/")) {
            // Chọn cơ sở dữ liệu
            MongoDatabase database = mongoClient.getDatabase("Hotel_Management");
            // Chọn bảng
            MongoCollection<Document> collection = database.getCollection("User");

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

    private void displayUser() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Kiểm tra nếu là double click
                    int column = table.columnAtPoint(e.getPoint()); // Lấy cột được click
                    if (column != 0) { // Kiểm tra xem cột được click có phải là cột đầu tiên không
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            User selectedUser = userList.get(selectedRow);

                            // Hiển thị form chỉnh sửa thông tin người dùng
                            if (editUserForm == null || !editUserForm.isVisible()) {
                                // Nếu form thêm chưa được mở hoặc đã bị đóng, thì tạo một form mới
                                editUserForm = new EditUserForm(Form_Account.this, selectedUser, selectedRow);
                            } else {
                                // Nếu form thêm đã được mở, đưa nó ra phía trước
                                editUserForm.toFront();
                            }
                            editUserForm.setVisible(true);

                        }
                    }
                }
            }
        });
    }

    private void customize() {
        spTable.setVerticalScrollBar(new ScrollBar());
        spTable.getVerticalScrollBar().setBackground(Color.WHITE);
        spTable.getViewport().setBackground(Color.WHITE);
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);

        // Add checkbox column
        int checkboxColumnIndex = 0;
        table.getColumnModel().getColumn(checkboxColumnIndex).setCellRenderer(new CheckboxRenderer());
        table.getColumnModel().getColumn(checkboxColumnIndex).setCellEditor(new CheckboxEditor(new JCheckBox()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBorder1 = new com.myproject.swings.PanelBorder();
        spTable = new javax.swing.JScrollPane();
        table = new com.myproject.swings.Table();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(476, 349));

        panelBorder1.setBackground(new java.awt.Color(255, 255, 255));

        spTable.setBorder(null);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Xóa", "Tài khoản", "Họ tên"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spTable.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMinWidth(40);
            table.getColumnModel().getColumn(0).setPreferredWidth(40);
            table.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(spTable, javax.swing.GroupLayout.PREFERRED_SIZE, 839, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(spTable, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jButton1.setText("Thêm user");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/myproject/icons/10.png"))); // NOI18N
        jButton2.setText("Phân quyền");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Xóa user");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Sửa user");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

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
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (addUserForm == null || !addUserForm.isVisible()) {
            // Nếu form thêm chưa được mở hoặc đã bị đóng, thì tạo một form mới
            addUserForm = new AddUserForm(this);
        } else {
            // Nếu form thêm đã được mở, đưa nó ra phía trước
            addUserForm.toFront();
        }
        addUserForm.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Lấy các hàng đã chọn
        List<Integer> selectedRows = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            Boolean selected = (Boolean) table.getValueAt(i, 0);
            if (selected != null && selected) {
                selectedRows.add(i);
            }
        }

        // Xóa các hàng đã chọn
        Collections.reverse(selectedRows); // Đảo ngược danh sách các hàng đã chọn để tránh lỗi khi xóa
        for (int rowIndex : selectedRows) {
            String userId = userList.get(rowIndex).getUserId();
            userList.remove(rowIndex);
            tableModel.removeRow(rowIndex);
            deleteUserDataInMongoDB(userId);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private com.myproject.swings.PanelBorder panelBorder1;
    private javax.swing.JScrollPane spTable;
    private com.myproject.swings.Table table;
    // End of variables declaration//GEN-END:variables
}

// Renderer class for rendering checkboxes
class CheckboxRenderer extends DefaultTableCellRenderer {

    private JCheckBox checkbox;

    public CheckboxRenderer() {
        checkbox = new JCheckBox();
        checkbox.setHorizontalAlignment(JCheckBox.CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            checkbox.setSelected((boolean) value);
        }
        return checkbox;
    }
}

// Editor class for interacting with checkboxes
class CheckboxEditor extends DefaultCellEditor {

    private JCheckBox checkbox;

    public CheckboxEditor(JCheckBox checkbox) {
        super(checkbox);
        this.checkbox = checkbox;
        checkbox.setHorizontalAlignment(JCheckBox.CENTER);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value != null) {
            checkbox.setSelected((boolean) value);
        }
        return checkbox;
    }
}

class AddUserForm extends JFrame {

    private JTextField txtUsername;
    private JTextField txtFullName;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private Form_Account formAccount;

    public AddUserForm(Form_Account formAccount) {
        this.formAccount = formAccount;

        setResizable(false);
        setTitle("Thêm người dùng");
        setSize(300, 200);
        setLocationRelativeTo(null);

        // Tạo các label
        JLabel lblUsername = new JLabel("Tên người dùng:");
        JLabel lblFullName = new JLabel("Họ tên:");
        JLabel lblPassword = new JLabel("Mật khẩu:");
        JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu:");

        // Tạo các trường nhập liệu
        txtUsername = new JTextField(20);
        txtFullName = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtConfirmPassword = new JPasswordField(20);

        JButton btnAdd = new JButton("Thêm");
        btnAdd.addActionListener((ActionEvent e) -> {
            String username = txtUsername.getText();
            String fullName = txtFullName.getText();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());

            if (username.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(AddUserForm.this, "Vui lòng điền đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(AddUserForm.this, "Mật khẩu và xác nhận mật khẩu không khớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lưu dữ liệu lên mongodb
            try (
                    MongoClient mongoClient = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/")) {
                // Chọn cơ sở dữ liệu
                MongoDatabase database = mongoClient.getDatabase("Hotel_Management");
                // Chọn bảng
                MongoCollection<Document> collection = database.getCollection("User");
                // Tạo một Document mới chứa dữ liệu bạn muốn thêm
                Document document = new Document("username", username)
                        .append("fullName", fullName)
                        .append("password", password);
                // Thêm Document vào bảng
                collection.insertOne(document);
                // Get userId
                String userId = document.getObjectId("_id").toString();
                // Sau khi thêm thành công, cập nhật lại dữ liệu trong bảng chính
                User user = new User(userId, username, fullName, password);
                formAccount.addUserToListAndTable(user);
            } catch (MongoException ex) {
                JOptionPane.showMessageDialog(AddUserForm.this, "Lỗi khi cập nhật người dùng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            // Sau khi xử lý xong, có thể hiển thị thông báo thành công
            JOptionPane.showMessageDialog(AddUserForm.this, "Thêm người dùng thành công!");

            setVisible(false);
        });

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener((ActionEvent e) -> {
            setVisible(false);
        });

        // Tạo panel và thêm các thành phần vào panel
        JPanel panel1 = new JPanel(new GridLayout(5, 2));
        panel1.add(lblUsername);
        panel1.add(txtUsername);
        panel1.add(lblFullName);
        panel1.add(txtFullName);
        panel1.add(lblPassword);
        panel1.add(txtPassword);
        panel1.add(lblConfirmPassword);
        panel1.add(txtConfirmPassword);
        panel1.add(btnAdd);
        panel1.add(btnClose);

        // Thêm panel vào frame
        add(panel1);

        setVisible(true);
    }
}

class EditUserForm extends JFrame {

    private JTextField txtUsername;
    private JTextField txtFullName;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private User userData;
    private int intdexRow;
    private Form_Account formAccount;

    public EditUserForm(Form_Account formAccount, User userData, int intdexRow) {
        this.userData = userData;
        this.intdexRow = intdexRow;
        this.formAccount = formAccount;

        setResizable(false);
        setTitle("Chỉnh sửa người dùng");
        setSize(300, 200);
        setLocationRelativeTo(null);

        // Tạo các label
        JLabel lblUsername = new JLabel("Tên người dùng:");
        JLabel lblFullName = new JLabel("Họ tên:");
        JLabel lblPassword = new JLabel("Mật khẩu:");
        JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu:");

        // Tạo các trường nhập liệu và điền thông tin người dùng cần chỉnh sửa
        txtUsername = new JTextField(20);
        txtUsername.setText(userData.getUsername());
        txtFullName = new JTextField(20);
        txtFullName.setText(userData.getFullName());
        txtPassword = new JPasswordField(20);
        txtPassword.setText(userData.getPassword());
        txtConfirmPassword = new JPasswordField(20);
        txtConfirmPassword.setText(userData.getPassword());

        // Tạo nút "Lưu"
        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener((ActionEvent e) -> {
            String username = txtUsername.getText();
            String fullName = txtFullName.getText();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());

            if (username.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(EditUserForm.this, "Vui lòng điền đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(EditUserForm.this, "Mật khẩu và xác nhận mật khẩu không khớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Thực hiện cập nhật thông tin người dùng trong cơ sở dữ liệu
            updateUserDataInMongoDB(userData.getUserId(), username, fullName, password);
        });

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener((ActionEvent e) -> {
            setVisible(false);
        });

        // Tạo panel và thêm các thành phần vào panel
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblFullName);
        panel.add(txtFullName);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(lblConfirmPassword);
        panel.add(txtConfirmPassword);
        panel.add(btnSave);
        panel.add(btnClose);

        // Thêm panel vào frame
        add(panel);

        setVisible(true);
    }

    public void updateUserDataInMongoDB(String userId, String username, String fullName, String password) {
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/")) {
            // Chọn cơ sở dữ liệu
            MongoDatabase database = mongoClient.getDatabase("Hotel_Management");
            // Chọn bảng
            MongoCollection<Document> collection = database.getCollection("User");

            // Tạo điều kiện để xác định người dùng cần cập nhật
            Bson filter = Filters.eq("_id", new ObjectId(userId));

            // Tạo một Document mới chứa thông tin người dùng đã cập nhật
            Document updateDocument = new Document("$set", new Document("username", username)
                    .append("fullName", fullName)
                    .append("password", password));

            // Thực hiện cập nhật thông tin người dùng trong bảng
            UpdateResult updateResult = collection.updateOne(filter, updateDocument);
            if (updateResult.getModifiedCount() == 1) {
                User updatedUser = new User(userId, username, fullName, password);
                formAccount.updateUserToListAndTable(updatedUser, intdexRow);
                System.out.println("Cập nhật người dùng thành công!");
            } else {
                System.out.println("Không tìm thấy người dùng để cập nhật.");
            }
        } catch (Exception ex) {
            System.err.println("Lỗi khi cập nhật người dùng: " + ex.getMessage());
        }
        setVisible(false);
    }
}

class User {

    private String userId;
    private String username;
    private String fullName;
    private String password;

    public User(String userId, String username, String fullName, String password) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{"
                + "username='" + username + '\''
                + ", fullName='" + fullName + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
