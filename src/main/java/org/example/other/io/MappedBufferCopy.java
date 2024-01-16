package org.example.other.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public class MappedBufferCopy extends FileCopy {

    @Override
    public void copy(Path from, Path to) throws IOException {
        if(!Files.exists(from)) throw new NoSuchFileException("No file: "+ from);
        if(Files.exists(to)) throw new FileAlreadyExistsException(to.toString());

        long sourceLen = Files.size(from);


        try(FileChannel channel = FileChannel.open(from, StandardOpenOption.READ);
            FileChannel writeChannel = FileChannel.open(to, StandardOpenOption.CREATE_NEW, StandardOpenOption.READ, StandardOpenOption.WRITE)){

            long copied = 0;
            while(copied < sourceLen){
                int bytesToCopy = (int)Math.min(1024L, sourceLen-copied);
                MappedByteBuffer buffer = writeChannel.map(FileChannel.MapMode.READ_WRITE, copied, bytesToCopy);
                copied+=channel.read(buffer);
            };
        }
    }

}
