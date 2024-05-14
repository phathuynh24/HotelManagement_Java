/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myproject.forms;

import javax.swing.JTextField;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.myproject.models.Model_Room;
import com.myproject.models.types.RoomStatus;
import com.myproject.swings.SearchText;
import java.awt.FlowLayout;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.bson.Document;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;

public class Form_Room extends javax.swing.JPanel {

    public Form_Room() {
        initComponents();    
        fetchDataFromMongoDB();
    }

    private void initComponents() {
        searchField = new SearchText();
        jScrollPane1 = new javax.swing.JScrollPane();
        roomTable = new com.myproject.swings.GoodsTable();
        jPanelAddRoom = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jRoomNameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jRoomTypeTextField = new javax.swing.JComboBox();
        jRoomCapacityTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        

        jScrollPane1.setBackground(new java.awt.Color(28,181,224));
        jRoomTypeTextField.addItem("Thường");
        jRoomTypeTextField.addItem("VIP");

        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(255, 255, 255));

        roomTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên Phòng", "Sức chứa", "Loại phòng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        roomTable.setSelectionBackground(new java.awt.Color(28, 181, 224));
        roomTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(roomTable);

        jPanelAddRoom.setBackground(new java.awt.Color(28, 181, 224));
        jPanelAddRoom.setAutoscrolls(true);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tên Phòng");


        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Loại Phòng");

        jRoomCapacityTextField.setToolTipText("");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Sức chứa");


