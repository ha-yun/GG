package com.example.products.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ProductDto {
    private String pdtName;
    private Integer pdtPrice;
    @Builder
    public ProductDto(String pdtName, Integer pdtPrice) {
        this.pdtName = pdtName;
        this.pdtPrice = pdtPrice;
    }
}
