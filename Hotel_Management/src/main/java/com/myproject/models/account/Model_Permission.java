package com.myproject.models.account;

public class Model_Permission {

    private String name;
    private String permissionStatus;

    // Constructors
    public Model_Permission(String name, String permissionStatus) {
        this.name = name;
        this.permissionStatus = permissionStatus;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(String permissionStatus) {
        this.permissionStatus = permissionStatus;
    }
}
