package com.klpdapp.klpd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "product")
public class product {
    @Id
    @Column(length = 15, nullable = false)
    private String prodId;

    @Column(length = 15, nullable = false)
    private String categoryId;

    @Column(length = 8, nullable = false)
    private String attId;

    @Column(length = 60, nullable = false)
    private String brand;

    @Column(length = 100, nullable = false)
    private String prodName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(length = 30, nullable = false)
    private String availability;

    @Column(nullable = false)
    private int stock;

    @Column(precision = 10, scale = 2, nullable = false)
    private float mrp;

    @Column(precision = 10, scale = 2)
    private Float offerPrice;
}
