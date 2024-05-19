package com.myproject.forms.reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookingGroupForm extends JFrame {

    private JTable roomsTable;
    private DefaultTableModel roomsTableModel;
    private JComboBox<String> floorComboBox;
    private JComboBox<String> customerComboBox;

    // Dữ liệu phòng theo tầng
    private Object[][] roomsData = {
            {"Phòng 101", "3500000", 1}, {"Phòng 102", "3000000", 1}, {"Phòng 103", "2500000", 1},
            {"Phòng 104", "3500000", 1}, {"Phòng 105", "1500000", 1}, {"Phòng 106", "1500000", 1},
            {"Phòng 107", "1500000", 1}, {"Phòng 201", "2500000", 2}, {"Phòng 202", "2500000", 2},
            {"Phòng 203", "3500000", 2}, {"Phòng 204", "3500000", 2}, {"Phòng 205", "1500000", 2},
            {"Phòng 206", "3000000", 2}, {"Phòng 301", "3000000", 3}, {"Phòng 302", "2500000", 3},
            {"Phòng 303", "2500000", 3}, {"Phòng 304", "3000000", 3}, {"Phòng 305", "1500000", 3},
            {"Phòng 401", "2500000", 4}, {"Phòng 402", "1500000", 4}, {"Phòng 403", "1500000", 4},
            {"Phòng 404", "1500000", 4}, {"Phòng 405", "1500000", 4}
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
        String[] columnNamesRooms = {"TÊN PHÒNG", "ĐƠN GIÁ"};
        roomsTableModel = new DefaultTableModel(columnNamesRooms, 0);
        roomsTable = new JTable(roomsTableModel);
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

        JButton addCustomerButton = new JButton("Thêm khách hàng");
        addCustomerButton.addActionListener((ActionEvent e) -> {
            CustomerForm customerForm = new CustomerForm(customerComboBox);
            customerForm.setVisible(true);
        });

        customerPanel.add(addCustomerButton);

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
        Object[][] dataBookedRooms = {};
        JTable bookedRoomsTable = new JTable(new DefaultTableModel(dataBookedRooms, columnNamesBookedRooms));
        JScrollPane bookedRoomsScrollPane = new JScrollPane(bookedRoomsTable);
        centerPanel.add(new JLabel("Danh sách phòng đặt"));
        centerPanel.add(bookedRoomsScrollPane);

        // Bảng sản phẩm - dịch vụ
        String[] columnNamesProducts = {"PHÒNG", "TÊN SP - DV", "SL", "ĐƠN GIÁ", "THÀNH TIỀN"};
        Object[][] dataProducts = {};
        JTable productsTable = new JTable(new DefaultTableModel(dataProducts, columnNamesProducts));
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
                {"Coca Cola", "15000"}, {"Nước suối", "12000"}, {"Redbull", "20000"},
                {"Fanta", "15000"}, {"Cam ép", "15000"}, {"Trà Ô Long", "15000"}
        };
        JTable servicesTable = new JTable(new DefaultTableModel(dataServices, columnNamesServices));
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
        for (Object[] room : roomsData) {
            roomsTableModel.addRow(new Object[]{room[0], room[1]});
        }
    }

    private void filterRoomsByFloor() {
        int selectedFloor = floorComboBox.getSelectedIndex(); // 0: Tất cả các tầng, 1: Tầng 1, 2: Tầng 2, ...

        roomsTableModel.setRowCount(0);
        for (Object[] room : roomsData) {
            if (selectedFloor == 0 || (int) room[2] == selectedFloor) {
                roomsTableModel.addRow(new Object[]{room[0], room[1]});
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookingForm bookingForm = new BookingForm();
            bookingForm.setVisible(true);
        });
    }
}
