package com.klpdapp.klpd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table ( name = "images")
public class images {
     @Id
    @Column(length = 15, nullable = false)
    private String imgId;

    @Column(length = 15, nullable = false)
    private String product;

    @Column(length = 255, nullable = false, unique = true)
    private String imageUrl;

    @Column
    private Boolean isPrimary = false;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
