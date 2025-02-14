package com.example.products.controller;


import com.example.products.dto.PaymentDto;
import com.example.products.dto.ProductDetailDto;
import com.example.products.dto.ProductDto;
import com.example.products.dto.ShoppingCartReqDto;
import com.example.products.entity.ProductEntity;
import com.example.products.kafka.KafkaProducer;
import com.example.products.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pdts")
public class ProductsController {
    @Autowired
    private ProductsService productsService;
    @Autowired
    private KafkaProducer kafkaProducer;


    @GetMapping
    public ResponseEntity<List<ProductDto>> allProducts() {
        return ResponseEntity.ok( productsService.allProducts() );
    }

    @GetMapping("/detail/{pdtId}")
    public ResponseEntity<ProductDetailDto> productDetail(@PathVariable Integer pdtId) {
        return ResponseEntity.ok(productsService.getProductDetailInfo(pdtId));
    }

    @PostMapping("/shoppingcart")
    public ResponseEntity<String> addShoppingCart(
            @RequestHeader("X-Auth-User") String email,
            @RequestBody ShoppingCartReqDto shoppingCartReqDto) {
        productsService.addShoppingCart(email, shoppingCartReqDto);
        return ResponseEntity.ok("ShoppingCart added");
    }
    @GetMapping("/shoppingcart")
    public ResponseEntity<List<ShoppingCartReqDto>> getShoppingCart(@RequestHeader("X-Auth-User") String email) {
        return ResponseEntity.ok(productsService.getShoppingCart(email));
    }

    // 장바구니 목록 삭제
    @DeleteMapping("/shoppingcart/{pdtId}")
    public ResponseEntity<String> removeItemFromCart(
            @RequestHeader("X-Auth-User") String email,
            @PathVariable Integer pdtId) {
        productsService.removeItemFromCart(email, pdtId);
        return ResponseEntity.ok("Item removed from shopping cart");
    }
    // 장바구니 전체 목록 삭제
    @DeleteMapping("/shoppingcart")
    public ResponseEntity<String> clearShoppingCart(@RequestHeader("X-Auth-User") String email) {
        productsService.clearShoppingCart(email);
        return ResponseEntity.ok("Shopping cart cleared");
    }



    @PostMapping("/payment")
    public ResponseEntity<String> payment(
            @RequestHeader("X-Auth-User") String email,
            @RequestBody PaymentDto paymentDto)
    {
        paymentDto.setEmail(email);
        try {
            kafkaProducer.sendMsg("msa-sb-products-payment", paymentDto);
        }catch (Exception e){}
        return ResponseEntity.ok("결제 완료");
    }

    @GetMapping("/search")
    public List<ProductEntity> searchProducts(@RequestParam String keyword) {
        return productsService.searchProducts(keyword);
    }

    // 가격 필터링 API
    @GetMapping("/filter")
    public List<ProductEntity> filterProductsByPrice(@RequestParam int minPrice, @RequestParam int maxPrice) {
        return productsService.filterProductsByPrice(minPrice, maxPrice);
    }


}
