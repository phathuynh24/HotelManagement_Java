package com.myproject.models.types;

public enum PermissionType {
    BOOK_ROOM("Đặt phòng"),
    INVOICE("Hóa đơn"),
    MANAGE_EMPLOYEES("Quản lý nhân viên"),
    CUSTOMER_INFO("Thông tin khách hàng"),
    REVENUE_STATISTICS("Thống kê doanh thu"),
    INVENTORY_MANAGEMENT("Quản lý hàng hóa"),
    SERVICE_MANAGEMENT("Quản lý dịch vụ"),
    FLOOR_MANAGEMENT("Quản lý tầng"),
    ROOM_MANAGEMENT("Quản lý phòng"),
    ROOM_TYPE_MANAGEMENT("Quản lý loại phòng");

    private final String displayName;

    PermissionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
