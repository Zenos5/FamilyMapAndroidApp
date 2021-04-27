package result;

/**
 * Creates a data object for loading results
 */
public class LoadResult {
    private String message;
    private boolean success;

    /**
     * Constructs an object with the results for clearing the database
     * and loading user, event, and person data to the database
     *
     * @param message explanation of what the result was
     * @param success whether loading into the database succeeded or not
     */
    public LoadResult(String message, boolean success) {
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
