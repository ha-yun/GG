package com.example.products.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Integer pdtId;
    private Integer pdtPrice;
    private String pdtName;
    private Integer pdtQuantity;

    private Integer amount;
    private String orderStatus;
    private LocalDateTime orderDate;
    private String email;
}
