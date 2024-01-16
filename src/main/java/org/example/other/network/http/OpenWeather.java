package org.example.other.network.http;

import lombok.RequiredArgsConstructor;
import org.example.other.concur.collections.CacheManager;
import org.example.other.network.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class OpenWeather {

    private final String apiKey;
    private final GeoCodeManager geoCodeManager;

    private final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    public OpenWeather(String apiKey) {
        this.apiKey = apiKey;
        this.geoCodeManager = new GeoCodeManager(apiKey);
        CacheManager cacheManager = CacheManager.instance();
        cacheManager.createCache("weather", 100_000);
    }

    public Map<String, Object> city(String city){
        CacheManager cacheManager = CacheManager.instance();
        return cacheManager.getOrElse("weather", city, () -> byCity(city));
    }

    private Map<String, Object> byCity(String city){
        Map<String, Object> geoData = geoCodeManager.getGeoData(city);
        StringBuilder builder = new StringBuilder(WEATHER_URL+"?");
        builder.append("lat=").append(geoData.get("lat"));
        builder.append("&lon=").append(geoData.get("lon"));
        builder.append("&appid=").append(apiKey);

        try{
            System.out.println("Requesting weather data for "+city);
            URL url = new URL(builder.toString());
            builder = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while (reader.ready()){
                builder.append(reader.readLine());
            }

            return JsonParser.parse(builder.toString());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
