package service.test;

import dao.*;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {
    private Database db;
    private User user;
    private RegisterService registerService;
    private RegisterRequest registerRequest;

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
        registerService = new RegisterService();
        registerRequest = new RegisterRequest();
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
    public void registerPass() throws DataAccessException {
        assertNull(db.getUserDao().find(user.getUsername()));
        registerRequest.setUsername(user.getUsername());
        registerRequest.setPassword(user.getPassword());
        registerRequest.setEmail(user.getEmail());
        registerRequest.setFirstName(user.getFirstName());
        registerRequest.setLastName(user.getLastName());
        registerRequest.setGender(user.getGender());
        db.closeConnection(true);

        RegisterResult result = registerService.register(registerRequest);
        assertTrue(result.isSuccess());
        assertNotNull(result.getAuthtoken());

        db.openConnection();
        assertEquals(31, db.getPersonDao().findAll(user.getUsername()).length);
        assertEquals(91, db.getEventDao().findAll(user.getUsername()).length);
        db.closeConnection(false);
    }

    @Test
    public void registerFail() throws DataAccessException {
        db.getUserDao().insert(user);
        assertNotNull(db.getUserDao().find(user.getUsername()));
        registerRequest.setUsername(user.getUsername());
        registerRequest.setPassword(user.getPassword());
        registerRequest.setEmail(user.getEmail());
        registerRequest.setFirstName(user.getFirstName());
        registerRequest.setLastName(user.getLastName());
        registerRequest.setGender(user.getGender());
        db.closeConnection(true);

        RegisterResult result = registerService.register(registerRequest);
        assertFalse(result.isSuccess());
    }
}
