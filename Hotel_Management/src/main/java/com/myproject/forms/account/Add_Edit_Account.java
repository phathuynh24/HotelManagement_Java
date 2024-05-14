package com.myproject.forms.account;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import com.myproject.commons.Constants;
import com.myproject.models.account.Model_Account;
import com.myproject.models.types.PermissionStatus;
import com.myproject.models.types.PermissionType;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class Add_Edit_Account extends JFrame {

    private JTextField txtUsername;
    private JTextField txtFullName;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private Model_Account userData;
    private int intdexRow;
    private Form_Account formAccount;
    private boolean isEditForm;

    public Add_Edit_Account(Form_Account formAccount, Model_Account userData, int intdexRow, boolean isEditForm) {
        this.formAccount = formAccount;
        this.userData = userData;
        this.intdexRow = intdexRow;
        this.isEditForm = isEditForm;

        String title = isEditForm ? "Chỉnh sửa người dùng" : "Thêm người dùng";
        setTitle(title);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel lblUsername = new JLabel("Tên người dùng:");
        JLabel lblFullName = new JLabel("Họ tên:");
        JLabel lblPassword = new JLabel("Mật khẩu:");
        JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu:");

        txtUsername = new JTextField(20);
        txtFullName = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtConfirmPassword = new JPasswordField(20);

        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener((ActionEvent e) -> {
            saveUserData();
        });

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener((ActionEvent e) -> {
            setVisible(false);
        });

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

        if (isEditForm) {
            txtUsername.setText(userData.getUsername());
            txtFullName.setText(userData.getFullName());
            txtPassword.setText(userData.getPassword());
            txtConfirmPassword.setText(userData.getPassword());
        }

        add(panel);

        setVisible(true);
    }

    private void saveUserData() {
        String username = txtUsername.getText();
        String fullName = txtFullName.getText();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        if (username.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu và xác nhận mật khẩu không khớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Thực hiện lưu dữ liệu vào cơ sở dữ liệu
        try (MongoClient mongoClient = MongoClients.create(Constants.MONGODB_URI)) {
            // Chọn cơ sở dữ liệu
            MongoDatabase database = mongoClient.getDatabase(Constants.DATABASE_NAME);
            // Chọn bảng
            MongoCollection<Document> collection = database.getCollection(Constants.COLLECTION_USER);

            Document document = new Document("username", username)
                    .append("fullName", fullName)
                    .append("password", password);

            if (isEditForm) {
                // Nếu là form chỉnh sửa, cập nhật thông tin người dùng
                updateUserDataInMongoDB(userData.getUserId(), username, fullName, password);
            } else {
                // Lưu trữ quyền cho mỗi permission
                Map<String, String> permissionMap = new HashMap<>();
                for (PermissionType permissionType : PermissionType.values()) {
                    permissionMap.put(permissionType.getDisplayName(), PermissionStatus.LOCKED.getStatusString());
                }
                document.append("permissions", permissionMap);
                // Thêm Document người dùng vào cơ sở dữ liệu
                collection.insertOne(document);

                // Get userId
                String userId = document.getObjectId("_id").toString();
                // Sau khi thêm thành công, cập nhật lại dữ liệu trong bảng chính
                Model_Account user = new Model_Account(userId, username, fullName, password);
                user.convertMapToPermissions(permissionMap);
                
                formAccount.addUserToListAndTable(user);
                // Sau khi xử lý xong, hiển thị thông báo thành công và đóng form
                JOptionPane.showMessageDialog(this, "Lưu thông tin người dùng thành công!");
            }
        } catch (MongoException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật người dùng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(false);
    }

    private void updateUserDataInMongoDB(String userId, String username, String fullName, String password) {
        try (MongoClient mongoClient = MongoClients.create(Constants.MONGODB_URI)) {
            // Chọn cơ sở dữ liệu
            MongoDatabase database = mongoClient.getDatabase(Constants.DATABASE_NAME);
            // Chọn bảng
            MongoCollection<Document> collection = database.getCollection(Constants.COLLECTION_USER);

            // Tạo điều kiện để xác định người dùng cần cập nhật
            Bson filter = Filters.eq("_id", new ObjectId(userId));

            // Tạo một Document mới chứa thông tin người dùng đã cập nhật
            Document updateDocument = new Document("$set", new Document("username", username)
                    .append("fullName", fullName)
                    .append("password", password));

            // Thực hiện cập nhật thông tin người dùng trong bảng
            UpdateResult updateResult = collection.updateOne(filter, updateDocument);
            if (updateResult.getModifiedCount() == 1) {
                Model_Account updatedUser = new Model_Account(userId, username, fullName, password);
                updatedUser.setPermissions(userData.getPermissions());
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
