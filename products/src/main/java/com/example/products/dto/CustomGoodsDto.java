package com.example.products.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CustomGoodsDto {
    private String customgoodsName;
    private String customgoodsImageUrl;
    private String customgoodsDescription;
    @Builder
    public CustomGoodsDto(String customgoodsName, String customgoodsImageUrl, String customgoodsDescription) {
        this.customgoodsName = customgoodsName;
        this.customgoodsImageUrl = customgoodsImageUrl;
        this.customgoodsDescription = customgoodsDescription;
    }
}
