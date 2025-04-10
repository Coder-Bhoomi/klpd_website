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
    private int WholesellerId;

    @Column(length = 100)
    private String WholesellerName;

    @Column(length = 100)
    private String CompanyName;

    @Column(length = 20)
    private String GSTIN;

    @Column(length = 250)
    private String OfficeAddress;

    @Column(length = 250)
    private String ShippingAddress;

    @Column(length = 20)
    private String ContactNumber;

    @Column(length = 100)
    private String Email;

    @Column(length = 20)
    private String OrganisationNumber;

    @Column(length = 20)
    private String password;
}
