package com.example.products.service;

import com.example.products.dto.CustomGoodsDto;
import com.example.products.entity.CustomGoods;
import com.example.products.repository.CustomGoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomGoodsService {
    private final CustomGoodsRepository customGoodsRepository;

    public void saveCustomGoods(CustomGoodsDto customGoodsDto) {
        CustomGoods customGoods = new CustomGoods();
        customGoods.setCustomgoodsName(customGoodsDto.getCustomgoodsName());
        customGoods.setCustomgoodsImageUrl(customGoodsDto.getCustomgoodsImageUrl());
        customGoods.setCustomgoodsDescription(customGoodsDto.getCustomgoodsDescription());
        customGoodsRepository.save(customGoods);
    }

    public List<CustomGoodsDto> allCustomGoods() {
        List<CustomGoods> customGoodsList = customGoodsRepository.findAll();
        return customGoodsList.stream()
                .map( g -> CustomGoodsDto.builder()
                        .customgoodsName(g.getCustomgoodsName())
                        .customgoodsImageUrl(g.getCustomgoodsImageUrl())
                        .customgoodsDescription(g.getCustomgoodsDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
