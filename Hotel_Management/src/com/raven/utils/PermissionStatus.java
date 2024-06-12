package com.raven.utils;

public enum PermissionStatus {
    FULL_ACCESS("Toàn quyền"), // Toàn quyền
    LOCKED("Khóa quyền");      // Khóa quyền

    private final String status; // Chuỗi tương ứng với enum

    // Constructor
    PermissionStatus(String status) {
        this.status = status;
    }

    // Phương thức để lấy chuỗi từ enum
    public String getStatusString() {
        return status;
    }
}