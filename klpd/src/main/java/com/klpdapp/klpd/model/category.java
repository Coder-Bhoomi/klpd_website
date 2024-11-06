package com.klpdapp.klpd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table ( name ="Category")
public class category {
    @Id
    @Column ( length = 15 , nullable = false)
    private String categoryId;
    @Column ( length = 60 , nullable = false)
    private String categoryName;
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
