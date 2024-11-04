package com.klpdapp.klpd.dto;

public class productdto {
    private String prodId;         
    private String categoryId;     
    private String attId;         
    private String brand;
    private String prodName;      
    private String description;     
    private String availability;
    private int stock;    
    private float mrp;        
    private Float offerPrice;
    public String getProdId() {
        return prodId;
    }
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public String getAttId() {
        return attId;
    }
    public void setAttId(String attId) {
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
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
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
