package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.*;
import model.Event;
import model.Person;
import result.FillResult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Takes in a fill request and processes it
 */
public class FillService {
    private Database db;
    private int generations;

    /**
     * Fills in the database and determines an appropriate response
     *
     * @param username username of user's database to fill with generated data
     * @return the result of filling in the database
     */
    public FillResult fill(String username) {
        return fill(username, 4);
    }

    /**
     * Fills in the database and determines an appropriate response
     *
     * @param username username username of user's database to fill with generated data
     * @param generations generations of data to fill (optional)
     * @return the result of filling in the database
     */
    public FillResult fill(String username, int generations) {
        int personNum = 1;
        int eventNum = 1;
        try {
            db = new Database();
            db.getConnection();

            if (db.getUserDao().find(username) == null) {
                db.closeConnection(false);
                return new FillResult("Error: Invalid username", false);
            } else if (generations < 0) {
                db.closeConnection(false);
                return new FillResult("Error: Invalid generations parameter", false);
            } else if (generations == 0) {
                db.getEventDao().deleteAll(username);
                db.getPersonDao().deleteAll(username);
                db.getPersonDao().insert(new Person(db.getUserDao().find(username).getPersonID(),
                        username, db.getUserDao().find(username).getFirstName(),
                        db.getUserDao().find(username).getLastName(),
                        db.getUserDao().find(username).getGender(),
                        null, null, null));
                insertEvent(db.getPersonDao().find(db.getUserDao().find(username).getPersonID()));
                db.closeConnection(true);
                return new FillResult("Successfully added 1 person and 1 event to the database.",
                        true);
            } else {

                this.generations = generations;
                db.getEventDao().deleteAll(username);
                db.getPersonDao().deleteAll(username);
                String fatherID = java.util.UUID.randomUUID().toString();
                String motherID = java.util.UUID.randomUUID().toString();

                while (db.getPersonDao().find(fatherID) != null) {
                    fatherID = java.util.UUID.randomUUID().toString();
                }
                while (db.getPersonDao().find(motherID) != null) {
                    motherID = java.util.UUID.randomUUID().toString();
                }

                db.getPersonDao().insert(new Person(db.getUserDao().find(username).getPersonID(),
                        username, db.getUserDao().find(username).getFirstName(),
                        db.getUserDao().find(username).getLastName(), db.getUserDao().find(username).getGender(),
                        fatherID, motherID, null));
                insertEvent(db.getPersonDao().find(db.getUserDao().find(username).getPersonID()));
                insertGenerations(db.getPersonDao().find(db.getUserDao().find(username).getPersonID()), 0);

                for (int i = 0; i < generations; i++) {
                    personNum += Math.pow(2, i + 1);
                    eventNum += Math.pow(2, i + 1) * 3;
                }

                db.closeConnection(true);
                return new FillResult("Successfully added " + personNum +
                        " persons and " + eventNum + " events to the database.",
                        true);
            }
        } catch (DataAccessException | FileNotFoundException e) {
            try {
                db.closeConnection(false);
                return new FillResult("Error: " + e, false);
            } catch (DataAccessException d) {
                return new FillResult("Error: " + d, false);
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

        if (generation < generations) {
            currentGeneration++;
            if (currentGeneration == generations) {
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

        int year = 1902 + randomNumGenerator(120);
        db.getEventDao().insert(new Event(eventID, person.getUsername(), person.getPersonID(),
                locations[eventIndex1].getLatitude(),
                locations[eventIndex1].getLongitude(),
                locations[eventIndex1].getCountry(), locations[eventIndex1].getCity(),
                "Birth", year));
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
        while (((year - birthYear) < 13) || ((year - birthYear) > 50)) {
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
        while (birthYear > mYear || ((year - birthYear) < 13) || ((year - birthYear) > 50)
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
