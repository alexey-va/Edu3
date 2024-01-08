package org.example.other.network.websocketchat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class ChatSocket {

    @Getter
    Socket socket;
    PrintWriter output;
    BufferedReader input;

    @SneakyThrows
    public ChatSocket(Socket socket) {
        this.socket = socket;
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String message) {
        output.println(message);
    }

    @SneakyThrows
    public String receiveMessage(){
        return input.readLine();
    }

    @SneakyThrows
    public void close() {
        socket.close();
    }
}
