package com.klpdapp.klpd.model;

import jakarta.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table ( name = "Images")
public class Images {
    @Id
    @Column(length = 15, nullable = false)
    private String imgId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "prod_id")
    private Product prodId;

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
    
    public Product getProdId() {
        return prodId;
    }

    public void setProdId(Product prodId) {
        this.prodId = prodId;
    }

    public void setImageUrl(String ImageUrl) {
        this.imageUrl = ImageUrl;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
