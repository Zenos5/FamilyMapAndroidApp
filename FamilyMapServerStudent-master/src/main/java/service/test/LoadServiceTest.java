package service.test;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import request.RegisterRequest;
import result.LoadResult;
import result.RegisterResult;
import service.LoadService;
import service.RegisterService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LoadServiceTest {
    private Database db;
    private User user;
    private AuthToken authToken;
    private Person person;
    private Person person2;
    private Event event;
    private Event event2;
    private Event event3;
    private LoadService loadService;
    private LoadRequest loadRequest;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        user = new User("Galen", "G65723", "Galen@blank.com",
                "Galen", "Edwards", "m", "Newbie21_3");
        authToken = new AuthToken("123jfu4", "Galen");
        person = new Person("Newbie21_3", "Galen", "Galen",
                "Edwards", "m", "Jerem_246", "Lili4_5",
                "Elise_718");
        person2 = new Person("For_All345", "Cole", "Nicole",
                "Jack", "f", "657_Olden", "Ruby_294",
                "Rudy_Linette");
        event = new Event("Birth_123A", "Galen", "Newbie21_3",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Birth", 2002);
        event2 = new Event("Marriage_2853", "Galen", "Newbie21_3",
                58.9f, 123.0f, "Unknown", "Land",
                "Marriage", 2018);
        event3 = new Event("Death_5812", "Galen", "Newbie21_3",
                140.1f, 58.9f, "Libraria", "Weild",
                "Death", 2021);
        //Here, we'll open the connection in preparation for the test case to use it
        db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        loadService = new LoadService();
        loadRequest = new LoadRequest();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.getConnection();
        db.clearTables();
        db.closeConnection(false);
    }

    @Test
    public void loadPass() throws DataAccessException {
        db.getUserDao().insert(user);
        db.getAuthTokenDao().insert(authToken);
        db.getEventDao().insert(event);
        db.getPersonDao().insert(person);
        db.getPersonDao().insert(person2);
        Person[] persons = new Person[1];
        persons[0] = person;
        User[] users = new User[1];
        users[0] = user;
        Event[] events = new Event[3];
        events[0] = event;
        events[1] = event2;
        events[2] = event3;
        loadRequest.setPersons(persons);
        loadRequest.setUsers(users);
        loadRequest.setEvents(events);
        db.closeConnection(true);

        LoadResult result = loadService.load(loadRequest);
        assertTrue(result.isSuccess());

        db.openConnection();
        assertNotNull(db.getUserDao().find(user.getUsername()));
        assertNotNull(db.getPersonDao().find(person.getPersonID()));
        assertNotNull(db.getEventDao().find(event.getEventID()));
        assertNotNull(db.getEventDao().find(event2.getEventID()));
        assertNotNull(db.getEventDao().find(event3.getEventID()));
        assertNull(db.getPersonDao().find(person2.getPersonID()));
        db.closeConnection(false);
    }

    @Test
    public void loadFail() throws DataAccessException {
        assertNull(db.getUserDao().find(user.getUsername()));
        Person[] persons = new Person[1];
        Event[] events = new Event[1];
        User[] users = new User[1];
        loadRequest.setPersons(persons);
        loadRequest.setEvents(events);
        loadRequest.setUsers(users);
        db.closeConnection(true);

        LoadResult result = loadService.load(loadRequest);
        assertFalse(result.isSuccess());
    }
}
