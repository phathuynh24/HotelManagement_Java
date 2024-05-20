package com.myproject.forms.reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BookingGroupForm extends JFrame {

    private JTable roomsTable;
    private JTable bookedRoomsTable;
    private JTable productsTable;
    private JTable servicesTable;
    private DefaultTableModel roomsTableModel;
    private DefaultTableModel bookedRoomsTableModel;
    private DefaultTableModel productsTableModel;
    private DefaultTableModel servicesTableModel;
    private JComboBox<String> floorComboBox;
    private JComboBox<String> customerComboBox;

    // Dữ liệu phòng theo tầng
    private String[] roomColumnNames = {"TẦNG", "TÊN PHÒNG", "ĐƠN GIÁ", "SỐ PHÒNG TRỐNG"};
    private Object[][] roomData = {
            {"Tầng 1", "Phòng 101", 3500000, 7},
            {"Tầng 1", "Phòng 102", 3000000, 7},
            {"Tầng 1", "Phòng 103", 2500000, 7},
            {"Tầng 1", "Phòng 104", 3500000, 7},
            {"Tầng 1", "Phòng 105", 1500000, 7},
            {"Tầng 1", "Phòng 106", 1500000, 7},
            {"Tầng 1", "Phòng 107", 1500000, 7},
            {"Tầng 2", "Phòng 201", 2500000, 5},
            {"Tầng 2", "Phòng 203", 3500000, 5},
            {"Tầng 2", "Phòng 204", 3500000, 5},
            {"Tầng 2", "Phòng 205", 3500000, 5},
            {"Tầng 2", "Phòng 206", 3000000, 5},
            {"Tầng 3", "Phòng 301", 3000000, 5},
            {"Tầng 3", "Phòng 302", 2500000, 5},
            {"Tầng 3", "Phòng 303", 2500000, 5},
            {"Tầng 3", "Phòng 304", 2500000, 5},
            {"Tầng 3", "Phòng 305", 2500000, 5},
            {"Tầng 4", "Phòng 401", 2500000, 5},
            {"Tầng 4", "Phòng 402", 2500000, 5},
            {"Tầng 4", "Phòng 403", 1500000, 5},
            {"Tầng 4", "Phòng 404", 1500000, 5},
            {"Tầng 4", "Phòng 405", 1500000, 5}
    };

    public BookingGroupForm() {
        setTitle("Booking Form");
        setSize(1500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel trái (Danh sách phòng trống)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Danh sách phòng trống"));

        // ComboBox chọn tầng
        floorComboBox = new JComboBox<>(new String[]{"Tất cả các tầng", "Tầng 1", "Tầng 2", "Tầng 3", "Tầng 4"});
        floorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterRoomsByFloor();
            }
        });

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Tầng:"));
        filterPanel.add(floorComboBox);

        leftPanel.add(filterPanel, BorderLayout.NORTH);

        // Bảng phòng trống
        roomsTableModel = new DefaultTableModel(roomColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomsTable = new JTable(roomsTableModel);
        roomsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = roomsTable.getSelectedRow();
                String roomName = (String) roomsTableModel.getValueAt(selectedRow, 1);
                int roomPrice = (int) roomsTableModel.getValueAt(selectedRow, 2);
                bookedRoomsTableModel.addRow(new Object[]{roomName, roomPrice});
            }
        });
        JScrollPane roomsScrollPane = new JScrollPane(roomsTable);
        leftPanel.add(roomsScrollPane, BorderLayout.CENTER);

        loadAllRooms();

        // Panel giữa (Thông tin đặt phòng)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đặt phòng"));

        // Thông tin khách hàng
        JPanel customerPanel = new JPanel(new GridLayout(6, 2));
        customerPanel.add(new JLabel("Khách hàng:"));
        customerComboBox = new JComboBox<>(new String[]{"Nguyễn Minh Triết", "Nguyễn Mai Lan", "Hoàng Anh Tuấn"});
        customerPanel.add(customerComboBox);

        customerPanel.add(new JLabel("Ngày đến:"));
        JTextField checkInField = new JTextField("01/09/2021");
        customerPanel.add(checkInField);

        customerPanel.add(new JLabel("Ngày trả:"));
        JTextField checkOutField = new JTextField("02/09/2021");
        customerPanel.add(checkOutField);

        customerPanel.add(new JLabel("Số người:"));
        JTextField peopleField = new JTextField("1");
        customerPanel.add(peopleField);

        customerPanel.add(new JLabel("Trạng thái:"));
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Chưa hoàn tất", "Đã hoàn tất"});
        customerPanel.add(statusComboBox);

        centerPanel.add(customerPanel);

        // Bảng phòng đặt
        String[] columnNamesBookedRooms = {"TÊN PHÒNG", "ĐƠN GIÁ"};
        bookedRoomsTableModel = new DefaultTableModel(columnNamesBookedRooms, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookedRoomsTable = new JTable(bookedRoomsTableModel);
        JScrollPane bookedRoomsScrollPane = new JScrollPane(bookedRoomsTable);
        centerPanel.add(new JLabel("Danh sách phòng đặt"));
        centerPanel.add(bookedRoomsScrollPane);

        // Bảng sản phẩm - dịch vụ
        String[] columnNamesProducts = {"PHÒNG", "TÊN SP - DV", "SL", "ĐƠN GIÁ", "THÀNH TIỀN"};
        productsTableModel = new DefaultTableModel(columnNamesProducts, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productsTable = new JTable(productsTableModel);
        JScrollPane productsScrollPane = new JScrollPane(productsTable);
        centerPanel.add(new JLabel("Danh sách Sản phẩm - Dịch vụ"));
        centerPanel.add(productsScrollPane);

        // Tổng tiền
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.add(new JLabel("TỔNG THANH TOÁN"), BorderLayout.WEST);
        JTextField totalField = new JTextField("0 đồng");
        totalField.setEditable(false);
        totalPanel.add(totalField, BorderLayout.CENTER);
        centerPanel.add(totalPanel);

        // Panel phải (Sản phẩm - Dịch vụ)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Sản phẩm - Dịch vụ"));

        String[] columnNamesServices = {"TÊN SP - DV", "ĐƠN GIÁ"};
        Object[][] dataServices = {
                {"Coca Cola", 15000}, {"Nước suối", 12000}, {"Redbull", 20000},
                {"Fanta", 15000}, {"Cam ép", 15000}, {"Trà Ô Long", 15000}
        };
        servicesTableModel = new DefaultTableModel(dataServices, columnNamesServices) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        servicesTable = new JTable(servicesTableModel);
        servicesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = servicesTable.getSelectedRow();
                String serviceName = (String) servicesTableModel.getValueAt(selectedRow, 0);
                int servicePrice = (int) servicesTableModel.getValueAt(selectedRow, 1);
                int selectedRoomRow = bookedRoomsTable.getSelectedRow();
                if (selectedRoomRow != -1) {
                    String roomName = (String) bookedRoomsTableModel.getValueAt(selectedRoomRow, 0);
                    productsTableModel.addRow(new Object[]{roomName, serviceName, 1, servicePrice, servicePrice});
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn phòng trước khi thêm sản phẩm - dịch vụ.");
                }
            }
        });
        JScrollPane servicesScrollPane = new JScrollPane(servicesTable);
        rightPanel.add(servicesScrollPane, BorderLayout.CENTER);

        // Thêm các panel vào mainPanel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    private void loadAllRooms() {
        roomsTableModel.setRowCount(0);
        for (Object[] room : roomData) {
            roomsTableModel.addRow(room);
        }
    }

    private void filterRoomsByFloor() {
        int selectedFloor = floorComboBox.getSelectedIndex(); // 0: Tất cả các tầng, 1: Tầng 1, 2: Tầng 2, ...
        String selectedFloorString = selectedFloor == 0 ? "Tất cả các tầng" : "Tầng " + selectedFloor;
        roomsTableModel.setRowCount(0);
        for (Object[] room : roomData) {
            if (selectedFloor == 0 || room[0].equals(selectedFloorString)) {
                roomsTableModel.addRow(room);
            }
        }
    }
}
