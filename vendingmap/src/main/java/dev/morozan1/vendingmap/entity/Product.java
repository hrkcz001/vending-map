package dev.morozan1.vendingmap.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @Column(name = "PRODUCT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "PICTURE_URL")
    private String picture;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        Objects.requireNonNull(productId, "Product id must not be null");
        this.productId = productId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return productId != null && productId.equals(product.productId);
    }

    @Override
    public int hashCode() {
        return productId.hashCode();
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", picture='" + picture + '\'' +
                '}';
    }
}
