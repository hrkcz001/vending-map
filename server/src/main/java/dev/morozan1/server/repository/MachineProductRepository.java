package dev.morozan1.server.repository;

import dev.morozan1.server.entity.MachineProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineProductRepository extends JpaRepository<MachineProduct, Long> {

    @Query("SELECT mp FROM MachineProduct mp WHERE mp.machine.machineId = :machineId")
    List<MachineProduct> findAllByMachineId(@Param("machineId") long machineId);
}