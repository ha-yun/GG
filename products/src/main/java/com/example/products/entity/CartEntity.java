package com.example.products.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="cart")
@Data
@NoArgsConstructor
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer cartId;
    private Integer pdtId;

    private String pdtName;
    private Integer price;
    private Integer quantity;

    private String email;

    @Builder
    public CartEntity(Integer pdtId, String pdtName, Integer price, Integer quantity, String email) {
        this.pdtId = pdtId;
        this.pdtName = pdtName;
        this.price = price;
        this.quantity = quantity;
        this.email = email;
    }
}
