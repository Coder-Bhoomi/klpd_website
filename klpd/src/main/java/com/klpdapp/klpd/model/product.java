package com.klpdapp.klpd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "product")
public class product {
    @Id
    @Column(length = 15, nullable = false)
    private String prodId;

    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    private category categoryId;

    @ManyToOne
    @JoinColumn(name = "attId")
    private attribute attId;

    @Column(length = 60, nullable = false)
    private String brand;

    @Column(length = 100, nullable = false)
    private String prodName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(length = 30, nullable = false)
    private String availability;

    @Column(precision = 10, nullable = false)
    private float mrp;

    @Column(precision = 10)
    private Float offerPrice;

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(category categoryId) {
        this.categoryId = categoryId;
    }

    public attribute getAttId() {
        return attId;
    }

    public void setAttId(attribute attId) {
        this.attId = attId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
    
    public float getMrp() {
        return mrp;
    }

    public void setMrp(float mrp) {
        this.mrp = mrp;
    }

    public Float getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Float offerPrice) {
        this.offerPrice = offerPrice;
    }

    
}
