package com.klpdapp.klpd.dto;

import com.klpdapp.klpd.model.Category;

public class SubCategoryDto {
    private String subcategoryId;
    private String subcategoryName;
    private Category category;

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public String getSubcategoryId() {
        return subcategoryId;
    }
    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }
    public String getSubcategoryName() {
        return subcategoryName;
    }
    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }
    
}
