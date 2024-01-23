package dev.morozan1.server.repository;

import dev.morozan1.server.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.productId NOT IN (SELECT mp.product.productId FROM MachineProduct mp WHERE mp.machine.machineId = :machineId)")
    List<Product> findProductsThatNotIn(@Param("machineId") long machineId);
}