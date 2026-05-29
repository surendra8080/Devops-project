package com.devops;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;

public class OnlineLearningApplication {

    public static void main(String[] args) throws Exception {

        int port = 9090;

        HttpServer server = HttpServer.create(
                new InetSocketAddress(port),
                0
        );

        server.createContext("/", new StaticFileHandler());

        server.setExecutor(null);

        System.out.println("Server started at http://localhost:" + port);

        server.start();
    }

    static class StaticFileHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange)
                throws IOException {

            String path = exchange.getRequestURI().getPath();

            if (path.equals("/")) {
                path = "/index.html";
            }

            InputStream inputStream =
                    getClass().getResourceAsStream(
                            "/static" + path
                    );

            if (inputStream == null) {

                String response = "404 File Not Found";

                exchange.sendResponseHeaders(
                        404,
                        response.length()
                );

                OutputStream os =
                        exchange.getResponseBody();

                os.write(response.getBytes());

                os.close();

                return;
            }

            byte[] response = readAllBytes(inputStream);

            exchange.getResponseHeaders().set(
                    "Content-Type",
                    getContentType(path)
            );

            exchange.sendResponseHeaders(
                    200,
                    response.length
            );

            OutputStream os =
                    exchange.getResponseBody();

            os.write(response);

            os.close();
        }

        private String getContentType(String path) {

            if (path.endsWith(".html")) {
                return "text/html";
            }

            if (path.endsWith(".css")) {
                return "text/css";
            }

            if (path.endsWith(".js")) {
                return "application/javascript";
            }

            return "text/plain";
        }

        private byte[] readAllBytes(InputStream inputStream) throws IOException {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }
}