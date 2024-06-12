package com.raven.form.reservation;

import com.raven.controller.CustomerController;
import com.raven.controller.GoodsController;
import com.raven.controller.InvoiceController;
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
import com.raven.swing.table.EventAction;
import com.raven.swing.table.Table;
import com.raven.utils.RoomPrice;
import java.awt.*;
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
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class Form_Booking extends JFrame {

    private String purpose;
    private final Model_Room room;
    private Model_Invoice invoice;
    private int roomPrice;
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

    private final String[] servicesAndGoodsColumns = {"TÊN HH - DV", "GIÁ", "ĐV/LOẠI"};
    private final String[] roomBookingListColumns = {"PHÒNG", "TÊN HH - DV", "SL", "ĐV", "ĐƠN GIÁ", "THÀNH TIỀN", "LOẠI", "XÓA"};

    private final UtilDateModel dateModelCheckIn = new UtilDateModel();
    private final UtilDateModel dateModelCheckOut = new UtilDateModel();
    private final DecimalFormat df = new DecimalFormat("#,### VNĐ");

    private JLabel roomInfo;
    private JLabel serviceAmount;
    private JLabel roomAmount;
    private JLabel totalAmount;
    private JDatePickerImpl datePickerCheckIn;
    private JDatePickerImpl datePickerCheckOut;
    private final Table servicesAndGoodsTable = new Table();
    private final Table roomBookingTable = new Table();
    private JComboBox<String> customerComboBox;
    private JComboBox<String> statusComboBox;
    private JSpinner spinnerNumberOfGuests;
    private JTextField textFieldNote;

    private DefaultTableModel servicesAndGoodTableModel;
    private DefaultTableModel roomBookingTableModel;
    private final EventAction eventAction;

    private final CustomerController customerController = new CustomerController();
    private java.util.List<Model_Customer> customerList;

    private boolean isCheckSetingDate;

    private InvoiceController invoiceController = new InvoiceController();

    private RoomPrice roomPriceUtil;

    public Form_Booking(Model_Room room, String purpose, Model_Invoice invoice) {
//        roomPriceUtil = new RoomPrice();

        this.purpose = purpose;
        this.room = room;
        this.invoice = invoice;
        roomPrice = 0;
        roomMoney = 0;
        serviceAndGoodsMoney = 0;
        totalMoney = 0;
        numberOfGuests = 1;
        note = "Khách lẻ";
        dateModelCheckIn.setValue(new Date());
        numberOfNights = 1;

        roomList = new ArrayList<>();
        serviceList = new ArrayList<>();
        productList = new ArrayList<>();

        eventAction = new EventAction<Model_Invoice_ServicesAndGoods>() {
            @Override
            public void delete(Model_Invoice_ServicesAndGoods booking) {
                if (roomBookingTable.isEditing()) {
                    roomBookingTable.getCellEditor().stopCellEditing();
                }

                // Tìm chỉ số hàng chứa đối tượng cần xóa
                int rowToDelete = -1;
                for (int i = 0; i < roomBookingTableModel.getRowCount(); i++) {
                    if (roomBookingTableModel.getValueAt(i, 1).equals(booking.getName())) {
                        rowToDelete = i;
                        break;
                    }
                }

                // Nếu tìm thấy chỉ số hàng, xóa hàng đó
                if (rowToDelete != -1) {
                    roomBookingTableModel.removeRow(rowToDelete);
                    updateServiceAndGoodsAmount();
                    updateTotalAmount();
                }
            }

            @Override
            public void update(Model_Invoice_ServicesAndGoods booking) {

            }
        };

        isCheckSetingDate = true;

        initComponents();

        updateRoomAmount();
        updateServiceAndGoodsAmount();
        updateTotalAmount();

        setupServicesAndGoodsTableModel();
        setupRoomBookingTableModel();

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

    private void loadDataAsync() {
        Thread loadDataThread1 = new Thread(() -> {
            initRoomInformation();
        });

        Thread loadDataThread2 = new Thread(() -> {
            initGoodsData();
        });

        Thread loadDataThread3 = new Thread(() -> {
            initServicesData();
        });

        Thread loadDataThread4 = new Thread(() -> {
            initCustomerData();
        });

        loadDataThread1.start();
        loadDataThread2.start();
        loadDataThread3.start();
        loadDataThread4.start();
    }

    private void initRoomInformation() {
        roomPriceUtil = new RoomPrice();

        roomPrice = roomPriceUtil.getPrice(room.getType());
        roomMoney = roomPrice;

        roomInfo.setText(room.getName() + " - Giá cả: " + df.format(roomPrice));
        updateRoomAmount();
        updateTotalAmount();
    }

    private void initGoodsData() {
        GoodsController goodsController = new GoodsController();
        java.util.List<Model_Goods> goods = goodsController.getAllGoods();

        for (Model_Goods merchandise : goods) {
            servicesAndGoodTableModel.addRow(merchandise.toRowTableInBooking());
        }
    }

    private void initServicesData() {
        ServiceController serviceController = new ServiceController();
        java.util.List<Model_Service> services = serviceController.getAllServices();

        for (Model_Service service : services) {
            servicesAndGoodTableModel.addRow(service.toRowTableInBooking());
        }
    }

    private void initCustomerData() {
        customerList = customerController.getAllCustomers();
        updateCustomerComboBox(customerController.getAllNameCustomers(customerList));
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

        for (Model_Invoice_ServicesAndGoods invoiceServicesAndGoods : invoice.getProductList()) {
            roomBookingTableModel.addRow(invoiceServicesAndGoods.toRowTable(eventAction));
        }

        for (Model_Invoice_ServicesAndGoods invoiceServicesAndGoods : invoice.getServiceList()) {
            roomBookingTableModel.addRow(invoiceServicesAndGoods.toRowTable(eventAction));
        }

        updateRoomMoney();
        updateServiceAndGoodsAmount();
        updateTotalAmount();
    }

    private void handleUpdateAction(Model_Invoice invoice) {
        loadInvoiceData(invoice);
    }

    private void updateRoomAmount() {
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

    private void setupServicesAndGoodsTableModel() {
        servicesAndGoodTableModel = new DefaultTableModel(servicesAndGoodsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        servicesAndGoodsTable.setModel(servicesAndGoodTableModel);

        // Add selection listener to productListTable
        servicesAndGoodsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addServicesAndGoodsToBookingTable();
            }
        });
    }

    private void setupRoomBookingTableModel() {
        Object[][] data = {};
        roomBookingTableModel = new DefaultTableModel(data, roomBookingListColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 7;
            }
        };

        // Thiết lập trình lắng nghe sự kiện để cập nhật tổng thành tiền khi thay đổi số lượng
        roomBookingTableModel.addTableModelListener(
                (TableModelEvent e) -> {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();

                        // Cập nhật thành tiền khi thay đổi số lượng (cột 2)
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
        // Thiết lập kích thước cho các cột
        TableColumn col3 = roomBookingTable.getColumnModel().getColumn(2);
        col3.setPreferredWidth(20);
        TableColumn col6 = roomBookingTable.getColumnModel().getColumn(6);
        col6.setPreferredWidth(40);

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
    }

    private void initComponents() {
        setTitle("Đặt phòng khách lẻ");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.white);
        setLayout(new BorderLayout());

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Mục điền thông tin khách hàng
        JPanel customerInfoPanel = new JPanel(new GridBagLayout());
        customerInfoPanel.setBackground(Color.white);
        TitledBorder titledCustomer = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Thông tin khách hàng",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);
        customerInfoPanel.setBorder(titledCustomer);
        GridBagConstraints gbcCustomer = new GridBagConstraints();
        gbcCustomer.insets = new Insets(5, 5, 5, 5);
        gbcCustomer.anchor = GridBagConstraints.WEST;

        // Panel chứa roomInfo
        JPanel roomInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        roomInfoPanel.setBackground(Color.white);
        Dimension newDimension = new Dimension(roomInfoPanel.getPreferredSize().width, 25);
        roomInfoPanel.setPreferredSize(newDimension);
        roomInfo = new JLabel(room.getName() + " - Giá cả: " + "### VNĐ");
        roomInfo.setForeground(Color.RED);
        roomInfo.setFont(new Font("SansSerif", Font.BOLD, 16));
        GridBagConstraints gbcRoomInfo = new GridBagConstraints();
        gbcRoomInfo.gridx = 0;
        gbcRoomInfo.gridy = 0;
        gbcCustomer.gridwidth = 3;
        gbcRoomInfo.anchor = GridBagConstraints.CENTER;
        gbcRoomInfo.fill = GridBagConstraints.HORIZONTAL;
        roomInfoPanel.add(roomInfo, gbcRoomInfo);

        // Panel chứa các thông tin khách hàng
        JPanel customerFieldsPanel = new JPanel(new GridBagLayout());
        customerFieldsPanel.setBackground(Color.white);

        // Adding roomInfoPanel to customerInfoPanel
        gbcCustomer.gridx = 0;
        gbcCustomer.gridy = 0;
        gbcCustomer.gridwidth = 3;
        gbcCustomer.fill = GridBagConstraints.HORIZONTAL;
        customerInfoPanel.add(roomInfoPanel, gbcCustomer);

        // Adding customerFieldsPanel to customerInfoPanel
        gbcCustomer.gridx = 0;
        gbcCustomer.gridy = 1;
        gbcCustomer.gridwidth = 3;
        gbcCustomer.fill = GridBagConstraints.NONE;
        customerInfoPanel.add(customerFieldsPanel, gbcCustomer);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        Dimension size = new Dimension(150, 25);

        // Tạo các JDatePickerImpl cho ngày đặt và ngày trả
        datePickerCheckIn = createDatePicker(dateModelCheckIn, true);
        datePickerCheckOut = createDatePicker(dateModelCheckOut, false);
        addPropertyChangeListenerToCheckInDatePicker();
        addPropertyChangeListenerToCheckOutDatePicker();

        // Spinner for "Số lượng người"
        spinnerNumberOfGuests = new JSpinner(new SpinnerNumberModel(1, 1, 150, 1));
        spinnerNumberOfGuests.setPreferredSize(size);
        spinnerNumberOfGuests.addChangeListener((ChangeEvent e) -> {
            numberOfGuests = (int) spinnerNumberOfGuests.getValue();

            if (numberOfGuests > room.getCapacity()) {
                JOptionPane.showMessageDialog(null, "Quá số người quy định của phòng!");
                spinnerNumberOfGuests.setValue(room.getCapacity());
            }
        });

        customerComboBox = createComboBox(new String[]{"Tải dữ liệu..."}, size);
        customerComboBox.setEditable(true);
        statusComboBox = createComboBox(new String[]{"Chưa thanh toán", "Đã thanh toán"}, size);

        textFieldNote = createTextField(size);

        // Adding customer information fields to customerFieldsPanel
        addLabelAndField(customerFieldsPanel, gbc, "Khách hàng", customerComboBox, 0, 0);
        addLabelAndField(customerFieldsPanel, gbc, "Ngày đặt", datePickerCheckIn, 1, 0);
        addLabelAndField(customerFieldsPanel, gbc, "Ghi chú", textFieldNote, 2, 0);
        addLabelAndField(customerFieldsPanel, gbc, "Số người", spinnerNumberOfGuests, 0, 1);
        addLabelAndField(customerFieldsPanel, gbc, "Ngày trả", datePickerCheckOut, 1, 1);
        addLabelAndField(customerFieldsPanel, gbc, "Trạng thái", statusComboBox, 2, 1);

        // Panel sản phẩm - dịch vụ
        JPanel servicePanel = new JPanel(new BorderLayout());
        servicePanel.setBackground(Color.white);
        TitledBorder titledService = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Sản phẩm - Dịch vụ",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);
        servicePanel.setBorder(titledService);

        JScrollPane serviceScrollPane = new JScrollPane(servicesAndGoodsTable);
        servicePanel.add(serviceScrollPane, BorderLayout.CENTER);

        // Panel danh sách sản phẩm - dịch vụ
        JPanel productListPanel = new JPanel(new BorderLayout());
        productListPanel.setBackground(Color.white);
        TitledBorder titledProductList = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Danh sách Hàng hóa - Dịch vụ",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);
        productListPanel.setBorder(titledProductList);

        JScrollPane productListScrollPane = new JScrollPane(roomBookingTable);
        productListPanel.add(productListScrollPane, BorderLayout.CENTER);

        // Panel tổng thanh toán
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(Color.white);
        TitledBorder titledTotal = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Tổng thanh toán",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);
        totalPanel.setBorder(titledTotal);

        // Panel dịch vụ và tiền phòng
        JPanel totalInfoPanel = new JPanel(new GridLayout(3, 2));
        totalInfoPanel.setBackground(Color.white);
        JLabel serviceLabel = new JLabel("Tổng tiền dịch vụ", SwingConstants.LEFT);
        serviceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalInfoPanel.add(serviceLabel);
        serviceAmount = new JLabel("");
        serviceAmount.setFont(new Font("Arial", Font.PLAIN, 14));
        serviceAmount.setHorizontalAlignment(JTextField.LEFT);
        totalInfoPanel.add(serviceAmount);

        JLabel roomLabel = new JLabel("Tổng tiền phòng", SwingConstants.LEFT);
        roomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalInfoPanel.add(roomLabel);
        roomAmount = new JLabel("");
        roomAmount.setFont(new Font("Arial", Font.PLAIN, 14));
        roomAmount.setHorizontalAlignment(JTextField.LEFT);
        totalInfoPanel.add(roomAmount);

        JLabel totalLabel = new JLabel("Tổng tiền thanh toán", SwingConstants.LEFT);
        totalLabel.setForeground(Color.RED);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalInfoPanel.add(totalLabel);
        totalAmount = new JLabel("");
        totalAmount.setForeground(Color.RED);
        totalAmount.setFont(new Font("Arial", Font.BOLD, 14));
        totalAmount.setHorizontalAlignment(JTextField.LEFT);
        totalInfoPanel.add(totalAmount);
        totalPanel.add(totalInfoPanel, BorderLayout.CENTER);

        // Add panels to main panel with padding
        mainPanel.add(customerInfoPanel, BorderLayout.NORTH);
        mainPanel.add(servicePanel, BorderLayout.EAST);
        mainPanel.add(productListPanel, BorderLayout.CENTER);
        mainPanel.add(totalPanel, BorderLayout.SOUTH);

        // Panel for buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.white);
        ButtonHover btnSave = new ButtonHover();
        ButtonHover btnExit = new ButtonHover();

        btnSave.setText("Lưu");
        btnSave.setBorder(null);
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 14));

        btnExit.setText("Thoát");
        btnExit.setBorder(null);
        btnExit.setFont(new java.awt.Font("Segoe UI", 1, 14));

        btnSave.addActionListener(e -> saveAction());
        btnExit.addActionListener(e -> exitAction());

        Dimension buttonSize = new Dimension(100, 30);
        btnSave.setPreferredSize(buttonSize);
        btnExit.setPreferredSize(buttonSize);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnExit);

        add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void addServicesAndGoodsToBookingTable() {
        int selectedRow = servicesAndGoodsTable.getSelectedRow();

        if (selectedRow != -1) {
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

                    tableModel.setValueAt(currentQuantity + 1, i, 2);

                    int totalPrice = (currentQuantity + 1) * price;
                    tableModel.setValueAt(df.format(totalPrice), i, 5);

                    productExists = true;
                    break;
                }
            }

            if (!productExists) {
                tableModel.addRow(new Model_Invoice_ServicesAndGoods(room.getName(), productName, 1, price, price, unit, type).toRowTable(eventAction));
            }
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
        roomList.add(new Model_Invoice_Rooms(room, roomPrice, (int) numberOfNights, roomMoney));
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
        }
    }

    private void exitAction() {
        dispose();
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

            roomMoney = (int) (numberOfNights * roomPrice);
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
}
