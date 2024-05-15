package com.myproject.forms;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.myproject.swings.ScrollBar;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.bson.Document;

public class Form_DichVu extends javax.swing.JPanel {

    public Form_DichVu() {
        initComponents();
        jScrollPane1.setVerticalScrollBar(new ScrollBar());
        jScrollPane1.getVerticalScrollBar().setBackground(Color.WHITE);
        jScrollPane1.getViewport().setBackground(Color.WHITE);
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        jScrollPane1.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);


        table1.addRow(new Object[]{"1", "Dịch vụ Massage", "vé", "1000$"});
        table1.addRow(new Object[]{"2", "Dịch vụ tắm trắng", "vé", "1500$"});
        table1.addRow(new Object[]{"3", "Thuê xe", "ngày", "700$"});
        table1.addRow(new Object[]{"4", "Bar", "vé", "500$"});
        table1.addRow(new Object[]{"5", "Xông hơi", "vé" , "1000$"});
        table1.addRow(new Object[]{"6", "Dịch vụ làm Nail", "vé", "500$"});

    }

    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        searchText1 = new com.myproject.swings.SearchText();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        panelBorder1 = new com.myproject.swings.PanelBorder();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new com.myproject.swings.Table();

        searchText1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchText1ActionPerformed(evt);
            }
        });

        jButton1.setText("Thêm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("sansserif", 0, 36));
        jLabel1.setForeground(new java.awt.Color(106, 106, 106));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Dịch Vụ");

        jButton2.setText("Cập nhật");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLayeredPane1.setLayer(searchText1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jButton2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(searchText1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addGap(34, 34, 34))
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Loại Dịch Vụ", "Đơn Vị Tính", "Đơn Giá", "Chức Năng"
            }
        ));
        jScrollPane1.setViewportView(table1);

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane1)
                    .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
    }

    private void searchText1ActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        Add_Service addser = new Add_Service();
        addser.setVisible(true);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.myproject.swings.PanelBorder panelBorder1;
    private com.myproject.swings.SearchText searchText1;
    private com.myproject.swings.Table table1;
}

class Add_Service extends JFrame
{
    private JTextField txtServiceCode;
    private JTextField txtTypeService;
    private JTextField txtUnit;  
    private JTextField txtCostService;

    public Add_Service()
    {
        setTitle("Thêm Loại Dịch Vụ");
        setSize(450, 200);
        setLocationRelativeTo(null);


        //Tạo các label
         JLabel lblServiceCode = new JLabel("Mã Dịch vụ:");
         JLabel lblTypeService = new JLabel("Tên Dịch vụ:");
         JLabel lblUnit = new JLabel("Đơn vị tính:");
         JLabel lblCostService = new JLabel("Đơn Giá:");

        //Tạo các trường nhập

        txtServiceCode = new JTextField(20);
        txtTypeService = new JTextField(20);
        txtUnit = new JTextField(20);
        txtCostService = new JTextField(20);

        JButton btnAdd = new JButton("Thêm");
            btnAdd.addActionListener((ActionEvent e) ->  {
                String servicecode = txtServiceCode.getText();
                String typeservice = txtTypeService.getText();
                String unit = txtUnit.getText();
                String costservice = txtCostService.getText();

                if (servicecode.isEmpty() || typeservice.isEmpty() || unit.isEmpty() || costservice.isEmpty() ) {
                    JOptionPane.showMessageDialog(Add_Service.this, "Vui lòng điền đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (
                        MongoClient mongoClient = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/")) {
                    // Chọn cơ sở dữ liệu
                    MongoDatabase database = mongoClient.getDatabase("Hotel_Management");

                    // Chọn bảng
                    MongoCollection<Document> collection = database.getCollection("User");
                    // Tạo một Document mới chứa dữ liệu bạn muốn thêm
                    Document document = new Document("servicecode", servicecode)
                            .append("typeservice", typeservice)
                            .append("unit", unit)
                            .append("costservice", costservice);
                    // Thêm Document vào bảng
                    collection.insertOne(document);
                }
                catch (MongoException ex) {
                    JOptionPane.showMessageDialog(Add_Service.this, "Lỗi khi cập nhật dịch vụ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }

                // Sau khi xử lý xong, có thể hiển thị thông báo thành công
                JOptionPane.showMessageDialog(Add_Service.this, "Thêm thành công!");

                setVisible(false);
        });
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener((ActionEvent e) -> {
            setVisible(false);
        });

        // Tạo panel và thêm các thành phần vào panel
        JPanel panel1 = new JPanel(new GridLayout(5, 2));
        panel1.add(lblServiceCode);
        panel1.add(txtServiceCode);
        panel1.add(lblTypeService);
        panel1.add(txtTypeService);
        panel1.add(lblUnit);
        panel1.add(txtUnit);
        panel1.add(lblCostService);
        panel1.add(txtCostService);
        panel1.add(btnAdd);
        panel1.add(btnClose);

        // Thêm panel vào frame
        add(panel1);
        setVisible(true);
    }   
}
