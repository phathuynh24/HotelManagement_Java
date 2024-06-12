package com.raven.model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Model_Employee {

    private String id;
    private String name;
    private String phone;
    private String position;
    private String dob;
    private String gender;
    private String email;
    private String idCard;
    private String status;
    private String userName;
    private String passWord;
    private List<Model_Permission> permissions;

    public Model_Employee() {
        // Default constructor
    }
    
    public Model_Employee(String id, String name, String gender, String phone, String email, String dob, 
                          String status, String userName, String passWord, String position, String idCard) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
        this.status = status;
        this.userName = userName;
        this.passWord = passWord;
        this.position = position;
        this.idCard = idCard;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPosition() {
        return position;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return passWord;
    }

    public List<Model_Permission> getPermissions() {
        return permissions;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String passWord) {
        this.passWord = passWord;
    }

    public void setPermissions(List<Model_Permission> permissions) {
        this.permissions = permissions;
    }

    public void setPermissionsFromDoc(Object permissionsDoc) {
        if (permissionsDoc instanceof Document) {
            Document doc = (Document) permissionsDoc;
            List<Model_Permission> permissionsList = new ArrayList<>();
            for (String key : doc.keySet()) {
                String permissionName = key;
                String permissionStatus = doc.getString(key);
                Model_Permission permission = new Model_Permission(permissionName, permissionStatus);
                permissionsList.add(permission);
            }
            this.permissions = permissionsList;
        }
    }

    // toString method
    @Override
    public String toString() {
        return "Model_Employee{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", phone='" + phone + '\''
                + ", position='" + position + '\''
                + ", dob='" + dob + '\''
                + ", gender='" + gender + '\''
                + ", email='" + email + '\''
                + ", idCard='" + idCard + '\''
                + ", status='" + status + '\''
                + ", userName='" + userName + '\''
                + ", passWord='" + passWord + '\''
                + ", permissions=" + permissions
                + '}';
    }

    // toRowTable method
    public Object[] toRowTable(EventAction<Model_Employee> eventAction) {
        return new Object[]{
            id, name, gender, position, phone, email, idCard, dob, status, new ModelAction<>(this, eventAction, "action_all")
        };
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
}
