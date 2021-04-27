package model;

/**
 * Creates a model for an event
 */
public class Event {

    /**
     * The unique identifier for the event
     */
    private String eventID;

    /**
     * The user associated with this event
     */
    private String associatedUsername;

    /**
     * The person to which this event belongs
     */
    private String personID;

    /**
     * The latitude of this event
     */
    private float latitude;

    /**
     * The longitude of this event
     */
    private float longitude;

    /**
     * The country this event is located in
     */
    private String country;

    /**
     * The city this event is located in
     */
    private String city;

    /**
     * The type of event this is
     */
    private String eventType;

    /**
     * The year this event was in
     */
    private int year;

    /**
     * Constructs an event object
     *
     * @param eventID unique ID for this event (non-empty string)
     * @param username user associated with this event (username)
     * @param personID person which this event belongs to
     * @param latitude latitude of the event's location
     * @param longitude longitude of the event's location
     * @param country country the event is located in
     * @param city city the event is located in
     * @param eventType type of event (birth, death, marriage, etc)
     * @param year year the event occurred in
     */
    public Event(String eventID, String username, String personID, float latitude, float longitude,
                 String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public void setUsername(String username) {
        this.associatedUsername = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Event) {
            Event oEvent = (Event) o;
            return oEvent.getEventID().equals(getEventID()) &&
                    oEvent.getUsername().equals(getUsername()) &&
                    oEvent.getPersonID().equals(getPersonID()) &&
                    oEvent.getLatitude() == getLatitude() &&
                    oEvent.getLongitude() == getLongitude() &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getEventType().equals(getEventType()) &&
                    oEvent.getYear() == getYear();
        } else {
            return false;
        }
    }
}
