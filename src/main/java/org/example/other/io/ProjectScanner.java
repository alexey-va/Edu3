package org.example.other.io;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Log4j2
public class ProjectScanner {

    String str;

    public ProjectScanner(String str) {
        this.str = str;

    }

    @SneakyThrows
    public List<Path> lookup(boolean parallel){
        Stream<Path> walk = Files.walk(Paths.get("."), 15)
                .filter(p -> p.toString().toLowerCase().endsWith(".java"))
                .filter(p -> !Files.isDirectory(p))
                .filter(p -> {
                    try {
                        Stream<String> lines = Files.lines(p);
                        try(lines) {
                            if(parallel) return lines.parallel().anyMatch(s -> s.contains(str));
                            return lines.anyMatch(s -> s.contains(str));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        try(walk){
            if(parallel) return walk.parallel().toList();
            return walk.toList();
        }
    }

    @SneakyThrows
    public List<Path> lookupWeb(){
        return null;
    }
}
