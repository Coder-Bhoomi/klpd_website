package com.klpdapp.klpd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name= "Attribute_Table")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attribute_id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "pid")
    private Product product;

    @Column(length = 100)
    private String attributeName;

    @Column(length = 100)
    private String attributeValue;

    
}
