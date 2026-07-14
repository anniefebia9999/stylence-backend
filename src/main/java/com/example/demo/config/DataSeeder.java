package com.example.demo.config;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return; // already seeded, don't duplicate
        }

        productRepository.save(new Product("Floral Wrap Dress", 1299.0, 2499.0, "https://picsum.photos/seed/f1/300/380", "Women",
                "A lightweight wrap dress with a floral print, adjustable waist tie, and flutter sleeves. Made from breathable rayon fabric, perfect for daytime outings. Machine washable, available in sizes S-XL."));

        productRepository.save(new Product("Slim Fit Denim Jacket", 1799.0, 2999.0, "https://picsum.photos/seed/f2/300/380", "Men",
                "Classic slim-fit denim jacket with button closure and chest pockets. Made from durable cotton denim with a slight stretch for comfort. Pairs well with any casual outfit."));

        productRepository.save(new Product("Cotton Kurti Set", 999.0, 1799.0, "https://picsum.photos/seed/f3/300/380", "Ethnic Wear",
                "Comfortable cotton kurti with matching palazzo pants, featuring traditional block print. Ideal for daily wear or festive occasions. Hand wash recommended to preserve print quality."));

        productRepository.save(new Product("Kids Printed T-Shirt", 399.0, 699.0, "https://picsum.photos/seed/f4/300/380", "Kids",
                "Soft, breathable cotton t-shirt with fun printed graphics. Designed for all-day play with reinforced stitching for durability. Machine washable."));

        productRepository.save(new Product("Casual Sneakers", 1499.0, 2499.0, "https://picsum.photos/seed/f5/300/380", "Footwear",
                "Everyday sneakers with cushioned insole and breathable mesh upper. Non-slip rubber sole for all-day comfort. Available in multiple sizes."));

        productRepository.save(new Product("Statement Earrings", 349.0, 599.0, "https://picsum.photos/seed/f6/300/380", "Accessories",
                "Bold statement earrings with a gold-tone finish, lightweight and comfortable for all-day wear. Hypoallergenic posts, perfect for both casual and festive looks."));

        productRepository.save(new Product("Men's Formal Shirt", 899.0, 1499.0, "https://picsum.photos/seed/f7/300/380", "Men",
                "Crisp formal shirt in a regular fit, made from wrinkle-resistant cotton blend. Suitable for office wear or formal occasions. Machine washable."));

        productRepository.save(new Product("Anarkali Suit", 1999.0, 3499.0, "https://picsum.photos/seed/f8/300/380", "Ethnic Wear",
                "Elegant Anarkali suit with intricate embroidery, flared silhouette, and matching dupatta. Ideal for weddings and festive celebrations. Dry clean recommended."));

        productRepository.save(new Product("Women's Handbag", 1199.0, 1999.0, "https://picsum.photos/seed/f9/300/380", "Accessories",
                "Spacious handbag with multiple compartments, adjustable strap, and durable synthetic leather finish. Fits everyday essentials with room to spare."));

        productRepository.save(new Product("Kids Party Dress", 799.0, 1299.0, "https://picsum.photos/seed/f10/300/380", "Kids",
                "Frilly party dress with a soft tulle overlay and satin lining. Perfect for birthdays and special occasions. Gentle hand wash recommended."));

        productRepository.save(new Product("Leather Sandals", 1099.0, 1799.0, "https://picsum.photos/seed/f11/300/380", "Footwear",
                "Genuine leather sandals with cushioned footbed and adjustable buckle strap. Durable sole built for everyday comfort and long-lasting wear."));

        productRepository.save(new Product("Women's Palazzo Pants", 649.0, 1099.0, "https://picsum.photos/seed/f12/300/380", "Women",
                "Flowy palazzo pants with an elastic waistband for a comfortable fit. Made from soft rayon fabric, great for both casual and ethnic outfits."));

        System.out.println("Seeded 12 products into the database.");
    }
}