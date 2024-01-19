package dev.morozan1.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @Setter
    @Column(name = "PRODUCT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Setter
    @Column(name = "PICTURE_URL")
    private String picture;

    @Setter
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MachineProduct> productsMachine = new HashSet<>();

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Product name must not be null");
    }

    public Double getAveragePrice() {
        if (productsMachine == null || productsMachine.isEmpty()) {
            return null;
        }

        double result = productsMachine.stream()
                .mapToDouble(MachineProduct::getPrice)
                .average()
                .orElse(-1);

        return result == -1 ? null : result;
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
