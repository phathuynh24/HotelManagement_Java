package com.raven.form.reservation;

import com.raven.controller.CustomerController;
import com.raven.controller.GoodsController;
import com.raven.controller.InvoiceController;
import com.raven.controller.RoomController;
import com.raven.controller.ServiceController;
import com.raven.model.Model_Customer;
import com.raven.model.Model_Goods;
import com.raven.model.Model_Invoice;
import com.raven.model.Model_Invoice_Rooms;
import com.raven.model.Model_Invoice_ServicesAndGoods;
import com.raven.model.Model_Room;
import com.raven.model.Model_Service;
import com.raven.swing.ButtonHover;
import com.raven.swing.DateLabelFormatter;
import com.raven.swing.RoundedBorder;
import com.raven.swing.table.EventAction;
import com.raven.swing.table.Table;
import com.raven.utils.RoomPrice;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import org.jdatepicker.impl.UtilDateModel;

public class Form_GroupBooking extends javax.swing.JPanel {

    private int roomMoney;
    private int serviceAndGoodsMoney;
    private int totalMoney;
    private int numberOfGuests;
    private String note;
    private Model_Customer customerSelected;
    private String status;
    private java.util.List<Model_Invoice_Rooms> roomList;
    private java.util.List<Model_Invoice_ServicesAndGoods> serviceList;
    private java.util.List<Model_Invoice_ServicesAndGoods> productList;
    private int numberOfNights;

    private final String[] servicesAndGoodsColumns = {"HH/DV", "GIÁ", "ĐV/LOẠI"};
    private final String[] roomSelectedColumns = {"PHÒNG", "LOẠI", "GIÁ", "S.CHỨA", "TẦNG", "XÓA"};
    private final String[] roomBookingListColumns = {"PHÒNG", "HH-DV", "SL", "ĐV", "GIÁ", "THÀNH TIỀN", "LOẠI", "XÓA"};
    private final String[] roomListColumns = {"PHÒNG", "LOẠI", "GIÁ", "S.CHỨA", "TẦNG"};

    private final UtilDateModel dateModelCheckIn = new UtilDateModel();
    private final UtilDateModel dateModelCheckOut = new UtilDateModel();
    private final DecimalFormat df = new DecimalFormat("#,### VNĐ");

    private JLabel serviceAmount;
    private JLabel roomAmount;
    private JLabel totalAmount;
    private JDatePickerImpl datePickerCheckIn;
    private JDatePickerImpl datePickerCheckOut;

    private final Table servicesAndGoodsTable = new Table();
    private final Table roomBookingTable = new Table();
    private final Table roomTable = new Table();
    private final Table roomSelectedTable = new Table();

    private JComboBox<String> customerComboBox;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> floorComboBox;
    private JComboBox<String> serviceAndGoodComboBox;

    private JSpinner spinnerNumberOfGuests;
    private JTextField textFieldNote;

    private DefaultTableModel servicesAndGoodTableModel;
    private DefaultTableModel roomBookingTableModel;
    private DefaultTableModel roomTableModel;
    private DefaultTableModel roomSelectedTableModel;

    private final EventAction eventActionRoomTable;
    private final EventAction eventActionRoomBookingTable;

    private final CustomerController customerController = new CustomerController();
    private java.util.List<Model_Customer> customerList;
    private java.util.List<Model_Room> rooms;
    private java.util.List<Model_Room> roomAvailable;
    private java.util.List<Object[]> servicesAndGoods;

    private RoomPrice roomPrice;

    private final SqlDateModel modelCheckInTest;
    private final SqlDateModel modelCheckOutTest;
    private final SqlDateModel modelCheckIn;
    private final SqlDateModel modelCheckOut;

    private java.util.List<Model_Invoice> invoices;

    private String purpose;
    private Model_Invoice invoice;
    private boolean isCheckSetingDate;
    private final InvoiceController invoiceController = new InvoiceController();
    private boolean updateSuccessful;

