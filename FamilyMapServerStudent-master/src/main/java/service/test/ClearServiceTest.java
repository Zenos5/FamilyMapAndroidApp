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
import result.ClearResult;
import service.ClearService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private Database db;
    private Person person;
    private AuthToken firstAuthToken;
    private AuthToken secondAuthToken;
    private ClearService clearService;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        //and a new event with random data
        firstAuthToken = new AuthToken("123jfu4", "Galen");
        secondAuthToken = new AuthToken("0dge24j", "Elementary");
        person = new Person("Newbie21_3", "Galen", "Galen",
                "Edwards", "m", "Jerem_246", "Lili4_5",
                "Elise_718");
        //Here, we'll open the connection in preparation for the test case to use it
        db.getConnection();
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        clearService = new ClearService();
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
    public void clearPass() throws DataAccessException {
        db.getPersonDao().insert(person);
        db.getAuthTokenDao().insert(firstAuthToken);
        db.closeConnection(true);

        ClearResult result = clearService.clear();
        assertTrue(result.isSuccess());

        db.getConnection();
        assertNull(db.getPersonDao().find(person.getPersonID()));
        assertNull(db.getAuthTokenDao().find(firstAuthToken.getAuthToken()));
        db.closeConnection(false);
    }

    @Test
    public void clearPass2() throws DataAccessException {
        db.getAuthTokenDao().insert(secondAuthToken);
        db.closeConnection(true);

        ClearResult result = clearService.clear();
        assertTrue(result.isSuccess());

        db.getConnection();
        assertNull(db.getPersonDao().find(secondAuthToken.getAuthToken()));
        assertNull(db.getAuthTokenDao().find(firstAuthToken.getAuthToken()));
        db.closeConnection(false);
    }
}
