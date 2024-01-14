package org.example.other.concur.collections;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class DownloadManagerTest {


    @Test
    public void testDownloads() throws IOException, InterruptedException, ExecutionException {
        DownloadManager manager = new DownloadManager(Paths.get("downloads"), 20);

        record Download(String url, String fileName){}

        List<Download> testUrls = new ArrayList<>();
        // Text Files

        testUrls.add(new Download("https://www.gutenberg.org/files/11/11-0.txt", "alice_in_wonderland.txt"));
        testUrls.add(new Download("https://www.gutenberg.org/files/1661/1661-0.txt", "sherlock_holmes.txt"));
        testUrls.add(new Download("https://www.gutenberg.org/files/2701/2701-0.txt", "moby_dick.txt"));

        // Image Files (JPEG)
        testUrls.add(new Download("https://source.unsplash.com/random/800x601", "random_image_1.jpg"));
        testUrls.add(new Download("https://source.unsplash.com/random/800x603", "random_image_3.jpg"));
        testUrls.add(new Download("https://source.unsplash.com/random/800x604", "random_image_4.jpg"));
        testUrls.add(new Download("https://source.unsplash.com/random/800x605", "random_image_5.jpg"));
        testUrls.add(new Download("https://source.unsplash.com/random/800x606", "random_image_6.jpg"));


        // Audio Files (MP3)
        testUrls.add(new Download("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3", "sample_audio_1.mp3"));
        testUrls.add(new Download("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3", "sample_audio_2.mp3"));

        // Video Files (MP4)
        testUrls.add(new Download("https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_5mb.mp4", "sample_video_1.mp4"));
        testUrls.add(new Download("https://sample-videos.com/video321/mp4/480/big_buck_bunny_480p_30mb.mp4", "sample_video_2.mp4"));

        List<CompletableFuture<DownloadResult>> futures = new ArrayList<>();
        for(Download download : testUrls){
            futures.add(manager.addTask(download.url(), download.fileName()));
        }

        for(CompletableFuture<DownloadResult> future : futures){
            System.out.println("Result: "+future.get().status+" | "+future.get().file);
            if(future.get().status == Status.FAILED) future.get().exception.printStackTrace();
        }
    }

}