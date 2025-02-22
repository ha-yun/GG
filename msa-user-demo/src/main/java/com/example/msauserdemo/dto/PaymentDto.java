package com.example.msauserdemo.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PaymentDto {
    private String goodsId;
    private String quantity;
    private String email;

    @Builder
    public PaymentDto(String goodsId, String quantity, String email) {
        this.goodsId = goodsId;
        this.quantity = quantity;
        this.email = email;
    }
}
