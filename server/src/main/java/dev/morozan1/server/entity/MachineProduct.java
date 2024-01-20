package dev.morozan1.server.entity;

import lombok.Getter;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Table(name = "MACHINE_PRODUCT",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"MACHINE_ID", "PRODUCT_ID"})

})
public class MachineProduct {

    @EmbeddedId
    private MachineProductPK machineProductPK = new MachineProductPK();

    @ManyToOne
    @MapsId("machineId")
    @JoinColumn(name = "MACHINE_ID", nullable = false)
    private Machine machine;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Column(name = "AVAILABILITY", nullable = false)
    private Boolean availability;

    @Setter
    @Column(name = "PRICE", nullable = false)
    private Double price;

    public void setMachine(Machine machine) {
        Objects.requireNonNull(machine, "Machine must not be null");
        this.machine = machine;
    }

    public void setProduct(Product product) {
        Objects.requireNonNull(product, "Product must not be null");
        this.product = product;
    }

    public void setAvailability(Boolean availability) {
        Objects.requireNonNull(availability, "Availability must not be null");
        this.availability = availability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MachineProduct machineProduct)) return false;
        return machine.equals(machineProduct.machine) &&
                product.equals(machineProduct.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machine, product);
    }

    @Override
    public String toString() {
        return "MachineProduct{" +
                "machineProductId=" +
                ", machine=" + machine +
                ", product=" + product +
                ", availability=" + availability +
                '}';
    }
}