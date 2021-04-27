package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.FillResult;
import service.FillService;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * Handler for fill web api
 */
public class FillHandler implements HttpHandler {

    /**
     * Handles http requests which contain /fill/[username]/{generations} path
     *
     * @param exchange contains http request/response pair
     * @throws IOException if there is an issue with input and output, is thrown
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        FillService fillService = new FillService();
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                String url = exchange.getRequestURI().toString();
                String username = "";
                FillResult fillResult;
                if (!url.substring(6).contains("/")) {
                    String[] tokens = url.split("/");
                    username = tokens[2];
                    fillResult = fillService.fill(username);
                } else {
                    String[] tokens = url.split("/");
                    username = tokens[2];
                    int generations = Integer.parseInt(tokens[3]);
                    fillResult = fillService.fill(username, generations);
                }
                /*
                if (fillResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                 */
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Gson gson = new Gson();
                String result = gson.toJson(fillResult);
                OutputStream respBody = exchange.getResponseBody();
                writeString(result, respBody);
                respBody.close();

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

    /**
     * Writes a string to an output stream
     *
     * @param str string to be written to output steam
     * @param os output stream
     * @throws IOException thrown if there is an issue writing to the stream
     */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
