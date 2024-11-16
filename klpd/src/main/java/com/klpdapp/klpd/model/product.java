package com.klpdapp.klpd.model;

import jakarta.persistence.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import java.util.List;

@Entity
@Indexed 
@Table(name = "Product")
@Access(AccessType.FIELD)
public class Product {

    @Id
    @Column(length = 15, nullable = false)
    @GenericField 
    private String prodId;

    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @IndexedEmbedded 
    private Category category;

    @OneToOne
    @JoinColumn(name = "attId")
    @IndexedEmbedded
    private Attribute attribute;

    @Column(length = 60, nullable = false)
    @FullTextField 
    private String brand;

    @Column(length = 100, nullable = false)
    @FullTextField 
    private String prodName;

    @Column(columnDefinition = "TEXT", nullable = false)
    @FullTextField 
    private String description;

    @Column(length = 30, nullable = false)
    @GenericField 
    private String availability;

    @Column(precision = 10, nullable = false)
    @GenericField 
    private float mrp;

    @Column(precision = 10)
    @GenericField 
    private Float offerPrice;

    @OneToMany(mappedBy = "prodId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @IndexedEmbedded 
    private List<Images> images;

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
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

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> Images) {
        this.images = Images;
    }

}
