package org.example.other.io;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public class DirectCopy extends FileCopy{

    public void copy(Path from, Path to) throws IOException {
        if(!Files.exists(from)) throw new NoSuchFileException("No file: "+ from);
        if(Files.exists(to)) throw new FileAlreadyExistsException(to.toString());

        try(FileChannel channel = FileChannel.open(from, StandardOpenOption.READ);
            FileChannel writeChannel = FileChannel.open(to, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)){
            channel.transferTo(0, channel.size(), writeChannel);
        }
    }

}
