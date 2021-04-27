package service;

import dao.DataAccessException;
import dao.Database;
import model.Person;
import result.PersonIDResult;

/**
 * Takes in a personID request and processes it
 */
public class PersonIDService {
    private Database db;

    /**
     * Returns a single person object with the personID
     *
     * @param personID personID to search for
     * @param authToken authorization token for the current user
     * @return the result of searching for a person with the personID
     */
    public PersonIDResult personID(String personID, String authToken) {
        try {
            db = new Database();
            db.getConnection();

            if (db.getPersonDao().find(personID) == null) {
                db.closeConnection(false);
                return new PersonIDResult("Error: Invalid PersonID Parameter");

            } else if (db.getAuthTokenDao().find(authToken) == null) {
                db.closeConnection(false);
                return new PersonIDResult("Error: Invalid Authorization Token");

            } else if (!db.getPersonDao().find(personID).getUsername().
                    equals(db.getAuthTokenDao().find(authToken).getUsername())) {
                db.closeConnection(false);
                return new PersonIDResult("Error: Requested person does not belong to this user");

            } else {
                Person person = db.getPersonDao().find(personID);
                db.closeConnection(true);
                return new PersonIDResult(person.getUsername(), personID, person.getFirstName(),
                        person.getLastName(), person.getGender(), person.getFatherID(),
                        person.getMotherID(), person.getSpouseID());
            }
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
                return new PersonIDResult("Error: Internal server error");
            } catch ( DataAccessException d) {
                return new PersonIDResult("Error: Internal server error");
            }
        }
    }
}
