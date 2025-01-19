package com.klpdapp.klpd.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, length = 100)
    private String name;

    private LocalDate dob;

    @Column(length = 20)
    private String gender;

    @Column(nullable = false, unique = true, length = 60)
    private String email;

    @Column(nullable = false, length = 500)
    private String password;

    private long mobile;

    @Column(name = "alternate_mobile")
    private Long alternateMobile;

    @Column(length = 50)
    private String status;

    @Column(nullable = false)
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public Long getAlternateMobile() {
        return alternateMobile;
    }

    public void setAlternateMobile(Long alternateMobile) {
        this.alternateMobile = alternateMobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}