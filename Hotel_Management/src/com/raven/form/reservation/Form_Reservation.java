package com.raven.form.reservation;

import com.raven.controller.FloorController;
import com.raven.controller.InvoiceController;
import com.raven.controller.RoomController;
import com.raven.model.Model_Invoice;
import com.raven.model.Model_Invoice_Rooms;
import com.raven.model.Model_Room;
import com.raven.swing.ButtonRoom;
import com.raven.swing.DateLabelFormatter;
import com.raven.utils.RoomPrice;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

public class Form_Reservation extends javax.swing.JPanel {

    private RoomController roomController;
    private FloorController floorController;
    private List<Model_Room> rooms;
    private List<Model_Invoice> invoices;
    private Form_Booking bookingForm;
    private Form_SwapRoom swapRoomForm;
    private int countFloors;
    private boolean roomBooked;
    private final SqlDateModel modelCheckIn;
    private final SqlDateModel modelCheckOut;
    private Model_Invoice invoiceSelected;
    private RoomPrice roomPrice;

    public Form_Reservation() {
        initComponents();

        // Get the current date
        java.util.Date currentDate = new java.util.Date();
        java.sql.Date sqlCurrentDate = new java.sql.Date(currentDate.getTime());
        modelCheckOut = new SqlDateModel(sqlCurrentDate);
        modelCheckIn = new SqlDateModel(sqlCurrentDate);

        initRoomLayout();
        setOpaque(false);
        loadDataAsync();
    }

    private void loadDataAsync() {
        countFloors = 4; // Số lượng tầng mặc định, có thể cập nhật sau

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                initializeRooms();
                Thread loadDataThread1 = new Thread(() -> {
                    roomPrice = new RoomPrice();
                });
                loadDataThread1.start();

                return null;
            }

