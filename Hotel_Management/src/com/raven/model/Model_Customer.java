package com.raven.model;

import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;

public class Model_Customer {

    private String idCard;
    private String name;
    private String gender;
    private String phone;
    private String email;
    private String address;

    public Model_Customer() {
        // Default constructor
    }

    public Model_Customer(String idCard, String name, String gender, String phone,
            String email, String address) {
        this.idCard = idCard;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object[] toRowTable(EventAction<Model_Customer> eventAction) {
        return new Object[]{idCard, name, gender, phone, email, address, new ModelAction<>(this, eventAction, "action_all")};
    }

    @Override
    public String toString() {
        return "Model_Customer [idCard=" + idCard + ", name=" + name + ", gender=" + gender
                + ", phoneNumber=" + phone + ", email=" + email + ", address=" + address + " ]";
    }
}
