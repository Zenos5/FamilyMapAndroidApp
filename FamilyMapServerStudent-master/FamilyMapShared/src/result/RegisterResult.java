package result;

/**
 * Creates a data object for registration results
 */
public class RegisterResult {
    private String authtoken;
    private String username;
    private String personID;
    private boolean success;
    private String message;

    /**
     * Constructs an object with the results for registering successfully
     *
     * @param authtoken authtoken for logging in
     * @param username username for the registered user
     * @param personID personal ID for the user
     */
    public RegisterResult(String authtoken, String username, String personID) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.success = true;
    }

    /**
     * Constructs an object with the results for failing to register
     *
     * @param message explanation of what went wrong with registering
     */
    public RegisterResult(String message) {
        this.message = message;
        this.success = false;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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
