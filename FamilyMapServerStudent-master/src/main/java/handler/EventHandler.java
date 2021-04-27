package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.EventIDResult;
import result.EventIDResultFail;
import result.EventResult;
import service.EventIDService;
import service.EventService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Handler for event web api
 */
public class EventHandler implements HttpHandler {

    /**
     * Handles http requests which contain /event/[eventID] path
     *
     * @param exchange contains http request/response pair
     * @throws IOException if there is an issue with input and output, is thrown
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EventService eventService = new EventService();
        EventIDService eventIDService = new EventIDService();
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")){
                    String authToken = reqHeaders.getFirst("Authorization");
                    String url = exchange.getRequestURI().toString();
                    Gson gson = new Gson();
                    String result = "";
                    if (url.equals("/event")) {
                        EventResult eventResult = eventService.event(authToken);
                        result = gson.toJson(eventResult);
                        if (eventResult.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }
                        //exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream respBody = exchange.getResponseBody();
                        writeString(result, respBody);
                        respBody.close();
                    } else {
                        String eventID = url.substring(7);
                        EventIDResult eventIDResult = eventIDService.eventID(eventID, authToken);
                        if (!eventIDResult.isSuccess()) {
                            EventIDResultFail eventIDResultFail =
                                    new EventIDResultFail(eventIDResult.getMessage());
                            result = gson.toJson(eventIDResultFail);
                        } else {
                            result = gson.toJson(eventIDResult);
                        }
                        if (eventIDResult.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }
                        //exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream respBody = exchange.getResponseBody();
                        writeString(result, respBody);
                        respBody.close();
                    }
                    success = true;
                }
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
