package dao.test;

import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDAOTest {
    private Database db;
    private Event firstEvent;
    private Event secondEvent;
    private EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        //and a new event with random data
        firstEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        secondEvent = new Event("Birth_2853", "Cole", "Cole5946",
                58.9f, 123.0f, "Unknown", "Land",
                "Birth", 2002);
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        eDao = new EventDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        eDao.insert(firstEvent);
        //So lets use a find method to get the event that we just put in back out
        Event compareTest = eDao.find(firstEvent.getEventID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(firstEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        eDao.insert(firstEvent);
        //but our sql table is set up so that "eventID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> eDao.insert(firstEvent));
    }

    @Test
    public void findPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        eDao.insert(firstEvent);
        //So lets use a find method to get the event that we just put in back out
        Event compareTest = eDao.find(firstEvent.getEventID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(firstEvent, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(eDao.find(firstEvent.getEventID()));
    }

    @Test
    public void clearPass() throws DataAccessException {
        eDao.insert(firstEvent);
        eDao.clear();
        assertNull(eDao.find(firstEvent.getEventID()));
    }

    @Test
    public void clearPass2() throws DataAccessException {
        eDao.insert(firstEvent);
        eDao.insert(secondEvent);
        eDao.clear();
        assertNull(eDao.find(firstEvent.getEventID()));
        assertNull(eDao.find(secondEvent.getEventID()));
    }

    @Test
    public void deletePass() throws DataAccessException {
        eDao.insert(firstEvent);
        eDao.delete(firstEvent.getEventID());
        assertNull(eDao.find(firstEvent.getEventID()));
    }

    @Test
    public void deletePass2() throws DataAccessException {
        eDao.insert(firstEvent);
        eDao.insert(secondEvent);
        eDao.delete(firstEvent.getEventID());
        assertNull(eDao.find(firstEvent.getEventID()));
        Event compareTest = eDao.find(secondEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(secondEvent, compareTest);
    }

    @Test
    public void deleteAllPass() throws DataAccessException {
        eDao.insert(firstEvent);
        eDao.deleteAll(firstEvent.getUsername());
        assertEquals(0, eDao.findAll(firstEvent.getUsername()).length);
    }

    @Test
    public void deleteAllPass2() throws DataAccessException {
        eDao.insert(firstEvent);
        eDao.insert(secondEvent);
        eDao.deleteAll(secondEvent.getUsername());
        Event[] compareTest = eDao.findAll(firstEvent.getUsername());
        assertNotNull(compareTest);
        assertEquals(0, eDao.findAll(secondEvent.getUsername()).length);
        assertEquals(firstEvent, compareTest[0]);

    }

    @Test
    public void findAllPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        eDao.insert(firstEvent);
        eDao.insert(secondEvent);
        //So lets use a find method to get the event that we just put in back out
        Event[] compareTest = eDao.findAll(firstEvent.getUsername());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(firstEvent, compareTest[0]);
    }

    @Test
    public void findAllFail() throws DataAccessException {
        assertEquals(0, eDao.findAll(firstEvent.getUsername()).length);
    }

    @Test
    public void findPersonEventsFail() throws DataAccessException {
        assertEquals(0, eDao.findPersonEvents(firstEvent.getPersonID()).length);
    }

    @Test
    public void findPersonEventsPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        eDao.insert(firstEvent);
        eDao.insert(secondEvent);
        //So lets use a find method to get the event that we just put in back out
        Event[] compareTest = eDao.findPersonEvents(firstEvent.getPersonID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(firstEvent, compareTest[0]);
    }
}
