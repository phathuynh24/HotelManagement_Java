package com.myproject.models.types;

public enum PermissionStatus {
    FULL_ACCESS("Toàn quyền"), // Toàn quyền
    VIEW_ONLY("Chỉ xem"),      // Chỉ xem
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