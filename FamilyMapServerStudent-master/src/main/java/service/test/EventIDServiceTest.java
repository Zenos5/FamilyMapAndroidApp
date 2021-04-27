package service.test;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.EventIDResult;
import result.PersonIDResult;
import service.EventIDService;
import service.PersonIDService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventIDServiceTest {
    private Database db;
    private Event event;
    private AuthToken firstAuthToken;
    private AuthToken secondAuthToken;
    private EventIDService eventIDService;

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
        eventIDService = new EventIDService();
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
    public void eventIDPass() throws DataAccessException {
        db.getEventDao().insert(event);
        db.getAuthTokenDao().insert(firstAuthToken);
        db.closeConnection(true);

        EventIDResult result = eventIDService.eventID(event.getEventID(),
                firstAuthToken.getAuthToken());
        assertEquals(event.getUsername(), result.getAssociatedUsername());
        assertEquals(event.getEventID(), result.getEventID());
        assertEquals(event.getPersonID(), result.getPersonID());
        assertEquals(event.getLatitude(), result.getLatitude());
        assertEquals(event.getLongitude(), result.getLongitude());
        assertEquals(event.getCountry(), result.getCountry());
        assertEquals(event.getCity(), result.getCity());
        assertEquals(event.getEventType(), result.getEventType());
        assertEquals(event.getYear(), result.getYear());
        assertTrue(result.isSuccess());
    }

    @Test
    public void eventIDFail() throws DataAccessException {
        db.closeConnection(true);
        EventIDResult result = eventIDService.eventID(event.getEventID(),
                secondAuthToken.getAuthToken());
        assertFalse(result.isSuccess());
    }
}
