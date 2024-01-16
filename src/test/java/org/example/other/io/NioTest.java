package org.example.other.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NioTest {
    @Test
    void testWriteFile(){
        Nio.writeFile();
    }

    @Test
    void testEndiands(){
        Nio.endiands();
    }
}