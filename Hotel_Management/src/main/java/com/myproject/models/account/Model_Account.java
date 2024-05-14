package com.myproject.models.account;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.myproject.commons.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Model_Account {

    private String userId;
    private String username;
    private String fullName;
    private String password;
    private List<Model_Permission> permissions;

    public Model_Account(String userId, String username, String fullName, String password) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.permissions = new ArrayList<>();
    }

    // Getter và Setter cho thuộc tính userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter và Setter cho thuộc tính username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter và Setter cho thuộc tính fullName
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter và Setter cho thuộc tính password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter và Setter cho thuộc tính permissions
    public List<Model_Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Model_Permission> permissions) {
        this.permissions = new ArrayList<>();
        for (Model_Permission permission : permissions) {
            Model_Permission copy = new Model_Permission(permission.getName(), permission.getPermissionStatus());
            this.permissions.add(copy);
        }
    }

    public void addPermission(Model_Permission permission) {
        permissions.add(permission);
    }

    public void convertMapToPermissions(Map<String, String> permissionMap) {
        for (Map.Entry<String, String> entry : permissionMap.entrySet()) {
            String name = entry.getKey();
            String permissionType = entry.getValue();
            permissions.add(new Model_Permission(name, permissionType));
        }
    }

    public Map<String, String> convertPermissionsToMap() {
        Map<String, String> permissionMap = new HashMap<>();

        for (Model_Permission permission : permissions) {
            permissionMap.put(permission.getName(), permission.getPermissionStatus());
        }

        return permissionMap;
    }

    public void updatePermissions(int index, String permissionStatus) {
        permissions.get(index).setPermissionStatus(permissionStatus);
    }

    public void updatePermissionsInMongoDB() {
        try (MongoClient mongoClient = MongoClients.create(Constants.MONGODB_URI)) {
            MongoDatabase database = mongoClient.getDatabase(Constants.DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(Constants.COLLECTION_USER);

            // Tạo một Document mới chứa danh sách quyền hạn đã được cập nhật
            Document updateDocument = new Document("$set", new Document("permissions", convertPermissionsToMap()));

            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId và cập nhật quyền hạn
            collection.updateOne(Filters.eq("_id", new ObjectId(userId)), updateDocument, new UpdateOptions().upsert(true));

            System.out.println("Cập nhật quyền hạn của người dùng thành công trong MongoDB.");
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật quyền hạn của người dùng trong MongoDB: " + e.getMessage());
        }
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
