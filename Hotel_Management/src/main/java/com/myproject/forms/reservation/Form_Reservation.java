package com.myproject.forms.reservation;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Form_Reservation extends JPanel {

    private BookingForm bookingForm;
    private BookingGroupForm bookingGroupForm;

    public Form_Reservation() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(4, 1)); // Bố trí 4 hàng cho 4 tầng

        // Tạo các tầng và phòng
        for (int floor = 1; floor <= 4; floor++) {
            JPanel floorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            floorPanel.setBorder(BorderFactory.createTitledBorder("Tầng " + floor));
            for (int room = 1; room <= 6; room++) {
                String roomNumber = String.format("Phòng %d%02d", floor, room);
                JButton roomButton = new JButton(roomNumber);
                roomButton.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/room_1.png")));
                roomButton.setHorizontalTextPosition(JButton.CENTER);
                roomButton.setVerticalTextPosition(JButton.BOTTOM);

                // Thêm menu ngữ cảnh
                JPopupMenu popupMenu = createContextMenu();
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
            add(floorPanel);
        }
    }

    private JPopupMenu createContextMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem menuItem1 = new JMenuItem("Đặt phòng khách lẻ");
        menuItem1.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/booking.png")));
        menuItem1.addActionListener(e -> {
            if (bookingForm == null || !bookingForm.isVisible()) {
                bookingForm = new BookingForm();
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
            }
            else {
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
}
