package model;

/**
 * Creates a model for an authorization token
 */
public class AuthToken {

    /**
     * The identifying string for this authorization token
     */
    private String authToken;

    /**
     * The user associated with this authorization token
     */
    private String username;


    /**
     * Constructs an authorization token object
     *
     * @param authToken unique authorization token identifier (non-empty string)
     * @param username user associated with this authorization token (non-empty string)
     */
    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof AuthToken) {
            AuthToken oAuthToken = (AuthToken) o;
            return oAuthToken.getAuthToken().equals(getAuthToken()) &&
                    oAuthToken.getUsername().equals(getUsername());
        } else {
            return false;
        }
    }
}
