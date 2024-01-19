package dev.morozan1.server.repository;

import dev.morozan1.server.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.machine.machineId = :machineId")
    List<Review> findAllByMachineId(long machineId);
}