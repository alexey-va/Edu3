package org.example;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Loggers;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LoggingExtension implements AfterEachCallback, AfterAllCallback {
    Logger logger = LogManager.getLogger("FileLogger");

    private static int totalTests = 0;
    private static int failedTests = 0;
    private static List<String> messages = new ArrayList<>();


    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        String result = "Test "+ extensionContext.getDisplayName()+" finished with status" +
                " "+(extensionContext.getExecutionException().isPresent() ? "FAIL" : "OK");
        messages.add(result);
        if (extensionContext.getExecutionException().isPresent()) {
            failedTests++;
        }
        totalTests++;
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        messages.add("---------------------------");
        messages.add("|  All tests finished");
        messages.add("|  Total tests: "+ totalTests);
        messages.add("|  Failed tests: "+ failedTests);
        messages.add("|  Success rate: "+ ((totalTests - failedTests) * 100.0 / totalTests));
        messages.add("---------------------------");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy.HH-mm-ss");
        String dateStr = now.format(formatter);
        String name = "LOG_"+dateStr;
        Path pth = Paths.get("ziplogs/"+name+".zip");

        if(!Files.exists(pth.getParent())) Files.createDirectories(pth.getParent());
        Files.deleteIfExists(pth);

        FileSystem fs = FileSystems.newFileSystem(pth, Map.of("create", true));
        Files.createFile(fs.getPath("/"+name+".log"));
        Files.write(fs.getPath("/"+name+".log"), messages);

        fs.close();
    }
}
