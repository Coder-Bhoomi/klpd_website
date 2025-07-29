package com.klpdapp.klpd.dto;

import java.time.LocalDate;

public class UserDto {
    private int userId;
    private String name;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String email;
    private long mobile;
    private String status;
    private String password;
    private LocalDate SpouseDob;

    private LocalDate anniversary;
    private String childName;

    private LocalDate childDob;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LocalDate getDob() {
        return dob;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public long getMobile() {
        return mobile;
    }
    public void setMobile(long mobile) {
        this.mobile = mobile;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public LocalDate getSpouseDob() {
        return SpouseDob;
    }
    public void setSpouseDob(LocalDate spouseDob) {
        SpouseDob = spouseDob;
    }
    public LocalDate getAnniversary() {
        return anniversary;
    }
    public void setAnniversary(LocalDate anniversary) {
        this.anniversary = anniversary;
    }
    public String getChildName() {
        return childName;
    }
    public void setChildName(String childName) {
        this.childName = childName;
    }
    public LocalDate getChildDob() {
        return childDob;
    }
    public void setChildDob(LocalDate childDob) {
        this.childDob = childDob;
    }

    
}
