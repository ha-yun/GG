package com.example.products.controller;

import com.example.products.entity.Order;
import com.example.products.service.OrderService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pdts/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 내 주문 목록 조회
    @GetMapping
    public List<Order> getMyOrders(@RequestHeader("X-Auth-User") String userEmail) {
        return orderService.getOrdersByUser(userEmail);
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public Order getOrderDetail(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }
}
