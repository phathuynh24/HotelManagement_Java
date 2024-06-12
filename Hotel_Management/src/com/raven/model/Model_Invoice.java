package com.raven.model;

import com.raven.constant.HotelInfo;
import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Model_Invoice {

    private String id;
    private final String hotelName;
    private final String hotelAddress;
    private final String hotelPhone;
    private final String hotelEmail;
    private Model_Customer customer;
    private int numberOfGuests;
    private String note;
    private Date bookingDate;
    private Date checkInDate;
    private Date checkOutDate;
    private String status;
    private List<Model_Invoice_Rooms> roomList;
    private List<Model_Invoice_ServicesAndGoods> productList;
    private List<Model_Invoice_ServicesAndGoods> serviceList;
    private int totalAmount;

    // Constructors
    public Model_Invoice() {
        this.hotelEmail = HotelInfo.COMPANY_EMAIL;
        this.hotelPhone = HotelInfo.HOTEL_PHONE;
        this.hotelAddress = HotelInfo.HOTEL_ADDRESS;
        this.hotelName = HotelInfo.HOTEL_NAME;
    }

    public Model_Invoice(String id, Model_Customer customer, int numberOfGuests,
            String note, Date bookingDate, Date checkInDate, Date checkOutDate, String status,
            List<Model_Invoice_Rooms> roomList, List<Model_Invoice_ServicesAndGoods> productList, List<Model_Invoice_ServicesAndGoods> serviceList,
            int totalAmount) {
        this.hotelEmail = HotelInfo.COMPANY_EMAIL;
        this.hotelPhone = HotelInfo.HOTEL_PHONE;
        this.hotelAddress = HotelInfo.HOTEL_ADDRESS;
        this.hotelName = HotelInfo.HOTEL_NAME;
        this.id = id;
        this.customer = customer;
        this.numberOfGuests = numberOfGuests;
        this.note = note;
        this.bookingDate = bookingDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.roomList = roomList;
        this.productList = productList;
        this.serviceList = serviceList;
        this.totalAmount = totalAmount;
    }

    public Model_Invoice(Model_Customer customer, int numberOfGuests,
            String note, Date bookingDate, Date checkInDate, Date checkOutDate, String status,
            List<Model_Invoice_Rooms> roomList, List<Model_Invoice_ServicesAndGoods> productList, List<Model_Invoice_ServicesAndGoods> serviceList,
            int totalAmount) {
        this.hotelEmail = HotelInfo.COMPANY_EMAIL;
        this.hotelPhone = HotelInfo.HOTEL_PHONE;
        this.hotelAddress = HotelInfo.HOTEL_ADDRESS;
        this.hotelName = HotelInfo.HOTEL_NAME;
        this.customer = customer;
        this.numberOfGuests = numberOfGuests;
        this.note = note;
        this.bookingDate = bookingDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.roomList = roomList;
        this.productList = productList;
        this.serviceList = serviceList;
        this.totalAmount = totalAmount;
    }

    // Getter and Setter methods for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter methods
    public String getHotelName() {
        return hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public String getHotelPhone() {
        return hotelPhone;
    }

    public String getHotelEmail() {
        return hotelEmail;
    }

    public Model_Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Model_Customer customer) {
        this.customer = customer;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Model_Invoice_Rooms> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Model_Invoice_Rooms> roomList) {
        this.roomList = roomList;
    }

    public List<Model_Invoice_ServicesAndGoods> getProductList() {
        return productList;
    }

    public void setProductList(List<Model_Invoice_ServicesAndGoods> productList) {
        this.productList = productList;
    }

    public List<Model_Invoice_ServicesAndGoods> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Model_Invoice_ServicesAndGoods> serviceList) {
        this.serviceList = serviceList;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setRoomById(String currentName, Model_Room newRoom, int newPricePerNight) {
        int oldTotalAmount = totalAmount;

        for (Model_Invoice_Rooms room : roomList) {
            if (room.getName().equals(currentName)) {
                if (newRoom != null) {
                    room.setCode(newRoom.getId());
                    room.setName(newRoom.getName());
                    room.setPricePerNight(newPricePerNight);
                    room.setTotalRoomPrice(room.getNumberOfNights() * newPricePerNight);
                }
                break;
            }
        }

        // Cập nhật phòng trong danh sách productList
        for (Model_Invoice_ServicesAndGoods product : productList) {
            if (product.getRoomName().equals(currentName)) {
                product.setRoomName(newRoom.getName());
            }
        }

        // Cập nhật phòng trong danh sách serviceList
        for (Model_Invoice_ServicesAndGoods service : serviceList) {
            if (service.getRoomName().equals(currentName)) {
                service.setRoomName(newRoom.getName());
            }
        }

        // Cập nhật totalAmount
        int newTotalAmount = calculateTotalAmount();
        totalAmount = newTotalAmount;

        int difference = newTotalAmount - oldTotalAmount;
        System.out.println("Total amount updated. Difference: " + difference);
    }

    private int calculateTotalAmount() {
        int roomTotal = 0;
        int productTotal = 0;
        int serviceTotal = 0;

        for (Model_Invoice_Rooms room : roomList) {
            roomTotal += room.getTotalRoomPrice();
        }

        for (Model_Invoice_ServicesAndGoods product : productList) {
            productTotal += product.getTotalPrice();
        }

        for (Model_Invoice_ServicesAndGoods service : serviceList) {
            serviceTotal += service.getTotalPrice();
        }

        return roomTotal + productTotal + serviceTotal;
    }
    
    public List<Model_Room> getAllRoomFromInvoice() {
        List<Model_Room> rooms = new ArrayList<>();
        if (roomList != null) {
            for (Model_Invoice_Rooms invoiceRoom : roomList) {
                rooms.add(new Model_Room(
                        invoiceRoom.getCode(),
                        invoiceRoom.getName(),
                        invoiceRoom.getType(),
                        invoiceRoom.getFloor(),
                        invoiceRoom.getCapacity()
                ));
            }
        }
        return rooms;
    }

    public Object[] toRowTable(EventAction<Model_Invoice> eventAction) {
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String formattedBookingDate = dateFormat.format(bookingDate);
        String formattedCheckInDate = dateFormat.format(checkInDate);
        String formattedCheckOutDate = dateFormat.format(checkOutDate);

        return new Object[]{
            id,
            customer.getName(),
            numberOfGuests,
            formattedBookingDate,
            formattedCheckInDate,
            formattedCheckOutDate,
            df.format(totalAmount),
            note,
            status,
            new ModelAction<>(this, eventAction, "action_all")
        };
    }
    
    // toString method
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invoice ID: ").append(id) // Thêm id vào chuỗi toString
                .append("\nHotel Name: ").append(hotelName)
                .append("\nHotel Address: ").append(hotelAddress)
                .append("\nCustomer: ").append(customer.toString())
                .append("\nNumber of Guests: ").append(numberOfGuests)
                .append("\nNote: ").append(note)
                .append("\nBooking Date: ").append(bookingDate)
                .append("\nCheck-In Date: ").append(checkInDate)
                .append("\nCheck-Out Date: ").append(checkOutDate)
                .append("\nStatus: ").append(status)
                .append("\nRoom List: ").append(roomList.toString())
                .append("\nProduct List: ").append(productList.toString())
                .append("\nService List: ").append(serviceList.toString())
                .append("\nTotal Amount: ").append(totalAmount);
        return stringBuilder.toString();
    }
}
