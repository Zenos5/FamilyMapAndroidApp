package dao;

import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Creates a data access object with the database for person objects
 */
public class PersonDAO {

    /**
     * The connection with the database
     */
    private final Connection conn;

    /**
     * Establishes a connection with the database
     *
     * @param conn connection with the database
     */
    public PersonDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts a person into the database
     *
     * @param person person object which is being inserted
     * @throws DataAccessException if something goes wrong when inserting, throws an exception
     */
    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO person (PersonID, AssociatedUsername, FirstName, LastName, Gender, " +
                "FatherID, MotherID, SpouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Retrieves a person from the database
     *
     * @param personID key used to find the person in the database
     * @return person which is found in the database
     * @throws DataAccessException if something goes wrong when retrieving, throws an exception
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM person WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("PersonID"),
                        rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("FatherID"),
                        rs.getString("MotherID"), rs.getString("SpouseID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
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
     * Retrieves every person from the database associated with a user
     *
     * @return collection of all persons which are found in the database connected to a user
     * @throws DataAccessException if something goes wrong when retrieving, throws an exception
     */
    public Person[] findAll(String username) throws DataAccessException {
        ArrayList<Person> personList = new ArrayList<>();
        Person[] persons;
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM person WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("PersonID"),
                        rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("FatherID"),
                        rs.getString("MotherID"), rs.getString("SpouseID"));
                personList.add(person);
            }
            persons = new Person[personList.size()];
            for (int i = 0; i < persons.length; i++) {
                persons[i] = personList.get(i);
            }
            return persons;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding persons");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Removes a person from the database
     *
     * @param personID key used to find the person in the database
     * @throws DataAccessException if something goes wrong when deleting, throws an exception
     */
    public void delete(String personID) throws DataAccessException {
        String sql = "DELETE FROM person WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    /**
     * Removes every person from the database associated with a username
     *
     * @param username key used to find people associated with a user in the database
     * @throws DataAccessException if something goes wrong when deleting, throws an exception
     */
    public void deleteAll(String username) throws DataAccessException {
        String sql = "DELETE FROM person WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
    }

    /**
     * Removes every person from the database
     *
     * @throws DataAccessException if something goes wrong when deleting, throws an exception
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM person";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the database");
        }
    }
}
