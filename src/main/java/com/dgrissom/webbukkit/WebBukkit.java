package com.dgrissom.webbukkit;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.util.List;

public class WebBukkit {
    @Getter
    private static final WebBukkit instance = new WebBukkit();

    @Getter @Setter
    private WebsiteRequestHandler requestHandler;
    private HttpServer server;

    private WebBukkit() {
        this.requestHandler = new BasicWebsiteRequestHandler();
    }

    public void start(int port) throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RequestHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public void stop() {
        server.stop(10);
    }

    private void handle(HttpExchange exchange, File output) throws IOException {
        List<String> lines = FileUtils.readFile(output);
        StringBuilder response = new StringBuilder();

        for (String line : lines)
            response.append(line);

        exchange.sendResponseHeaders(200, response.toString().getBytes().length);
        OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
        writer.write(response.toString());
        writer.close();
    }

    private class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // http://example.org/<REQUEST HERE>
            String method = exchange.getRequestMethod();
            String request = exchange.getRequestURI().toASCIIString().substring(1); // ASCII string replaces "%20" with " " for example. subtring removes first "/"
            String response = requestHandler.handle(exchange, request, method);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
            writer.write(response);
            writer.close();
        }
    }
}
