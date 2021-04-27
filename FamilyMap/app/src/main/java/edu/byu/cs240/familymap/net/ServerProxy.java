package edu.byu.cs240.familymap.net;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import request.LoginRequest;
import request.RegisterRequest;
import result.ClearResult;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

/**
 * Proxy to communicate between the server and client
 */
public class ServerProxy {
    public static String serverHostName;
    public static String serverPortNumber;

    public ServerProxy(String serverHost, String serverPort) {
        serverHostName = serverHost;
        serverPortNumber = serverPort;
    }

    /**
     * Checks if the login info is valid, then logs the user in if it is
     *
     * @param r request to log in
     * @return result of logging in
     */
    public LoginResult login(LoginRequest r) {
        try {
            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            System.out.println("Established Connection.");
            //setup json
            Gson gson = new Gson();
            String reqData = gson.toJson(r);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();
            System.out.println("Setup JSON");
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Got HTTP OK response code");
                //process
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                System.out.println("Login: " + respData);
                return gson.fromJson(respData, LoginResult.class);
            } else {
                System.out.println("Response code was not HTTP OK");
                System.out.println("Error: " + http.getResponseMessage());
                return null;
            }
        } catch (IOException e) {
            System.out.println("Login Server Proxy Error: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if the registration info is valid, then registers the user if it is
     *
     * @param r request to register
     * @return result of registering
     */
    public RegisterResult register(RegisterRequest r) {
        try {
            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/user/register");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            System.out.println("Established Connection.");
            //setup json
            Gson gson = new Gson();
            String reqData = gson.toJson(r);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();
            System.out.println("Setup JSON");
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Got HTTP OK response code");
                //process
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                System.out.println("Registration: " + respData);
                return gson.fromJson(respData, RegisterResult.class);
            } else {
                System.out.println("Response code was not HTTP OK");
                System.out.println("Error: " + http.getResponseMessage());
                return null;
            }
        } catch (IOException e) {
            System.out.println("Register Server Proxy Error: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all of the people objects stored in the server and
     * pushes them to the data base
     *
     * @param authToken if valid, is logged in
     * @return result containing all people associated with a user
     */
    public PersonResult getAllPeople(String authToken) {
        try {
            System.out.println("Retrieving People");
            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            System.out.println("Response Code: " + http.getResponseCode());
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Gson gson = new Gson();
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                return gson.fromJson(respData, PersonResult.class);
                //process
            } else {
                System.out.println("Error: " + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all of the event objects stored in the server and
     * pushes them to the data base
     *
     * @param authToken if valid, is logged in
     * @return result containing all events associated with a user
     */
    public EventResult getAllEvents(String authToken) {
        try {
            System.out.println("Retrieving Events");
            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            System.out.println("Response Code: " + http.getResponseCode());
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Gson gson = new Gson();
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                return gson.fromJson(respData, EventResult.class);
                //process
            } else {
                System.out.println("Error: " + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Clears the database
     *
     * @return result of clearing
     */
    /*
    public ClearResult clear() {
        try {
            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/clear");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(false);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            System.out.println("Established Connection.");
            //setup json
            System.out.println("Setup JSON");
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Got HTTP OK response code");
                //process
                Gson gson = new Gson();
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                System.out.println("Clear: " + respData);
                return gson.fromJson(respData, ClearResult.class);
            } else {
                System.out.println("Response code was not HTTP OK");
                System.out.println("Error: " + http.getResponseMessage());
                return null;
            }
        } catch (IOException e) {
            System.out.println("Login Server Proxy Error: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }
    */

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