        javax.swing.GroupLayout jPanelAddRoomLayout = new javax.swing.GroupLayout(jPanelAddRoom);
        jPanelAddRoom.setLayout(jPanelAddRoomLayout);
        jPanelAddRoomLayout.setHorizontalGroup(
            jPanelAddRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddRoomLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelAddRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRoomNameTextField)
                    .addGroup(jPanelAddRoomLayout.createSequentialGroup()
                        .addGroup(jPanelAddRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRoomTypeTextField, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelAddRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRoomCapacityTextField)
                
                            
                        )))));
        jPanelAddRoomLayout.setVerticalGroup(
            jPanelAddRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddRoomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRoomNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelAddRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelAddRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRoomTypeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRoomCapacityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                
            .addContainerGap(23, Short.MAX_VALUE))
        );

        btnSearch.setBackground(new java.awt.Color(28, 181, 224));
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setText("Tìm kiếm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        
        btnAdd.setBackground(new java.awt.Color(28, 181, 224));
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm phòng");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        
        btnDelete.setBackground(new java.awt.Color(28, 181, 224));
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Xóa phòng");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        
        btnEdit.setBackground(new java.awt.Color(28, 181, 224));
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Sửa thông tin phòng");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSearch)
                        .addGap(5)
                        .addComponent(btnAdd)
                        .addGap(5)
                        .addComponent(btnDelete)
                        .addGap(5)
                        .addComponent(btnEdit)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(btnAdd)
                    .addComponent(btnDelete)
                    .addComponent(btnEdit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
      
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt){
        String search = searchField.getText();
        searchRoom(search); 
    }
    
    private void searchRoom(String searchText) {
        DefaultTableModel model = (DefaultTableModel) roomTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        roomTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText)); // Tìm kiếm theo thông tin người dùng nhập
    }
    
    
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String uri = "mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Chọn hoặc tạo database
            MongoDatabase database = mongoClient.getDatabase("Hotel_Management");

            // Chọn hoặc tạo collection
            MongoCollection<Document> collection = database.getCollection("Room");

            //Hiển thị dialog để người dùng thêm phòng
            JOptionPane.showInputDialog(jPanelAddRoom);
            
                // Lấy các giá trị từ dialog
                String name = jRoomNameTextField.getText();
                String capacity = jRoomCapacityTextField.getText();
                String type = (String) jRoomTypeTextField.getSelectedItem();

                if ("".equals(name) || "".equals(capacity) || "".equals(type)) {
                    JOptionPane.showMessageDialog(this, "Vui lòng không bỏ trống thông tin để thêm phòng thành công!");
                } else {                        
                    //Thêm mới dữ liệu
                    collection.insertOne(     
                        new Document(new Document("RoomName", name)
                            .append("Capacity", capacity)
                            .append("RoomType", type)
                        )
                    );
                    fetchDataFromMongoDB();
                } 
            
            clearInput();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {                                         
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow != -1) { // Kiểm tra xem có hàng nào được chọn không
        int confirm = javax.swing.JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa hàng này?", "Xác nhận xóa", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            deleteSelectedData(selectedRow);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng để xóa.", "Thông báo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }
      
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {                                         
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow != -1) { // Kiểm tra xem có hàng nào được chọn không
            int confirm = javax.swing.JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn chỉnh sửa hàng này?", "Xác nhận chỉnh sửa", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            editSelectedData(selectedRow);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng để chỉnh sửa.", "Thông báo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void clearInput(){
        jRoomNameTextField.setText("");
        jRoomTypeTextField.setSelectedIndex(0);
        jRoomCapacityTextField.setText("");
    }
    
    private void fetchDataFromMongoDB(){
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/")) {
            // Chọn cơ sở dữ liệu
            MongoDatabase database = mongoClient.getDatabase("Hotel_Management");
            // Chọn bảng
            MongoCollection<Document> collection = database.getCollection("Room");
            FindIterable<Document> cursor = collection.find();
            
            //Reset cái table
            DefaultTableModel model = (DefaultTableModel)roomTable.getModel();
            model.setRowCount(0);
            
            // Tạo một Document mới chứa dữ liệu bạn muốn thêm            
            for (Document document : cursor) {
                String value1 = document.getString("RoomName");
                String value2 = document.getString("Capacity");
                String value3 = document.getString("RoomType");

                // Thêm hàng mới vào tableModel
                model.addRow(new Object[]{value1, value2, value3});
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    

    private void deleteSelectedData(int selectedRow) {
        String uri = "mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Chọn hoặc tạo database
            MongoDatabase database = mongoClient.getDatabase("Hotel_Management");

            // Chọn hoặc tạo collection
            MongoCollection<Document> collection = database.getCollection("Room");

            // Lấy tên phòng từ hàng được chọn trong bảng
            String name = (String) roomTable.getValueAt(selectedRow, 0);

            // Xóa hàng được chọn từ MongoDB
            collection.deleteOne(new Document("RoomName", name));

            // Xóa hàng được chọn từ bảng
            DefaultTableModel model = (DefaultTableModel) roomTable.getModel();
            model.removeRow(selectedRow);
            
        } catch (Exception e) {
            e.printStackTrace();    
        }
    }
    
    private void editSelectedData(int selectedRow) {
        String uri = "mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Chọn hoặc tạo database
            MongoDatabase database = mongoClient.getDatabase("Hotel_Management");

            // Chọn hoặc tạo collection
            MongoCollection<Document> collection = database.getCollection("Room");

            // Lấy thông tin phòng từ hàng được chọn trong bảng        
            String name = (String) roomTable.getValueAt(selectedRow, 0);
            
            //Hiển thị dialog để người dùng chỉnh sửa phòng
            JOptionPane.showInputDialog(jPanelAddRoom);
            
                // Lấy các giá trị mới từ dialog
                String newName = jRoomNameTextField.getText();
                String newCapacity = jRoomCapacityTextField.getText();
                String newType = (String) jRoomTypeTextField.getSelectedItem();

                if ("".equals(newName) || "".equals(newCapacity) || "".equals(newType)) {
                    JOptionPane.showMessageDialog(this, "Vui lòng không bỏ trống thông tin để cập nhật phòng thành công!");
                } else {    
                    // Cập nhật thông tin mới vào model của bảng
                    roomTable.setValueAt(newName, selectedRow, 0); 
                    roomTable.setValueAt(newCapacity, selectedRow, 1); 
                    roomTable.setValueAt(newType, selectedRow, 2); 
                    
                    // Tìm và cập nhật dòng tương ứng trong cơ sở dữ liệu
                    collection.updateOne(
                        new Document("RoomName", name),
                        new Document("$set", new Document("RoomName", newName)
                            .append("Capacity", newCapacity)
                            .append("RoomType", newType)
                        )
                    );
                    clearInput();
                } 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanelAddRoom;
    private javax.swing.JTextField jRoomCapacityTextField;
    private javax.swing.JTextField jRoomNameTextField;
    private javax.swing.JComboBox jRoomTypeTextField;

    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable roomTable;
    private javax.swing.JTextField searchField;
    
}