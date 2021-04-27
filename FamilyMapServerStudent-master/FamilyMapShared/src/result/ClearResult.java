package result;

/**
 * Creates a data object for clearing results
 */
public class ClearResult {
    private String message;
    private boolean success;

    /**
     * Constructs an object with the results for clearing the database
     *
     * @param message explanation of what the result was
     * @param success whether clearing the database succeeded or not
     */
    public ClearResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
