package com.klpdapp.klpd.model;

import jakarta.persistence.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import java.time.LocalDate;

@Entity
@Indexed
@Table(name = "products")
@Access(AccessType.FIELD)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericField
    private int pid;

    @Column(nullable = false, length = 50, unique = true)
    @FullTextField
    private String companyPid;

    @Column(nullable = false, length = 50, unique = true)
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

    // Getters and Setters

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCompanyPid() {
        return companyPid;
    }

    public void setCompanyPid(String companyPid) {
        this.companyPid = companyPid;
    }

    public String getHapPid() {
        return hapPid;
    }

    public void setHapPid(String hapPid) {
        this.hapPid = hapPid;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(SubCategory subcategory) {
        this.subcategory = subcategory;
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

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Float getMrp() {
        return mrp;
    }

    public void setMrp(Float mrp) {
        this.mrp = mrp;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public Float getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Float offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getCartonDimension() {
        return cartonDimension;
    }

    public void setCartonDimension(String cartonDimension) {
        this.cartonDimension = cartonDimension;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
