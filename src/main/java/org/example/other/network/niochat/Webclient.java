package org.example.other.network.niochat;

public abstract class Webclient {

    String username;

    public abstract boolean connect(String address, int port);
    public abstract boolean sendMessageTo(String message, String user);

}
