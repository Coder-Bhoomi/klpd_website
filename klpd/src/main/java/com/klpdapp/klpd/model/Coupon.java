package com.klpdapp.klpd.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name ="coupon")
public class Coupon {
	
	@Id
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

	private int minCartValue;

	private int minQuantity;
	
}