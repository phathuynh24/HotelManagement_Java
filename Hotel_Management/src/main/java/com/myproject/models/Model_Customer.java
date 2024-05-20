package com.myproject.models;

public class Model_Customer {
    private String customerId;
    private String name;
    private String gender;
    private String phoneNumber;
    private String idCard;
    private String address;

    public Model_Customer() {
        // Default constructor
    }

    public Model_Customer(String customerId, String name, String gender, String phoneNumber, String idCard,
            String address) {
        this.customerId = customerId;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.idCard = idCard;
        this.address = address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String doB) {
        this.address = doB;
    }

    @Override
    public String toString() {
        return "Model_Customer [customerId=" + customerId + ", name=" + name + ", gender=" + gender
                + ", phoneNumber=" + phoneNumber + ", idCard=" + idCard + ", address=" + address + "]";
    }
}
