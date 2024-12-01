package com.klpdapp.klpd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name= "pincode")
public class Pincode {
    @Column(name="pincode_id")
    @Id
    private int pincodeId;
    @Column(name="Pincode")
    private int pincode;
    @Column(name = "CircleName")
    private String circleName;
    @Column(name = "RegionName")
    private String regionName;
    @Column(name = "DivisionName")
    private String divisionName;
    @Column(name="District")
    private String district;
    @Column(name="StateName")
    private String stateName;
    @Column(name = "Delivery")
    private String delivery;
    public int getPincodeId() {
        return pincodeId;
    }
    public void setPincodeId(int pincodeId) {
        this.pincodeId = pincodeId;
    }
    public int getPincode() {
        return pincode;
    }
    public void setPincode(int pincode) {
        this.pincode = pincode;
    }
    public String getCircleName() {
        return circleName;
    }
    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }
    public String getRegionName() {
        return regionName;
    }
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
    public String getDivisionName() {
        return divisionName;
    }
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getStateName() {
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    public String getDelivery() {
        return delivery;
    }
    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }
}
