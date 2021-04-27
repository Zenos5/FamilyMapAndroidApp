package service;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import result.LoadResult;

/**
 * Takes in a load request and processes it
 */
public class LoadService {
    private Database db;

    /**
     * Clears the database and loads in data for users, persons, and events
     *
     * @param l request object containing data for loading
     * @return the result of loading
     */
    public LoadResult load(LoadRequest l) {
        try {
            db = new Database();
            db.getConnection();
            db.clearTables();

            for (User user: l.getUsers()) {
                db.getUserDao().insert(user);
            }
            for (Person person: l.getPersons()) {
                db.getPersonDao().insert(person);
            }
            for (Event event: l.getEvents()) {
                db.getEventDao().insert(event);
            }

            /*
            for (Person person: l.getPersons()) {
                boolean birthPresent = false;
                boolean marriagePresent = false;
                boolean deathPresent = false;
                Event[] events = db.getEventDao().findPersonEvents(person.getPersonID());

                if (person.getPersonID().equals(db.getUserDao().find(person.getUsername()).getPersonID())) {
                    if (events.length < 1) {
                        db.closeConnection(false);
                        return new LoadResult("Error: Invalid request data", false);
                    } else {
                        for (Event event: events) {
                            if (event.getEventType().equalsIgnoreCase("birth")) {
                                birthPresent = true;
                                break;
                            }
                        }
                        if (!birthPresent) {
                            db.closeConnection(false);
                            return new LoadResult("Error: Invalid request data", false);
                        }
                    }

                } else {
                    if (events.length < 3) {
                        db.closeConnection(false);
                        return new LoadResult("Error: Invalid request data", false);
                    } else {
                        for (Event event: events) {
                            if (event.getEventType().equalsIgnoreCase("birth")) {
                                birthPresent = true;
                            } else if (event.getEventType().equalsIgnoreCase("marriage")) {
                                marriagePresent = true;
                            } else if (event.getEventType().equalsIgnoreCase("death")) {
                                deathPresent = true;
                            }
                            if (birthPresent && deathPresent && marriagePresent) {
                                break;
                            }
                        }
                        if (!birthPresent || !deathPresent || !marriagePresent) {
                            db.closeConnection(false);
                            return new LoadResult("Error: Invalid request data", false);
                        }
                    }
                }
            }
            */

            User[] users = l.getUsers();
            Person[] persons = l.getPersons();
            Event[] events = l.getEvents();
            db.closeConnection(true);
            return new LoadResult("Successfully added " + users.length + " users, " +
                    persons.length + " persons, and " + events.length +
                    " events to the database.", true);
        } catch (DataAccessException | NullPointerException e) {
            try {
                db.closeConnection(false);
                return new LoadResult("Error: " + e, false);
            } catch ( DataAccessException d) {
                return new LoadResult("Error: " + d, false);
            }
        }
    }
}
