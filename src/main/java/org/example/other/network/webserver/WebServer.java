package org.example.other.network.webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.example.other.network.JsonParser;
import org.example.other.test.RequestManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class WebServer {

    HttpServer server;

    public WebServer(int port){
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();

    }



    static class MyHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String reqMethod = exchange.getRequestMethod();
            URI uri = exchange.getRequestURI();
            System.out.println("Got "+reqMethod+" on "+uri.toString()+" "+exchange.getHttpContext().getAttributes()+" "+Thread.currentThread().getName());

            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes());
            Map<String, Object> data = JsonParser.parse(body);
            int number = (int) data.get("number");
            System.out.println(number);
            List<Integer> list = null;
            try {
                list = RequestManager.makeRequest(number).get();
            } catch (Exception e){
                e.printStackTrace();
            }

            String response = list.toString();

            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream stream = exchange.getResponseBody();
            stream.write(response.getBytes());
            stream.close();
        }
    }
}
