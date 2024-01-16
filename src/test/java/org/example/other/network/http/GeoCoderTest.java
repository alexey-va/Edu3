package org.example.other.network.http;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GeoCoderTest {

    @Test
    void testGeoCoder(){
        GeoCodeManager geoCodeManager = new GeoCodeManager("89aaa867ca18be2c092923155a3c2268");
        geoCodeManager.getGeoData("London");
        geoCodeManager.getGeoData("London");
        geoCodeManager.getGeoData("London");

    }

}