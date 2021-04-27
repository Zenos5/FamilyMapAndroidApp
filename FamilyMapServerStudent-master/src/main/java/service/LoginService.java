package service;

import dao.*;
import model.AuthToken;
import request.LoginRequest;
import result.LoginResult;

/**
 * Takes in a login request and processes it
 */
public class LoginService {
    private Database db;

    /**
     * Logs in the user and determines an appropriate response
     *
     * @param l request object containing data for logging in
     * @return the result of logging in
     */
    public LoginResult login(LoginRequest l) {
        try {
            db = new Database();
            db.getConnection();
            boolean loginSuccess = false;

            if (db.getUserDao().find(l.getUsername()) != null) {
                if (db.getUserDao().find(l.getUsername()).getPassword().equals(l.getPassword())) {
                    loginSuccess = true;
                }
            }

            if (loginSuccess) {
                String authToken = java.util.UUID.randomUUID().toString();
                while (db.getAuthTokenDao().find(authToken) != null) {
                    authToken = java.util.UUID.randomUUID().toString();
                }
                db.getAuthTokenDao().insert(new AuthToken(authToken, l.getUsername()));
                String personID = db.getUserDao().find(l.getUsername()).getPersonID();
                db.closeConnection(true);
                return new LoginResult(authToken, l.getUsername(),
                        personID);
            } else {
                db.closeConnection(false);
                return new LoginResult("Error: Invalid Input");
            }
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
                return new LoginResult("Error: " + e);
            } catch ( DataAccessException d) {
                return new LoginResult("Error: " + d);
            }
        }
    }
}
