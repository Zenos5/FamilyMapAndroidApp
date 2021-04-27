package service;

import dao.*;
import model.Event;
import result.EventResult;

/**
 * Takes in a event request and processes it
 */
public class EventService {
    private Database db;

    /**
     * Returns a object with all events in the database
     *
     * @return the result of searching for events in the database
     */
    public EventResult event(String authToken) {
        try {
            db = new Database();
            db.getConnection();

            if (db.getAuthTokenDao().find(authToken) == null) {
                db.closeConnection(false);
                return new EventResult("Error: Invalid Authorization Token");
            } else {
                Event[] events = db.getEventDao().findAll
                        (db.getAuthTokenDao().find(authToken).getUsername());
                db.closeConnection(true);
                return new EventResult(events);
            }
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
                return new EventResult("Error: Internal server error");
            } catch ( DataAccessException d) {
                return new EventResult("Error: Internal server error");
            }
        }
    }
}
