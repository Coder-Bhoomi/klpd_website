package com.klpdapp.klpd.dto;

public class imagesdto {
    private String prodId;
    private String imgId;
    private String imgURL;
    private Boolean isPrimary;
    public String getProdId() {
        return prodId;
    }
    public void setProdId(String prodId) {
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
