package com.myproject.models;

import java.util.List;
import com.myproject.models.types.EmployeeStatus;
import com.myproject.models.types.Role;

public class Model_Employee {
    private String employeeId;
    private String name;
    private String gender;
    private String phoneNumber;
    private String email;
    private String doB;
    private String position;
    private EmployeeStatus status;
    private String userName;
    private String passWord;
    private List<Role> roleList;

    public Model_Employee() {
        // Default constructor
    }

    public Model_Employee(String employeeId, String name, String gender, String phoneNumber, String email,
            String doB, String position, EmployeeStatus status, String userName, String passWord,
            List<Role> roleList) {
        this.employeeId = employeeId;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.doB = doB;
        this.position = position;
        this.status = status;
        this.userName = userName;
        this.passWord = passWord;
        this.roleList = roleList;
    }

    // Getters and setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDoB() {
        return doB;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "Model_Employee [employeeId=" + employeeId + ", name=" + name + ", gender=" + gender + ", phoneNumber="
                + phoneNumber + ", email=" + email + ", doB=" + doB + ", position=" + position + ", status=" + status
                + ", userName=" + userName + ", passWord=" + passWord + ", roleList=" + roleList + "]";
    }
}

