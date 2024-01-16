package org.example.other.network.http;

import org.example.other.concur.collections.CacheManager;

import java.util.Map;

public class GeoCodeManager {

    private GeoCoder geoCoder;
    CacheManager cacheManager = CacheManager.instance();

    public GeoCodeManager(String apiKey) {
        geoCoder = new GeoCoder(apiKey);
        cacheManager.createCache("geoData", 1000_000_000);
    }

    public Map<String, Object> getGeoData(String cityName) {
        return cacheManager.getOrElse("geoData", cityName, () -> geoCoder.getGeoData(cityName));
    }

}
