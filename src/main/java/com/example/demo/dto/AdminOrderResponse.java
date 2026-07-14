package com.example.demo.dto;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AdminOrderResponse {

    public static class OrderItemDetail {
        private String productName;
        private String productImage;
        private Integer quantity;
        private Double priceAtPurchase;

        public OrderItemDetail(OrderItem item) {
            this.productName = item.getProduct().getName();
            this.productImage = item.getProduct().getImage();
            this.quantity = item.getQuantity();
            this.priceAtPurchase = item.getPriceAtPurchase();
        }

        public String getProductName() { return productName; }
        public String getProductImage() { return productImage; }
        public Integer getQuantity() { return quantity; }
        public Double getPriceAtPurchase() { return priceAtPurchase; }
    }

    private Long orderId;
    private String customerEmail;
    private String customerName;
    private Double totalAmount;
    private String status;
    private String paymentMethod;
    private LocalDateTime createdAt;

    // Address fields, flattened for easy display
    private String shipFullName;
    private String shipPhone;
    private String shipAddressLine1;
    private String shipAddressLine2;
    private String shipCity;
    private String shipState;
    private String shipPincode;

    private List<OrderItemDetail> items;

    public AdminOrderResponse(Order order) {
        this.orderId = order.getId();
        this.customerEmail = order.getUser().getEmail();
        this.customerName = order.getUser().getFullName();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.paymentMethod = order.getPaymentMethod();
        this.createdAt = order.getCreatedAt();

        this.shipFullName = order.getShippingAddress().getFullName();
        this.shipPhone = order.getShippingAddress().getPhone();
        this.shipAddressLine1 = order.getShippingAddress().getAddressLine1();
        this.shipAddressLine2 = order.getShippingAddress().getAddressLine2();
        this.shipCity = order.getShippingAddress().getCity();
        this.shipState = order.getShippingAddress().getState();
        this.shipPincode = order.getShippingAddress().getPincode();

        this.items = order.getItems().stream().map(OrderItemDetail::new).collect(Collectors.toList());
    }

    public Long getOrderId() { return orderId; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerName() { return customerName; }
    public Double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getShipFullName() { return shipFullName; }
    public String getShipPhone() { return shipPhone; }
    public String getShipAddressLine1() { return shipAddressLine1; }
    public String getShipAddressLine2() { return shipAddressLine2; }
    public String getShipCity() { return shipCity; }
    public String getShipState() { return shipState; }
    public String getShipPincode() { return shipPincode; }
    public List<OrderItemDetail> getItems() { return items; }
}