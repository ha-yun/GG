package com.example.products.controller;

import com.example.products.entity.Order;
import com.example.products.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")  // 모든 출처 허용 (테스트용)
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 내 주문 목록 조회 (이메일 기반)
    @GetMapping
    public List<Order> getMyOrders(@RequestHeader("X-Auth-User") String userEmail) {
        return orderService.getOrdersByUser(userEmail);
    }

    // 특정 주문 상세 조회
    @GetMapping("/{orderId}")
    public Order getOrderDetail(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    // (3) 전체 주문 목록 조회 (관리자용)
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}
