package com.klpdapp.klpd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "attributes")
public class attribute {
    @Id
    @Column(length = 15, nullable = false)
    private String attId;

    @Column(length = 15)
    private String diameter;

    @Column(length = 15)
    private String thickness;

    @Column(length = 15)
    private String capacity;

    @Column(length = 50)
    private String cartonDimension;

    @Column(length = 15)
    private String weight;

    @Column(length = 10)
    private String guarantee;

    @Column(length = 20)
    private String color;

    public String getAttId() {
        return attId;
    }

    public void setAttId(String attId) {
        this.attId = attId;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCartonDimension() {
        return cartonDimension;
    }

    public void setCartonDimension(String cartonDimension) {
        this.cartonDimension = cartonDimension;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
