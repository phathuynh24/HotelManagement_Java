package com.myproject.forms.reservation;

import com.myproject.components.action_button.CustomTableCellRenderer;
import com.myproject.components.action_button.TableActionCellEditor;
import com.myproject.components.action_button.TableActionCellRender;
import com.myproject.components.action_button.TableActionEvent;
import com.myproject.models.Model_Room;
import com.myproject.models.Model_RoomType;
import com.myproject.models.types.RoomType;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import java.util.Properties;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import org.jdatepicker.impl.UtilDateModel;

public class BookingForm extends JFrame {

    // Test
//    private Model_RoomType roomType = new Model_RoomType(RoomType.SINGLE, 1500000, 1);
//    private Model_Room room = new Model_Room("101", roomType, 1);

    private Model_Room room;
    
    private double serviceMoney; // Số tiền dịch vụ thật
    private double roomMoney; // Số tiền phòng thật
    private double totalMoney; // Tổng số tiền thanh toán

    private JLabel serviceAmount;
    private JLabel roomAmount;
    private JLabel totalAmount;

    // Khởi tạo mẫu cho JDatePicker
    private UtilDateModel dateModelNgayDat = new UtilDateModel();
    private UtilDateModel dateModelNgayTra = new UtilDateModel();

    private JDatePickerImpl datePickerNgayDat;
    private JDatePickerImpl datePickerNgayTra;

    private JTable serviceTable = new JTable();
    private JTable productListTable = new JTable();

    public BookingForm(Model_Room room) {
        this.room = room;
        serviceMoney = 0; // Số tiền dịch vụ thật
        roomMoney = room.getPrice(); // Số tiền phòng thật
        totalMoney = roomMoney + serviceMoney; // Tổng số tiền thanh toán

        dateModelNgayDat.setValue(new Date());

        initComponents();

        updateServiceAmount();
        updateRoomAmount();
        updateTotalAmount();

    }

    private void updateServiceAmount() {
        serviceMoney = 0;
        int rowCount = productListTable.getRowCount();
        int columnNumber = 4; // Số cột bắt đầu từ 0, ở đây chọn cột số 5

        for (int row = 0; row < rowCount; row++) {
            double amount = Double.parseDouble(productListTable.getValueAt(row, columnNumber).toString());
            serviceMoney += amount;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        serviceAmount.setText(decimalFormat.format(serviceMoney) + " VNĐ");

    }

    private void updateRoomAmount() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        roomAmount.setText(decimalFormat.format(roomMoney) + " VNĐ");
    }

    private void updateTotalAmount() {
        this.totalMoney = roomMoney + serviceMoney;
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        totalAmount.setText(decimalFormat.format(totalMoney) + " VNĐ");
    }

    private void initComponents() {
        String[] productListColumns = {"PHÒNG", "TÊN SP - DV", "SL", "ĐƠN GIÁ", "THÀNH TIỀN", "XÓA"};
        Object[][] productListData = {};

        DefaultTableModel productListTableModel = new DefaultTableModel(productListData, productListColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 5; // Cho phép chỉnh sửa cột SL và cột Xóa
            }
        };

