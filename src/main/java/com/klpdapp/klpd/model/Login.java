package com.klpdapp.klpd.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "login")
@Getter
@Setter

public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(nullable = false, unique = true, length = 60)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 500)
    private String password;

    @Column(nullable = false)
    private String userType;

    @Column(nullable = false, unique = true)
    private int userId;

    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt = LocalDateTime.now();

    private boolean isEnabled = true;

    @Column(unique = true, length = 50)
    private String googleId;


}
