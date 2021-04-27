package result;

import model.Person;

/**
 * Creates a data object for person results
 */
public class PersonResult {
    private Person[] data;
    private boolean success;
    private String message;

    /**
     * Constructs a result from retrieving the collection of people
     * from the database
     *
     * @param data collection of all people from the database
     */
    public PersonResult(Person[] data) {
        this.data = data;
        this.success = true;
    }

    /**
     * Constructs a result when returning people has an error
     *
     * @param message explanation of what the result was
     */
    public PersonResult(String message) {
        this.message = message;
        this.success = false;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
