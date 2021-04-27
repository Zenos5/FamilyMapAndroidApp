package edu.byu.cs240.familymap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs240.familymap.model.DataCache;
import edu.byu.cs240.familymap.model.Settings;
import edu.byu.cs240.familymap.net.ServerProxy;
import model.Event;
import model.Person;
import request.RegisterRequest;
import result.EventResult;
import result.PersonResult;
import result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

public class DataCacheTest {
    private ServerProxy serverProxy;
    private Settings settings;

    @BeforeEach
    public void setup() {
        DataCache.getInstance();
        DataCache.startSync();
        serverProxy = new ServerProxy("localhost", "8080");
        serverProxy.clear();

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        registerRequest.setFirstName("First");
        registerRequest.setLastName("Last");
        registerRequest.setGender("m");
        registerRequest.setEmail("email@out.com");

        RegisterResult registerResult = serverProxy.register(registerRequest);
        PersonResult personResult = serverProxy.getAllPeople(registerResult.getAuthtoken());
        for (Person person: personResult.getData()) {
            DataCache.addPerson(person);
        }
        DataCache.setUser(DataCache.getPersonByID(registerResult.getPersonID()));
        EventResult eventResult = serverProxy.getAllEvents(registerResult.getAuthtoken());
        for (Event event: eventResult.getData()) {
            DataCache.addEvent(event);
        }

        settings = new Settings();
        settings.setMotherSide(false);
        settings.setMaleEvents(false);
    }

    @Test
    public void relationsTrue() {
        DataCache.endSync();
        for (Person person: DataCache.getAllPeople()) {
            if (DataCache.getPersonChildren(person).size() > 0) {
                if (person.getGender().equals("m")){
                    assertEquals(person.getPersonID(),
                            DataCache.getPersonChildren(person).get(0).getFatherID());
                } else {
                    assertEquals(person.getPersonID(),
                            DataCache.getPersonChildren(person).get(0).getMotherID());
                }
            }
        }
    }

    @Test
    public void relationsTrue2() {
        DataCache.endSync();
        Person user = DataCache.getUser();
        Person father = DataCache.getPersonByID(user.getFatherID());
        Person mother = DataCache.getPersonByID(user.getMotherID());
        assertNotNull(user.getFatherID());
        assertNotNull(user.getMotherID());
        assertEquals(father.getPersonID(),
                DataCache.getPersonByID(user.getMotherID()).getSpouseID());
        assertEquals(mother.getPersonID(),
                DataCache.getPersonByID(user.getFatherID()).getSpouseID());
        assertEquals(user.getPersonID(),
                DataCache.getPersonChildren(father).get(0).getPersonID());
        assertEquals(user.getPersonID(),
                DataCache.getPersonChildren(mother).get(0).getPersonID());
    }

    @Test
    public void filterTrue() {
        DataCache.endSync();
        assertNotNull(DataCache.getAllEvents());
        assertNotNull(DataCache.getAllFilteredEvents());
        assertEquals(DataCache.getAllEvents().size(), DataCache.getAllFilteredEvents().size());
    }

    @Test
    public void filterTrue2() {
        DataCache.setSettings(settings);
        DataCache.endSync();
        assertNotNull(DataCache.getAllEvents());
        assertNotNull(DataCache.getAllFilteredEvents());
        assertNotEquals(DataCache.getAllEvents(), DataCache.getAllFilteredEvents());
        assertFalse(DataCache.getAllFilteredEvents()
                .contains(DataCache.getPersonEvents(DataCache.getUser()).get(0)));
        assertFalse(DataCache.getAllFilteredEvents()
                .contains(DataCache.getPersonEvents(DataCache
                        .getPersonByID(DataCache.getUser().getMotherID())).get(0)));
        assertFalse(DataCache.getAllFilteredEvents()
                .contains(DataCache.getPersonEvents(DataCache
                        .getPersonByID(DataCache.getUser().getFatherID())).get(0)));
        assertTrue(DataCache.getAllFilteredEvents()
                .contains(DataCache.getPersonEvents(DataCache
                        .getPersonByID(DataCache.getPersonByID(DataCache.getUser().getFatherID()).getMotherID())).get(0)));
        assertFalse(DataCache.getAllFilteredEvents()
                .contains(DataCache.getPersonEvents(DataCache
                        .getPersonByID(DataCache.getPersonByID(DataCache.getUser().getMotherID()).getMotherID())).get(0)));
    }

    @Test
    public void eventSort() {
        DataCache.endSync();
        boolean isSorted = true;
        int year = -1000;
        for (Event event: DataCache.getPersonEvents(DataCache.getUser())) {
            if (event.getYear() >= year) {
                year = event.getYear();
            } else {
                isSorted = false;
            }
        }
        assertTrue(isSorted);
    }

    @Test
    public void eventSort2() {
        DataCache.endSync();
        boolean isSorted = true;
        int year = -50000;
        for (Event event: DataCache.getPersonEvents
                (DataCache.getPersonByID(DataCache.getUser().getMotherID()))) {
            if (event.getYear() >= year) {
                year = event.getYear();
            } else {
                isSorted = false;
            }
        }
        assertTrue(isSorted);
    }

    @Test
    public void searchForPersonTrue() {
        DataCache.endSync();
        String personID = DataCache.getUser().getPersonID();
        String personID2 = DataCache.getUser().getFatherID();
        assertNotNull(DataCache.getPersonByID(personID));
        assertNotNull(DataCache.getPersonByID(personID2));
        assertEquals(DataCache.getUser().getFirstName(),
                DataCache.getPersonByID(personID).getFirstName());
        assertEquals(personID2, DataCache.getPersonByID(personID2).getPersonID());
    }

    @Test
    public void searchForEventTrue() {
        DataCache.endSync();
        String eventID = DataCache.getPersonEvents(DataCache.getUser()).get(0).getEventID();
        String eventID2 = DataCache.getPersonEvents(DataCache.getPersonByID
                (DataCache.getUser().getFatherID())).get(0).getEventID();
        assertNotNull(DataCache.getEventByID(eventID));
        assertNotNull(DataCache.getEventByID(eventID2));
        assertEquals(DataCache.getPersonEvents(DataCache.getUser()).get(0),
                DataCache.getEventByID(eventID));
        assertEquals(eventID2, DataCache.getEventByID(eventID2).getEventID());
    }
}
