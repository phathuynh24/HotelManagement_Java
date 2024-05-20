package com.myproject.models;

import java.util.Date;
import java.util.List;

public class Model_Invoice {
    private String hotelName;
    private String hotelAddress;
    private Model_Customer customer;
    private int numberOfGuests;
    private String note;
    private Date bookingDate;
    private Date checkInDate;
    private Date checkOutDate;
    private String status;
    private List<Model_Room> roomList;
    private List<Model_Product> productList;
    private List<Model_Service> serviceList;
    private double totalAmount;

    // Constructors

    public Model_Invoice(String hotelName, String hotelAddress, Model_Customer customer, int numberOfGuests,
                         String note, Date bookingDate, Date checkInDate, Date checkOutDate, String status,
                         List<Model_Room> roomList, List<Model_Product> productList, List<Model_Service> serviceList,
                         double totalAmount) {
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
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

    // Getter and Setter methods

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
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

    public List<Model_Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Model_Room> roomList) {
        this.roomList = roomList;
    }

    public List<Model_Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Model_Product> productList) {
        this.productList = productList;
    }

    public List<Model_Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Model_Service> serviceList) {
        this.serviceList = serviceList;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    // toString method

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hotel Name: ").append(hotelName)
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