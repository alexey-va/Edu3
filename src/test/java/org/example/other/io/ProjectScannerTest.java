package org.example.other.io;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class ProjectScannerTest {


    @ParameterizedTest
    @ValueSource(booleans = {false, true, false, true})
    void testLookup(boolean parallel) {
        ProjectScanner projectScanner = new ProjectScanner("lombok");
        var result = projectScanner.lookup(parallel);
        result.stream().map(Path::getFileName).forEach(log::info);
        log.info("Found total: {}", result.size());
    }

    @Test
    @SneakyThrows
    void testLookupWeb() {
        URI uri = URI.create("https://google.com");




    }

}