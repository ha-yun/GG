package com.example.products.kafka;

import com.example.products.dto.PaymentDto;
import com.example.products.entity.Order;
import com.example.products.entity.ProductEntity;
import com.example.products.repository.OrderRepository;
import com.example.products.repository.ProductsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class KafkaConsumer {
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final ProductsRepository productsRepository;

    @Autowired
    public KafkaConsumer(ObjectMapper objectMapper, OrderRepository orderRepository, ProductsRepository productsRepository) {
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
        this.productsRepository = productsRepository;
    }

    @KafkaListener(topics = "msa-sb-products-payment", groupId = "order-group")
    public void listenPaymentComplete(String message) {
        try {
            System.out.println("✅ Kafka 메시지 수신: " + message);

            // Kafka 메시지를 DTO로 변환
            PaymentDto paymentDto = objectMapper.readValue(message, PaymentDto.class);

            // 상품 정보 가져오기
            Optional<ProductEntity> optionalProduct = productsRepository.findById(Integer.parseInt(paymentDto.getPdtId()));

            if (optionalProduct.isEmpty()) {
                System.out.println("❌ 상품 정보를 찾을 수 없음! 상품 ID: " + paymentDto.getPdtId());
                return;
            }

            ProductEntity product = optionalProduct.get();

            // 주문 객체 생성 후 저장
            Order order = new Order();
            order.setPdtId(product.getPdtId());
            order.setPdtPrice(product.getPdtPrice());
            order.setPdtName(product.getPdtName());
            order.setPdtQuantity(product.getPdtQuantity());
            order.setAmount(Integer.parseInt(paymentDto.getAmount()));
            order.setOrderStatus("COMPLETED");
            order.setOrderDate(LocalDateTime.now());
            order.setEmail(paymentDto.getEmail());

            // DB에 저장
            orderRepository.save(order);
            System.out.println("✅ 주문 저장 완료: " + order);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
