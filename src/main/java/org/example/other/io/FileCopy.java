package org.example.other.io;

import java.io.IOException;
import java.nio.file.Path;

public abstract class FileCopy {

    public abstract void copy(Path from, Path to) throws IOException;
}
