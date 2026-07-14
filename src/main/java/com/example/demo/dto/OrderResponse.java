package com.example.demo.dto;

import com.example.demo.model.Order;
import java.time.LocalDateTime;

public class OrderResponse {
    private Long orderId;
    private Double totalAmount;
    private String status;
    private String paymentMethod;
    private LocalDateTime createdAt;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.paymentMethod = order.getPaymentMethod();
        this.createdAt = order.getCreatedAt();
    }

    public Long getOrderId() { return orderId; }
    public Double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}