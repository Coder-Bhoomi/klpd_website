package com.klpdapp.klpd.model;

import jakarta.persistence.*;

@Entity
@Table(name = "size")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate IDs
    @Column(name = "id")
    private Long id;

    @Column(name = "subcategory_id")
    private String subcategoryId;

    @Column(name = "size")
    private String size;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
