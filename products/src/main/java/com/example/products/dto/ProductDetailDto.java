package com.example.products.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDetailDto {
    private Integer pdtId;
    private String pdtName;
    private Integer pdtPrice;
    private Integer pdtQuantity;
    @Builder
    public ProductDetailDto(Integer ptId, Integer ptPrice, String ptName, Integer ptQuantity) {
        this.pdtId = ptId;
        this.pdtName = ptName;
        this.pdtPrice = ptPrice;
        this.pdtQuantity = ptQuantity;
    }
}
