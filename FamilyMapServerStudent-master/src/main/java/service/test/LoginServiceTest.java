package service.test;

import dao.*;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LoginServiceTest {
    private Database db;
    private User user;
    private LoginService loginService;
    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        //and a new event with random data
        user = new User("Gale", "G65723", "Galen@blank.com",
                "Galen", "Edwards", "m", "Newbie21_3");
        //Here, we'll open the connection in preparation for the test case to use it
        db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        loginService = new LoginService();
        loginRequest = new LoginRequest();
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
    public void loginPass() throws DataAccessException {
        db.getUserDao().insert(user);
        assertNotNull(db.getUserDao().find(user.getUsername()));
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(user.getPassword());
        db.closeConnection(true);

        LoginResult result = loginService.login(loginRequest);
        assertTrue(result.isSuccess());
        assertNotNull(result.getAuthtoken());
        assertEquals(user.getPersonID(), result.getPersonID());
    }

    @Test
    public void loginFail() throws DataAccessException {
        assertNull(db.getUserDao().find(user.getUsername()));
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(user.getPassword());
        db.closeConnection(true);

        LoginResult result = loginService.login(loginRequest);
        assertFalse(result.isSuccess());

        db.openConnection();
        db.getUserDao().insert(user);
        loginRequest.setPassword("nonPassword");
        db.closeConnection(true);

        result = loginService.login(loginRequest);
        assertFalse(result.isSuccess());
    }
}
