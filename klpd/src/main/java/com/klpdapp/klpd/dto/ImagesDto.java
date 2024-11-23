package com.klpdapp.klpd.dto;

import com.klpdapp.klpd.model.Product;

public class ImagesDto {
    private Product pid;
    private String imgId;
    private String imgURL;  
    private Boolean isPrimary;
    
    public Product getpid() {
        return pid;
    }
    public void setpid(Product pid) {
        this.pid = pid;
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
