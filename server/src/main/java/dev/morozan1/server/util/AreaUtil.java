package dev.morozan1.server.util;

import dev.morozan1.server.config.AreaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AreaUtil {

    private static AreaConfig areaConfig;

    @Autowired
    public AreaUtil(AreaConfig areaConfig) {
        AreaUtil.areaConfig = areaConfig;
    }

    public static boolean isInArea(Double latitude, Double longitude) {
        return latitude >= areaConfig.getMinLatitude() && latitude <= areaConfig.getMaxLatitude()
                && longitude >= areaConfig.getMinLongitude() && longitude <= areaConfig.getMaxLongitude();
    }
}
