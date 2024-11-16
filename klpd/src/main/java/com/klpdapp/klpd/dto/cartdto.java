package com.klpdapp.klpd.dto;

public class CartDto {
    private String ProductId;
    private Integer quantity;
    private Integer delivery;
    private float totalPrice;
    public String getProductId() {
        return ProductId;
    }
    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Integer getDelivery() {
        return delivery;
    }
    public void setDelivery(Integer delivery) {
        this.delivery = delivery;
    }
    public float getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
