package org.example.living;

import java.util.Collections;
import java.util.Random;

public class Cuckoo extends Bird {
    @Override
    public String getMessage() {
        return String.join(" ",
                Collections.nCopies(new Random().nextInt(1, 11), "ку-ку")
        );
    }
}
