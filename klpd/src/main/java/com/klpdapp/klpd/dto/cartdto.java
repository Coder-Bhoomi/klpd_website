package com.klpdapp.klpd.dto;

public class cartdto {
    private String productId;
    private Integer quantity;
    private Integer delivery;
    private float totalPrice;
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
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
