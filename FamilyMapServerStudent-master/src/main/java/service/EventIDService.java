package service;

import dao.*;
import model.Event;
import result.EventIDResult;

/**
 * Takes in a eventID request and processes it
 */
public class EventIDService {
    private Database db;

    /**
     * Returns a single event object with the eventID
     *
     * @param eventID eventID to search for
     * @return the result of searching for a event with the eventID
     */
    public EventIDResult eventID(String eventID, String authToken) {
        try {
            db = new Database();
            db.getConnection();

            if (db.getEventDao().find(eventID) == null) {
                db.closeConnection(false);
                return new EventIDResult("Error: Invalid EventID Parameter");
            } else if (db.getAuthTokenDao().find(authToken) == null) {
                db.closeConnection(false);
                return new EventIDResult("Error: Invalid Authorization Token");
            } else if (!db.getEventDao().find(eventID).getUsername().
                    equals(db.getAuthTokenDao().find(authToken).getUsername())) {
                db.closeConnection(false);
                return new EventIDResult("Error: Requested event does not belong to this user");
            } else {
                Event event = db.getEventDao().find(eventID);
                db.closeConnection(true);
                return new EventIDResult(event.getUsername(), eventID, event.getPersonID(),
                        event.getLatitude(), event.getLongitude(), event.getCountry(),
                        event.getCity(), event.getEventType(), event.getYear());
            }
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
                return new EventIDResult("Error: Internal server error");
            } catch ( DataAccessException d) {
                return new EventIDResult("Error: Internal server error");
            }
        }
    }
}
