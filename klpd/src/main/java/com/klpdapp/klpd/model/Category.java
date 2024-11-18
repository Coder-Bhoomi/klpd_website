package com.klpdapp.klpd.model;

import jakarta.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table ( name ="Category")
public class Category {
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
