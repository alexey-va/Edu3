package org.example.other.network.http;


import lombok.RequiredArgsConstructor;
import org.example.other.network.JsonParser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class GeoCoder {

    private final String apiKey;
    static final String GEOCODER_URL = "http://api.openweathermap.org/geo/1.0/direct";


    public Map<String, Object> getGeoData(String cityName) {
        System.out.println("getGeoData of "+cityName);
        return (Map<String, Object>) getGeoData(cityName, null, null, null).get(0);
    }


    private List<Object> getGeoData(@Nonnull String cityName, @Nullable String stateCode,
                                            @Nullable String countryCode, @Nullable Integer limit) {
        String locationName = Stream.of(cityName, stateCode, countryCode)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(","));

        StringBuilder builder = new StringBuilder(GEOCODER_URL);
        builder.append("?q=").append(locationName);
        if (limit != null) builder.append("&limit=").append(limit);
        builder.append("&appid=").append(apiKey);
        String urlString = builder.toString();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            builder = new StringBuilder();
            while (reader.ready()) {
                String line = reader.readLine();
                builder.append(line);
                //System.out.println(line);
            }
            return JsonParser.parseArray(builder.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
