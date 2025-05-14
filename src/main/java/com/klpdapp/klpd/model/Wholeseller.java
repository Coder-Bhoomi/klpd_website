package com.klpdapp.klpd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "wholeseller")
public class Wholeseller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wholesellerId;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String companyName;

    @Column(length = 20)
    private String gSTIN;

    @Column(length = 250)
    private String officeAddress;

    @Column(length = 250)
    private String shippingAddress;

    @Column(length = 20)
    private String contactNumber;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String organisationNumber;

}
