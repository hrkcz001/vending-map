package dev.morozan1.server.repository;

import dev.morozan1.server.config.AreaConfig;
import dev.morozan1.server.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

    @Query("SELECT m FROM Machine m WHERE " +
            "m.latitude BETWEEN :minLatitude AND :maxLatitude " +
            "AND m.longitude BETWEEN :minLongitude AND :maxLongitude")
    List<Machine> findInSquare(@Param("minLatitude") double minLatitude,
                               @Param("maxLatitude") double maxLatitude,
                               @Param("minLongitude") double minLongitude,
                               @Param("maxLongitude") double maxLongitude);
}