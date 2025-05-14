package com.klpdapp.klpd.dto;

import java.time.LocalDate;

public class CouponDto {
	
	private int couponId;
	private String couponCode;
	private String couponName;
	private String description;
	private LocalDate issueDate;
	private LocalDate expireDate;
	private int discountRate;
	private int uptoAmount;
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}
	public LocalDate getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(LocalDate expireDate) {
		this.expireDate = expireDate;
	}
	public int getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(int discountRate) {
		this.discountRate = discountRate;
	}
	public int getUptoAmount() {
		return uptoAmount;
	}
	public void setUptoAmount(int uptoAmount) {
		this.uptoAmount = uptoAmount;
	}

}
