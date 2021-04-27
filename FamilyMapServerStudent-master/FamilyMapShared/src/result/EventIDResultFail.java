package result;

/**
 * Creates a data object for eventID failure results
 */
public class EventIDResultFail {
    private boolean success;
    private String message;

    /**
     * Constructs a result when returning a event has an error
     *
     * @param message explanation of what the result was
     */
    public EventIDResultFail(String message) {
        this.message = message;
        this.success = false;
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
