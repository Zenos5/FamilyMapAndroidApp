package dao;

import model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Creates a data access object with the database for authToken objects
 */
public class AuthTokenDAO {

    /**
     * The connection with the database
     */
    private final Connection conn;

    /**
     * Establishes a connection with the database
     *
     * @param conn connection with the database
     */
    public AuthTokenDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts a authToken into the database
     *
     * @param authToken authToken object which is being inserted
     * @throws DataAccessException if something goes wrong when inserting, throws an exception
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        String sql = "INSERT INTO auth_token (AuthToken, Username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getAuthToken());
            stmt.setString(2, authToken.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Retrieves a authToken from the database
     *
     * @param aToken key used to find the authToken in the database
     * @return authToken which is found in the database
     * @throws DataAccessException if something goes wrong when retrieving, throws an exception
     */
    public AuthToken find(String aToken) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM auth_token WHERE AuthToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("AuthToken"),
                        rs.getString("Username"));
                return authToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding authToken");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Removes a authToken from the database
     *
     * @param aToken key used to find the authToken in the database
     * @throws DataAccessException if something goes wrong when deleting, throws an exception
     */
    public void delete(String aToken) throws DataAccessException {
        String sql = "DELETE FROM auth_token WHERE AuthToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aToken);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    /**
     * Removes every authToken from the database
     *
     * @throws DataAccessException if something goes wrong when deleting, throws an exception
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM auth_token";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the database");
        }
    }
}
