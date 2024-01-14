package org.example.other.filemanager;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerFrameTest {
    @Test
    public void testManager() throws InterruptedException {
        FMPane pane = new FMPane(Paths.get("."));
        JFrame frame = new FileManagerFrame(pane);
        Thread.sleep(1000000);
    }
}