    public Form_GroupBooking(String purpose, Model_Invoice invoice) {
        this.purpose = purpose;
        this.invoice = invoice;
        isCheckSetingDate = false;
        updateSuccessful = false;

        roomMoney = 0;
        serviceAndGoodsMoney = 0;
        totalMoney = 0;
        numberOfGuests = 1;
        note = "Khách theo đoàn";
        dateModelCheckIn.setValue(new Date());
        numberOfNights = 1;

        roomList = new ArrayList<>();
        serviceList = new ArrayList<>();
        productList = new ArrayList<>();
        servicesAndGoods = new ArrayList<>();

        roomAvailable = new ArrayList<>();

        eventActionRoomTable = new EventAction<Model_Invoice_Rooms>() {
            @Override
            public void delete(Model_Invoice_Rooms roomBooking) {
                if (roomSelectedTable.isEditing()) {
                    roomSelectedTable.getCellEditor().stopCellEditing();
                }

                int selectedRow = roomSelectedTable.getSelectedRow();

                if (selectedRow != -1) {
                    String roomName = roomBooking.getName();
                    boolean hasServices = hasServicesInRoom(roomName);

                    if (hasServices) {
                        JOptionPane.showMessageDialog(Form_GroupBooking.this, roomName + " đã chọn dịch vụ. Vui lòng gỡ hết các dịch vụ để xóa chọn phòng.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    } else {
                        DefaultTableModel tableModel = (DefaultTableModel) roomSelectedTable.getModel();
                        tableModel.removeRow(selectedRow);
                        roomList.remove(selectedRow);
                        updateRoomAmount();
                        updateTotalAmount();
                    }
                }
            }

            @Override
            public void update(Model_Invoice_Rooms roomBooking) {

            }
        };

        eventActionRoomBookingTable = new EventAction<Model_Invoice_ServicesAndGoods>() {
            @Override
            public void delete(Model_Invoice_ServicesAndGoods booking) {
                if (roomBookingTable.isEditing()) {
                    roomBookingTable.getCellEditor().stopCellEditing();
                }

                int selectedRow = roomBookingTable.getSelectedRow();
                if (selectedRow != -1) {
                    DefaultTableModel tableModel = (DefaultTableModel) roomBookingTable.getModel();
                    tableModel.removeRow(selectedRow);
                    updateServiceAndGoodsAmount();
                    updateTotalAmount();
                }
            }

            @Override
            public void update(Model_Invoice_ServicesAndGoods booking) {

            }
        };

        // Get the current date
        java.util.Date currentDate = new java.util.Date();
        java.sql.Date sqlCurrentDate = new java.sql.Date(currentDate.getTime());
        modelCheckOutTest = new SqlDateModel(sqlCurrentDate);
        modelCheckInTest = new SqlDateModel(sqlCurrentDate);
        modelCheckOut = new SqlDateModel(sqlCurrentDate);
        modelCheckIn = new SqlDateModel(sqlCurrentDate);

        initComponents();
        initScreen();
        setOpaque(false);

        updateRoomAmount();
        updateServiceAndGoodsAmount();
        updateTotalAmount();

        setupServicesAndGoodsTableModel();
        setupRoomBookingTableModel();
        setupRoomTableModel();
        setupRoomSelectedTableModel();

        loadDataAsync();

        switch (purpose) {
            case "Add":
                break;
            case "Update":
                if (invoice != null) {
                    handleUpdateAction(invoice);
                }
                break;
            default:
                break;
        }
    }

    public boolean isUpdateSuccessful() {
        return updateSuccessful;
    }

    private void handleUpdateAction(Model_Invoice invoice) {
        loadInvoiceData(invoice);
    }

    private void loadInvoiceData(Model_Invoice invoice) {
        isCheckSetingDate = false;
        Date checkInDate = invoice.getCheckInDate();
        dateModelCheckIn.setValue(checkInDate);

        Date checkOutDate = invoice.getCheckOutDate();
        dateModelCheckOut.setValue(checkOutDate);
        isCheckSetingDate = true;

        note = invoice.getNote();
        textFieldNote.setText(note);

        numberOfGuests = invoice.getNumberOfGuests();
        spinnerNumberOfGuests.setValue(numberOfGuests);

        status = invoice.getStatus();
        statusComboBox.setSelectedItem(status);

        roomList = invoice.getRoomList();

        for (Model_Invoice_Rooms invoiceRoom : roomList) {
            roomSelectedTableModel.addRow(invoiceRoom.toRowTable(eventActionRoomTable));
        }

        for (Model_Invoice_ServicesAndGoods invoiceServicesAndGoods : invoice.getProductList()) {
            roomBookingTableModel.addRow(invoiceServicesAndGoods.toRowTable(eventActionRoomBookingTable));
        }

        for (Model_Invoice_ServicesAndGoods invoiceServicesAndGoods : invoice.getServiceList()) {
            roomBookingTableModel.addRow(invoiceServicesAndGoods.toRowTable(eventActionRoomBookingTable));
        }

        updateRoomMoney();
        updateServiceAndGoodsAmount();
        updateTotalAmount();
    }

    private void loadDataAsync() {
        Thread loadDataThread1 = new Thread(() -> {
            initGoodsData();
        });

        Thread loadDataThread2 = new Thread(() -> {
            initServicesData();
        });

        Thread loadDataThread3 = new Thread(() -> {
            initCustomerData();
        });

        Thread loadDataThread4 = new Thread(() -> {
            initRoomData();
        });

        loadDataThread1.start();
        loadDataThread2.start();
        loadDataThread3.start();
        loadDataThread4.start();
    }

    private void initGoodsData() {
        GoodsController goodsController = new GoodsController();
        java.util.List<Model_Goods> goods = goodsController.getAllGoods();

        for (Model_Goods merchandise : goods) {
            servicesAndGoods.add(merchandise.toRowTableInBooking());
            servicesAndGoodTableModel.addRow(merchandise.toRowTableInBooking());
        }
    }

    private void initServicesData() {
        ServiceController serviceController = new ServiceController();
        java.util.List<Model_Service> services = serviceController.getAllServices();

        for (Model_Service service : services) {
            servicesAndGoods.add(service.toRowTableInBooking());
            servicesAndGoodTableModel.addRow(service.toRowTableInBooking());
        }
    }

    private void initCustomerData() {
        customerList = customerController.getAllCustomers();
        updateCustomerComboBox(customerController.getAllNameCustomers(customerList));
    }

    private void initRoomData() {
        roomPrice = new RoomPrice();
        RoomController roomController = new RoomController();
        rooms = roomController.getAllRooms();

        for (Model_Room room : rooms) {
            roomTableModel.addRow(room.toRowTableGroupBooking(roomPrice.getPrice(room.getType())));
        }
    }

    private void updateRoomAmount() {
        roomMoney = 0;
        for (Model_Invoice_Rooms room : roomList) {
            room.setNumberOfNights(numberOfNights);
            room.setTotalRoomPrice((int) numberOfNights * room.getPricePerNight());
            roomMoney += room.getTotalRoomPrice();
        }
        roomAmount.setText(df.format(roomMoney));
    }

    private void updateServiceAndGoodsAmount() {
        serviceAndGoodsMoney = 0;
        int rowCount = roomBookingTable.getRowCount();
        int columnNumber = 4; // Số cột bắt đầu từ 0, ở đây chọn cột số 5

        for (int row = 0; row < rowCount; row++) {
            String priceString = roomBookingTable.getValueAt(row, columnNumber).toString();
            priceString = priceString.replaceAll("[, VNĐ]", "");
            int price = Integer.parseInt(priceString);
            serviceAndGoodsMoney += price;
        }

        serviceAmount.setText(df.format(serviceAndGoodsMoney));
    }

    private void updateTotalAmount() {
        this.totalMoney = roomMoney + serviceAndGoodsMoney;
        totalAmount.setText(df.format(totalMoney));
    }

    private void setupRoomTableModel() {
        roomTableModel = new DefaultTableModel(roomListColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable.setModel(roomTableModel);

        roomTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = roomTable.getSelectedRow();
                if (selectedRow != -1) {
                    String roomName = (String) roomTableModel.getValueAt(selectedRow, 0);

                    // Check if the room is already selected
                    boolean roomAlreadySelected = false;
                    for (int i = 0; i < roomSelectedTableModel.getRowCount(); i++) {
                        String selectedRoomName = (String) roomSelectedTableModel.getValueAt(i, 0);
                        if (selectedRoomName.equals(roomName)) {
                            roomAlreadySelected = true;
                            break;
                        }
                    }

                    if (roomAlreadySelected) {
                        JOptionPane.showMessageDialog(Form_GroupBooking.this, "Phòng này đã được chọn.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // Get other details of the selected room
                        String code = "P" + roomName.substring(6);
                        String roomType = (String) roomTableModel.getValueAt(selectedRow, 1);
                        String priceString = (String) roomTableModel.getValueAt(selectedRow, 2);
                        priceString = priceString.replaceAll("[, VNĐ]", "");
                        int roomPrice = Integer.parseInt(priceString);
                        int roomCapacity = (int) roomTableModel.getValueAt(selectedRow, 3);
                        String roomFloor = (String) roomTableModel.getValueAt(selectedRow, 4);
                        // Create a new row for roomSelectedTableModel
                        Model_Invoice_Rooms newRow = new Model_Invoice_Rooms(code, roomName, roomCapacity, roomType, roomFloor, roomPrice, 1, roomPrice);
                        roomList.add(newRow);
                        // Add the selected room to roomSelectedTableModel
                        roomSelectedTableModel.addRow(newRow.toRowTable(eventActionRoomTable));

                        updateRoomAmount();

                    }
                }
            }
        });

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

    private void setupRoomSelectedTableModel() {
        roomSelectedTableModel = new DefaultTableModel(roomSelectedColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        roomSelectedTable.setModel(roomSelectedTableModel);
    }

    private void setupServicesAndGoodsTableModel() {
        servicesAndGoodTableModel = new DefaultTableModel(servicesAndGoodsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        servicesAndGoodsTable.setModel(servicesAndGoodTableModel);

        servicesAndGoodsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addServicesAndGoodsToBookingTable();
            }
        });

        // Thiết lập kích thước cho các cột
        TableColumn col0 = servicesAndGoodsTable.getColumnModel().getColumn(0);
        col0.setPreferredWidth(40);
        TableColumn col1 = servicesAndGoodsTable.getColumnModel().getColumn(1);
        col1.setPreferredWidth(5);
        TableColumn col2 = servicesAndGoodsTable.getColumnModel().getColumn(2);
        col2.setPreferredWidth(5);
    }

