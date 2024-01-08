package org.example.other.io;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Log4j2
class FirstReaderTest {

    @Test
    void abc() throws Exception {
        Path path = Paths.get("test.zip");
        Files.deleteIfExists(path);
        log.info(path.toFile().exists());
        var fs = FileSystems.newFileSystem(path, Map.of("create", true));
        log.info(fs.getPath("/"));
        Files.createDirectories(fs.getPath("/test/test"));
        Files.createFile(fs.getPath("/test/test/kek.txt"));
        Files.createFile(fs.getPath("/kek2.txt"));

        Files.write(fs.getPath("/kek2.txt"), "kek".getBytes());

        fs.close();

        MyInterface myInterface = () -> {
            log.info("123");
        };
        myInterface.doStuff();
        myInterface.doOther();


    }

    @FunctionalInterface
    static interface MyInterface {
        public void doStuff();

        public default void doOther(){
            log.info("321");
        }
    }

}