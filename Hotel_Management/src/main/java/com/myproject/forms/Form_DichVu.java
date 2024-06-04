/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
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
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;



public class Form_DichVu extends javax.swing.JPanel {
    
//    private List<Service> serList = new ArrayList<>();
//    private DefaultTableModel tableModel;
//    private Add_Service add_Service;
//    private Edit_ServiceForm edit_ServiceForm;
    
    public Form_DichVu() {
        initComponents();
        jScrollPane1.setVerticalScrollBar(new ScrollBar());
        jScrollPane1.getVerticalScrollBar().setBackground(Color.WHITE);
        jScrollPane1.getViewport().setBackground(Color.WHITE);
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        jScrollPane1.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
        
        
        int buttonColumnIndex = 4; // Chỉ số của cột mà bạn muốn hiển thị các ô JButtons
        table1.getColumnModel().getColumn(buttonColumnIndex).setCellRenderer(new ButtonRenderer());
        table1.getColumnModel().getColumn(buttonColumnIndex).setCellEditor(new ButtonEditor());
        
        fetchDataFromMongoDB();
        //displayUser();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        searchText1 = new com.myproject.swings.SearchText();
        jButton1 = new javax.swing.JButton();
        buttonSec1 = new com.myproject.swings.ButtonSec();
        panelBorder1 = new com.myproject.swings.PanelBorder();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new com.myproject.swings.Table();

        searchText1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchText1ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 133, 190));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Thêm");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        buttonSec1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/myproject/icons/search.png"))); // NOI18N
        buttonSec1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonSec1MouseClicked(evt);
            }
        });

        jLayeredPane1.setLayer(searchText1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(buttonSec1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(searchText1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(buttonSec1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 377, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchText1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSec1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Loại Dịch Vụ", "Đơn Vị Tính", "Đơn Giá", "Chức Năng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table1);
        if (table1.getColumnModel().getColumnCount() > 0) {
            table1.getColumnModel().getColumn(0).setMinWidth(70);
            table1.getColumnModel().getColumn(0).setMaxWidth(70);
            table1.getColumnModel().getColumn(2).setMinWidth(200);
            table1.getColumnModel().getColumn(2).setMaxWidth(200);
            table1.getColumnModel().getColumn(3).setMinWidth(200);
            table1.getColumnModel().getColumn(3).setMaxWidth(200);
            table1.getColumnModel().getColumn(4).setMinWidth(100);
            table1.getColumnModel().getColumn(4).setMaxWidth(100);
        }

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLayeredPane1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
 

    private void fetchDataFromMongoDB(){
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/")) {
            // Chọn cơ sở dữ liệu
            MongoDatabase database = mongoClient.getDatabase("Hotel_Management");
            // Chọn bảng
            MongoCollection<Document> collection = database.getCollection("Service");
            FindIterable<Document> cursor = collection.find();
            //tableModel = (DefaultTableModel)table1.getModel();
            DefaultTableModel model = (DefaultTableModel)table1.getModel();
            //tableModel.setRowCount(0);
            model.setRowCount(0);
            // Tạo một Document mới chứa dữ liệu bạn muốn thêm
            
            for (Document document : cursor) {
                String value1 = document.getString("IDService");
                String value2 = document.getString("TypeService");
                String value3 = document.getString("Unit");
                String value4 = document.getString("CostService");
                // Thêm hàng mới vào tableModel
                model.addRow(new Object[]{value1, value2, value3, value4});
            }      
            // Thêm Document vào bảng
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

class ButtonRenderer extends DefaultTableCellRenderer {
    private JButton button;

    public ButtonRenderer() {
        button = new JButton();
        button.setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        button.setText("Sửa");
        button.setBackground(new Color(153, 255, 153));
        return button;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;

       public ButtonEditor() {
        super(new JCheckBox());
        button = new JButton();
        button.addActionListener(e -> fireEditingStopped());    
        button.addActionListener((ActionEvent e) -> {
            // Xử lý sự kiện nhấp chuột vào nút ở đây
            Edit_Service editFrame = new Edit_Service();
            editFrame.setVisible(true);
        });
}

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText("Sửa");
        button.setBackground(new Color(153, 255, 153)); 
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }
}
class Edit_Service extends JFrame {
    public Edit_Service(){
        setTitle("Chỉnh sửa thông tin");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng cửa sổ này mà không đóng ứng dụng chính
        setLocationRelativeTo(null); // Hiển thị cửa sổ ở giữa màn hình
        
    }
        
}
    private void searchText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchText1ActionPerformed
          
    }//GEN-LAST:event_searchText1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Add_Service addser = new Add_Service();
        addser.setVisible(true);
      
    }//GEN-LAST:event_jButton1ActionPerformed

    private void buttonSec1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonSec1MouseClicked
        DefaultTableModel ob=(DefaultTableModel) table1.getModel();
        TableRowSorter<DefaultTableModel> obj=new TableRowSorter<>(ob);
        table1.setRowSorter(obj);
        obj.setRowFilter(RowFilter.regexFilter(searchText1.getText()));
    }//GEN-LAST:event_buttonSec1MouseClicked

    public Object getEvent() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void update(String typeService, String typeService1, int unit, double priceService) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


class Add_Service extends JFrame{
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
        btnAdd.setBounds(50, 50, 100, 30);
        btnAdd.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
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
                    
            // Chọn bảsng
            MongoCollection<Document> collection = database.getCollection("Service");
            // Tạo một Document mới chứa dữ liệu bạn muốn thêm
            Document document = new Document("IDService", servicecode)
                 .append("TypeService", typeservice)
                 .append("Unit", unit)
                 .append("CostService", costservice);
                // Thêm Document vào bảng
                collection.insertOne(document);
                fetchDataFromMongoDB();
            }
            catch (MongoException ex) {
                JOptionPane.showMessageDialog(Add_Service.this, "Lỗi khi cập nhật dịch vụ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
                
            // Sau khi xử lý xong, có thể hiển thị thông báo thành công
                JOptionPane.showMessageDialog(Add_Service.this, "Thêm thành công!");
                
                setVisible(false);
            }
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
        panel1.add(btnClose);
        panel1.add(btnAdd);

        // Thêm panel vào frame
        add(panel1);
        setVisible(true);
    }   
}
class Del_DichVuForm extends JFrame{
}
//class Edit_ServiceForm extends JFrame {
//    private JTextField txtServiceCode;
//    private JTextField txtTypeService;
//    private JTextField txtUnit; 
//    private JTextField txtCostService;     
//    private Service serviceData;
//    private int intdexRow;
//    private Form_DichVu formDichVu;
//
//    public Edit_ServiceForm(Service serviceData, Form_DichVu formDichVu, int intdexRow) {
//        this.serviceData = serviceData;
//        this.formDichVu = formDichVu;
//        this.intdexRow = intdexRow;
//
//        setResizable(false);
//        setTitle("Chỉnh sửa thông tin");
//        setSize(300, 200);
//        setLocationRelativeTo(null);
//
//        // Tạo các label
//         JLabel lblServiceCode = new JLabel("Mã Dịch vụ:");
//         JLabel lblTypeService = new JLabel("Tên Dịch vụ:");
//         JLabel lblUnit = new JLabel("Đơn vị tính:");
//         JLabel lblCostService = new JLabel("Đơn Giá:");
//
//        // Tạo các trường nhập liệu và điền thông tin người dùng cần chỉnh sửa
//        txtServiceCode = new JTextField(20);
//        txtServiceCode.setText(serviceData.getServiceCode());
//        txtTypeService = new JTextField(20);
//        txtTypeService.setText(serviceData.getTypeService());
//        txtUnit = new JTextField(20);
//        txtUnit.setText(serviceData.getUnit());
//        txtCostService = new JTextField(20);
//        txtCostService.setText(serviceData.getCostService());
//        
//        // Tạo nút "Lưu"
//        JButton btnSave = new JButton("Lưu");
//        btnSave.addActionListener((ActionEvent e) -> {
//            String servicecode = txtServiceCode.getText();
//            String typeservice = txtTypeService.getText();
//            String unit = txtUnit.getText();
//            String costservice = txtCostService.getText();
//            
//            if (servicecode.isEmpty() || typeservice.isEmpty() || unit.isEmpty() || costservice.isEmpty()) {
//                JOptionPane.showMessageDialog(Edit_ServiceForm.this, "Vui lòng điền đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            // Thực hiện cập nhật thông tin người dùng trong cơ sở dữ liệu
//            try (
//                MongoClient mongoClient = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/")) {
//            // Chọn cơ sở dữ liệu
//            MongoDatabase database = mongoClient.getDatabase("Hotel_Management");
//            // Chọn bảng
//            MongoCollection<Document> collection = database.getCollection("Service");
//
//            // Tạo điều kiện để xác định người dùng cần cập nhật
//            Bson filter = Filters.eq("_id", new ObjectId(servicecode));
//
//            // Tạo một Document mới chứa thông tin người dùng đã cập nhật
//            Document updateDocument = new Document("$set", new Document("typeService", typeservice)
//                    .append("unit", unit)
//                    .append("costService", costservice));
//            // Thực hiện cập nhật thông tin người dùng trong bảng
//            UpdateResult updateResult = collection.updateOne(filter, updateDocument);
//                // Code cập nhật thông tin người dùng vào cơ sở dữ liệu tại đây
//                
//                // Hiển thị thông báo thành công
//                JOptionPane.showMessageDialog(Edit_ServiceForm.this, "Cập nhật thành công!");
//                setVisible(false);
//            } catch (HeadlessException ex) {
//                JOptionPane.showMessageDialog(Edit_ServiceForm.this, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        }); 
//            JButton btnClose = new JButton("Đóng");
//            btnClose.addActionListener((ActionEvent e) -> {
//            setVisible(false);
//        });
//
//        // Tạo panel và thêm các thành phần vào panel
//        JPanel panel = new JPanel(new GridLayout(4, 2));
//        panel.add(lblServiceCode);
//        panel.add(txtServiceCode);
//        panel.add(lblTypeService);
//        panel.add(txtTypeService);
//        panel.add(lblUnit);
//        panel.add(txtUnit);
//        panel.add(lblCostService);
//        panel.add(txtCostService);
//        panel.add(btnSave);
//
//        // Thêm panel vào frame
//        add(panel);
//        setVisible(true);
//    }
//}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.myproject.swings.ButtonSec buttonSec1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.myproject.swings.PanelBorder panelBorder1;
    private com.myproject.swings.SearchText searchText1;
    private com.myproject.swings.Table table1;
    // End of variables declaration//GEN-END:variables
}

class Service{
    private String ServiceCode;
    private String TypeService;
    private String Unit;  
    private String CostService;
    
    public Service(String ServiceCode, String TypeService, String Unit, String CostService) {
        this.ServiceCode = ServiceCode;
        this.TypeService = TypeService;
        this.Unit = Unit;
        this.CostService = CostService;
    }

    public String getServiceCode() {
        return ServiceCode;
    }

    public void setServiceCode(String ServiceCode) {
        this.ServiceCode = ServiceCode;
    }

    public String getTypeService() {
        return TypeService;
    }

    public void setTypeService(String TypeService) {
        this.TypeService = TypeService;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }

    public String getCostService() {
        return CostService;
    }

    public void setCostService(String CostService) {
        this.CostService = CostService;
    }
}