package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import request.RegisterRequest;
import result.RegisterResult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Takes in a registration request and processes it
 */
public class RegisterService {
    private Database db;

    /**
     * Registers the user and determines an appropriate response
     *
     * @param r request object containing data for registration
     * @return the result of registration
     */
    public RegisterResult register(RegisterRequest r) {
        try {
            db = new Database();
            db.getConnection();
            String personID = java.util.UUID.randomUUID().toString();
            String fatherID = java.util.UUID.randomUUID().toString();
            String motherID = java.util.UUID.randomUUID().toString();

            while (db.getPersonDao().find(personID) != null) {
                personID = java.util.UUID.randomUUID().toString();
            }
            while (db.getPersonDao().find(fatherID) != null) {
                fatherID = java.util.UUID.randomUUID().toString();
            }
            while (db.getPersonDao().find(motherID) != null) {
                motherID = java.util.UUID.randomUUID().toString();
            }

            if (db.getUserDao().find(r.getUsername()) != null) {
                db.closeConnection(false);
                return new RegisterResult("Error: Invalid Input");
            }

            if (!(r.getGender().equals("m") || r.getGender().equals("f"))) {
                db.closeConnection(false);
                return new RegisterResult("Error: Invalid Input");
            }

            db.getUserDao().insert(new User(r.getUsername(), r.getPassword(), r.getEmail(),
                    r.getFirstName(), r.getLastName(), r.getGender(), personID));
            db.getPersonDao().insert(new Person(personID, r.getUsername(), r.getFirstName(),
                    r.getLastName(), r.getGender(), fatherID, motherID, null));
            insertEvent(db.getPersonDao().find(personID));
            insertGenerations(db.getPersonDao().find(personID), 0);
            String authToken = java.util.UUID.randomUUID().toString();

            while (db.getAuthTokenDao().find(authToken) != null) {
                authToken = java.util.UUID.randomUUID().toString();
            }
            db.getAuthTokenDao().insert(new AuthToken(authToken, r.getUsername()));

            db.closeConnection(true);
            return new RegisterResult(authToken, r.getUsername(), personID);
        } catch (DataAccessException | FileNotFoundException e) {
            try {
                db.closeConnection(false);
                return new RegisterResult("Error: " + e);
            } catch ( DataAccessException d) {
                return new RegisterResult("Error: " + d);
            }
        }
    }

    /**
     * Inserts four generations of people for the associated user recursively
     *
     * @param person current person who's ancestors is being inserted
     * @param generation current generation of the person who's ancestors is being inserted
     * @throws DataAccessException Thrown when there is an internal error
     * @throws FileNotFoundException Thrown when random data files cannot be found
     */
    private void insertGenerations(Person person, int generation) throws DataAccessException, FileNotFoundException {
        int currentGeneration = generation;
        String[] femaleNames = deserializeJsonNameFile("json/fnames.json");
        String[] maleNames = deserializeJsonNameFile("json/mnames.json");
        String[] surNames = deserializeJsonNameFile("json/snames.json");
        int fRandomIndex = randomNumGenerator(femaleNames.length);
        int mRandomIndex = randomNumGenerator(maleNames.length);
        int sRandomIndex = randomNumGenerator(surNames.length);

        if (generation < 4) {
            currentGeneration++;
            if (currentGeneration == 4) {
                db.getPersonDao().insert(new Person(person.getFatherID(), person.getUsername(),
                        femaleNames[fRandomIndex], surNames[sRandomIndex],
                        "m", null, null, person.getMotherID()));
                insertEvents(db.getPersonDao().find(person.getFatherID()),
                        db.getPersonDao().find(person.getPersonID()));
                sRandomIndex = randomNumGenerator(surNames.length);
                db.getPersonDao().insert(new Person(person.getMotherID(), person.getUsername(),
                        maleNames[mRandomIndex], surNames[sRandomIndex],
                        "f", null, null, person.getFatherID()));
                for (Event event: db.getEventDao().findPersonEvents(person.getFatherID())) {
                    if (event.getEventType().toLowerCase().equals("marriage")) {
                        insertEventsSpouse(db.getPersonDao().find(person.getMotherID()),
                                db.getPersonDao().find(person.getPersonID()),
                                event);
                    }
                }
            } else {
                String fatherID = java.util.UUID.randomUUID().toString();
                String motherID = java.util.UUID.randomUUID().toString();

                while (db.getPersonDao().find(fatherID) != null) {
                    fatherID = java.util.UUID.randomUUID().toString();
                }
                while (db.getPersonDao().find(motherID) != null) {
                    motherID = java.util.UUID.randomUUID().toString();
                }

                db.getPersonDao().insert(new Person(person.getFatherID(), person.getUsername(),
                        femaleNames[fRandomIndex], surNames[sRandomIndex],
                        "m", fatherID, motherID, person.getMotherID()));
                insertEvents(db.getPersonDao().find(person.getFatherID()),
                        db.getPersonDao().find(person.getPersonID()));
                sRandomIndex = randomNumGenerator(surNames.length);
                fatherID = java.util.UUID.randomUUID().toString();
                motherID = java.util.UUID.randomUUID().toString();
                while (db.getPersonDao().find(fatherID) != null) {
                    fatherID = java.util.UUID.randomUUID().toString();
                }
                while (db.getPersonDao().find(motherID) != null) {
                    motherID = java.util.UUID.randomUUID().toString();
                }
                db.getPersonDao().insert(new Person(person.getMotherID(), person.getUsername(),
                        maleNames[mRandomIndex], surNames[sRandomIndex],
                        "f", fatherID, motherID, person.getFatherID()));
                for (Event event: db.getEventDao().findPersonEvents(person.getFatherID())) {
                    if (event.getEventType().toLowerCase().equals("marriage")) {
                        insertEventsSpouse(db.getPersonDao().find(person.getMotherID()),
                                db.getPersonDao().find(person.getPersonID()),
                                event);
                    }
                }
                insertGenerations(db.getPersonDao().find(person.getFatherID()), currentGeneration);
                insertGenerations(db.getPersonDao().find(person.getMotherID()), currentGeneration);
            }
        }
    }

