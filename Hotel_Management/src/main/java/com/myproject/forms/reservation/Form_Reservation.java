package com.myproject.forms.reservation;

import com.myproject.models.Model_Room;
import com.myproject.models.Model_RoomType;
import com.myproject.models.types.RoomStatus;
import com.myproject.models.types.RoomType;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Form_Reservation extends JPanel {

    private BookingForm bookingForm;
    private BookingGroupForm bookingGroupForm;
    private List<Model_Room> rooms;

    public Form_Reservation() {
        rooms = initializeRooms(); // Lấy danh sách phòng
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(4, 1)); // Bố trí 4 hàng cho 4 tầng

        // Tạo các tầng và phòng từ danh sách phòng
        for (int floor = 1; floor <= 4; floor++) {
            JPanel floorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            floorPanel.setBorder(BorderFactory.createTitledBorder("Tầng " + floor));

            for (Model_Room room : rooms) {
                String roomFloor = room.getRoomId().substring(0, 1); // Lấy số tầng từ tên phòng
                if (Integer.parseInt(roomFloor) == floor) {
                    JButton roomButton = new JButton(room.getRoomId());
                    roomButton.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/room_1.png")));
                    roomButton.setHorizontalTextPosition(JButton.CENTER);
                    roomButton.setVerticalTextPosition(JButton.BOTTOM);

                    // Thêm menu ngữ cảnh
                    JPopupMenu popupMenu = createContextMenu(room); // Truyền đối tượng phòng vào
                    roomButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                popupMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    });

                    floorPanel.add(roomButton);
                }
            }
            add(floorPanel);
        }
    }

    private JPopupMenu createContextMenu(Model_Room room) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem menuItem1 = new JMenuItem("Đặt phòng khách lẻ");
        menuItem1.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/booking.png")));
        menuItem1.addActionListener(e -> {
            if (bookingForm == null || !bookingForm.isVisible()) {
                bookingForm = new BookingForm(room);
                bookingForm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Form Đặt phòng khách lẻ đã được mở.");
            }
        });

        JMenuItem menuItem2 = new JMenuItem("Đặt phòng theo đoàn");
        menuItem2.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/booking_group.png")));
        menuItem2.addActionListener(e -> {
            if (bookingGroupForm == null || !bookingGroupForm.isVisible()) {
                bookingGroupForm = new BookingGroupForm();
                bookingGroupForm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Form Đặt phòng theo đoàn đã được mở.");
            }
        });

        JMenuItem menuItem3 = new JMenuItem("Cập nhật Sản phẩm - Dịch vụ");
        menuItem3.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/update.png")));

        JMenuItem menuItem4 = new JMenuItem("Chuyển phòng");
        menuItem4.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/room_transfer.png")));

        JMenuItem menuItem5 = new JMenuItem("Thanh toán");
        menuItem5.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/payment.png")));

        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem3);
        popupMenu.add(menuItem4);
        popupMenu.add(menuItem5);

        return popupMenu;
    }

    public static List<Model_Room> initializeRooms() {
        List<Model_Room> rooms = new ArrayList<>();

        // Tầng 1
        rooms.add(new Model_Room("101", new Model_RoomType(RoomType.STANDARD, 3500000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("102", new Model_RoomType(RoomType.STANDARD, 3000000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("103", new Model_RoomType(RoomType.STANDARD, 2500000, 2), 2, RoomStatus.RESERVED));
        rooms.add(new Model_Room("104", new Model_RoomType(RoomType.STANDARD, 3500000, 2), 2, RoomStatus.OCCUPIED));
        rooms.add(new Model_Room("105", new Model_RoomType(RoomType.STANDARD, 1500000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("106", new Model_RoomType(RoomType.STANDARD, 1500000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("107", new Model_RoomType(RoomType.STANDARD, 1500000, 2), 2, RoomStatus.OCCUPIED));

        // Tầng 2
        rooms.add(new Model_Room("201", new Model_RoomType(RoomType.STANDARD, 2500000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("202", new Model_RoomType(RoomType.STANDARD, 3500000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("203", new Model_RoomType(RoomType.STANDARD, 3500000, 2), 2, RoomStatus.OCCUPIED));
        rooms.add(new Model_Room("204", new Model_RoomType(RoomType.STANDARD, 3500000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("205", new Model_RoomType(RoomType.STANDARD, 3000000, 2), 2, RoomStatus.UNDER_REPAIR));
        rooms.add(new Model_Room("206", new Model_RoomType(RoomType.STANDARD, 3000000, 2), 2, RoomStatus.AVAILABLE));

        // Tầng 3
        rooms.add(new Model_Room("301", new Model_RoomType(RoomType.STANDARD, 3000000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("302", new Model_RoomType(RoomType.STANDARD, 2500000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("303", new Model_RoomType(RoomType.STANDARD, 2500000, 2), 2, RoomStatus.OCCUPIED));
        rooms.add(new Model_Room("304", new Model_RoomType(RoomType.STANDARD, 2500000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("305", new Model_RoomType(RoomType.STANDARD, 2500000, 2), 2, RoomStatus.AVAILABLE));

        // Tầng 4
        rooms.add(new Model_Room("401", new Model_RoomType(RoomType.STANDARD, 2500000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("402", new Model_RoomType(RoomType.STANDARD, 2500000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("403", new Model_RoomType(RoomType.STANDARD, 1500000, 2), 2, RoomStatus.OCCUPIED));
        rooms.add(new Model_Room("404", new Model_RoomType(RoomType.STANDARD, 1500000, 2), 2, RoomStatus.AVAILABLE));
        rooms.add(new Model_Room("405", new Model_RoomType(RoomType.STANDARD, 1500000, 2), 2, RoomStatus.OCCUPIED));

        return rooms;
    }
}
