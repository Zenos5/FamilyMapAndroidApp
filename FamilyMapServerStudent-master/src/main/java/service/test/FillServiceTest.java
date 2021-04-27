package service.test;

import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.FillResult;
import result.PersonResult;
import service.FillService;
import service.PersonService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    private Database db;
    private User user;
    private int generations;
    private FillService fillService;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        user = new User("Gale", "G65723", "Galen@blank.com",
                "Galen", "Edwards", "m", "Newbie21_3");
        //Here, we'll open the connection in preparation for the test case to use it
        db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        generations = 3;
        fillService = new FillService();
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
    public void fillPass() throws DataAccessException {
        db.getUserDao().insert(user);
        db.closeConnection(true);

        FillResult result = fillService.fill(user.getUsername());
        assertTrue(result.isSuccess());

        db.openConnection();
        assertEquals(31, db.getPersonDao().findAll(user.getUsername()).length);
        assertEquals(91, db.getEventDao().findAll(user.getUsername()).length);
        db.closeConnection(false);
    }

    @Test
    public void fillFail() throws DataAccessException {
        db.closeConnection(true);
        FillResult result = fillService.fill(user.getUsername());
        assertFalse(result.isSuccess());
    }

    @Test
    public void fill2Pass() throws DataAccessException {
        db.getUserDao().insert(user);
        db.closeConnection(true);

        FillResult result = fillService.fill(user.getUsername(), generations);
        assertTrue(result.isSuccess());

        db.openConnection();
        assertEquals(15, db.getPersonDao().findAll(user.getUsername()).length);
        assertEquals(43,db.getEventDao().findAll(user.getUsername()).length);
        db.closeConnection(false);
    }

    @Test
    public void fill2Fail() throws DataAccessException {
        db.closeConnection(true);
        FillResult result = fillService.fill(user.getUsername());
        assertFalse(result.isSuccess());
    }
}