    /**
     * Inserts birth, marriage, and death events for the person
     *
     * @param parentPerson person the events are associated with
     * @throws FileNotFoundException thrown if the location file is not found
     * @throws DataAccessException thrown if there is an internal error
     */
    private void insertEvents(Person parentPerson, Person childPerson) throws FileNotFoundException, DataAccessException {
        Location[] locations = deserializeJsonLocationFile();
        int eventIndex1 = randomNumGenerator(locations.length);
        int eventIndex2 = randomNumGenerator(locations.length);
        int eventIndex3 = randomNumGenerator(locations.length);
        String eventID = java.util.UUID.randomUUID().toString();

        while (db.getEventDao().find(eventID) != null) {
            eventID = java.util.UUID.randomUUID().toString();
        }
        String eventID2 = java.util.UUID.randomUUID().toString();
        while (db.getEventDao().find(eventID2) != null) {
            eventID2 = java.util.UUID.randomUUID().toString();
        }
        String eventID3 = java.util.UUID.randomUUID().toString();
        while (db.getEventDao().find(eventID3) != null) {
            eventID3 = java.util.UUID.randomUUID().toString();
        }

        int[] years = new int[3];
        for (Event event: db.getEventDao().
                findPersonEvents(childPerson.getPersonID())) {
            if (event.getEventType().toLowerCase().equals("birth")) {
                years = yearGenerator(event.getYear());
            }
        }

        db.getEventDao().insert(new Event(eventID, parentPerson.getUsername(), parentPerson.getPersonID(),
                locations[eventIndex1].getLatitude(),
                locations[eventIndex1].getLongitude(),
                locations[eventIndex1].getCountry(), locations[eventIndex1].getCity(),
                "Birth", years[0]));
        db.getEventDao().insert(new Event(eventID2, parentPerson.getUsername(), parentPerson.getPersonID(),
                locations[eventIndex2].getLatitude(),
                locations[eventIndex2].getLongitude(),
                locations[eventIndex2].getCountry(), locations[eventIndex2].getCity(),
                "Marriage", years[1]));
        db.getEventDao().insert(new Event(eventID3, parentPerson.getUsername(), parentPerson.getPersonID(),
                locations[eventIndex3].getLatitude(),
                locations[eventIndex3].getLongitude(),
                locations[eventIndex3].getCountry(), locations[eventIndex3].getCity(),
                "Death", years[2]));
    }

    /**
     * Inserts birth, marriage, and death events for the spouse
     *
     * @param parentPerson person the events are associated with
     * @param childPerson child of the person the events are associated with
     * @param marriage marriage event to husband
     * @throws FileNotFoundException thrown if the location file is not found
     * @throws DataAccessException thrown if there is an internal error
     */
    private void insertEventsSpouse(Person parentPerson, Person childPerson, Event marriage)
            throws FileNotFoundException, DataAccessException {
        Location[] locations = deserializeJsonLocationFile();
        int eventIndex1 = randomNumGenerator(locations.length);
        int eventIndex3 = randomNumGenerator(locations.length);
        String eventID = java.util.UUID.randomUUID().toString();

        while (db.getEventDao().find(eventID) != null) {
            eventID = java.util.UUID.randomUUID().toString();
        }
        String eventID2 = java.util.UUID.randomUUID().toString();
        while (db.getEventDao().find(eventID2) != null) {
            eventID2 = java.util.UUID.randomUUID().toString();
        }
        String eventID3 = java.util.UUID.randomUUID().toString();
        while (db.getEventDao().find(eventID3) != null) {
            eventID3 = java.util.UUID.randomUUID().toString();
        }

        int[] years = new int[3];
        for (Event event: db.getEventDao().
                findPersonEvents(childPerson.getPersonID())) {
            if (event.getEventType().toLowerCase().equals("birth")) {
                years = yearGenerator(event.getYear(), marriage.getYear());

            }
        }

        db.getEventDao().insert(new Event(eventID, parentPerson.getUsername(), parentPerson.getPersonID(),
                locations[eventIndex1].getLatitude(),
                locations[eventIndex1].getLongitude(),
                locations[eventIndex1].getCountry(), locations[eventIndex1].getCity(),
                "Birth", years[0]));
        db.getEventDao().insert(new Event(eventID2, parentPerson.getUsername(), parentPerson.getPersonID(),
                marriage.getLatitude(), marriage.getLongitude(), marriage.getCountry(),
                marriage.getCity(), "Marriage", marriage.getYear()));
        db.getEventDao().insert(new Event(eventID3, parentPerson.getUsername(), parentPerson.getPersonID(),
                locations[eventIndex3].getLatitude(),
                locations[eventIndex3].getLongitude(),
                locations[eventIndex3].getCountry(), locations[eventIndex3].getCity(),
                "Death", years[2]));
    }

