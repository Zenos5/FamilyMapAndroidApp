package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Creates a data access object with the database for user objects
 */
public class UserDAO {

    /**
     * The connection with the database
     */
    private final Connection conn;

    /**
     * Establishes a connection with the database
     *
     * @param conn connection with the database
     */
    public UserDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts a user into the database
     *
     * @param user user object which is being inserted
     * @throws DataAccessException if something goes wrong when inserting, throws an exception
     */
    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO user (Username, Password, Email, FirstName, LastName, " +
                "Gender, PersonID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Retrieves a user from the database
     *
     * @param username key used to find the user in the database
     * @return user which is found in the database
     * @throws DataAccessException if something goes wrong when retrieving, throws an exception
     */
    public User find(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM user WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("PersonID"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
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
     * Removes a user from the database
     *
     * @param username key used to find the user in the database
     * @throws DataAccessException if something goes wrong when deleting, throws an exception
     */
    public void delete(String username) throws DataAccessException {
        String sql = "DELETE FROM user WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    /**
     * Removes every user from the database
     *
     * @throws DataAccessException if something goes wrong when deleting, throws an exception
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM user";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the database");
        }
    }
}
