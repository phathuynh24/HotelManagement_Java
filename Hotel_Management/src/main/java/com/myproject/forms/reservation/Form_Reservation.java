package com.myproject.forms.reservation;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Form_Reservation extends JPanel {

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
                roomButton.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/logo.png"))); // Thêm icon nhà bình thường
                roomButton.setHorizontalTextPosition(JButton.CENTER);
                roomButton.setVerticalTextPosition(JButton.BOTTOM);

//                // Kiểm tra nếu phòng là Phòng 202 để thay đổi icon
//                if (floor == 2 && room == 2) {
//                    roomButton.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/menu.png"))); // Thêm icon nhà màu đỏ
//                }

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

        JMenuItem menuItem1 = new JMenuItem("Đặt phòng");
        menuItem1.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/7.png")));
        menuItem1.addActionListener(e -> {
//            BookingForm bookingForm = new BookingForm();
//            bookingForm.setVisible(true);

              BookingGroupForm bookingGroupForm = new BookingGroupForm();
              bookingGroupForm.setVisible(true);
        });

        JMenuItem menuItem2 = new JMenuItem("Cập nhật Sản phẩm - Dịch vụ");
        menuItem2.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/7.png")));

        JMenuItem menuItem3 = new JMenuItem("Chuyển phòng");
        menuItem3.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/7.png")));

        JMenuItem menuItem4 = new JMenuItem("Thanh toán");
        menuItem4.setIcon(new ImageIcon(getClass().getResource("/com/myproject/icons/7.png")));

        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem3);
        popupMenu.add(menuItem4);

        return popupMenu;
    }
}