    private void setupRoomBookingTableModel() {
        Object[][] data = {};
        roomBookingTableModel = new DefaultTableModel(data, roomBookingListColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 7;
            }
        };

        roomBookingTableModel.addTableModelListener(
                (TableModelEvent e) -> {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();

                        if (column == 2) {
                            try {
                                int quantity = Integer.parseInt(roomBookingTableModel.getValueAt(row, 2).toString());
                                String priceString = roomBookingTableModel.getValueAt(row, 4).toString();
                                priceString = priceString.replaceAll("[, VNĐ]", "");
                                int price = Integer.parseInt(priceString);
                                int total = quantity * price;
                                roomBookingTableModel.setValueAt(df.format(total), row, 5);
                            } catch (NumberFormatException ex) {
                            }
                        }
                    }
                }
        );

        roomBookingTable.setModel(roomBookingTableModel);

        // Thiết lập trình lắng nghe sự kiện để giới hạn việc nhập liệu chỉ cho phép số
        DefaultCellEditor editor = new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JTextField textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if (!Character.isDigit(c)) {
                            e.consume();
                        }
                    }
                });
                return textField;
            }
        };

        roomBookingTable.getColumnModel().getColumn(2).setCellEditor(editor);

        // Thiết lập kích thước cho các cột
        TableColumn col0 = roomBookingTable.getColumnModel().getColumn(0);
        col0.setPreferredWidth(30);
        TableColumn col2 = roomBookingTable.getColumnModel().getColumn(2);
        col2.setPreferredWidth(5);
        TableColumn col3 = roomBookingTable.getColumnModel().getColumn(3);
        col3.setPreferredWidth(5);
        TableColumn col6 = roomBookingTable.getColumnModel().getColumn(6);
        col6.setPreferredWidth(5);
        TableColumn col7 = roomBookingTable.getColumnModel().getColumn(7);
        col7.setPreferredWidth(3);
    }

    private void filterRoomsByFloor() {
        String selectedFloor = (String) floorComboBox.getSelectedItem();
        roomTableModel.setRowCount(0); // Clear existing rows

        for (Model_Room room : roomAvailable) {
            if (selectedFloor.equals("Tất cả các tầng") || selectedFloor.equals(room.getFloor())) {
                roomTableModel.addRow(new Object[]{room.getName(), room.getType(), df.format(roomPrice.getPrice(room.getType())), room.getCapacity(), room.getFloor()});
            }
        }
    }

    private void filterServiceAndGoods() {
        String selectedServicesOrGoodType = (String) serviceAndGoodComboBox.getSelectedItem();
        servicesAndGoodTableModel.setRowCount(0); // Clear existing rows
        for (Object[] serviceOrGoods : servicesAndGoods) {
            String type = ((String) serviceOrGoods[2]).split("/")[1].trim();
            if (selectedServicesOrGoodType.equals("HH và DV") || selectedServicesOrGoodType.equals(type)) {
                servicesAndGoodTableModel.addRow(new Object[]{serviceOrGoods[0], serviceOrGoods[1], serviceOrGoods[2]});
            }
        }
    }

    private void addServicesAndGoodsToBookingTable() {
        int selectedRow = servicesAndGoodsTable.getSelectedRow();

        if (selectedRow != -1) {
            int selectedRoomRow = roomSelectedTable.getSelectedRow();
            if (selectedRoomRow != -1) {
                String productName = (String) servicesAndGoodsTable.getValueAt(selectedRow, 0);
                String priceString = (String) servicesAndGoodsTable.getValueAt(selectedRow, 1);
                String unitAndType = (String) servicesAndGoodsTable.getValueAt(selectedRow, 2);
                String unit, type;
                String[] parts = unitAndType.split("/");
                unit = parts[0].trim();
                type = parts[1].trim();

                priceString = priceString.replaceAll("[, VNĐ]", "");
                int price = Integer.parseInt(priceString);

                DefaultTableModel tableModel = (DefaultTableModel) roomBookingTable.getModel();
                boolean productExists = false;

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String leftProductName = (String) tableModel.getValueAt(i, 1);

                    if (leftProductName.equals(productName)) {
                        int currentQuantity = (int) tableModel.getValueAt(i, 2);

                        tableModel.setValueAt((int) (currentQuantity + 1), i, 2);

                        int totalPrice = (currentQuantity + 1) * price;
                        tableModel.setValueAt(df.format(totalPrice), i, 5);

                        productExists = true;
                        break;
                    }
                }

                if (!productExists) {
                    // Tạo đối tượng Model_Invoice_ServicesAndGoods
                    Model_Invoice_ServicesAndGoods newItem = new Model_Invoice_ServicesAndGoods(
                            roomSelectedTableModel.getValueAt(selectedRoomRow, 0).toString(), // Room Name
                            productName, // Product Name
                            1, // Quantity
                            price, // Price per unit
                            price, // Total Price
                            unit, // Unit
                            type // Type
                    );

                    // Thêm hàng mới vào bảng
                    tableModel.addRow(newItem.toRowTable(eventActionRoomBookingTable));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng trước khi thêm dịch vụ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ để thêm vào đơn đặt phòng.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }

        updateServiceAndGoodsAmount();
        updateTotalAmount();
    }

    private void saveAction() {
        String selectedName = (String) customerComboBox.getSelectedItem();
        customerSelected = customerController.getCustomersByName(selectedName, customerList);
        status = (String) statusComboBox.getSelectedItem();
        Date bookingDate = new Date();
        Date checkIn = (Date) datePickerCheckIn.getModel().getValue();
        Date checkOut = (Date) datePickerCheckOut.getModel().getValue();
        for (int row = 0; row < roomBookingTableModel.getRowCount(); row++) {
            String roomName = (String) roomBookingTableModel.getValueAt(row, 0);
            String productName = (String) roomBookingTableModel.getValueAt(row, 1);
            int quantity = (int) roomBookingTableModel.getValueAt(row, 2);
            String unit = (String) roomBookingTableModel.getValueAt(row, 3);
            String unitPriceString = (String) roomBookingTableModel.getValueAt(row, 4);
            unitPriceString = unitPriceString.replaceAll("[, VNĐ]", "");
            int unitPrice = Integer.parseInt(unitPriceString);
            String totalPriceString = (String) roomBookingTableModel.getValueAt(row, 5);
            totalPriceString = totalPriceString.replaceAll("[, VNĐ]", "");
            int totalPrice = Integer.parseInt(totalPriceString);
            String type = (String) roomBookingTableModel.getValueAt(row, 6);

            Model_Invoice_ServicesAndGoods servicesAndGoodsUse = new Model_Invoice_ServicesAndGoods(roomName, productName, quantity, unitPrice, totalPrice, unit, type);
            if ("HH".equals(type)) {
                productList.add(servicesAndGoodsUse);
            } else {
                serviceList.add(servicesAndGoodsUse);
            }
        }

        Model_Invoice i = new Model_Invoice(
                customerSelected,
                numberOfGuests,
                note,
                bookingDate,
                checkIn,
                checkOut,
                status,
                roomList, // Danh sách phòng
                productList, // Danh sách sản phẩm
                serviceList, // Danh sách dịch vụ
                totalMoney); // Tổng tiền

        if ("Add".equals(purpose)) {
            invoiceController.addInvoice(i);
            JOptionPane.showMessageDialog(this, "Đã lưu thông tin đặt phòng thành công");
        } else {
            i.setId(invoice.getId());
            invoiceController.updateInvoice(invoice.getId(), i);
            JOptionPane.showMessageDialog(this, "Đã cập nhật thông tin đặt phòng thành công");
            updateSuccessful = true;
        }
    }

    private boolean hasServicesInRoom(String roomName) {
        DefaultTableModel tableModel = (DefaultTableModel) roomBookingTable.getModel();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String rowRoomName = (String) tableModel.getValueAt(i, 0);

            if (rowRoomName.equals(roomName)) {
                return true;
            }
        }

        return false;
    }

    private JTextField createTextField(Dimension size) {
        JTextField textField = new JTextField(note);
        textField.setPreferredSize(size);
        textField.setMinimumSize(size);
        textField.setMaximumSize(size);

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateNote();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateNote();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateNote();
            }

            private void updateNote() {
                note = textField.getText();
            }
        });

        return textField;
    }

    private JComboBox<String> createComboBox(String[] items, Dimension size) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setPreferredSize(size);
        comboBox.setMinimumSize(size);
        comboBox.setMaximumSize(size);
        return comboBox;
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int x, int y) {
        gbc.gridx = x * 2;
        gbc.gridy = y;
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.BLUE);
        panel.add(label, gbc);
        gbc.gridx = x * 2 + 1;
        panel.add(field, gbc);
    }

    private void addPropertyChangeListenerToCheckInDatePicker() {
        datePickerCheckIn.getJFormattedTextField().addPropertyChangeListener("value", evt -> {
            Date checkIn = (Date) datePickerCheckIn.getModel().getValue();
            Date checkOut = (Date) datePickerCheckOut.getModel().getValue();
            if (checkOut != null && checkOut.before(checkIn) && isCheckSetingDate) {
                JOptionPane.showMessageDialog(null, "Ngày trả phòng phải sau ngày đặt.");
            }
        });
    }

    private void addPropertyChangeListenerToCheckOutDatePicker() {
        datePickerCheckOut.getJFormattedTextField().addPropertyChangeListener("value", evt -> {
            Date checkIn = (Date) datePickerCheckIn.getModel().getValue();
            Date checkOut = (Date) datePickerCheckOut.getModel().getValue();
            if (checkIn != null && checkOut != null && checkOut.before(checkIn) && isCheckSetingDate) {
                JOptionPane.showMessageDialog(null, "Ngày trả phòng phải sau ngày đặt.");
            }
        });
    }

    private JDatePickerImpl createDatePicker(UtilDateModel dateModel, boolean isCheckIn) {
        if (isCheckIn) {
            dateModel.setValue(new Date()); // Đặt ngày mặc định là ngày hôm nay cho ngày đặt
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Cộng 1 ngày từ ngày hiện tại
            dateModel.setValue(calendar.getTime());
        }

        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, properties);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        datePicker.addActionListener((ActionEvent e) -> {
            updateRoomMoney();
        });
        Dimension preferredSize = new Dimension(150, datePicker.getPreferredSize().height);
        datePicker.setPreferredSize(preferredSize);
        datePicker.setOpaque(false);
        return datePicker;
    }

    private void updateRoomMoney() {
        Date checkInValue = (Date) datePickerCheckIn.getModel().getValue();
        Date checkOutValue = (Date) datePickerCheckOut.getModel().getValue();

        if (checkInValue != null && checkOutValue != null) {
            LocalDate checkIn = checkInValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOut = checkOutValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            numberOfNights = (int) ChronoUnit.DAYS.between(checkIn, checkOut);
            numberOfNights = Math.max(numberOfNights, 0);
            numberOfNights = (numberOfNights == 0) ? 1 : numberOfNights;

            updateRoomAmount();
            updateTotalAmount();
        }
    }

    private void updateCustomerComboBox(String[] newItems) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(newItems);
        customerComboBox.setModel(model);

        if ("Update".equals(purpose)) {
            String customerName = invoice.getCustomer().getName();
            customerComboBox.setSelectedItem(customerName);
        }
    }

    private JPanel createPanelCheckStatusRoom() {
        JPanel panelCheckStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCheckStatus.setBackground(Color.white);

        // Create labels
        JLabel lblCheckIn = new JLabel("Ngày Check-in:");
        lblCheckIn.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblCheckIn.setForeground(Color.red);
        JLabel lblCheckOut = new JLabel("Ngày Check-out:");
        lblCheckOut.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblCheckOut.setForeground(Color.red);

        // Create date pickers
        JDatePanelImpl datePanelCheckIn = new JDatePanelImpl(modelCheckInTest, new Properties());
        JDatePickerImpl datePickerCheckInTemp = new JDatePickerImpl(datePanelCheckIn, new DateLabelFormatter());
        datePickerCheckInTemp.setOpaque(false);

        JDatePanelImpl datePanelCheckOut = new JDatePanelImpl(modelCheckOutTest, new Properties());
        JDatePickerImpl datePickerCheckOutTemp = new JDatePickerImpl(datePanelCheckOut, new DateLabelFormatter());
        datePickerCheckOutTemp.setOpaque(false);

        // Create check button
        JButton btnCheck = new JButton("Kiểm tra");
        btnCheck.addActionListener(e -> checkStatusRoom((Date) modelCheckInTest.getValue(), (Date) modelCheckOutTest.getValue()));

        // Add components to panel
        panelCheckStatus.add(lblCheckIn);
        panelCheckStatus.add(datePickerCheckInTemp);
        panelCheckStatus.add(lblCheckOut);
        panelCheckStatus.add(datePickerCheckOutTemp);
        panelCheckStatus.add(btnCheck);

        return panelCheckStatus;
    }

    private void checkStatusRoom(Date checkInDate, Date checkOutDate) {
        System.out.println("Checking status from " + checkInDate + " to " + checkOutDate);
        invoices = invoiceController.getInvoicesWithinTimePeriod(checkInDate, checkOutDate);

        roomAvailable.clear();
        roomTableModel.setRowCount(0);
        java.util.List<Model_Room> roomsBooked = new ArrayList<>();

        for (Model_Invoice i : invoices) {
            if ("Đã thanh toán".equals(i.getStatus())) continue;
            roomsBooked.addAll(i.getAllRoomFromInvoice());
        }

        for (Model_Room room : rooms) {
            boolean isBooked = false;
            for (Model_Room bookedRoom : roomsBooked) {
                if (bookedRoom.getName().equals(room.getName())) {
                    isBooked = true;
                    break;
                }
            }
            if (!isBooked) {
                roomAvailable.add(room);
                roomTableModel.addRow(new Object[]{room.getName(), room.getType(), df.format(roomPrice.getPrice(room.getType())), room.getCapacity(), room.getFloor()});
            }
        }
    }

    public final void initScreen() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Left Panel with padding
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        TitledBorder titledRoom = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Phòng trống",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);
        leftPanel.setBorder(titledRoom);

        JScrollPane roomScrollPane = new JScrollPane(roomTable);
        roomScrollPane.getViewport().setOpaque(false);
        leftPanel.add(roomScrollPane, BorderLayout.CENTER);

        // ComboBox chọn tầng
        floorComboBox = new JComboBox<>(new String[]{"Tất cả các tầng", "Tầng 1", "Tầng 2", "Tầng 3", "Tầng 4", "Tầng 5"});
        floorComboBox.addActionListener((ActionEvent e) -> {
            filterRoomsByFloor();
        });
        JPanel filterRoomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterRoomPanel.setOpaque(false);
        filterRoomPanel.add(new JLabel("Tầng:"));
        filterRoomPanel.add(floorComboBox);

        leftPanel.add(filterRoomPanel, BorderLayout.NORTH);

        // Center panel with padding
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        // Customer information section
        JPanel centerTopPanel = new JPanel(new GridBagLayout());
        centerTopPanel.setOpaque(false);
        TitledBorder titledCustomer = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Thông tin khách hàng",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);
        titledCustomer.setTitleColor(Color.RED);
        centerTopPanel.setBorder(titledCustomer);

        JPanel customerFieldsPanel = new JPanel(new GridBagLayout());
        customerFieldsPanel.setOpaque(false);

        centerTopPanel.add(customerFieldsPanel);

        GridBagConstraints gbcCustomer = new GridBagConstraints();
        gbcCustomer.insets = new Insets(5, 5, 5, 5);
        gbcCustomer.anchor = GridBagConstraints.WEST;

        Dimension size = new Dimension(150, 25);

        // Create date pickers for check-in and check-out dates
        datePickerCheckIn = createDatePicker(dateModelCheckIn, true);
        datePickerCheckOut = createDatePicker(dateModelCheckOut, false);
        addPropertyChangeListenerToCheckInDatePicker();
        addPropertyChangeListenerToCheckOutDatePicker();

        // Spinner for "Number of Guests"
        spinnerNumberOfGuests = new JSpinner(new SpinnerNumberModel(1, 1, 150, 1));
        spinnerNumberOfGuests.setPreferredSize(size);
        spinnerNumberOfGuests.addChangeListener((ChangeEvent e) -> {
            numberOfGuests = (int) spinnerNumberOfGuests.getValue();
        });

        customerComboBox = createComboBox(new String[]{"Tải dữ liệu..."}, size);
        customerComboBox.setEditable(true);
        statusComboBox = createComboBox(new String[]{"Chưa thanh toán", "Đã thanh toán"}, size);
        textFieldNote = createTextField(size);

        // Adding customer information fields to customerFieldsPanel
        addLabelAndField(customerFieldsPanel, gbcCustomer, "Khách hàng", customerComboBox, 0, 0);
        addLabelAndField(customerFieldsPanel, gbcCustomer, "Ngày đặt", datePickerCheckIn, 0, 1);
        addLabelAndField(customerFieldsPanel, gbcCustomer, "Ghi chú", textFieldNote, 0, 2);
        addLabelAndField(customerFieldsPanel, gbcCustomer, "Số người", spinnerNumberOfGuests, 1, 0);
        addLabelAndField(customerFieldsPanel, gbcCustomer, "Ngày trả", datePickerCheckOut, 1, 1);
        addLabelAndField(customerFieldsPanel, gbcCustomer, "Trạng thái", statusComboBox, 1, 2);

        // Tạo JPanel centerBetweenPanel
        JPanel centerBetweenPanel = new JPanel(new GridLayout(2, 1));
        centerBetweenPanel.setOpaque(false);
        TitledBorder titledProductList = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Danh sách Phòng và Hàng hóa - Dịch vụ đã chọn",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);
        centerBetweenPanel.setBorder(titledProductList);

        // Tạo bảng roomSelectedTable và đặt vào JScrollPane
        JScrollPane roomSelectedScrollPane = new JScrollPane(roomSelectedTable);
        roomSelectedScrollPane.getViewport().setOpaque(false);
        centerBetweenPanel.add(roomSelectedScrollPane);

        // Tạo bảng roomBookingTable và đặt vào JScrollPane
        JScrollPane roomBookingScrollPane = new JScrollPane(roomBookingTable);
        roomBookingScrollPane.getViewport().setOpaque(false);
        centerBetweenPanel.add(roomBookingScrollPane);

        // Total payment panel
        JPanel centerBottomPanel = new JPanel(new BorderLayout());
        centerBottomPanel.setOpaque(false);
        TitledBorder titledTotal = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Tổng thanh toán",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);
        centerBottomPanel.setBorder(titledTotal);

        // Services and room payment panel
        JPanel totalPanel = new JPanel(new GridLayout(3, 2));
        totalPanel.setOpaque(false);
        JLabel serviceLabel = new JLabel("Tổng tiền dịch vụ", SwingConstants.LEFT);
        serviceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalPanel.add(serviceLabel);
        serviceAmount = new JLabel("");
        serviceAmount.setFont(new Font("Arial", Font.PLAIN, 14));
        serviceAmount.setHorizontalAlignment(JTextField.LEFT);
        totalPanel.add(serviceAmount);

        JLabel roomLabel = new JLabel("Tổng tiền phòng", SwingConstants.LEFT);
        roomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalPanel.add(roomLabel);
        roomAmount = new JLabel("");
        roomAmount.setFont(new Font("Arial", Font.PLAIN, 14));
        roomAmount.setHorizontalAlignment(JTextField.LEFT);
        totalPanel.add(roomAmount);

        JLabel totalLabel = new JLabel("Tổng tiền thanh toán", SwingConstants.LEFT);
        totalLabel.setForeground(Color.RED);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalPanel.add(totalLabel);
        totalAmount = new JLabel("");
        totalAmount.setForeground(Color.RED);
        totalAmount.setFont(new Font("Arial", Font.BOLD, 14));
        totalAmount.setHorizontalAlignment(JTextField.LEFT);
        totalPanel.add(totalAmount);
        centerBottomPanel.add(totalPanel, BorderLayout.CENTER);

        // Add panels to center panel
        centerPanel.add(centerTopPanel, BorderLayout.NORTH);
        centerPanel.add(centerBetweenPanel, BorderLayout.CENTER);
        centerPanel.add(centerBottomPanel, BorderLayout.SOUTH);

        // Services panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        TitledBorder titledService = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Sản phẩm - Dịch vụ",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);
        rightPanel.setBorder(titledService);

        JScrollPane serviceScrollPane = new JScrollPane(servicesAndGoodsTable);
        serviceScrollPane.getViewport().setOpaque(false);
        rightPanel.add(serviceScrollPane, BorderLayout.CENTER);

        // ComboBox chọn dịch vụ hoặc hàng hóa
        serviceAndGoodComboBox = new JComboBox<>(new String[]{"HH và DV", "HH", "DV"});
        serviceAndGoodComboBox.addActionListener((ActionEvent e) -> {
            filterServiceAndGoods();
        });
        JPanel filterServicesAndGoodsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterServicesAndGoodsPanel.setOpaque(false);
        filterServicesAndGoodsPanel.add(new JLabel("Hàng hóa hoặc dịch vụ:"));
        filterServicesAndGoodsPanel.add(serviceAndGoodComboBox);

        rightPanel.add(filterServicesAndGoodsPanel, BorderLayout.NORTH);

        // Add panels to the layout with specified constraints
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(centerPanel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(rightPanel, gbc);

        // Panel for buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        ButtonHover btnSave = new ButtonHover();
        btnSave.setText("Lưu Phiếu");
        btnSave.setBorder(null);
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnSave.addActionListener(e -> saveAction());
        buttonPanel.add(btnSave);
        Dimension buttonSize = new Dimension(150, 35);
        btnSave.setPreferredSize(buttonSize);
        buttonPanel.setMaximumSize(new Dimension(200, 40));
        buttonPanel.setMinimumSize(new Dimension(200, 40));
        buttonPanel.setPreferredSize(new Dimension(200, 40));

        // Add the buttonPanel at the top
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(buttonPanel, gbc);

        JPanel panelCheckStatus = createPanelCheckStatusRoom();
        int borderWidth = 2;
        Color borderColor = new Color(92, 59, 197);
        int radius = 15; // Adjust the radius as needed
        Border roundedBorder = new RoundedBorder(radius, borderColor, borderWidth);
        panelCheckStatus.setBorder(roundedBorder);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(panelCheckStatus, gbc);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
