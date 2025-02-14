package com.example.products.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShoppingCartReqDto {
    private Integer pdtId;
    private Integer pdtPrice;
    private String pdtName;
    private Integer pdtQuantity;

    @Builder
    public ShoppingCartReqDto(Integer pdtId, Integer pdtPrice, String pdtName, Integer pdtQuantity) {
        this.pdtId = pdtId;
        this.pdtPrice = pdtPrice;
        this.pdtName = pdtName;
        this.pdtQuantity = pdtQuantity;
    }

    @Builder
    public ShoppingCartReqDto(Integer pdtPrice, String pdtName, Integer pdtQuantity) {
        this.pdtPrice = pdtPrice;
        this.pdtName = pdtName;
        this.pdtQuantity = pdtQuantity;
    }
}
