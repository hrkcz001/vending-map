package dev.morozan1.server.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
public class MachineProductPK implements Serializable {
    @Column(name = "MACHINE_ID", nullable = false)
    private Long machineId;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;
}
