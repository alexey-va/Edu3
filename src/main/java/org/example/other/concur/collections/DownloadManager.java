package org.example.other.concur.collections;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class DownloadManager {

    BlockingQueue<DownloadTask> queue = new LinkedBlockingQueue<>();
    ExecutorService service;
    Path folder;
    Thread thread;
    //Semaphore semaphore;

    public DownloadManager(Path downloadFolder, int threads) throws IOException {
        this.folder = downloadFolder;
        if (!Files.exists(folder)) Files.createDirectories(folder);
        if (Files.exists(folder) && !Files.isDirectory(folder)) {
            throw new IllegalArgumentException("Path is pointing to an existing file, not folder!");
        }
        this.service = Executors.newFixedThreadPool(threads);

        //semaphore = new Semaphore(0);
        thread = new Thread(this::pollTask);
        thread.start();
    }

    private void pollTask(){
        while (!Thread.interrupted()){
            try {
               // semaphore.acquire();
                DownloadTask task = queue.take();
                if(task == null){
                    System.out.println("No task found, waiting...");
                    continue;
                }
                service.submit(() -> {
                    System.out.println("Downloading: "+task.saveTo+" from "+Thread.currentThread().getName());
                    try {
                        task.download();
                        task.getFuture().complete(new DownloadResult(Status.COMPLETED, task.saveTo));
                    } catch (IOException e) {
                        task.setException(e);
                        DownloadResult result = new DownloadResult(Status.FAILED, task.saveTo);
                        result.exception = e;
                        task.getFuture().complete(result);
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public CompletableFuture<DownloadResult> addTask(String url, String fileName) {
        CompletableFuture<DownloadResult> future = new CompletableFuture<>();
        DownloadTask task = DownloadTask.builder()
                .url(url)
                .saveTo(folder.resolve(fileName))
                .future(future)
                .build();
        queue.add(task);
        //semaphore.release();

        return future;
    }

}


