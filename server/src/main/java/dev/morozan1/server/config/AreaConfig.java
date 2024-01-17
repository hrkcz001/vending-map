package dev.morozan1.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AreaConfig {

    @Value("${area.min.latitude}")
    private Double minLatitude;

    @Value("${area.max.latitude}")
    private Double maxLatitude;

    @Value("${area.min.longitude}")
    private Double minLongitude;

    @Value("${area.max.longitude}")
    private Double maxLongitude;
}
