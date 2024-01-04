package dev.morozan1.vendingmap.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "MACHINE_PRODUCT",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"MACHINE_ID", "PRODUCT_ID"})

})
public class MachineProduct {

    @Id
    @Column(name = "MACHINE_PRODUCT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long machineProductId;

    @ManyToOne
    @JoinColumn(name = "MACHINE_ID", nullable = false)
    private Machine machine;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    //@NotNull(message = "Machine availability must not be null")
    @Column(name = "AVAILABILITY", nullable = false)
    private Boolean availability;

    public Long getMachineProductId() {
        return machineProductId;
    }

    public void setMachineProductId(Long machineProductId) {
        Objects.requireNonNull(machineProductId, "Machine product id must not be null");
        this.machineProductId = machineProductId;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        Objects.requireNonNull(machine, "Machine must not be null");
        this.machine = machine;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        Objects.requireNonNull(product, "Product must not be null");
        this.product = product;
    }

    public boolean getAvailability() {
        return availability;
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
                "machineProductId=" + machineProductId +
                ", machine=" + machine +
                ", product=" + product +
                ", availability=" + availability +
                '}';
    }
}