package dao.test;

import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class PersonDAOTest {
    private Database db;
    private Person firstPerson;
    private Person secondPerson;
    private PersonDAO pDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        //and a new event with random data
        firstPerson = new Person("Newbie21_3", "Gale", "Galen",
                "Edwards", "m", "Jerem_246", "Lili4_5",
                "Elise_718");
        secondPerson = new Person("For_All345", "Cole", "Nicole",
                "Jack", "f", "657_Olden", "Ruby_294",
                "Rudy_Linette");
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        pDao = new PersonDAO(conn);
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
        pDao.insert(firstPerson);
        //So lets use a find method to get the event that we just put in back out
        Person compareTest = pDao.find(firstPerson.getPersonID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(firstPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        pDao.insert(firstPerson);
        //but our sql table is set up so that "eventID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> pDao.insert(firstPerson));
    }


    @Test
    public void findPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        pDao.insert(firstPerson);
        //So lets use a find method to get the event that we just put in back out
        Person compareTest = pDao.find(firstPerson.getPersonID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(firstPerson, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(pDao.find(firstPerson.getPersonID()));
    }

    @Test
    public void clearPass() throws DataAccessException {
        pDao.insert(firstPerson);
        pDao.clear();
        assertNull(pDao.find(firstPerson.getPersonID()));
    }

    @Test
    public void clearPass2() throws DataAccessException {
        pDao.insert(firstPerson);
        pDao.insert(secondPerson);
        pDao.clear();
        assertNull(pDao.find(firstPerson.getPersonID()));
        assertNull(pDao.find(secondPerson.getPersonID()));
    }

    @Test
    public void deletePass() throws DataAccessException {
        pDao.insert(firstPerson);
        pDao.delete(firstPerson.getPersonID());
        assertNull(pDao.find(firstPerson.getPersonID()));
    }

    @Test
    public void deletePass2() throws DataAccessException {
        pDao.insert(firstPerson);
        pDao.insert(secondPerson);
        pDao.delete(firstPerson.getPersonID());
        assertNull(pDao.find(firstPerson.getPersonID()));
        Person compareTest = pDao.find(secondPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(secondPerson, compareTest);
    }

    @Test
    public void deleteAllPass() throws DataAccessException {
        pDao.insert(firstPerson);
        pDao.deleteAll(firstPerson.getUsername());
        assertEquals(0, pDao.findAll(firstPerson.getUsername()).length);
    }

    @Test
    public void deleteAllPass2() throws DataAccessException {
        pDao.insert(firstPerson);
        pDao.insert(secondPerson);
        pDao.deleteAll(secondPerson.getUsername());
        Person[] compareTest = pDao.findAll(firstPerson.getUsername());
        assertNotNull(compareTest);
        assertEquals(0, pDao.findAll(secondPerson.getUsername()).length);
        assertEquals(firstPerson, compareTest[0]);

    }

    @Test
    public void findAllPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        pDao.insert(firstPerson);
        pDao.insert(secondPerson);
        //So lets use a find method to get the event that we just put in back out
        Person[] compareTest = pDao.findAll(firstPerson.getUsername());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(firstPerson, compareTest[0]);
    }

    @Test
    public void findAllFail() throws DataAccessException {
        assertEquals(0, pDao.findAll(firstPerson.getUsername()).length);
    }
}
