package edu.byu.cs240.familymap.model;

/**
 * Model for each person item in the person extendable list view
 */
public class PersonListItem {
    private final String gender;
    private final String name;
    private final String relation;
    private final String personID;

    public PersonListItem(String firstName, String lastName, String gender, String relation, String personID) {
        this.name = firstName + " " + lastName;
        this.gender = gender;
        this.relation = relation;
        this.personID = personID;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getRelation() {
        return relation;
    }

    public String getPersonID() {
        return personID;
    }
}
