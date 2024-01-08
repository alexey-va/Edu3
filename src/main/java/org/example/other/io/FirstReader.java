package org.example.other.io;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.nio.file.Paths;

@Log4j2

public class FirstReader implements AutoCloseable{

    File file;
    FileReader reader;
    InputStream inputStream;

    @SneakyThrows
    public FirstReader(String name) {
        this.file = new File(getClass().getClassLoader().getResource(name).getFile());
        reader = new FileReader(file);
        InputStream inputStream = getClass().getClassLoader().getResource(name).openStream();
       //InputStream inputStream1 = Paths.get(getClass().getClassLoader().getResource(name);
    }

    @SneakyThrows
    public String read(int lines) {
        StringBuilder builder = new StringBuilder();
        int linesRead = 0;
        while (reader.ready() && linesRead < lines){
            char c = (char)reader.read();
            //log.info(c+" "+(int)c);
            if(c == '\n'){
                linesRead++;
                if (linesRead == lines) break;
            }
            builder.append(c);
        }
        return builder.toString();
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }
}
