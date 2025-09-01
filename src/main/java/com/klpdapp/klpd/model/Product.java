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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
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

    private Float discount;

    @Column(precision = 10)
    @GenericField
    private Float offerPrice;

    private Float rating;   

    private int sales;

    @OneToMany(mappedBy = "pid", cascade = CascadeType.ALL)
    @IndexedEmbedded 
    private List<Images> images;

    @OneToOne(mappedBy = "pid", cascade = CascadeType.ALL)
    @IndexedEmbedded
    private Videos videos;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @IndexedEmbedded
    private List<Attribute> attributes;
    // Getters and Setters
}
