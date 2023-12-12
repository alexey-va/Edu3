package org.example;

import lombok.Builder;

@Builder
public class Name {

    String n1, n2, n3;

    public Name(String n1, String n2, String n3) {
        if(n1 == null && n2 == null && n3 == null) throw new IllegalArgumentException("All nulls!");
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
    }

    public Name(String n1, String n2) {
        this(n1, n2, null);
    }
    public Name(String n2) {
        this(null, n2, null);
    }


    @Override
    public String toString() {
        return ((n1 != null ? n1 + " " : "") +
                (n2 != null ? n2 + " " : "") +
                (n3 != null ? n3 : "")).trim();
    }
}
