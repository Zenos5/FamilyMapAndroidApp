package result;

/**
 * Creates a data object for fill results
 */
public class FillResult {
    private String message;
    private boolean success;

    /**
     * Constructs an object with the results for filling the database
     *
     * @param message explanation of what the result was
     * @param success whether filling the database succeeded or not
     */
    public FillResult(String message, boolean success) {
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
