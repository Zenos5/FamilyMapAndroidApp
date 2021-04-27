package model;

/**
 * Creates a model for a person
 */
public class Person {

    /**
     * The unique identifier for the this person
     */
    private String personID;

    /**
     * The user associated with this person
     */
    private String associatedUsername;

    /**
     * The person's first name
     */
    private String firstName;

    /**
     * The person's last name
     */
    private String lastName;

    /**
     * The person's gender
     */
    private String gender;

    /**
     * The unique identifier for the person's father
     */
    private String fatherID;

    /**
     * The unique identifier for the person's mother
     */
    private String motherID;

    /**
     * The unique identifier for the person's spouse
     */
    private String spouseID;

    /**
     * Constructs a person object
     *
     * @param personID unique identifier for this person (non-empty string)
     * @param username user associated with this event (username)
     * @param firstName first name of the person (non-empty string)
     * @param lastName last name of the person (non-empty string)
     * @param gender gender of the person (string: "f" or "m")
     * @param fatherID identifier for the father of this person (can be null)
     * @param motherID identifier for the mother of this person (can be null)
     * @param spouseID identifier for the spouse of this person (can be null)
     */
    public Person(String personID, String username, String firstName, String lastName, String gender,
                  String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public void setUsername(String username) {
        this.associatedUsername = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Person) {
            Person oPerson = (Person) o;
            return oPerson.getPersonID().equals(getPersonID()) &&
                    oPerson.getUsername().equals(getUsername()) &&
                    oPerson.getFirstName().equals(getFirstName()) &&
                    oPerson.getLastName().equals(getLastName()) &&
                    oPerson.getGender().equals(getGender()) &&
                    oPerson.getFatherID().equals(getFatherID()) &&
                    oPerson.getMotherID().equals(getMotherID()) &&
                    oPerson.getSpouseID().equals(getSpouseID());
        } else {
            return false;
        }
    }
}
