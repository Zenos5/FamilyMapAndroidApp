package dao;

/**
 * Creates an exception when there is an issue with accessing data from the database
 */
public class DataAccessException extends Exception {

    /**
     * Constructs the data access exception
     *
     * @param message message which clarifies what went wrong
     */
    DataAccessException(String message)
    {
        super(message);
    }

    /**
     * Constructs the data access exception
     */
    DataAccessException()
    {
        super();
    }
}
