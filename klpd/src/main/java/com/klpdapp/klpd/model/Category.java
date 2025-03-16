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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "segmentId", nullable = false)
    private Segment segment;

    @Column ( length = 60 , nullable = false)
    private String categoryImage;
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

    public Segment getSegment() {
        return segment;
    }
    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public String getCategoryImage() {
        return categoryImage;
    }
    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }
}
