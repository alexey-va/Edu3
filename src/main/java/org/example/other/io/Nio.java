package org.example.other.io;

import com.thedeanda.lorem.LoremIpsum;
import lombok.extern.log4j.Log4j2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

@Log4j2
public class Nio {


    public static void writeFile() {
        try (RandomAccessFile file = new RandomAccessFile("test.txt", "rw")) {
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);


            for(int i=0;i<1000000;i++){
                buffer.put(LoremIpsum.getInstance().getWords(6).getBytes());
                buffer.put("\n".getBytes());
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearTestFile(){
        try (RandomAccessFile file = new RandomAccessFile("test.txt", "rw");
        FileChannel channel = file.getChannel()) {
            channel.truncate(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void endiands(){
        ByteBuffer buf = ByteBuffer.allocate(8);
        IntBuffer buffer = IntBuffer.allocate(8);
        buffer.put(12345);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putDouble(12345678.123);



        buf.flip();
        while (buf.hasRemaining()){
            System.out.println(buf.get());
        }
    }

}