        int rowHeight = 40; // Đặt chiều cao hàng theo ý muốn

        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
            }

            @Override
            public void onView(int row) {
            }

            @Override
            public void onDelete(int row) {
                if (productListTable.isEditing()) {
                    productListTable.getCellEditor().stopCellEditing();
                }
                productListTableModel.removeRow(row);

                updateServiceAmount();
                updateTotalAmount();
            }
        };

        setTitle("Đặt phòng khách lẻ");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel thông tin khách hàng
        JPanel customerInfoPanel = new JPanel(new GridBagLayout());
        TitledBorder titledCustomer = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Thông tin khách hàng",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);
        titledCustomer.setTitleColor(Color.RED); // Thiết lập màu đỏ cho tiêu đề

        customerInfoPanel.setBorder(titledCustomer);

        GridBagConstraints gbcCustomer = new GridBagConstraints();
        gbcCustomer.insets = new Insets(5, 5, 5, 5);
        gbcCustomer.anchor = GridBagConstraints.WEST;

        // Panel chứa roomInfo
        JPanel roomInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        roomInfoPanel.setOpaque(true);

        JLabel roomInfo = new JLabel("Phòng " + room.getRoomId() + " - Giá cả: " + room.getPriceFormated() + " VNĐ");
        roomInfo.setForeground(Color.MAGENTA);
        roomInfo.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Căn giữa theo chiều ngang
        GridBagConstraints gbcRoomInfo = new GridBagConstraints();
        gbcRoomInfo.gridx = 0;
        gbcRoomInfo.gridy = 0;
        gbcCustomer.gridwidth = 3;
        gbcRoomInfo.anchor = GridBagConstraints.CENTER;
        gbcRoomInfo.fill = GridBagConstraints.HORIZONTAL;

        roomInfoPanel.add(roomInfo, gbcRoomInfo);

        // Panel chứa các thông tin khách hàng khác
        JPanel customerFieldsPanel = new JPanel(new GridBagLayout());
        customerFieldsPanel.setOpaque(false);

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
        datePickerNgayDat = createDatePicker(dateModelNgayDat, true);
        datePickerNgayTra = createDatePicker(dateModelNgayTra, false);

        // Thêm PropertyChangeListener cho datePickerNgayDat
        datePickerNgayDat.getJFormattedTextField().addPropertyChangeListener("value", evt -> {
            // Lấy ngày đặt và ngày trả từ JDatePickerImpl
            Date ngayDat = (Date) datePickerNgayDat.getModel().getValue();
            Date ngayTra = (Date) datePickerNgayTra.getModel().getValue();

            // Kiểm tra ngày trả
            if (ngayTra != null && ngayTra.before(ngayDat)) {
                // Ngày trả phải sau ngày đặt
                JOptionPane.showMessageDialog(null, "Ngày trả phòng phải sau ngày đặt.");
            }
        });

        // Thêm PropertyChangeListener cho datePickerNgayTra
        datePickerNgayTra.getJFormattedTextField().addPropertyChangeListener("value", evt -> {
            // Lấy ngày đặt và ngày trả từ JDatePickerImpl
            Date ngayDat = (Date) datePickerNgayDat.getModel().getValue();
            Date ngayTra = (Date) datePickerNgayTra.getModel().getValue();

            // Kiểm tra ngày trả
            if (ngayDat != null && ngayTra != null && ngayTra.before(ngayDat)) {
                // Ngày trả phải sau ngày đặt
                JOptionPane.showMessageDialog(null, "Ngày trả phòng phải sau ngày đặt.");
            }
        });

        // Spinner for "Số lượng người"
        JSpinner spinnerSoNguoi = new JSpinner(new SpinnerNumberModel(1, 1, 150, 1));
        spinnerSoNguoi.setPreferredSize(size);
        spinnerSoNguoi.addChangeListener((ChangeEvent e) -> {
            int soNguoi = (int) spinnerSoNguoi.getValue();

            if (soNguoi > room.getCapacity()) {
                JOptionPane.showMessageDialog(null, "Quá số người quy định của phòng!");
                spinnerSoNguoi.setValue(room.getCapacity()); // Đặt lại giá trị thành sức chứa quy định
            }
        });

        // Adding customer information fields to customerFieldsPanel
        addLabelAndField(customerFieldsPanel, gbc, "Khách hàng", createComboBox(new String[]{"Khách lẻ",
            "Khách đoàn", "VIP"}, size), 0, 0);
        addLabelAndField(customerFieldsPanel, gbc,
                "Ngày đặt", datePickerNgayDat, 1, 0);
        addLabelAndField(customerFieldsPanel, gbc,
                "Ghi chú", createTextField("Khách lẻ", size), 2, 0);
        addLabelAndField(customerFieldsPanel, gbc,
                "Số người", spinnerSoNguoi, 0, 1);
        addLabelAndField(customerFieldsPanel, gbc,
                "Ngày trả", datePickerNgayTra, 1, 1);
        addLabelAndField(customerFieldsPanel, gbc, "Trạng thái", createComboBox(new String[]{"Chưa hoàn tất",
            "Đã hoàn tất"}, size), 2, 1);

        // Panel sản phẩm - dịch vụ
        JPanel servicePanel = new JPanel(new BorderLayout());
        TitledBorder titledService = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Sản phẩm - Dịch vụ",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);

        titledService.setTitleColor(Color.RED); // Thiết lập màu đỏ cho tiêu đề

        servicePanel.setBorder(titledService);

        String[] serviceColumns = {"TÊN SP - DV", "ĐƠN GIÁ"};
        Object[][] serviceData = {
            {"Coca Cola", "15000"},
            {"Nước suối", "12000"},
            {"Redbull", "20000"},
            {"Fanta", "15000"},
            {"Cam ép", "15000"},
            {"Trà Ô Long", "15000"}
        };

        DefaultTableModel serviceTableModel = new DefaultTableModel(serviceData, serviceColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa
            }
        };

        serviceTable.setModel(serviceTableModel);

        CustomTableCellRenderer heightCellRenderer = new CustomTableCellRenderer(rowHeight);

        serviceTable.setDefaultRenderer(Object.class,
                heightCellRenderer);

        JScrollPane serviceScrollPane = new JScrollPane(serviceTable);

        servicePanel.add(serviceScrollPane, BorderLayout.CENTER);

        // Panel danh sách sản phẩm - dịch vụ
        JPanel productListPanel = new JPanel(new BorderLayout());
        TitledBorder titledProductList = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Danh sách Sản phẩm - Dịch vụ",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);

        titledProductList.setTitleColor(Color.RED); // Thiết lập màu đỏ cho tiêu đề

        productListPanel.setBorder(titledProductList);

        // Thiết lập trình lắng nghe sự kiện để cập nhật tổng thành tiền khi thay đổi số lượng
        productListTableModel.addTableModelListener(
                (TableModelEvent e) -> {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();

                        // Cập nhật thành tiền khi thay đổi số lượng (cột 2)
                        if (column == 2) {
                            try {
                                int quantity = Integer.parseInt(productListTableModel.getValueAt(row, 2).toString());
                                int price = Integer.parseInt(productListTableModel.getValueAt(row, 3).toString());
                                int total = quantity * price;
                                productListTableModel.setValueAt(total, row, 4);
                            } catch (NumberFormatException ex) {
                            }
                        }
                    }
                }
        );

        productListTable.setModel(productListTableModel);

        productListTable.setDefaultRenderer(Object.class,
                heightCellRenderer);

        TableActionCellRender cellRenderer = new TableActionCellRender();
        TableActionCellEditor cellEditor = new TableActionCellEditor(event);

        // Đặt chỉ gọi nút delete
        cellRenderer.setButtonVisibility(
                false, true, false);
        cellEditor.setButtonVisibility(
                false, true, false);

        // Đặt renderer và editor cho cột chứa nút
        productListTable.getColumnModel()
                .getColumn(5).setCellRenderer(cellRenderer);
        productListTable.getColumnModel()
                .getColumn(5).setCellEditor(cellEditor);

        // Thiết lập kích thước cho cột "XÓA"
        TableColumn deleteColumn = productListTable.getColumnModel().getColumn(5);

        deleteColumn.setPreferredWidth(
                40); // Đặt kích thước mong muốn cho cột

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
                            e.consume(); // Chặn việc nhập liệu không phải số
                        }
                    }
                });
                return textField;
            }
        };

        productListTable.getColumnModel()
                .getColumn(2).setCellEditor(editor);

        JScrollPane productListScrollPane = new JScrollPane(productListTable);

        productListPanel.add(productListScrollPane, BorderLayout.CENTER);

        // Panel tổng thanh toán
        JPanel totalPanel = new JPanel(new BorderLayout());
        TitledBorder titledTotal = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Tổng thanh toán",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14), Color.RED);

        titledTotal.setTitleColor(Color.RED); // Thiết lập màu đỏ cho tiêu đề

        totalPanel.setBorder(titledTotal);

        // Panel dịch vụ và tiền phòng
        JPanel totalInfoPanel = new JPanel(new GridLayout(3, 2));

        JLabel serviceLabel = new JLabel("Tổng tiền dịch vụ", SwingConstants.LEFT);
        serviceLabel.setFont(
                new Font("Arial", Font.PLAIN, 14));
        totalInfoPanel.add(serviceLabel);
        serviceAmount = new JLabel("");
        serviceAmount.setFont(
                new Font("Arial", Font.PLAIN, 14));
        serviceAmount.setHorizontalAlignment(JTextField.LEFT); // Căn lề trái cho nội dung trong ô
        totalInfoPanel.add(serviceAmount);

        JLabel roomLabel = new JLabel("Tổng tiền phòng", SwingConstants.LEFT);
        roomLabel.setFont(
                new Font("Arial", Font.PLAIN, 14));
        totalInfoPanel.add(roomLabel);
        roomAmount = new JLabel("");
        roomAmount.setFont(
                new Font("Arial", Font.PLAIN, 14));
        roomAmount.setHorizontalAlignment(JTextField.LEFT); // Căn lề trái cho nội dung trong ô
        totalInfoPanel.add(roomAmount);

        JLabel totalLabel = new JLabel("Tổng tiền thanh toán", SwingConstants.LEFT);
        totalLabel.setForeground(Color.RED); // Màu đỏ cho nhãn
        totalLabel.setFont(
                new Font("Arial", Font.BOLD, 14));
        totalInfoPanel.add(totalLabel);
        totalAmount = new JLabel("");
        totalAmount.setForeground(Color.RED); // Màu đỏ cho ô
        totalAmount.setFont(
                new Font("Arial", Font.BOLD, 14));
        totalAmount.setHorizontalAlignment(JTextField.LEFT); // Căn lề trái cho nội dung trong ô
        totalInfoPanel.add(totalAmount);
        totalPanel.add(totalInfoPanel, BorderLayout.CENTER);

        // Add panels to main panel with padding
        mainPanel.add(customerInfoPanel, BorderLayout.NORTH);

        mainPanel.add(servicePanel, BorderLayout.EAST);

        mainPanel.add(productListPanel, BorderLayout.CENTER);

        mainPanel.add(totalPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);

        // Add selection listener to productListTable
        serviceTable.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e
            ) {
                // Get the selected row index
                int selectedRow = serviceTable.getSelectedRow();

                // Check if a row is selected
                if (selectedRow != -1) {
                    // Get the data from the selected row in serviceTable
                    String productName = (String) serviceTable.getValueAt(selectedRow, 0);
                    int price = Integer.parseInt((String) serviceTable.getValueAt(selectedRow, 1));

                    // Iterate over the rows in productListTable and update the quantity and unit price
                    DefaultTableModel leftTableModel = (DefaultTableModel) productListTable.getModel();
                    boolean productExists = false; // Flag to track if the product already exists in the table

                    for (int i = 0; i < leftTableModel.getRowCount(); i++) {
                        String leftProductName = (String) leftTableModel.getValueAt(i, 1);
                        if (leftProductName.equals(productName)) {
                            // Update quantity
                            int currentQuantity = Integer.parseInt((String) leftTableModel.getValueAt(i, 2));
                            leftTableModel.setValueAt(String.valueOf(currentQuantity + 1), i, 2);

                            // Calculate total price
                            int totalPrice = (currentQuantity + 1) * price;
                            leftTableModel.setValueAt(String.valueOf(totalPrice), i, 4);

                            productExists = true;
                            break; // Exit the loop after updating the row
                        }
                    }

                    if (!productExists) {
                        // Add a new row to the table
                        Object[] newRow = {"Phòng " + room.getRoomId(), productName, "1", String.valueOf(price), String.valueOf(price)};
                        leftTableModel.addRow(newRow);
                    }
                }
                System.out.println("111112");

                updateServiceAmount();
                updateTotalAmount();
            }
        }
        );
    }

    private JTextField createTextField(String text, Dimension size) {
        JTextField textField = new JTextField(text);
        textField.setPreferredSize(size);
        textField.setMinimumSize(size);
        textField.setMaximumSize(size);
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
        label.setForeground(Color.BLUE); // Đặt màu xanh dương cho các nhãn
        panel.add(label, gbc);
        gbc.gridx = x * 2 + 1;
        panel.add(field, gbc);
    }

    private JDatePickerImpl createDatePicker(UtilDateModel dateModel, boolean isNgayDat) {
        if (isNgayDat) {
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

        return datePicker;
    }

    private void updateRoomMoney() {
        Date ngayDatValue = (Date) datePickerNgayDat.getModel().getValue();
        Date ngayTraValue = (Date) datePickerNgayTra.getModel().getValue();

        if (ngayDatValue != null && ngayTraValue != null) {
            LocalDate ngayDat = ngayDatValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ngayTra = ngayTraValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            long soDemO = ChronoUnit.DAYS.between(ngayDat, ngayTra);
            soDemO = Math.max(soDemO, 0); // Đảm bảo số đêm ở không âm
            soDemO = (soDemO == 0) ? 1 : soDemO; // Đặt số đêm ở thành 1 nếu bằng 0

            roomMoney = soDemO * room.getPrice();
            updateRoomAmount();
            updateTotalAmount();
        }
    }

    private class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws java.text.ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}
