package com.myproject.forms.reservation;

import com.myproject.components.action_button.ActionButton;
import com.myproject.components.action_button.TableActionCellEditor;
import com.myproject.components.action_button.TableActionCellRender;
import com.myproject.components.action_button.TableActionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import java.util.Properties;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class BookingForm extends JFrame {

    private ActionButton cmdDelete;

    public BookingForm() {
        initComponents();
    }

    private void initComponents() {
        cmdDelete = new ActionButton();

//        cmdDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/myproject/icons/delete.png"))); // NOI18N
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row : " + row);
            }
            @Override
            public void onDelete(int row) {
                System.out.println("Edit row : " + row);
            }
            @Override
            public void onView(int row) {
                System.out.println("View row: " + row);
            }
        };

        setTitle("Đặt phòng khách lẻ");
        setSize(1000, 600);
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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        Dimension size = new Dimension(150, 25);

        // Date pickers for "Ngày đặt" and "Ngày trả"
        JDatePickerImpl datePickerNgayDat = createDatePicker();
        JDatePickerImpl datePickerNgayTra = createDatePicker();

        // Spinner for "Số lượng người"
        JSpinner spinnerSoNguoi = new JSpinner(new SpinnerNumberModel(1, 1, 150, 1));
        spinnerSoNguoi.setPreferredSize(size);

        // Adding customer information fields
        addLabelAndField(customerInfoPanel, gbc, "Khách hàng", createComboBox(new String[]{"Khách lẻ", "Khách đoàn", "VIP"}, size), 0, 0);
        addLabelAndField(customerInfoPanel, gbc, "Ngày đặt", datePickerNgayDat, 1, 0);
        addLabelAndField(customerInfoPanel, gbc, "Ghi chú", createTextField("Khách lẻ", size), 2, 0);
        addLabelAndField(customerInfoPanel, gbc, "Số người", spinnerSoNguoi, 0, 1);
        addLabelAndField(customerInfoPanel, gbc, "Ngày trả", datePickerNgayTra, 1, 1);
        addLabelAndField(customerInfoPanel, gbc, "Trạng thái", createComboBox(new String[]{"Chưa hoàn tất", "Đã hoàn tất"}, size), 2, 1);

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

        JTable serviceTable = new JTable(new DefaultTableModel(serviceData, serviceColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa
            }
        });
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

        String[] productListColumns = {"PHÒNG", "TÊN SP - DV", "SL", "ĐƠN GIÁ", "THÀNH TIỀN", "XÓA"};
        Object[][] productListData = {
            {"Phòng 107", "Redbull", "2", "20000", "40000"},
            {"Phòng 107", "Cam ép", "1", "15000", "15000"}
        };

        DefaultTableModel productListTableModel = new DefaultTableModel(productListData, productListColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Cho phép chỉnh sửa cột SL (cột có chỉ số 2)
            }

            public void moveColumn(int column, int targetColumn) {
                throw new UnsupportedOperationException("Thay đổi vị trí cột không được hỗ trợ.");
            }
        };

        // Thiết lập trình lắng nghe sự kiện để cập nhật tổng thành tiền khi thay đổi số lượng
        productListTableModel.addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2) {
                int row = e.getFirstRow();
                int quantity = Integer.parseInt(productListTableModel.getValueAt(row, 2).toString());
                int price = Integer.parseInt(productListTableModel.getValueAt(row, 3).toString());
                int total = quantity * price;
                productListTableModel.setValueAt(total, row, 4);
            }
        });

        JTable productListTable = new JTable(productListTableModel);

        // Đặt custom renderer và editor cho các cột chứa nút
//        TableColumn deleteColumn = productListTable.getColumnModel().getColumn(5);
//        deleteColumn.setCellRenderer(new ButtonRenderer(cmdDelete));
//        deleteColumn.setCellEditor(new ButtonEditor(cmdDelete));
        productListTable.getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRender());
        productListTable.getColumnModel().getColumn(5).setCellEditor(new TableActionCellEditor(event));

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

        productListTable.getColumnModel().getColumn(2).setCellEditor(editor);

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
        serviceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalInfoPanel.add(serviceLabel);

        JLabel serviceAmount = new JLabel("55,000 đồng");
        serviceAmount.setFont(new Font("Arial", Font.PLAIN, 14));
        serviceAmount.setHorizontalAlignment(JTextField.LEFT); // Căn lề trái cho nội dung trong ô
        totalInfoPanel.add(serviceAmount);

        JLabel roomLabel = new JLabel("Tổng tiền phòng", SwingConstants.LEFT);
        roomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalInfoPanel.add(roomLabel);

        JLabel roomAmount = new JLabel("1,500,000 đồng");
        roomAmount.setFont(new Font("Arial", Font.PLAIN, 14));
        roomAmount.setHorizontalAlignment(JTextField.LEFT); // Căn lề trái cho nội dung trong ô
        totalInfoPanel.add(roomAmount);

        JLabel totalLabel = new JLabel("Tổng tiền thanh toán", SwingConstants.LEFT);
        totalLabel.setForeground(Color.RED); // Màu đỏ cho nhãn
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalInfoPanel.add(totalLabel);

        JLabel totalAmount = new JLabel("1,535,000 đồng");
        totalAmount.setForeground(Color.RED); // Màu đỏ cho ô
        totalAmount.setFont(new Font("Arial", Font.BOLD, 14));
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
        serviceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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
                        Object[] newRow = {"Phòng 107", productName, "1", String.valueOf(price), String.valueOf(price)};
                        leftTableModel.addRow(newRow);
                    }
                }
            }
        });
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

    private JDatePickerImpl createDatePicker() {
        SqlDateModel model = new SqlDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
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
    // Custom renderer để hiển thị nút trong cột
//    static class ButtonRenderer implements TableCellRenderer {
//        private JButton button;
//
//        public ButtonRenderer(JButton button) {
//            this.button = button;
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            return button;
//        }
//    }
//
//    // Custom editor để xử lý sự kiện khi nút được nhấn
//    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
//        private JButton button;
//
//        public ButtonEditor(JButton button) {
//            this.button = button;
//            this.button.addActionListener(e -> fireEditingStopped());
//        }
//
//        @Override
//        public Object getCellEditorValue() {
//            return null;
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//            return button;
//        }
//    }
}
