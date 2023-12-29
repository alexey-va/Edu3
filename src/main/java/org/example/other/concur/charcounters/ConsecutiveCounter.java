package org.example.other.concur.charcounters;

public class ConsecutiveCounter implements AbstractCharCounter{
    @Override
    public long countChars(String str, char c, int param1, int param2) {
        return str.chars().filter(ch -> ch == c).count();
    }
}
