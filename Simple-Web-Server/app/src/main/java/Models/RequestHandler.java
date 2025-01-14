/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Axioo Pongo
 */
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javax.swing.JTextArea;

class RequestHandler implements HttpHandler {
    private String webDirectory;
    private String logDirectory;
    private JTextArea textArea;
    private LogHandler logHandle;

    public RequestHandler(String webDirectory, String logDirectory, JTextArea textArea) {
        this.webDirectory = webDirectory;
        this.logDirectory = logDirectory;
        this.textArea = textArea;
        this.logHandle = new LogHandler(logDirectory);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();

        logHandle.log(textArea, requestPath, exchange.getRemoteAddress().getAddress().getHostAddress());

        if (requestMethod.equalsIgnoreCase("GET")) {
            handleGetRequest(exchange, requestPath);
        } else {
            sendErrorResponse(exchange, HttpURLConnection.HTTP_BAD_METHOD, "Method Not Allowed");
        }
    }

    private void handleGetRequest(HttpExchange exchange, String requestPath) throws IOException {
        Path filePath = Paths.get(webDirectory, requestPath.substring(1));
        File file = filePath.toFile();

        if (file.exists() && file.isFile()) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, file.length());
            OutputStream outputStream = exchange.getResponseBody();
            Files.copy(file.toPath(), outputStream);
            outputStream.close();
        } else if (file.exists() && file.isDirectory()) {
            File indexFile = new File(file, "index.html");
            if (indexFile.exists() && indexFile.isFile()) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, indexFile.length());
                OutputStream outputStream = exchange.getResponseBody();
                Files.copy(indexFile.toPath(), outputStream);
                outputStream.close();
            } else {
                File[] files = file.listFiles();
                StringBuilder response = new StringBuilder();
                response.append("<html><body><h1>Index of ").append(requestPath).append("</h1><ul>");
                if (!requestPath.equals("/")) {
                    response.append("<li><a href=\"../\">.. (Back)</a></li>");
                }
                for (File f : files) {
                    String linkPath = f.isDirectory() ? requestPath + f.getName() + "/" : requestPath + f.getName();
                    response.append("<li><a href=\"").append(linkPath).append("\">").append(f.getName()).append("</a></li>");
                }
                response.append("</ul></body></html>");
                sendResponse(exchange, HttpURLConnection.HTTP_OK, response.toString());
            }
        } else {
            sendErrorResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Not Found");
        }
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        sendResponse(exchange, statusCode, message);
    }
}