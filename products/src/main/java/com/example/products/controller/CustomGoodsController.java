package com.example.products.controller;

import com.example.products.dto.CustomGoodsDto;
import com.example.products.service.CustomGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customgoods")
@RequiredArgsConstructor
public class CustomGoodsController {
    private final CustomGoodsService customGoodsService;

    @PostMapping("/save")
    public ResponseEntity<String> saveCustomGoods(
            @RequestBody CustomGoodsDto customGoodsDto) {
        customGoodsService.saveCustomGoods(customGoodsDto);
        return ResponseEntity.ok("커스텀 굿즈 저장 완료");
    }

    @GetMapping
    public ResponseEntity<List<CustomGoodsDto>> getAllCustomGoods() {
        return ResponseEntity.ok(customGoodsService.allCustomGoods());
    }
}
