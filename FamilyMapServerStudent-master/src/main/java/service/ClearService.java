package service;

import dao.*;
import result.ClearResult;

/**
 * Takes in a clearing request and processes it
 */
public class ClearService {
    private Database db;

    public ClearService() {}

    /**
     * Clears the database and determines an appropriate response
     *
     * @return the result of clearing the database
     */
    public ClearResult clear(){
        try {
            db = new Database();
            db.getConnection();
            db.clearTables();
            db.closeConnection(true);
            return new ClearResult("Clear succeeded.", true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
                return new ClearResult("Error: " + e, false);
            } catch ( DataAccessException d) {
                return new ClearResult("Error: " + d, false);
            }
        }
    }
}
