package com.klpdapp.klpd.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table (name ="coupon")
public class Coupon {
	
	@Id
	@Column(length = 15, nullable = false)
	private int couponId;
	
	@Column(length = 20, nullable = false)
	private String couponCode;
	
	@Column(length = 60, nullable = false)
	private String couponName;
	
	@Column(nullable = false, columnDefinition = "TEXT")
    private String description;

	@Column(name = "issue_date")
	private LocalDate issueDate;
	
	@Column(name = "expire_date")
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
