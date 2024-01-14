package org.example.other.concur.collections;

import lombok.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DownloadTask {
    String url;
    Path saveTo;
    private double progress = 0;
    private double lastProgress = 0;
    @Setter
    private Exception exception;
    @Setter @Getter
    CompletableFuture<DownloadResult> future;


    public void download() throws IOException {
        URLConnection connection = URI.create(url).toURL().openConnection();

        if(Files.exists(saveTo)){
            Files.delete(saveTo);
        }

        try(InputStream stream = connection.getInputStream();
            OutputStream outputStream = new FileOutputStream(saveTo.toFile())){
            long length = connection.getContentLengthLong();
            long counter = 0;
            //if(length > 100_000) System.out.println("Downloading: "+url+" | Progress: "+0+"%");

            byte[] buffer = new byte[4096];
            int read = stream.read(buffer);
            while(read != -1){
                outputStream.write(buffer, 0, read);
                counter += read;
                progress = (double)counter/length;
                read = stream.read(buffer);

                if(progress - lastProgress >= 0.25 && length > 100_000){
                    //System.out.println("Downloading: "+url+" | Progress: "+Math.round(progress*100)+"%");
                    lastProgress = progress;
                }
            }

            //if(length > 100_000) System.out.println("Downloading: "+url+" | Progress: "+100+"%");
        }

    }

}