            @Override
            protected void done() {
                // Sau khi dữ liệu đã được tải, cập nhật giao diện người dùng
                updateRoomLayout();
            }
        };

        worker.execute();
    }

    public void initializeRooms() {

        roomController = new RoomController();
        rooms = roomController.getAllRooms();

        floorController = new FloorController();
        countFloors = floorController.countFloors();
    }

    private JPopupMenu createContextMenu(Model_Room room) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem menuItem1 = new JMenuItem("Đặt phòng khách lẻ");
        menuItem1.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/booking.png")));
        menuItem1.addActionListener(e -> {
            if (bookingForm == null || !bookingForm.isVisible()) {
                bookingForm = new Form_Booking(room, "Add", null);
                bookingForm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Form Đặt phòng đã được mở.");
            }
        });

        JMenuItem menuItem2 = new JMenuItem("Cập nhật phiếu đặt phòng");
        menuItem2.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/update.png")));
        menuItem2.addActionListener(e -> {
            if (invoiceSelected == null) {
                return;
            }
            if (invoiceSelected.getRoomList().size() == 1) {
                if (bookingForm == null || !bookingForm.isVisible()) {
                    bookingForm = new Form_Booking(room, "Update", invoiceSelected);
                    bookingForm.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Form Đặt phòng đã được mở.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Phòng này đã được đặt theo đoàn, hãy vào mục khách đặt theo đoàn để xử lý!");
            }
        });

        JMenuItem menuItem3 = new JMenuItem("Chuyển phòng");
        menuItem3.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/room_transfer.png")));
        menuItem3.addActionListener(e -> {
            if (invoiceSelected == null) {
                return;
            }
            if (invoiceSelected.getRoomList().size() == 1) {
                if (swapRoomForm == null || !swapRoomForm.isVisible()) {
                    swapRoomForm = new Form_SwapRoom(room, roomPrice.getPrice(room.getType()), invoiceSelected, roomPrice);
                    swapRoomForm.setVisible(true);
                    swapRoomForm.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            boolean swapSuccessful = swapRoomForm.isSwapSuccessful(); // Kiểm tra kết quả swap
                            if (swapSuccessful) {
                                updateRoomLayout();
                            }
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "Form Chuyển phòng đã được mở.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Phòng này đã được đặt theo đoàn, hãy vào mục khách đặt theo đoàn để xử lý!");
            }
        });

        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem3);

        return popupMenu;
    }

    private void initRoomLayout() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 1));
        contentPanel.setBorder(BorderFactory.createEmptyBorder());
        contentPanel.setOpaque(false);

        for (int floor = 1; floor <= 5; floor++) {
            JPanel floorPanel = new JPanel(new BorderLayout());
            floorPanel.setSize(new Dimension(Integer.MAX_VALUE, floorPanel.getPreferredSize().height));
            floorPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            floorPanel.setBackground(Color.white);

            JPanel floorTitlePanel = new JPanel(new BorderLayout());
            floorTitlePanel.setSize(new Dimension(Integer.MAX_VALUE, floorPanel.getPreferredSize().height));
            floorTitlePanel.setBackground(new Color(240, 240, 240));
            floorTitlePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding

            JLabel floorTitleLabel = new JLabel("Tầng " + floor);
            floorTitleLabel.setHorizontalAlignment(JLabel.LEFT);
            floorTitleLabel.setVerticalAlignment(JLabel.CENTER);
            floorTitleLabel.setForeground(Color.GRAY); // Gray text color
            floorTitleLabel.setFont(floorTitleLabel.getFont().deriveFont(Font.BOLD, 14f)); // Bold and larger font size
            floorTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0)); // Left padding for the label
            floorTitlePanel.add(floorTitleLabel, BorderLayout.CENTER);

            JPanel roomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            roomButtonPanel.setOpaque(false);

            for (int i = 0; i < 6; i++) {
                JButton placeholderButton = new JButton("Loading...");
                roomButtonPanel.add(placeholderButton);
            }

            floorPanel.add(roomButtonPanel, BorderLayout.CENTER);

            JPanel floorWrapperPanel = new JPanel(new BorderLayout());
            floorWrapperPanel.add(floorTitlePanel, BorderLayout.NORTH);
            floorWrapperPanel.add(floorPanel, BorderLayout.CENTER);

            contentPanel.add(floorWrapperPanel);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getHorizontalScrollBar().setOpaque(false);

        setBorder(BorderFactory.createEmptyBorder());
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // Add the panelCheckStatus at the top
        JPanel panelCheckStatus = createPanelCheckStatusRoom();
        add(panelCheckStatus, BorderLayout.NORTH);
    }

    private void updateRoomLayout() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder());
        contentPanel.setOpaque(false);

        for (int floor = 1; floor <= countFloors; floor++) {
            JPanel floorPanel = new JPanel(new BorderLayout());
            floorPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            floorPanel.setBackground(Color.WHITE);

            JPanel floorTitlePanel = new JPanel(new BorderLayout());
            floorTitlePanel.setSize(new Dimension(Integer.MAX_VALUE, floorPanel.getPreferredSize().height));
            floorTitlePanel.setBackground(new Color(240, 240, 240));
            floorTitlePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding

            JLabel floorTitleLabel = new JLabel("Tầng " + floor);
            floorTitleLabel.setHorizontalAlignment(JLabel.LEFT);
            floorTitleLabel.setVerticalAlignment(JLabel.CENTER);
            floorTitleLabel.setForeground(Color.GRAY); // Gray text color
            floorTitleLabel.setFont(floorTitleLabel.getFont().deriveFont(Font.BOLD, 14f)); // Bold and larger font size
            floorTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0)); // Left padding for the label
            floorTitlePanel.add(floorTitleLabel, BorderLayout.CENTER);

            JPanel roomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            roomButtonPanel.setOpaque(false);

            for (Model_Room room : rooms) {
                String roomFloor = room.getId().substring(1, 2); // Lấy số tầng từ id phòng
                if (Integer.parseInt(roomFloor) == floor) {

                    // Check if the room is in the invoices
                    roomBooked = false;
                    if (invoices != null) {
                        for (Model_Invoice invoice : invoices) {
                            if("Đã thanh toán".equals(invoice.getStatus())) continue;
                            for (Model_Invoice_Rooms roomInvoice : invoice.getRoomList()) {
                                if (roomInvoice.getName().equals(room.getName())) {
                                    roomBooked = true;
                                    break;
                                }
                            }
                        }
                    }

                    ButtonRoom roomButton = new ButtonRoom(roomBooked);
                    roomButton.setText(room.getName());

                    if (roomBooked) {
                        roomButton.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/room_booked.png")));
                        roomButton.setBackground(Color.RED);
                    } else {
                        roomButton.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/room.png")));
                        roomButton.setBackground(Color.GREEN);
                    }

                    roomButton.setHorizontalTextPosition(JButton.CENTER);
                    roomButton.setVerticalTextPosition(JButton.BOTTOM);

                    JPopupMenu popupMenu = createContextMenu(room);
                    roomButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            roomButton.setBackground(Color.RED);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            roomButton.setBackground(null);
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                invoiceSelected = null;
                                if (invoices != null) {
                                    for (Model_Invoice invoice : invoices) {
                                        for (Model_Invoice_Rooms roomInvoice : invoice.getRoomList()) {
                                            if (roomInvoice.getName().equals(room.getName())) {
                                                invoiceSelected = invoice;
                                                break;
                                            }
                                        }
                                        if (invoiceSelected != null) {
                                            break;
                                        }
                                    }
                                }
                                popupMenu.show(e.getComponent(), e.getX(), e.getY());

                            }
                        }
                    });

                    roomButtonPanel.add(roomButton);
                }
            }

            floorPanel.add(roomButtonPanel, BorderLayout.CENTER);

            JPanel floorWrapperPanel = new JPanel(new BorderLayout());
            floorWrapperPanel.add(floorTitlePanel, BorderLayout.NORTH);
            floorWrapperPanel.add(floorPanel, BorderLayout.CENTER);
            contentPanel.add(floorWrapperPanel);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getHorizontalScrollBar().setOpaque(false);

        // Add the panelCheckStatus at the top
        JPanel panelCheckStatus = createPanelCheckStatusRoom();

        setLayout(new BorderLayout());
        removeAll();
        add(scrollPane, BorderLayout.CENTER);
        add(panelCheckStatus, BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    private void checkStatusRoom(Date checkInDate, Date checkOutDate) {
        System.out.println("Checking status from " + checkInDate + " to " + checkOutDate);
        InvoiceController invoiceController = new InvoiceController();
        invoices = invoiceController.getInvoicesWithinTimePeriod(checkInDate, checkOutDate);

        updateRoomLayout();
    }

    private JPanel createPanelCheckStatusRoom() {
        JPanel panelCheckStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCheckStatus.setOpaque(false);

        // Create labels
        JLabel lblCheckIn = new JLabel("Ngày Check-in:");
        lblCheckIn.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblCheckIn.setForeground(Color.red);
        JLabel lblCheckOut = new JLabel("Ngày Check-out:");
        lblCheckOut.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblCheckOut.setForeground(Color.red);
        
        // Create date pickers
        JDatePanelImpl datePanelCheckIn = new JDatePanelImpl(modelCheckIn, new Properties());
        JDatePickerImpl datePickerCheckIn = new JDatePickerImpl(datePanelCheckIn, new DateLabelFormatter());
        datePickerCheckIn.setOpaque(false);

        JDatePanelImpl datePanelCheckOut = new JDatePanelImpl(modelCheckOut, new Properties());
        JDatePickerImpl datePickerCheckOut = new JDatePickerImpl(datePanelCheckOut, new DateLabelFormatter());
        datePickerCheckOut.setOpaque(false);

        // Create check button
        JButton btnCheck = new JButton("Kiểm tra");
        btnCheck.addActionListener(e -> checkStatusRoom((Date) modelCheckIn.getValue(), (Date) modelCheckOut.getValue()));

        // Add components to panel
        panelCheckStatus.add(lblCheckIn);
        panelCheckStatus.add(datePickerCheckIn);
        panelCheckStatus.add(lblCheckOut);
        panelCheckStatus.add(datePickerCheckOut);
        panelCheckStatus.add(btnCheck);

        return panelCheckStatus;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
