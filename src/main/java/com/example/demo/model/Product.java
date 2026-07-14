package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double price;

    @NotNull
    @Positive
    @Column(name = "original_price", nullable = false)
    private Double originalPrice;

    @NotBlank
    @Column(nullable = false)
    private String image;

    @NotBlank
    @Column(nullable = false)
    private String category;

    @Column(length = 2000)
    private String description;

    @Column(name = "additional_images", length = 2000)
    private String additionalImages; // comma-separated URLs for extra views (side, back, etc.)

    @Column(length = 1000)
    private String remark; // ADMIN-ONLY internal note (e.g. supplier link) — never exposed on public storefront

    public Product() {}

    public Product(String name, Double price, Double originalPrice, String image, String category, String description) {
        this.name = name;
        this.price = price;
        this.originalPrice = originalPrice;
        this.image = image;
        this.category = category;
        this.description = description;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAdditionalImages() { return additionalImages; }
    public void setAdditionalImages(String additionalImages) { this.additionalImages = additionalImages; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}