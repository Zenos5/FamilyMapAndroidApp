package edu.byu.cs240.familymap.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import edu.byu.cs240.familymap.net.ServerProxy;
import model.Event;
import model.Person;
import result.PersonResult;

/**
 * Cache of saved information from server for use in the app
 * Uses singleton design pattern
 */
public class DataCache {
    private boolean isLoggedIn;
    private static DataCache instance;
    private Map<String, Person> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personEvents;
    private Settings settings;
    private List<String> eventTypes;
    private Map<String, MapColor> eventTypeColors;
    private Person user;
    private Set<String> paternalAncestors;
    private Set<String> maternalAncestors;
    private Map<String, List<Person>> personChildren;

    /**
     * Initializes the data cache if not initialized
     *
     * @return instance of the data cache
     */
    public static DataCache getInstance() {
        if(instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    /**
     * Clears the data cache
     */
    public static void clear() {
        instance._clear();
    }

    /**
     * Starts syncing the data cache
     */
    public static void startSync() {
        instance._startSync();
    }

    /**
     * Finishes syncing the data cache after data is put in
     */
    public static void endSync() {
        instance._endSync();
    }

    /**
     * Puts a user in the data cache
     * @param p the person object to save as the user
     */
    public static void setUser(Person p) {
        instance._setUser(p);
    }

    /**
     * Gets the user object from the data cache
     *
     * @return person object representing the user
     */
    public static Person getUser() {
        return instance._getUser();
    }

    /**
     * Checks if there is a user already assigned
     *
     * @return true if user is assigned
     */
    public static boolean hasUser() {
        return instance._hasUser();
    }

    /**
     * Adds person to the data cache
     *
     * @param p person object
     */
    public static void addPerson(Person p) {
        instance._addPerson(p);
    }

    /**
     * Gets a collection of all people in the data cache
     *
     * @return collection of people
     */
    public static Collection<Person> getAllPeople() {
        return instance._getAllPeople();
    }

    /**
     * Gets person from the data cache
     *
     * @param id personId of the person to search for
     * @return person object
     */
    public static Person getPersonByID(String id) {
        return instance._getPersonByID(id);
    }

    /**
     * Adds an event object to the data cache
     *
     * @param e event object
     */
    public static void addEvent(Event e) {
        instance._addEvent(e);
    }

    /**
     * Gets a collection of all events from the data cache
     *
     * @return collection of events
     */
    public static Collection<Event> getAllEvents() {
        return instance._getAllEvents();
    }

    /**
     * Gets a collection of events from the data cache
     * filtered by the settings
     *
     * @return Collection of all filtered events
     */
    public static Collection<Event> getAllFilteredEvents() {
        return instance._getAllFilteredEvents();
    }

    /**
     * Gets an event from the data cache
     *
     * @param id eventId of event to search for
     * @return event object
     */
    public static Event getEventByID(String id) {
        return instance._getEventByID(id);
    }

    /**
     * Gets all event types for all of the event objects
     * in the data cache
     *
     * @return list of event types
     */
    public static List<String> getEventTypes() {
        return instance._getEventTypes();
    }

    /**
     * Gets all of the colors mapped to event types
     *
     * @return map of event types to colors
     */
    public static Map<String, MapColor> getEventTypeColors() {
        return instance._getEventTypeColors();
    }

    /**
     * Checks if the person is a paternal ancestor
     *
     * @param p person to check
     * @return true if the person is from the father's side
     * of the family
     */
    public static boolean isPaternalAncestor(Person p) {
        return instance._isPaternalAncestor(p);
    }

    /**
     * Checks if the person is a maternal ancestor
     *
     * @param p person to check
     * @return true if the person is from the mother's
     * side of the family
     */
    public static boolean isMaternalAncestor(Person p) {
        return instance._isMaternalAncestor(p);
    }

    /**
     * Gets the settings assigned in the data cache
     *
     * @return settings
     */
    public static Settings getSettings() {
        return instance._getSettings();
    }

    /**
     * Changes the settings assigned in the data cache
     *
     * @param settings settings
     */
    public static void setSettings(Settings settings) { instance._setSettings(settings); }

    /**
     * Gets all of the events associated with a person
     *
     * @param p person events are associated with
     * @return list of events
     */
    public static List<Event> getPersonEvents(Person p) {
        return instance._getPersonEvents(p);
    }

    /**
     * Gets all events associated with a person
     * which is filtered by the settings
     *
     * @param p person events are associated with
     * @return list of events
     */
    public static List<Event> getPersonFilteredEvents(Person p) {
        return instance._getPersonFilteredEvents(p);
    }

    /**
     * Gets all of the children associated with a person
     *
     * @param p parent
     * @return list of children people objects
     */
    public static List<Person> getPersonChildren(Person p) {
        return instance._getPersonChildren(p);
    }

    /**
     * Sets whether the user is logged in
     *
     * @param login true if the user is logged in
     */
    public static void setLoggedIn(boolean login) {
        instance._setLoggedIn(login);
    }

    /**
     * Checks whether the user is logged in
     *
     * @return true if the user is logged in
     */
    public static boolean isLoggedIn() {
        return instance._isLoggedIn();
    }

    private DataCache() {
        isLoggedIn = false;
        people = new HashMap<>();
        events = new HashMap<>();
        personEvents = new HashMap<>();
        settings = new Settings();
        eventTypes = new ArrayList<>();
        eventTypeColors = new HashMap<>();
        user = null;
        paternalAncestors = new HashSet<>();
        maternalAncestors = new HashSet<>();
        personChildren = new HashMap<>();
    }

    /**
     * Clears all of the data in the data cache
     */
    private void _clear() {
        isLoggedIn = false;
        people.clear();
        events.clear();
        personEvents.clear();
        eventTypes.clear();
        eventTypeColors.clear();
        user = null;
        paternalAncestors.clear();
        maternalAncestors.clear();
        personChildren.clear();
        settings = new Settings();
    }

    /**
     * Starts sync by clearing the database
     */
    private void _startSync() {
        _clear();
    }

    /**
     * Sets logged in to true and calculates
     * the relations between events and people
     * after data is inputted
     */
    private void _endSync() {
        isLoggedIn = true;
        _calcPersonEvents();
        _calcEventTypes();
        _calcEventTypeColors();
        _calcPaternalAncestors();
        _calcMaternalAncestors();
        _calcPersonChildren();
    }

    /**
     * Sets the user to the person object
     *
     * @param p user person object
     */
    private void _setUser(Person p) {
        this.user = p;
    }

    /**
     * Gets the user from the data cache
     *
     * @return user person object
     */
    private Person _getUser() {
        return user;
    }

    /**
     * Checks that there is a user in the data cache
     *
     * @return true if there is a user person object
     */
    private boolean _hasUser() {
        return (user != null);
    }

    /**
     * Puts the person object into a collection of all people
     * in the data cache
     *
     * @param p person object
     */
    private void _addPerson(Person p) {
        people.put(p.getPersonID(), p);
    }

    /**
     * Sets the login value to a chosen true/false value
     *
     * @param login true if user is logged in
     */
    private void _setLoggedIn(boolean login) {
        isLoggedIn = login;
    }

    /**
     * Checks if the user is logged in
     *
     * @return true if user is logged in
     */
    private boolean _isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Calculates the events associated with each person in the data cache
     */
    private void _calcPersonEvents() {
        personEvents.clear();
        for (Event event: events.values()) {
            String personID = event.getPersonID();
            List<Event> eventList = null;
            if (personEvents.containsKey(personID)) {
                eventList = personEvents.get(personID);
            } else {
                eventList = new ArrayList<>();
                personEvents.put(personID, eventList);
            }
            eventList.add(event);
        }
        for (Person person: getAllPeople()) {
            Collections.sort(Objects.requireNonNull(personEvents.get(person.getPersonID())), (Event o1, Event o2) -> {
                // compare two instance of `Score` and return `int` as result.
                return Integer.compare(o2.getYear(), o1.getYear());
            });
        }
    }

    /**
     * Calculates all of the event types for all event objects
     * currently in the data cache
     */
    private void _calcEventTypes() {
        Set<String> allTypes = new TreeSet<>();
        for (Event event: _getAllEvents()) {
            allTypes.add(event.getEventType().toLowerCase());
        }
        eventTypes.clear();
        eventTypes.addAll(allTypes);
    }

    /**
     * Calculates all of the colors associated with each event type
     */
    private void _calcEventTypeColors() {
        MapColor[] colors = MapColor.values();
        int colorIndex = 0;
        eventTypeColors.clear();
        for (String eventType: eventTypes) {
            eventTypeColors.put(eventType.toLowerCase(), colors[colorIndex]);
            colorIndex = ((colorIndex + 1) % colors.length);
        }
    }

    /**
     * Recursively calculates all of the paternal ancestors for the user
     */
    private void _calcPaternalAncestors() {
        paternalAncestors.clear();
        _calcPaternalAncestors(people.get(user.getFatherID()));
    }

    /**
     * Gets each person on the user's father's side
     *
     * @param p current person object
     */
    private void _calcPaternalAncestors(Person p) {
        if (p != null) {
            paternalAncestors.add(p.getPersonID());
            _calcPaternalAncestors(people.get(p.getFatherID()));
            _calcPaternalAncestors(people.get(p.getMotherID()));
        }
    }

    /**
     * Recursively calculates all of the maternal ancestors for the user
     */
    private void _calcMaternalAncestors() {
        maternalAncestors.clear();
        _calcMaternalAncestors(people.get(user.getMotherID()));
    }

    /**
     * Gets each person on the user's mother's side
     *
     * @param p current person object
     */
    private void _calcMaternalAncestors(Person p) {
        if (p != null) {
            maternalAncestors.add(p.getPersonID());
            _calcMaternalAncestors(people.get(p.getMotherID()));
            _calcMaternalAncestors(people.get(p.getFatherID()));
        }
    }

    /**
     * Recursively calculates each person's child, starting with the user
     * as the first child
     */
    private void _calcPersonChildren() {
        personChildren.clear();
        personChildren.put(user.getPersonID(), new ArrayList<>());
        _calcPersonChildren(people.get(user.getPersonID()));
    }

    /**
     * Maps each child with their parents
     *
     * @param p current child person object
     */
    private void _calcPersonChildren(Person p) {
        List<Person> children = new ArrayList<>();
        if (p.getFatherID() != null) {
            children.add(p);
            personChildren.put(p.getFatherID(), children);
            _calcPersonChildren(people.get(p.getFatherID()));
        }
        if (p.getMotherID() != null) {
            children.add(p);
            personChildren.put(p.getMotherID(), children);
            _calcPersonChildren(people.get(p.getMotherID()));
        }
    }

    /**
     * Gets a collection of all people in the data cache
     *
     * @return collection of people
     */
    private Collection<Person> _getAllPeople() {
        return new HashSet<>(people.values());
    }

    /**
     * Gets a person by their id from the data cache
     *
     * @param id personId to search for
     * @return person object
     */
    private Person _getPersonByID(String id) {
        return people.get(id);
    }

    /**
     * Puts an event object in the data cache
     *
     * @param e event object
     */
    private void _addEvent(Event e) {
        events.put(e.getEventID(), e);
    }

    /**
     * Gets all event objects in the data cache
     *
     * @return collection of events
     */
    private Collection<Event> _getAllEvents() {
        return new HashSet<>(events.values());
    }

    /**
     * Creates and returns a list of all events which are associated
     * with a person
     *
     * @param p person to find events for
     * @return list of events
     */
    private List<Event> _getPersonEvents(Person p) {
        List<Event> peopleEvents = new LinkedList<>();
        for (Event event: events.values()) {
            if (event.getPersonID().equals(p.getPersonID())) {
                peopleEvents.add(event);
            }
        }
        return peopleEvents;
    }

    /**
     * Gets an event from the data cache
     *
     * @param id eventId to search for
     * @return event object
     */
    private Event _getEventByID(String id) {
        return events.get(id);
    }

    /**
     * Gets all of the types of events in the data cache
     *
     * @return list of event types
     */
    private List<String> _getEventTypes() {
        return eventTypes;
    }

    /**
     * Gets a map of event types to colors of markers for each
     * event type
     *
     * @return map of event types and colors
     */
    private Map<String, MapColor> _getEventTypeColors() {
        return eventTypeColors;
    }

    /**
     * Checks if the person is a paternal ancestor of the user
     *
     * @param p person to search for
     * @return true if the person is a paternal ancestor
     */
    private boolean _isPaternalAncestor(Person p) {
        return paternalAncestors.contains(p.getPersonID());
    }

    /**
     * Checks if the person is a maternal ancestor of the user
     *
     * @param p person to search for
     * @return true if the person is a maternal ancestor
     */
    private boolean _isMaternalAncestor(Person p) {
        return maternalAncestors.contains(p.getPersonID());
    }

    /**
     * Gets all of the current settings info
     *
     * @return settings
     */
    private Settings _getSettings() {
        return settings;
    }

    /**
     * Gets a list of the children of a person
     *
     * @param p person to find children of
     * @return list of children person objects
     */
    private List<Person> _getPersonChildren(Person p) {
        return personChildren.get(p.getPersonID());
    }

    /**
     * Gets a collection of events which have been filtered according to the settings
     *
     * @return collection of events
     */
    private Collection<Event> _getAllFilteredEvents() {
        Set<Event> events = new HashSet<>(getPersonFilteredEvents(user));
        if (settings.isFatherSide()) {
            for (String person: paternalAncestors) {
                events.addAll(getPersonFilteredEvents(getPersonByID(person)));
            }
        }
        if (settings.isMotherSide()) {
            for (String person: maternalAncestors) {
                events.addAll(getPersonFilteredEvents(getPersonByID(person)));
            }
        }
        return new ArrayList<>(events);
    }

    /**
     * Gets a collection of events associated with a person
     * which have been filtered according to the settings
     *
     * @return collection of events
     */
    private List<Event> _getPersonFilteredEvents(Person p) {
        List<Event> eventList = new LinkedList<>();
        if (settings.isMaleEvents()) {
            if (p.getGender().equals("m")) {
                eventList.addAll(personEvents.get(p.getPersonID()));
            }
        }
        if (settings.isFemaleEvents()) {
            if (p.getGender().equals("f")) {
                eventList.addAll(personEvents.get(p.getPersonID()));
            }
        }
        return eventList;
    }

    /**
     * Change the settings
     *
     * @param settings settings
     */
    private void _setSettings(Settings settings) { this.settings = settings; }
}
