package com.raven.form;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.myproject.raven.chart.ColumnChart;
import com.myproject.raven.chart.ModelChart;
import com.raven.controller.InvoiceController;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import static java.lang.Integer.parseInt;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.bson.Document;

public class Form_Statistic extends javax.swing.JPanel {

    private final InvoiceController invoiceController = new InvoiceController();

    public Form_Statistic() {
        initComponents();
        setOpaque(false);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        label1 = new JLabel();
        label1.setText("Chọn thời gian bắt đầu:");
        label2 = new JLabel();
        label2.setText("Chọn thời gian kết thúc:");

        cbBox1 = new JComboBox();
        cbBox3 = new JComboBox();
        for (int i = 1; i < 13; i++) {
            String temp = "Tháng " + i;
            cbBox1.addItem(temp);
            cbBox3.addItem(temp);
        }
        cbBox1.setSelectedIndex(3);
        cbBox3.setSelectedIndex(5);

        cbBox2 = new JComboBox();
        cbBox4 = new JComboBox();
        for (int i = 2021; i < 2025; i++) {
            String temp = "Năm " + i;
            cbBox2.addItem(temp);
            cbBox4.addItem(temp);
        }
        cbBox2.setSelectedIndex(3);
        cbBox4.setSelectedIndex(3);

        cbBox1.setBackground(new java.awt.Color(28, 181, 224));
        cbBox1.setForeground(java.awt.Color.WHITE);
        cbBox2.setBackground(new java.awt.Color(28, 181, 224));
        cbBox2.setForeground(java.awt.Color.WHITE);
        cbBox3.setBackground(new java.awt.Color(28, 181, 224));
        cbBox3.setForeground(java.awt.Color.WHITE);
        cbBox4.setBackground(new java.awt.Color(28, 181, 224));
        cbBox4.setForeground(java.awt.Color.WHITE);

        btn1 = new JButton();
        btn1.setText("Cập nhật thông tin cho biểu đồ");
        btn1.setBackground(new java.awt.Color(28, 181, 224));
        btn1.setForeground(java.awt.Color.WHITE);
        btn1.addActionListener((java.awt.event.ActionEvent evt) -> {
            btn1ActionPerformed(evt);
        });

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new java.awt.Color(28, 181, 224));
        tabbedPane.setForeground(java.awt.Color.WHITE);

        panel1 = new JPanel();
        setDataToColumnChart1(panel1);
        tabbedPane.addTab("Tổng doanh thu trong từng tháng", panel1);

        panel2 = new JPanel();
        setDataToColumnChart2(panel2);
        tabbedPane.addTab("Thành phần doanh thu trong từng tháng theo loại dịch vụ", panel2);

