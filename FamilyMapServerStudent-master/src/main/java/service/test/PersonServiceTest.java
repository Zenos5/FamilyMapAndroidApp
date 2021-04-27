package service.test;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.EventResult;
import result.PersonResult;
import service.EventService;
import service.PersonService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {
    private Database db;
    private Person person;
    private AuthToken firstAuthToken;
    private AuthToken secondAuthToken;
    private PersonService personService;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        firstAuthToken = new AuthToken("123jfu4", "Galen");
        secondAuthToken = new AuthToken("0dge24j", "Elementary");
        person = new Person("Newbie21_3", "Galen", "Galen",
                "Edwards", "m", "Jerem_246", "Lili4_5",
                "Elise_718");
        //Here, we'll open the connection in preparation for the test case to use it
        db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        personService = new PersonService();
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
    public void personPass() throws DataAccessException {
        db.getPersonDao().insert(person);
        db.getAuthTokenDao().insert(firstAuthToken);
        db.closeConnection(true);

        PersonResult result = personService.person(firstAuthToken.getAuthToken());
        assertEquals(person, result.getData()[0]);
        assertTrue(result.isSuccess());
    }

    @Test
    public void personFail() throws DataAccessException {
        db.closeConnection(true);
        PersonResult result = personService.person(secondAuthToken.getAuthToken());
        assertFalse(result.isSuccess());

        db.openConnection();
        db.getAuthTokenDao().insert(firstAuthToken);
        db.closeConnection(true);

        result = personService.person(firstAuthToken.getAuthToken());
        assertEquals(0, result.getData().length);
        assertTrue(result.isSuccess());
    }
}
