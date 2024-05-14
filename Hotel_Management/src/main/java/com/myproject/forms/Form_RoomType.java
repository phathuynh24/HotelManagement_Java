package com.myproject.forms;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.myproject.swings.SearchText;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;

public class Form_RoomType extends javax.swing.JPanel {

    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTable roomTypeTable;
    private JScrollPane jScrollPane1;
    private JTextField searchField;

    public Form_RoomType() {
        initComponents();
        fetchDataFromMongoDB();
        setupUI();
        setupEvents();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        searchField = new SearchText();
        jScrollPane1 = new javax.swing.JScrollPane();
        
        jScrollPane1.setViewportView(roomTypeTable);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(addButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(editButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(deleteButton)
                                                .addGap(20, 20, 20))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addButton)
                                        .addComponent(editButton)
                                        .addComponent(deleteButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }
    
    private void setupEvents() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xử lý khi nhấn nút Thêm
                showAddDialog();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = roomTypeTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Lấy các giá trị từ hàng được chọn
                    String roomTypeName = roomTypeTable.getValueAt(selectedRow, 0).toString();
                    String roomTypeDescription = roomTypeTable.getValueAt(selectedRow, 1).toString();
                    String roomTypePrice = roomTypeTable.getValueAt(selectedRow, 2).toString();

                    // Hiển thị dialog sửa đổi với các giá trị từ hàng được chọn
                    showEditDialog(roomTypeName, roomTypeDescription, roomTypePrice);
                } else {
                    JOptionPane.showMessageDialog(Form_RoomType.this, "Vui lòng chọn một loại phòng để sửa.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xử lý khi nhấn nút Xóa
                int selectedRow = roomTypeTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Xác nhận xóa và xóa loại phòng khỏi cơ sở dữ liệu
                    int option = JOptionPane.showConfirmDialog(Form_RoomType.this, "Bạn có chắc muốn xóa loại phòng này không?", "Xác nhận Xóa", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        deleteSelectedRoom();
                    }
                } else {
                    JOptionPane.showMessageDialog(Form_RoomType.this, "Vui lòng chọn một loại phòng để xóa.");
                }
            }
        });
    
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
//                searchGoods(searchField.getText());
            }
        });
    }

    private void showAddDialog() {
        JTextField nameField = new JTextField(30);
        JTextArea descriptionArea = new JTextArea(3, 30);
        JTextField priceField = new JTextField(30);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Tên loại phòng:"), constraints);

        constraints.gridx = 1;
        panel.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Giá loại phòng:"), constraints);

        constraints.gridx = 1;
        panel.add(priceField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(new JLabel("Mô tả loại phòng:"), constraints);

        constraints.gridx = 1;
        panel.add(new JScrollPane(descriptionArea), constraints);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm loại phòng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String roomTypeName = nameField.getText();
            String roomTypeDescription = descriptionArea.getText();
            String roomTypePrice = priceField.getText();
            if (roomTypeName.isEmpty() || roomTypeDescription.isEmpty() || roomTypePrice.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
                return;
            }
            try (MongoClient mongoClient = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/")) {
                MongoDatabase database = mongoClient.getDatabase("Hotel_Management");
                MongoCollection<Document> collection = database.getCollection("RoomType");
                Document document = new Document("RoomTypeName", roomTypeName)
                        .append("Description", roomTypeDescription)
                        .append("Price", roomTypePrice);
                collection.insertOne(document);
                fetchDataFromMongoDB();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    private void showEditDialog(String roomTypeName, String roomTypeDescription, String roomTypePrice) {
        JTextField nameField = new JTextField(roomTypeName, 20);
        JTextArea descriptionArea = new JTextArea(roomTypeDescription, 5, 20);
        JTextField priceField = new JTextField(roomTypePrice, 10);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Tên loại phòng:"), constraints);

        constraints.gridx = 1;
        panel.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Mô tả:"), constraints);

        constraints.gridx = 1;
        panel.add(new JScrollPane(descriptionArea), constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(new JLabel("Giá loại phòng:"), constraints);

        constraints.gridx = 1;
        panel.add(priceField, constraints);

        int result = JOptionPane.showConfirmDialog(this, panel, "Sửa loại phòng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Lấy các giá trị mới từ dialog
            String newRoomTypeName = nameField.getText();
            String newRoomTypeDescription = descriptionArea.getText();
            String newRoomTypePrice = priceField.getText();

            // Cập nhật dữ liệu trong cơ sở dữ liệu
            updateRoomTypeInDatabase(roomTypeName, newRoomTypeName, newRoomTypeDescription, newRoomTypePrice);

            // Cập nhật lại JTable
            fetchDataFromMongoDB();
        }
    }

    private void updateRoomTypeInDatabase(String oldRoomTypeName, String newRoomTypeName, String newRoomTypeDescription, String newRoomTypePrice) {
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/")) {
            MongoDatabase database = mongoClient.getDatabase("Hotel_Management");
            MongoCollection<Document> collection = database.getCollection("RoomType");

            // Tìm và cập nhật dòng tương ứng trong cơ sở dữ liệu
            collection.updateOne(
                    new Document("RoomTypeName", oldRoomTypeName),
                    new Document("$set", new Document("RoomTypeName", newRoomTypeName)
                            .append("Description", newRoomTypeDescription)
                            .append("Price", newRoomTypePrice)
                    )
            );
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void deleteSelectedRoom() {
        // Lấy chỉ mục hàng được chọn trong JTable
        int selectedRow = roomTypeTable.getSelectedRow();
        if (selectedRow != -1) {
            // Lấy giá trị cột RoomName tại hàng được chọn
            String roomName = roomTypeTable.getValueAt(selectedRow, 0).toString();

            try (MongoClient mongoClient = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/")) {
                // Chọn cơ sở dữ liệu
                MongoDatabase database = mongoClient.getDatabase("Hotel_Management");

                // Chọn bảng
                MongoCollection<Document> collection = database.getCollection("RoomType");

                // Tạo một Document để xóa dựa trên RoomName
                Document query = new Document("RoomTypeName", roomName);

                // Xóa Document từ bảng
                collection.deleteOne(query);

                // Sau khi xóa từ cơ sở dữ liệu, cập nhật lại JTable
                fetchDataFromMongoDB();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(Form_RoomType.this, "Vui lòng chọn một loại phòng để xóa.");
        }
    }

    private void fetchDataFromMongoDB() {
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/")) {
            // Chọn cơ sở dữ liệu
            MongoDatabase database = mongoClient.getDatabase("Hotel_Management");

            // Chọn bảng
            MongoCollection<Document> collection = database.getCollection("RoomType");

            // Lấy dữ liệu từ MongoDB
            var documents = collection.find().into(new ArrayList<>());

            // Tạo DefaultTableModel để chứa dữ liệu
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Loại phòng");
            model.addColumn("Mô tả");
            model.addColumn("Giá");
            DecimalFormat currencyFormat = new DecimalFormat("#,###");

            // Thêm dữ liệu từ MongoDB vào DefaultTableModel
            for (Document doc : documents) {
                String price = currencyFormat.format(Integer.parseInt(doc.getString("Price")));

                model.addRow(new Object[]{
                        doc.getString("RoomTypeName"),
                        doc.getString("Description"),
                        price
                });
            }

            // Cập nhật JTable với dữ liệu mới
            roomTypeTable.setModel(model);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void initComponents() {
        addButton = new JButton("Thêm loại phòng");
        addButton.setBackground(new java.awt.Color(28, 181, 224));
        addButton.setForeground(new java.awt.Color(255, 255, 255));
        editButton = new JButton("Sửa loại phòng");
        editButton.setBackground(new java.awt.Color(28, 181, 224));
        editButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteButton = new JButton("Xóa loại phòng");
        deleteButton.setBackground(new java.awt.Color(28, 181, 224));
        deleteButton.setForeground(new java.awt.Color(255, 255, 255));
        roomTypeTable = new com.myproject.swings.GoodsTable();
        
    }
}
