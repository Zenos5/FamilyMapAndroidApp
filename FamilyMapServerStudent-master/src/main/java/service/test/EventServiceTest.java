package service.test;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import model.AuthToken;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.EventIDResult;
import result.EventResult;
import service.EventIDService;
import service.EventService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {
    private Database db;
    private Event event;
    private AuthToken firstAuthToken;
    private AuthToken secondAuthToken;
    private EventService eventService;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        firstAuthToken = new AuthToken("123jfu4", "Galen");
        secondAuthToken = new AuthToken("0dge24j", "Elementary");
        event = new Event("Biking_123A", "Galen", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        //Here, we'll open the connection in preparation for the test case to use it
        db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        eventService = new EventService();
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
    public void eventPass() throws DataAccessException {
        db.getEventDao().insert(event);
        db.getAuthTokenDao().insert(firstAuthToken);
        db.closeConnection(true);

        EventResult result = eventService.event(firstAuthToken.getAuthToken());
        assertEquals(event, result.getData()[0]);
        assertTrue(result.isSuccess());
    }

    @Test
    public void eventFail() throws DataAccessException {
        db.closeConnection(true);
        EventResult result = eventService.event(secondAuthToken.getAuthToken());
        assertFalse(result.isSuccess());

        db.openConnection();
        db.getAuthTokenDao().insert(firstAuthToken);
        db.closeConnection(true);

        result = eventService.event(firstAuthToken.getAuthToken());
        assertEquals(0, result.getData().length);
        assertTrue(result.isSuccess());
    }
}
