package com.klpdapp.klpd.model;

import jakarta.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table ( name = "Images")
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imgId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "pid")
    private Product pid;

    @Column(length = 500, nullable = false, unique = true)
    private String imageUrl;

    @Column
    private Boolean isPrimary = false;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    
    public Product getpid() {
        return pid;
    }

    public void setpid(Product pid) {
        this.pid = pid;
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
