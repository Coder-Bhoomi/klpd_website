package com.klpdapp.klpd.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private String companyPid;
    private String hapPid;
    private String subcategory;
    private String brand;
    private String prodName;
    private String description;
    private LocalDate createdAt;
    private int stock;
    private Float mrp;
    private Float discount;
    private Float offerPrice;
    private int rating;
    private List<AttributeDto> attributes;
    private List<ImagesDto> images;
    // Getters and Setters
}
