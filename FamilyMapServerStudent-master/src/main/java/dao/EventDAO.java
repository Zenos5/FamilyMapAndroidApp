package dao;

import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Creates a data access object with the database for event objects
 */
public class EventDAO {

    /**
     * The connection with the database
     */
    private final Connection conn;

    /**
     * Establishes a connection with the database
     *
     * @param conn connection with the database
     */
    public EventDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts a event into the database
     *
     * @param event event object which is being inserted
     * @throws DataAccessException if something goes wrong when inserting, throws an exception
     */
    public void insert(Event event) throws DataAccessException {
        String sql = "INSERT INTO event (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Retrieves a event from the database
     *
     * @param eventID key used to find the event in the database
     * @return event which is found in the database
     * @throws DataAccessException if something goes wrong when retrieving, throws an exception
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"),
                        rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"),
                        rs.getFloat("Longitude"), rs.getString("Country"),
                        rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Retrieves every event from the database associated with a user
     *
     * @return collection of all events which are found in the database connected to a user
     * @throws DataAccessException if something goes wrong when retrieving, throws an exception
     */
    public Event[] findAll(String username) throws DataAccessException {
        ArrayList<Event> eventList = new ArrayList<>();
        Event[] events;
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(rs.getString("EventID"),
                        rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"),
                        rs.getFloat("Longitude"), rs.getString("Country"),
                        rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                eventList.add(event);
            }
            events = new Event[eventList.size()];
            for (int i = 0; i < events.length; i++) {
                events[i] = eventList.get(i);
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding events");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Retrieves every event from the database associated with a user
     *
     * @return collection of all events which are found in the database connected to a user
     * @throws DataAccessException if something goes wrong when retrieving, throws an exception
     */
    public Event[] findPersonEvents(String personID) throws DataAccessException {
        ArrayList<Event> eventList = new ArrayList<>();
        Event[] events;
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(rs.getString("EventID"),
                        rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"),
                        rs.getFloat("Longitude"), rs.getString("Country"),
                        rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                eventList.add(event);
            }
            events = new Event[eventList.size()];
            for (int i = 0; i < events.length; i++) {
                events[i] = eventList.get(i);
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding events");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Removes a event from the database
     *
     * @param eventID key used to find the event in the database
     * @throws DataAccessException if something goes wrong when deleting, throws an exception
     */
    public void delete(String eventID) throws DataAccessException {
        String sql = "DELETE FROM event WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    /**
     * Removes every event from the database associated with a username
     *
     * @param username key used to find events associated with a user in the database
     * @throws DataAccessException if something goes wrong when deleting, throws an exception
     */
    public void deleteAll(String username) throws DataAccessException {
        String sql = "DELETE FROM event WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    /**
     * Removes every event from the database
     *
     * @throws DataAccessException if something goes wrong when deleting, throws an exception
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM event";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the database");
        }
    }
}