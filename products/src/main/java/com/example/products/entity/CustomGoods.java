package com.example.products.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="cumtomgoods")
@Data
@NoArgsConstructor
@ToString
public class CustomGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer customgoodsId;
    private String customgoodsName;
    private String customgoodsImageUrl;
    private String customgoodsDescription;
}
