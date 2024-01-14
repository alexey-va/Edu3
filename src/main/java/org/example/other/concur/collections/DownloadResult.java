package org.example.other.concur.collections;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@RequiredArgsConstructor
public class DownloadResult{
    final Status status;
    final Path file;
    Exception exception;
}

enum Status{
    QUEUED, DOWNLOADING, PAUSED, COMPLETED, FAILED
}
