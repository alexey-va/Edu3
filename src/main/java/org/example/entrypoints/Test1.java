package org.example.entrypoints;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
class Test1 {
    String a = null;
    int b = 123;

    void print() {
        System.out.println("123");
    }

    String getString() {
        return "123";
    }
}
