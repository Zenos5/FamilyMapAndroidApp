package service;

import dao.DataAccessException;
import dao.Database;
import model.Person;
import result.PersonResult;

/**
 * Takes in a person request and processes it
 */
public class PersonService {
    private Database db;

    /**
     * Returns a object with all people in the database
     *
     * @return the result of searching for everyone in the database
     */
    public PersonResult person(String authToken) {
        try {
            db = new Database();
            db.getConnection();

            if (db.getAuthTokenDao().find(authToken) == null) {
                db.closeConnection(false);
                return new PersonResult("Error: Invalid Authorization Token");

            } else {
                Person[] people = db.getPersonDao().findAll
                        (db.getAuthTokenDao().find(authToken).getUsername());
                db.closeConnection(true);
                return new PersonResult(people);
            }
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
                return new PersonResult("Error: Internal server error");
            } catch ( DataAccessException d) {
                return new PersonResult("Error: Internal server error");
            }
        }
    }
}