        panel3 = new JPanel();
        setDataToColumnChart3(panel3);
        tabbedPane.addTab("Thành phần doanh thu trong từng tháng theo loại phòng ", panel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(tabbedPane, 900, 900, Short.MAX_VALUE)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, Short.MAX_VALUE)
                                                .addComponent(cbBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                                                .addGap(5)
                                                .addComponent(cbBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                                                .addGap(200)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, Short.MAX_VALUE)
                                                .addComponent(cbBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                                                .addGap(5)
                                                .addComponent(cbBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                                                .addGap(200)
                                        )
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGap(190)
                                                .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
                                                .addGap(480)
                                        )
                                )
                        )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                )
                                .addGap(5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                )
                                .addGap(30)
                                .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40)
                                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

    } 

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {
        setDataToColumnChart1(panel1);
        setDataToColumnChart2(panel2);
        setDataToColumnChart3(panel3);
    }

    public void setDataToColumnChart1(JPanel jpnItem) {

        ColumnChart chart = new ColumnChart();

        chart.addLegend("Tổng doanh thu theo tháng", new Color(28, 181, 224));

        //start
        String monthStart = (String) cbBox1.getSelectedItem();
        monthStart = monthStart.substring(6);
        String yearStart = (String) cbBox2.getSelectedItem();
        yearStart = yearStart.substring(4);

        //end
        String monthEnd = (String) cbBox3.getSelectedItem();
        monthEnd = monthEnd.substring(6);
        String yearEnd = (String) cbBox4.getSelectedItem();
        yearEnd = yearEnd.substring(4);

        int gap = (parseInt(yearEnd) - parseInt(yearStart)) * 12;
        int temp = 0;

        for (int i = parseInt(monthStart); i <= parseInt(monthEnd) + gap; i++) {

            if (i > 12) {
                i -= 12;
                gap -= 12;
                temp++;
            }

            String timeStart;
            String timeEnd;

            switch (i) {
                case 10:
                case 11:
                case 12: {
                    timeStart = yearStart + "-";
                    timeEnd = yearEnd + "-";
                    break;
                }
                default:
                    timeStart = yearStart + "-0";
                    timeEnd = yearEnd + "-0";
                    break;
            }

            switch (i) {
                case 2: {
                    timeStart += i + "-01T00:00:00.000+00:00";
                    timeEnd += i + "-28T23:59:59.000+00:00";
                    break;
                }
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12: {
                    timeStart += i + "-01T00:00:00.000+00:00";
                    timeEnd += i + "-31T23:59:59.000+00:00";
                    break;
                }
                default: {
                    timeStart += i + "-01T00:00:00.000+00:00";
                    timeEnd += i + "-30T23:59:59.000+00:00";
                    break;
                }
            }

            Instant from = Instant.parse(timeStart);
            Instant to = Instant.parse(timeEnd);

            Document filter = new Document("$and", Arrays.asList(
                    Filters.eq("status", "Đã thanh toán"),
                    Filters.gte("checkOutDate", from),
                    Filters.lte("checkOutDate", to)
            ));

            FindIterable<Document> cursor = invoiceController.invoiceCollection.find(filter);
            double sum = 0;
            for (Document document : cursor) {
                double income = document.getInteger("totalAmount") * 1.0;
                sum = sum + income;
            }
            chart.addData(new ModelChart(i + "/" + (parseInt(yearStart) + temp), new double[]{sum}));
        }

        jpnItem.removeAll();
        jpnItem.setLayout(new CardLayout());
        jpnItem.add(chart);
        jpnItem.validate();
        jpnItem.repaint();
    }

    public void setDataToColumnChart2(JPanel jpnItem) {

        ColumnChart chart = new ColumnChart();

        chart.addLegend("Massage", new Color(153, 0, 204));
        chart.addLegend("Spa", new Color(102, 102, 102));
        chart.addLegend("Chăm sóc da", new Color(255, 0, 0));
        chart.addLegend("Nhà hàng", new Color(255, 153, 0));
        chart.addLegend("Phòng họp, sự kiện", new Color(255, 153, 255));
        chart.addLegend("Giặt là", new Color(0, 102, 102));
        chart.addLegend("Đặt vé, tour du lịch", new Color(0, 204, 204));
        chart.addLegend("Đưa đón sân bay", new Color(255, 204, 204));

        //start
        String monthStart = (String) cbBox1.getSelectedItem();
        monthStart = monthStart.substring(6);
        String yearStart = (String) cbBox2.getSelectedItem();
        yearStart = yearStart.substring(4);

        //end
        String monthEnd = (String) cbBox3.getSelectedItem();
        monthEnd = monthEnd.substring(6);
        String yearEnd = (String) cbBox4.getSelectedItem();
        yearEnd = yearEnd.substring(4);

        int gap = (parseInt(yearEnd) - parseInt(yearStart)) * 12;
        int temp = 0;

        for (int i = parseInt(monthStart); i <= parseInt(monthEnd) + gap; i++) {

            if (i > 12) {
                i -= 12;
                gap -= 12;
                temp++;
            }

            String timeStart;
            String timeEnd;

            switch (i) {
                case 10:
                case 11:
                case 12: {
                    timeStart = yearStart + "-";
                    timeEnd = yearEnd + "-";
                    break;
                }
                default:
                    timeStart = yearStart + "-0";
                    timeEnd = yearEnd + "-0";
                    break;
            }

            switch (i) {
                case 2: {
                    timeStart += i + "-01T00:00:00.000+00:00";
                    timeEnd += i + "-28T23:59:59.000+00:00";
                    break;
                }
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12: {
                    timeStart += i + "-01T00:00:00.000+00:00";
                    timeEnd += i + "-31T23:59:59.000+00:00";
                    break;
                }
                default: {
                    timeStart += i + "-01T00:00:00.000+00:00";
                    timeEnd += i + "-30T23:59:59.000+00:00";
                    break;
                }
            }

            Instant from = Instant.parse(timeStart);
            Instant to = Instant.parse(timeEnd);

            Document filter = new Document("$and", Arrays.asList(
                    Filters.eq("status", "Đã thanh toán"),
                    Filters.gte("checkOutDate", from),
                    Filters.lte("checkOutDate", to)
            ));

            FindIterable<Document> cursor = invoiceController.invoiceCollection.find(filter);

            double sum1 = 0;
            double sum2 = 0;
            double sum3 = 0;
            double sum4 = 0;
            double sum5 = 0;
            double sum6 = 0;
            double sum7 = 0;
            double sum8 = 0;

            for (Document document : cursor) {
                List<Object> services = document.get("serviceList", new ArrayList<>());
                for (Object object : services) {
                    Document service = (Document) object;
                    double income = service.getInteger("totalPrice") * 1.0;
                    String name = service.getString("name");
                    switch (name) {
                        case "Dịch vụ massage": {
                            sum1 += income;
                            break;
                        }
                        case "Dịch vụ spa": {
                            sum2 += income;
                            break;
                        }
                        case "Dịch vụ chăm sóc da": {
                            sum3 += income;
                            break;
                        }
                        case "Dịch vụ nhà hàng": {
                            sum4 += income;
                            break;
                        }
                        case "Dịch vụ phòng họp và sự kiện": {
                            sum5 += income;
                            break;
                        }
                        case "Dịch vụ giặt là": {
                            sum6 += income;
                            break;
                        }
                        case "Dịch vụ đặt vé và tour du lịch": {
                            sum7 += income;
                            break;
                        }
                        case "Dịch vụ đưa đón sân bay": {
                            sum8 += income;
                            break;
                        }
                    }
                }
            }
            chart.addData(new ModelChart(i + "/" + (parseInt(yearStart) + temp), new double[]{sum1, sum2, sum3, sum4,
                sum5, sum6, sum7, sum8}));
        }
        jpnItem.removeAll();
        jpnItem.setLayout(new CardLayout());
        jpnItem.add(chart);
        jpnItem.validate();
        jpnItem.repaint();
    }

    public void setDataToColumnChart3(JPanel jpnItem) {

        ColumnChart chart = new ColumnChart();

        chart.addLegend("Phòng đôi", new Color(153, 0, 204));
        chart.addLegend("Phòng đôi có hai giường đơn", new Color(102, 102, 102));
        chart.addLegend("Phòng suite", new Color(255, 0, 0));
        chart.addLegend("Phòng tiêu chuẩn", new Color(255, 153, 0));
        chart.addLegend("Phòng sang trọng", new Color(255, 153, 255));
        chart.addLegend("Phòng doanh nghiệp", new Color(0, 102, 102));
        chart.addLegend("Phòng gia đình", new Color(0, 204, 204));
        chart.addLegend("Phòng kết nối", new Color(255, 204, 204));
        chart.addLegend("Phòng cao cấp trên tầng cao nhất", new Color(200, 204, 204));
        chart.addLegend("Phòng trang trí đặc biệt dành cho cặp đôi mới cưới", new Color(255, 154, 204));
        chart.addLegend("Phòng đơn", new Color(255, 104, 204));

        //start
        String monthStart = (String) cbBox1.getSelectedItem();
        monthStart = monthStart.substring(6);
        String yearStart = (String) cbBox2.getSelectedItem();
        yearStart = yearStart.substring(4);

        //end
        String monthEnd = (String) cbBox3.getSelectedItem();
        monthEnd = monthEnd.substring(6);
        String yearEnd = (String) cbBox4.getSelectedItem();
        yearEnd = yearEnd.substring(4);

        int gap = (parseInt(yearEnd) - parseInt(yearStart)) * 12;
        int temp = 0;

        for (int i = parseInt(monthStart); i <= parseInt(monthEnd) + gap; i++) {

            if (i > 12) {
                i -= 12;
                gap -= 12;
                temp++;
            }

            String timeStart;
            String timeEnd;

            switch (i) {
                case 10:
                case 11:
                case 12: {
                    timeStart = yearStart + "-";
                    timeEnd = yearEnd + "-";
                    break;
                }
                default:
                    timeStart = yearStart + "-0";
                    timeEnd = yearEnd + "-0";
                    break;
            }

            switch (i) {
                case 2: {
                    timeStart += i + "-01T00:00:00.000+00:00";
                    timeEnd += i + "-28T23:59:59.000+00:00";
                    break;
                }
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12: {
                    timeStart += i + "-01T00:00:00.000+00:00";
                    timeEnd += i + "-31T23:59:59.000+00:00";
                    break;
                }
                default: {
                    timeStart += i + "-01T00:00:00.000+00:00";
                    timeEnd += i + "-30T23:59:59.000+00:00";
                    break;
                }
            }

            Instant from = Instant.parse(timeStart);
            Instant to = Instant.parse(timeEnd);

            Document filter = new Document("$and", Arrays.asList(
                    Filters.eq("status", "Đã thanh toán"),
                    Filters.gte("checkOutDate", from),
                    Filters.lte("checkOutDate", to)
            ));

            FindIterable<Document> cursor = invoiceController.invoiceCollection.find(filter);

            double sum1 = 0;
            double sum2 = 0;
            double sum3 = 0;
            double sum4 = 0;
            double sum5 = 0;
            double sum6 = 0;
            double sum7 = 0;
            double sum8 = 0;
            double sum9 = 0;
            double sum10 = 0;
            double sum11 = 0;

            for (Document document : cursor) {
                List<Object> rooms = document.get("roomList", new ArrayList<>());
                for (Object object : rooms) {
                    Document room = (Document) object;
                    double income = room.getInteger("totalRoomPrice") * 1.0;
                    String type = room.getString("type");
                    switch (type) {
                        case "SINGLE": {
                            sum1 += income;
                            break;
                        }
                        case "DOUBLE": {
                            sum2 += income;
                            break;
                        }
                        case "TWIN": {
                            sum3 += income;
                            break;
                        }
                        case "SUITE": {
                            sum4 += income;
                            break;
                        }
                        case "STANDARD": {
                            sum5 += income;
                            break;
                        }
                        case "DELUXE": {
                            sum6 += income;
                            break;
                        }
                        case "EXECUTIVE": {
                            sum7 += income;
                            break;
                        }
                        case "FAMILY": {
                            sum8 += income;
                            break;
                        }
                        case "CONNECTING": {
                            sum9 += income;
                            break;
                        }
                        case "PENTHOUSE_SUITE": {
                            sum10 += income;
                            break;
                        }
                        case "HONEYMOON_SUITE": {
                            sum11 += income;
                            break;
                        }
                    }
                }
            }
            chart.addData(new ModelChart(i + "/" + (parseInt(yearStart) + temp), new double[]{sum1, sum2, sum3, sum4,
                sum5, sum6, sum7, sum8, sum9, sum10, sum11}));
        }
        jpnItem.removeAll();
        jpnItem.setLayout(new CardLayout());
        jpnItem.add(chart);
        jpnItem.validate();
        jpnItem.repaint();
    }

    private JTabbedPane tabbedPane;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JLabel label1;
    private JLabel label2;
    private JComboBox cbBox1;
    private JComboBox cbBox2;
    private JComboBox cbBox3;
    private JComboBox cbBox4;
    private JButton btn1;
}
