package com.klpdapp.klpd.dto;

import java.time.LocalDate;

public class OrderDTO {
    private int orderId;
    private int userId;
    private int totalAmt;
    private String paymentMode;
    private LocalDate orderDate;
    private String status;
    private int couponid;
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getTotalAmt() {
        return totalAmt;
    }
    public void setTotalAmt(int totalAmt) {
        this.totalAmt = totalAmt;
    }
    public String getPaymentMode() {
        return paymentMode;
    }
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
    public LocalDate getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getCouponid() {
        return couponid;
    }
    public void setCouponid(int couponid) {
        this.couponid = couponid;
    }
}

