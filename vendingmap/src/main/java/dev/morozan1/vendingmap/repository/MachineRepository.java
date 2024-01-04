package dev.morozan1.vendingmap.repository;

import dev.morozan1.vendingmap.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {}