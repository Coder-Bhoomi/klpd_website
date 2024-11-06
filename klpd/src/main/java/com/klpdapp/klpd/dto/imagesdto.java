package com.klpdapp.klpd.dto;

import com.klpdapp.klpd.model.product;

public class imagesdto {
    private product prodId;
    private String imgId;
    private String imgURL;
    private Boolean isPrimary;
    
    public product getProdId() {
        return prodId;
    }
    public void setProdId(product prodId) {
        this.prodId = prodId;
    }
    public String getImgId() {
        return imgId;
    }
    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
    public String getImgURL() {
        return imgURL;
    }
    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
    public Boolean getIsPrimary() {
        return isPrimary;
    }
    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }  
}
