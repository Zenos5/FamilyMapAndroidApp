package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The database which is being used to store family map data
 */
public class Database {

    /**
     * The connection with the database
     */
    private Connection conn;

    /**
     * Opens a connection with the database file
     *
     * @return connection with the database file
     * @throws DataAccessException if something goes wrong when connecting, throws an exception
     */
    public Connection openConnection() throws DataAccessException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";

            conn = DriverManager.getConnection(CONNECTION_URL);

            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    /**
     * Establishes a connection if not already present
     *
     * @return connection to the database file
     * @throws DataAccessException if something goes wrong when connecting, throws an exception
     */
    public Connection getConnection() throws DataAccessException {
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    /**
     * Closes any connection with the database file
     *
     * @param commit saves all changes to the database file
     * @throws DataAccessException if something goes wrong when clearing, throws an exception
     */
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     * Removes tables from the database
     *
     * @throws DataAccessException if something goes wrong when clearing, throws an exception
     */
    public void clearTables() throws DataAccessException
    {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM event";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }

        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM person";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }

        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM user";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }

        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM auth_token";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

    /**
     * Gets user data access object for the database
     *
     * @return userDAO
     */
    public UserDAO getUserDao() {
        return new UserDAO(conn);
    }

    /**
     * Gets person data access object for the database
     *
     * @return personDAO
     */
    public PersonDAO getPersonDao() {
        return new PersonDAO(conn);
    }

    /**
     * Gets event data access object for the database
     *
     * @return eventDAO
     */
    public EventDAO getEventDao() {
        return new EventDAO(conn);
    }

    /**
     * Gets auth token data access object for the database
     *
     * @return authTokenDAO
     */
    public AuthTokenDAO getAuthTokenDao() {
        return new AuthTokenDAO(conn);
    }
}

