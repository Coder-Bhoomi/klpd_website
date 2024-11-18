package com.klpdapp.klpd.model;

import jakarta.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table (name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "productid" , nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    private Integer delivery;

    @Column(precision = 10, nullable = false)
    private float totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getDelivery() {
        return delivery;
    }

    public void setDelivery(Integer delivery) {
        this.delivery = delivery;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product Product) {
        this.product = Product;
    }

}
