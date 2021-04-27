package result;

import model.Event;

/**
 * Creates a data object for event results
 */
public class EventResult {
    private Event[] data;
    private boolean success;
    private String message;

    /**
     * Constructs a result from retrieving the collection of events
     * from the database
     *
     * @param data collection of all events from the database
     */
    public EventResult(Event[] data) {
        this.data = data;
        this.success = true;
    }

    /**
     * Constructs a result when returning events has an error
     *
     * @param message explanation of what the result was
     */
    public EventResult(String message) {
        this.message = message;
        this.success = false;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
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
