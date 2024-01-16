package org.example.other.io;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileCopyTest {

    @BeforeAll
    static void init() throws IOException {
        Nio.clearTestFile();
        Nio.writeFile();

    }


    public static Stream<FileCopy> getImpls(){
        return Stream.of(new DirectCopy(), new BufferedCopy(), new MappedBufferCopy());
    }

    @ParameterizedTest
    @MethodSource("getImpls")
    void testCopy(FileCopy copy) throws IOException {
        if(Files.exists(Paths.get("test3.txt"))) Files.delete(Paths.get("test3.txt"));
        copy.copy(Paths.get("test.txt"), Paths.get("test3.txt"));

        assertTrue(Files.exists(Paths.get("test3.txt")));
        assertEquals(Files.size(Paths.get("test.txt")), Files.size(Paths.get("test3.txt")));
    }

}