package edu.byu.cs240.familymap.model;

/**
 * Model for each event item in the person expandable list view
 */
public class EventListItem {
    private final String name;
    private final String description;
    private final String eventID;

    public EventListItem(String firstName, String lastName, String eventType,
                             String city, String country, int year, String eventID) {
        this.name = firstName + " " + lastName;
        this.description = eventType.toUpperCase() + ": " + city + ", "
                + country + " (" + year + ")";
        this.eventID = eventID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEventID() { return eventID; }
}
