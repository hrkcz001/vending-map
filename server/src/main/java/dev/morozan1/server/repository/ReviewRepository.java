package dev.morozan1.server.repository;

import dev.morozan1.server.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.reviewId = :reviewId AND r.machine.machineId = :machineId")
    Optional<Review> findByMachineAndReviewIds(@Param("machineId") long machineId, @Param("reviewId") long reviewId);
}