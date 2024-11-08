package com.klpdapp.klpd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table ( name = "images")
public class images {
    @Id
    @Column(length = 15, nullable = false)
    private String imgId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "prod_id")
    private product prodId;

    @Column(length = 500, nullable = false, unique = true)
    private String imageUrl;

    @Column
    private Boolean isPrimary = false;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    
    public product getProdId() {
        return prodId;
    }

    public void setProdId(product prodId) {
        this.prodId = prodId;
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
