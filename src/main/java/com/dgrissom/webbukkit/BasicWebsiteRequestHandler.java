package com.dgrissom.webbukkit;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BasicWebsiteRequestHandler implements WebsiteRequestHandler {
    @Override
    public String handle(HttpExchange exchange, String request, String method) throws IOException {
        File page = FileUtils.getWebsitePage(request);
        List<String> lines = FileUtils.readFile(page);
        StringBuilder response = new StringBuilder();

        for (String line : lines)
            response.append(line);

        return response.toString();
    }
}
