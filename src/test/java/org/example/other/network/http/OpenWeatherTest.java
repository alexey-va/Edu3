package org.example.other.network.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenWeatherTest {

    @Test
    void testOpenWeather() throws InterruptedException {
        OpenWeather openWeather = new OpenWeather("89aaa867ca18be2c092923155a3c2268");
        System.out.println(openWeather.city("London"));
        System.out.println(openWeather.city("London"));
        System.out.println(openWeather.city("London"));

        Thread.sleep(3000);

        System.out.println(openWeather.city("London"));
    }

}