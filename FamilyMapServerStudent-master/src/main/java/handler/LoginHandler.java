package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * Handler for login web api
 */
public class LoginHandler implements HttpHandler {

    /**
     * Handles http requests which contain /user/login path
     *
     * @param exchange contains http request/response pair
     * @throws IOException if there is an issue with input and output, is thrown
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LoginService loginService = new LoginService();
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                System.out.println(reqData);
                Gson gson = new Gson();
                LoginRequest loginRequest = gson.fromJson(reqData, LoginRequest.class);
                System.out.println("Entered Login Service");
                LoginResult loginResult = loginService.login(loginRequest);
                System.out.println("Left Login Service");
                if (loginResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                String result = gson.toJson(loginResult);
                System.out.println(result);
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
     * Reads a string from an input stream
     *
     * @param is input stream from request
     * @return String from request
     * @throws IOException thrown if there is an issue reading file
     */
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
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
