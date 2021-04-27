package edu.byu.cs240.familymap;

import edu.byu.cs240.familymap.model.DataCache;
import edu.byu.cs240.familymap.net.ServerProxy;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class ServerProxyTest {
    private ServerProxy serverProxy;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest2;
    private LoginRequest loginRequest2;
    private String authToken;

    @BeforeEach
    public void setUp()
    {
        DataCache.getInstance();
        serverProxy = new ServerProxy("localhost", "8080");
        serverProxy.clear();
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        //and a new event with random data
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        registerRequest.setFirstName("First");
        registerRequest.setLastName("Last");
        registerRequest.setGender("m");
        registerRequest.setEmail("email@out.com");

        authToken = serverProxy.register(registerRequest).getAuthtoken();

        registerRequest2 = new RegisterRequest();
        registerRequest2.setUsername("new");
        registerRequest2.setPassword("one");
        registerRequest2.setFirstName("Alpha");
        registerRequest2.setLastName("Omega");
        registerRequest2.setGender("f");
        registerRequest2.setEmail("email@out.com");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");

        loginRequest2 = new LoginRequest();
        loginRequest2.setUsername("Hunky");
        loginRequest2.setPassword("Dorry");
    }

    @Test
    public void loginPass() {
        LoginResult loginResult = serverProxy.login(loginRequest);
        assertNotNull(loginResult);
        assertTrue(loginResult.isSuccess());
        assertNotNull(loginResult.getAuthtoken());
    }

    @Test
    public void loginFail() {
        LoginResult loginResult = serverProxy.login(loginRequest2);
        assertNull(loginResult);
    }

    @Test
    public void registerPass() {
        RegisterResult registerResult = serverProxy.register(registerRequest2);
        assertNotNull(registerResult);
        assertTrue(registerResult.isSuccess());
        assertNotNull(registerResult.getAuthtoken());
    }

    @Test
    public void registerFail() {
        RegisterResult registerResult = serverProxy.register(registerRequest);
        assertNull(registerResult);
    }

    @Test
    public void getPeoplePass() {
        PersonResult personResult = serverProxy.getAllPeople(authToken);
        assertNotNull(personResult);
        assertTrue(personResult.isSuccess());
        assertNotNull(personResult.getData());
    }

    @Test
    public void getPeopleFail() {
        PersonResult personResult = serverProxy.getAllPeople("");
        assertNull(personResult);
    }

    @Test
    public void getEventPass() {
        EventResult eventResult = serverProxy.getAllEvents(authToken);
        assertNotNull(eventResult);
        assertTrue(eventResult.isSuccess());
        assertNotNull(eventResult.getData());
    }

    @Test
    public void getEventFail() {
        EventResult eventResult = serverProxy.getAllEvents("");
        assertNull(eventResult);
    }
}