    /**
     * Inserts the birth event associated with the user person
     *
     * @param person the person the event is associated with
     * @throws FileNotFoundException thrown if the location file is not found
     * @throws DataAccessException thrown if there is an internal error
     */
    private void insertEvent(Person person) throws FileNotFoundException, DataAccessException {
        Location[] locations = deserializeJsonLocationFile();
        int eventIndex1 = randomNumGenerator(locations.length);
        String eventID = java.util.UUID.randomUUID().toString();

        while (db.getEventDao().find(eventID) != null) {
            eventID = java.util.UUID.randomUUID().toString();
        }

        db.getEventDao().insert(new Event(eventID, person.getUsername(), person.getPersonID(),
                locations[eventIndex1].getLatitude(),
                locations[eventIndex1].getLongitude(),
                locations[eventIndex1].getCountry(), locations[eventIndex1].getCity(),
                "Birth", 1902 + randomNumGenerator(120)));
    }

    /**
     * Helper function which takes a JSON file of names and forms an array list from it
     *
     * @param fileName name and location of the JSON file
     * @return array list of possible names to choose from
     * @throws FileNotFoundException thrown if the file cannot be found
     */
    private String[] deserializeJsonNameFile(String fileName) throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(fileName));
        return gson.fromJson(bufferedReader, NameData.class).getData();
    }

    /**
     * Helper function which takes a JSON file of locations and forms an array list from it
     *
     * @return array list of possible locations to choose from
     * @throws FileNotFoundException thrown if the file cannot be found
     */
    private Location[] deserializeJsonLocationFile() throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader("json/locations.json"));
        return gson.fromJson(bufferedReader, LocationData.class).getData();
    }

    /**
     * Generates a random number within the range of a list
     *
     * @param listSize size of the list
     * @return random integer within the given range
     */
    private int randomNumGenerator(int listSize) {
        return (int)(Math.random() * (listSize));
    }

    /**
     * Generates realistic years for a person's ancestral events
     *
     * @param year birth year of the direct descendant
     * @return array of birth, marriage, and death years
     */
    private int[] yearGenerator(int year) {
        int birthYear = year - randomNumGenerator(120);
        while (((year - birthYear) < 13) || (year - birthYear > 50)) {
            birthYear = year - randomNumGenerator(120);
        }
        int marriageYear = birthYear + randomNumGenerator(120);
        while (marriageYear < birthYear || (marriageYear - birthYear) < 13
                || (marriageYear - birthYear) > 50) {
            marriageYear = birthYear + randomNumGenerator(120);
        }
        int deathYear = birthYear + randomNumGenerator(120);
        while ((deathYear - birthYear) > 120 || deathYear < marriageYear) {
            deathYear = birthYear + randomNumGenerator(120);
        }
        int[] years = new int[3];
        years[0] = birthYear;
        years[1] = marriageYear;
        years[2] = deathYear;
        return years;
    }

    /**
     * Generates realistic years for a person's ancestral events
     * of spouse
     *
     * @param year birth year of the direct descendant
     * @param mYear marriage year of spouse
     * @return array of birth, marriage, and death years
     */
    private int[] yearGenerator(int year, int mYear) {
        int birthYear = year - randomNumGenerator(120);
        while (mYear < birthYear || ((year - birthYear) < 13) || ((year - birthYear) > 50)
        || (mYear - birthYear) < 13 || (mYear - birthYear) > 50) {
            birthYear = year - randomNumGenerator(120);
        }
        int deathYear = birthYear + randomNumGenerator(120);
        while ((deathYear - birthYear) > 120 || deathYear < mYear) {
            deathYear = birthYear + randomNumGenerator(120);
        }
        int[] years = new int[3];
        years[0] = birthYear;
        years[1] = mYear;
        years[2] = deathYear;
        return years;
    }
}
