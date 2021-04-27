package edu.byu.cs240.familymap.ui;

import android.os.Handler;
import android.os.Bundle;
import android.os.Message;

import edu.byu.cs240.familymap.model.DataCache;
import edu.byu.cs240.familymap.model.Settings;
import edu.byu.cs240.familymap.net.ServerProxy;
import model.AuthToken;
import model.Event;
import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

/**
 * Fields requests and results for login and puts data from server in the data cache
 */
public class LoginAsyncTask implements Runnable {
    private final static String LOGIN_TASK_RESULT_KEY = "LoginTaskResultKey";
    private final ServerProxy serverProxy;
    private final Handler messageHandler;
    private final String username;
    private final String password;


    public LoginAsyncTask(Handler messageHandler, String serverHost, String serverPort,
                          String userName, String password) {
        serverProxy = new ServerProxy(serverHost, serverPort);
        this.messageHandler = messageHandler;
        this.username = userName;
        this.password = password;
    }

    /**
     * Attempts to log in and then puts data into data cache
     */
    @Override
    public void run() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        try {
            LoginResult loginResult = serverProxy.login(loginRequest);
            System.out.println("Login: "+ loginResult.isSuccess() + " " + loginResult.getMessage()
                    + " " + loginResult.getUsername());
            if (loginResult != null) {
                if (loginResult.isSuccess()) {
                    PersonResult personResult = serverProxy.getAllPeople(loginResult.getAuthtoken());
                    EventResult eventResult = serverProxy.getAllEvents(loginResult.getAuthtoken());
                    System.out.println("Person Result: " + personResult.isSuccess());
                    System.out.println("Event Result: " + personResult.isSuccess());
                    DataCache.getInstance();
                    DataCache.startSync();
                    for (Person person: personResult.getData()) {
                        DataCache.addPerson(person);
                    }
                    DataCache.setUser(DataCache.getPersonByID(loginResult.getPersonID()));
                    for (Event event: eventResult.getData()) {
                        DataCache.addEvent(event);
                    }
                    DataCache.endSync();
                    String result = DataCache.getUser().getFirstName() + " "
                            + DataCache.getUser().getLastName() + " successfully signed in.";
                    sendLoginMessage(result);
                } else {
                    sendLoginMessage("Login failed.");
                }
            } else {
                sendLoginMessage("Login failed.");
            }
        } catch (Exception e) {
            System.out.println("Login Error: " + e.toString());
            sendLoginMessage("Login failed.");
        }
    }

    /**
     * Sends a message based on whether logging in worked or not
     *
     * @param endResult result of logging in attempt
     */
    private void sendLoginMessage(String endResult) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putString(LOGIN_TASK_RESULT_KEY, endResult);
        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }
}
