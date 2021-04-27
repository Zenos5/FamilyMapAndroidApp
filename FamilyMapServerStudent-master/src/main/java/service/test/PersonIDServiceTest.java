package service.test;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import model.AuthToken;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.PersonIDResult;
import service.PersonIDService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonIDServiceTest {
    private Database db;
    private Person person;
    private AuthToken firstAuthToken;
    private AuthToken secondAuthToken;
    private PersonIDService personIDService;

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
        personIDService = new PersonIDService();
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
    public void personIDPass() throws DataAccessException {
        db.getPersonDao().insert(person);
        db.getAuthTokenDao().insert(firstAuthToken);
        db.closeConnection(true);

        PersonIDResult result = personIDService.personID(person.getPersonID(),
                firstAuthToken.getAuthToken());
        assertEquals(person.getPersonID(), result.getPersonID());
        assertEquals(person.getUsername(), result.getAssociatedUsername());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getGender(), result.getGender());
        assertEquals(person.getFatherID(), result.getFatherID());
        assertEquals(person.getMotherID(), result.getMotherID());
        assertEquals(person.getSpouseID(), result.getSpouseID());
        assertTrue(result.isSuccess());
    }

    @Test
    public void personIDFail() throws DataAccessException {
        db.closeConnection(true);
        PersonIDResult result = personIDService.personID(person.getPersonID(),
                secondAuthToken.getAuthToken());
        assertFalse(result.isSuccess());
    }
}
