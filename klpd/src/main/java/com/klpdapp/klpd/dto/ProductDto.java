package com.klpdapp.klpd.dto;

import com.klpdapp.klpd.model.Attribute;
import com.klpdapp.klpd.model.Category;

public class ProductDto {
    private String prodId;         
    private Category categoryId;     
    private Attribute attId;         
    private String brand;
    private String prodName;      
    private String description;     
    private String availability;  
    private float mrp;        
    private Float offerPrice;
    public String getProdId() {
        return prodId;
    }
    public void setProdId(String prodId) {
        this.prodId = prodId;
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
    public Category getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }
    public Attribute getAttId() {
        return attId;
    }
    public void setAttId(Attribute attId) {
        this.attId = attId;
    }

}
