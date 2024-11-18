package com.klpdapp.klpd.model;

import jakarta.persistence.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Entity
@Indexed 
@Table(name = "attributes")
@Access(AccessType.FIELD)
public class Attribute {

    @Id
    @Column(length = 15, nullable = false)
    @GenericField 
    private String attId;

    @Column(length = 15)
    @FullTextField
    private String diameter;

    @Column(length = 15)
    @FullTextField 
    private String thickness;

    @Column(length = 15)
    @FullTextField 
    private String capacity;

    @Column(length = 50)
    @FullTextField 
    private String cartonDimension;

    @Column(length = 15)
    @FullTextField 
    private String weight;

    @Column(length = 10)
    @FullTextField 
    private String guarantee;

    @Column(length = 20)
    @FullTextField 
    private String color;

    @Column(length = 15)
    @FullTextField
    private String length;

    @Column(length = 15)
    @FullTextField
    private String height;

    @Column(length = 15)
    @FullTextField
    private String type;

    @OneToOne(mappedBy = "attribute")  
    private Product product;

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Getters and Setters
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
