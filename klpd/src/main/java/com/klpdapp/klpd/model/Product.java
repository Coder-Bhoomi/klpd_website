package com.klpdapp.klpd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import java.time.LocalDate;
import java.util.List;

@Entity
@Indexed
@Table(name = "products")
@Access(AccessType.FIELD)
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericField
    private int pid;

    @Column(nullable = false, length = 50)
    @FullTextField
    private String companyPid;

    @Column(nullable = false, length = 50)
    @FullTextField
    private String hapPid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ctg_id", nullable = false)
    @IndexedEmbedded
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subcategory_id", nullable = false)
    @IndexedEmbedded
    private SubCategory subcategory;

    @Column(length = 75)
    @FullTextField
    private String brand;

    @Column(nullable = false, columnDefinition = "TEXT")
    @FullTextField
    private String prodName;

    @Column(nullable = false, columnDefinition = "TEXT")
    @FullTextField
    private String description;

    @Column(nullable = false)
    @GenericField
    private int hits;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(nullable = false)
    @GenericField
    private int stock;

    @Column(precision = 10, nullable = false)
    @GenericField
    private Float mrp;

    private int percentage;

    @Column(precision = 10)
    @GenericField
    private Float offerPrice;

    @Column(length = 15)
    @FullTextField
    private String diameter;

    @Column(length = 50)
    @FullTextField
    private String dimension;

    @Column(length = 15)
    @FullTextField
    private String thickness;

    @Column(length = 15)
    @FullTextField
    private String capacity;

    @Column(length = 50)
    @FullTextField
    private String model;

    @Column(length = 15)
    @FullTextField
    private String length;

    @Column(length = 15)
    @FullTextField
    private String height;

    @Column(length = 50)
    @FullTextField
    private String cartonDimension;

    @Column(length = 15)
    @FullTextField
    private String weight;

    @Column(length = 10)
    @FullTextField
    private String guarantee;

    @Column(length = 10)
    @FullTextField
    private String warranty;

    @Column(length = 20)
    @FullTextField
    private String color;

    @Column(length = 200)
    @FullTextField
    private String material;

    @Column(length = 250)
    @FullTextField
    private String finish;

    private int rating;

    @OneToMany(mappedBy = "pid", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @IndexedEmbedded 
    private List<Images> images;
    // Getters and Setters

}
