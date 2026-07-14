package com.example.demo.dto;

import com.example.demo.model.Product;

public class ProductPublicResponse {
    private Long id;
    private String name;
    private Double price;
    private Double originalPrice;
    private String image;
    private String category;
    private String description;
    private String additionalImages;

    public ProductPublicResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.originalPrice = product.getOriginalPrice();
        this.image = product.getImage();
        this.category = product.getCategory();
        this.description = product.getDescription();
        this.additionalImages = product.getAdditionalImages();
        // remark intentionally omitted — never sent to the public storefront
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Double getPrice() { return price; }
    public Double getOriginalPrice() { return originalPrice; }
    public String getImage() { return image; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getAdditionalImages() { return additionalImages; }
}