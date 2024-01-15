package dev.morozan1.server.repository;

import dev.morozan1.server.entity.MachineProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineProductRepository extends JpaRepository<MachineProduct, Long> {}