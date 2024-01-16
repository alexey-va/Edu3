package org.example.other.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public class BufferedCopy extends FileCopy {


    @Override
    public void copy(Path from, Path to) throws IOException {
        if(!Files.exists(from)) throw new NoSuchFileException("No file: "+ from);
        if(Files.exists(to)) throw new FileAlreadyExistsException(to.toString());

        long sourceLen = Files.size(from);


        try(FileChannel channel = FileChannel.open(from, StandardOpenOption.READ);
            FileChannel writeChannel = FileChannel.open(to, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)){
            ByteBuffer buffer = ByteBuffer.allocate(32);
            long bytesRead = 0;

            int read;
            while ((read = channel.read(buffer)) != -1){
                buffer.flip();
                writeChannel.write(buffer);
                bytesRead+=read;
                //System.out.println(bytesRead+" "+read+" "+buffer.limit());

                buffer.clear();
                //System.out.println("Progress: "+(bytesRead*100/sourceLen)+"%");
            }
        }
    }
}
