package edu.byu.cs240.familymap.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.byu.cs240.familymap.model.DataCache;
import edu.byu.cs240.familymap.model.Settings;
import edu.byu.cs240.familymap.net.ServerProxy;
import model.Event;
import model.Person;
import request.RegisterRequest;
import result.EventResult;
import result.PersonResult;
import result.RegisterResult;

/**
 * Fields requests and results for registration and puts data from server in the data cache
 */
public class RegisterAsyncTask implements Runnable {
    private final static String REGISTER_TASK_RESULT_KEY = "RegisterTaskResultKey";
    private final ServerProxy serverProxy;
    private final Handler messageHandler;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final String email;
    private final String gender;


    public RegisterAsyncTask(Handler messageHandler, String serverHost, String serverPort,
                          String userName, String password, String firstName, String lastName,
                          String email, boolean isMale) {
        serverProxy = new ServerProxy(serverHost, serverPort);
        this.messageHandler = messageHandler;
        this.username = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        if (isMale) {
            gender = "m";
        } else {
            gender = "f";
        }
    }



    /**
     * Attempts to register and then puts data into data cache
     */
    @Override
    public void run() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
        registerRequest.setFirstName(firstName);
        registerRequest.setLastName(lastName);
        registerRequest.setEmail(email);
        registerRequest.setGender(gender);
        try {
            RegisterResult registerResult = serverProxy.register(registerRequest);
            if (registerResult != null) {
                if (registerResult.isSuccess()){
                    PersonResult personResult = serverProxy.getAllPeople(registerResult.getAuthtoken());
                    EventResult eventResult = serverProxy.getAllEvents(registerResult.getAuthtoken());
                    DataCache.getInstance();
                    DataCache.startSync();
                    for (Person person: personResult.getData()) {
                        DataCache.addPerson(person);
                    }
                    DataCache.setUser(DataCache.getPersonByID(registerResult.getPersonID()));
                    for (Event event: eventResult.getData()) {
                        DataCache.addEvent(event);
                    }
                    DataCache.endSync();
                    String result = firstName + " " + lastName + " successfully registered.";
                    sendRegisterMessage(result);

                } else {
                    sendRegisterMessage("Registration failed.");
                }
            } else {
                sendRegisterMessage("Registration failed.");
            }
        } catch (Exception e) {
            System.out.println("Register Error: " + e.toString());
            sendRegisterMessage("Registration failed.");
        }
    }

    /**
     * Sends a message based on whether registering worked or not
     *
     * @param endResult result of registering attempt
     */
    private void sendRegisterMessage(String endResult) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putString(REGISTER_TASK_RESULT_KEY, endResult);
        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }
}
