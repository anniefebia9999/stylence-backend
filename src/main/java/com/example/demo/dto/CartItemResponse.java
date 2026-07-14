package com.example.demo.dto;

import com.example.demo.model.CartItem;

public class CartItemResponse {
    private Long id;
    private Long productId;
    private String name;
    private Double price;
    private Double originalPrice;
    private String image;
    private String category;
    private Integer quantity;

    public CartItemResponse(CartItem item) {
        this.id = item.getId();
        this.productId = item.getProduct().getId();
        this.name = item.getProduct().getName();
        this.price = item.getProduct().getPrice();
        this.originalPrice = item.getProduct().getOriginalPrice();
        this.image = item.getProduct().getImage();
        this.category = item.getProduct().getCategory();
        this.quantity = item.getQuantity();
    }

    // Getters
    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getName() { return name; }
    public Double getPrice() { return price; }
    public Double getOriginalPrice() { return originalPrice; }
    public String getImage() { return image; }
    public String getCategory() { return category; }
    public Integer getQuantity() { return quantity; }
}