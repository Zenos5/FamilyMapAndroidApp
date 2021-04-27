package edu.byu.cs240.familymap.model;

/**
 * Model for each person item in the search recycler view
 */
public class PersonRecyclerItem {
    private final String gender;
    private final String name;
    private final String personID;

    public PersonRecyclerItem(String firstName, String lastName, String gender, String personID) {
        this.name = firstName + " " + lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getPersonID() { return personID; }
}
