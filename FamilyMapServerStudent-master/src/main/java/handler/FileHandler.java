package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

/**
 * Handler for directing web files
 */
public class FileHandler implements HttpHandler {

    /**
     * Handles http requests which contain /person/[personID] path
     *
     * @param exchange contains http request/response pair
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String url = exchange.getRequestURI().toString();
                if (url == null || url.equals("") || url.equals("/")) {
                    url = "/index.html";
                    System.out.println("Changed Path");
                }
                String filePath = "web" + url;
                System.out.println(filePath);
                if (Files.exists(Path.of(filePath), LinkOption.NOFOLLOW_LINKS)) {
                    File file = new File(filePath);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                    respBody.flush();
                    respBody.close();
                } else {
                    System.out.println("404 error");
                    filePath = "web/HTML/404.html";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    File file = new File(filePath);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                    respBody.flush();
                    respBody.close();
                }
                System.out.println("Sent file");
                success = true;
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
