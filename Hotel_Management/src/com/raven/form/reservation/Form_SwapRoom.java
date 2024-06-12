package com.raven.form.reservation;

import com.raven.controller.InvoiceController;
import com.raven.controller.RoomController;
import com.raven.model.Model_Invoice;
import com.raven.model.Model_Room;
import com.raven.swing.table.Table;
import com.raven.utils.RoomPrice;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import javax.swing.table.TableColumn;

public class Form_SwapRoom extends JFrame {

    private JComboBox<String> roomComboBox;
    private JButton swapRoomButton;
    private Model_Room currentRoom;
    private Model_Room selectedRoom;
    private int currentRoomPrice;
    private Model_Invoice invoice;
    private final DecimalFormat df = new DecimalFormat("#,### VNĐ");
    private java.util.List<Model_Room> rooms;
    private final String[] roomListColumns = {"PHÒNG", "LOẠI", "GIÁ", "S.CHỨA", "TẦNG"};
    private final Table roomTable = new Table();
    private DefaultTableModel roomTableModel;
    private RoomPrice roomPrice;
    private final InvoiceController invoiceController = new InvoiceController();
    private boolean swapSuccessful;

    public Form_SwapRoom(Model_Room currentRoom, int currentRoomPrice, Model_Invoice invoice, RoomPrice roomPrice) {
        this.currentRoom = currentRoom;
        this.currentRoomPrice = currentRoomPrice;
        this.invoice = invoice;
        this.roomPrice = roomPrice;
        swapSuccessful = false;
        initUI();
        setupRoomTableModel();
        loadDataAsync();
    }

    private void loadDataAsync() {
        Thread loadDataThread1 = new Thread(() -> {
            initRoomData();
        });

        loadDataThread1.start();
    }

    public boolean isSwapSuccessful() {
        return swapSuccessful;
    }
    
    private void setupRoomTableModel() {
        roomTableModel = new DefaultTableModel(roomListColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable.setModel(roomTableModel);
        // Thiết lập kích thước cho các cột
        TableColumn col0 = roomTable.getColumnModel().getColumn(0);
        col0.setPreferredWidth(20);
        TableColumn col1 = roomTable.getColumnModel().getColumn(1);
        col1.setPreferredWidth(10);
        TableColumn col2 = roomTable.getColumnModel().getColumn(2);
        col2.setPreferredWidth(30);
        TableColumn col3 = roomTable.getColumnModel().getColumn(3);
        col3.setPreferredWidth(5);
        TableColumn col4 = roomTable.getColumnModel().getColumn(4);
        col4.setPreferredWidth(10);
    }

    private void initRoomData() {
        RoomController roomController = new RoomController();
        rooms = roomController.getAllRooms();

        for (Model_Room room : rooms) {
            roomTableModel.addRow(room.toRowTableGroupBooking(roomPrice.getPrice(room.getType())));
        }
    }

    private void openRoomSelectionDialog() {
        JDialog roomSelectionDialog = new JDialog((Frame) null, "Chọn phòng chuyển đến", true);
        roomSelectionDialog.setSize(400, 800); // Thiết lập kích thước rộng và cao của dialog
        roomSelectionDialog.setPreferredSize(new Dimension(400, 800)); // Thiết lập kích thước ưu tiên của dialog
        roomSelectionDialog.setMinimumSize(new Dimension(400, 800));
        roomSelectionDialog.setMaximumSize(new Dimension(400, 800));
        roomSelectionDialog.pack(); // Tự động điều chỉnh kích thước của dialog dựa trên các thành phần bên trong
        roomSelectionDialog.setLocationRelativeTo(null);
        roomSelectionDialog.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Find");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        roomSelectionDialog.add(searchPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(roomTable);
        roomSelectionDialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(clearButton);
        roomSelectionDialog.add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener((ActionEvent e) -> {
            String searchText = searchField.getText().toLowerCase();
            if (!searchText.isEmpty()) {
                filterTable(searchText, roomTableModel);
            }
        });

        clearButton.addActionListener((ActionEvent e) -> {
            searchField.setText("");
            for (Model_Room room : rooms) {
                roomTableModel.addRow(room.toRowTableGroupBooking(roomPrice.getPrice(room.getType())));
            }
        });

        roomTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && roomTable.getSelectedRow() != -1) {
                String selectedRoomName = roomTable.getValueAt(roomTable.getSelectedRow(), 0).toString();
                roomComboBox.setSelectedItem(selectedRoomName);

                for (Model_Room room : rooms) {
                    if (room.getName().equals(selectedRoomName)) {
                        selectedRoom = room;
                        break;
                    }
                }

                roomSelectionDialog.dispose();
            }
        });

        roomSelectionDialog.setSize(400, 300);
        roomSelectionDialog.setLocationRelativeTo(null);
        roomSelectionDialog.setVisible(true);
    }

    private void filterTable(String searchText, DefaultTableModel tableModel) {
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            String roomName = tableModel.getValueAt(i, 0).toString().toLowerCase();
            if (!roomName.contains(searchText)) {
                tableModel.removeRow(i);
            }
        }
    }

    private void swapAction() {
        invoiceController.swapRoom(currentRoom, selectedRoom, invoice, roomPrice);
        JOptionPane.showMessageDialog(this, "Đổi phòng thành công!");
        swapSuccessful = true; 
        this.dispose(); // Tắt form swap
    }

    private void initUI() {
        setTitle("Chuyển phòng");
        setSize(460, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBackground(Color.white);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Hàng 2: Thông tin phòng hiện tại
        JLabel currentRoomLabel = new JLabel("Phòng hiện tại: " + currentRoom.getName() + " - Đơn giá: " + df.format(currentRoomPrice));
        currentRoomLabel.setForeground(Color.RED);
        currentRoomLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(currentRoomLabel, gbc);

        // Hàng 3: Combobox chọn phòng
        JLabel newRoomLabel = new JLabel("Phòng chuyển đến:");
        newRoomLabel.setForeground(Color.RED);
        newRoomLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(newRoomLabel, gbc);

        roomComboBox = new JComboBox<>(new String[]{"Chọn phòng"});
        Dimension size = new Dimension(180, 25);
        roomComboBox.setPreferredSize(size);
        roomComboBox.setMinimumSize(size);
        roomComboBox.setMaximumSize(size);
        roomComboBox.setEditable(true);
        roomComboBox.addActionListener((e) -> {
            openRoomSelectionDialog();
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(roomComboBox, gbc);

        // Hàng 4: Nút chuyển phòng
        swapRoomButton = new JButton("Chuyển phòng", new ImageIcon(getClass().getResource("/com/raven/icon/update.png")));
        swapRoomButton.addActionListener(e -> swapAction());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(swapRoomButton, gbc);

        getContentPane().add(mainPanel);
    }

}
