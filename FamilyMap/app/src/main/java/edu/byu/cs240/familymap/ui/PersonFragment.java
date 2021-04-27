package edu.byu.cs240.familymap.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs240.familymap.R;
import edu.byu.cs240.familymap.model.DataCache;
import edu.byu.cs240.familymap.model.EventListItem;
import edu.byu.cs240.familymap.model.PersonListItem;
import model.Event;

/**
 * Person fragment for the person activity
 */
public class PersonFragment extends Fragment {
    private static final String ARG_PARAM_PERSON_ID = "personID";

    private String personID;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView genderTextView;
    private ExpandableListView expandableListView;

    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Creates new instance of person fragment
     *
     * @param personID person identifier
     * @return person fragment
     */
    public static PersonFragment newInstance(String personID) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_PERSON_ID, personID);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates a new person fragment
     *
     * @param savedInstanceState bundle holding arguments
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            personID = getArguments().getString(ARG_PARAM_PERSON_ID);
        }
    }

    /**
     * Creates widgets and listeners for person fragment
     *
     * @param inflater inflater for views
     * @param container view context for views
     * @param savedInstanceState bundle holding arguments
     * @return overall view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_person, container, false);

        firstNameTextView = v.findViewById(R.id.firstNamePersonText);
        firstNameTextView.setText(DataCache.getPersonByID(personID).getFirstName());

        lastNameTextView = v.findViewById(R.id.lastNamePersonText);
        lastNameTextView.setText(DataCache.getPersonByID(personID).getLastName());

        genderTextView = v.findViewById(R.id.genderPersonText);
        if (DataCache.getPersonByID(personID).getGender().equals("m")) {
            genderTextView.setText(R.string.male);
        } else {
            genderTextView.setText(R.string.female);
        }

        expandableListView = v.findViewById(R.id.expandableListView);
        List<EventListItem> eventListItems = new ArrayList<>();
        List<PersonListItem> personListItems = new ArrayList<>();
        for (Event event: DataCache.getPersonEvents(DataCache.getPersonByID(personID))) {
            if (DataCache.getAllFilteredEvents().contains(event)){
                eventListItems.add(new EventListItem(DataCache.getPersonByID(personID).getFirstName(),
                        DataCache.getPersonByID(personID).getLastName(), event.getEventType(),
                        event.getCity(), event.getCountry(), event.getYear(), event.getEventID()));
            }
        }
        if (DataCache.getPersonByID(personID).getFatherID() != null) {
            String fatherID = DataCache.getPersonByID(personID).getFatherID();
            personListItems.add(new PersonListItem(DataCache.getPersonByID(fatherID).getFirstName(),
                    DataCache.getPersonByID(fatherID).getLastName(), DataCache.getPersonByID(fatherID).getGender(),
                    "Father", fatherID));
        }
        if (DataCache.getPersonByID(personID).getMotherID() != null) {
            String motherID = DataCache.getPersonByID(personID).getMotherID();
            personListItems.add(new PersonListItem(DataCache.getPersonByID(motherID).getFirstName(),
                    DataCache.getPersonByID(motherID).getLastName(), DataCache.getPersonByID(motherID).getGender(),
                    "Mother", motherID));
        }
        if (DataCache.getPersonByID(personID).getSpouseID() != null) {
            String spouseID = DataCache.getPersonByID(personID).getSpouseID();
            personListItems.add(new PersonListItem(DataCache.getPersonByID(spouseID).getFirstName(),
                    DataCache.getPersonByID(spouseID).getLastName(), DataCache.getPersonByID(spouseID).getGender(),
                    "Spouse", spouseID));
        }
        if (DataCache.getPersonChildren(DataCache.getPersonByID(personID)).size() > 0) {
            String childID = DataCache.getPersonChildren(DataCache.getPersonByID(personID)).get(0).getPersonID();
            personListItems.add(new PersonListItem(DataCache.getPersonByID(childID).getFirstName(),
                    DataCache.getPersonByID(childID).getLastName(), DataCache.getPersonByID(childID).getGender(),
                    "Child", childID));
        }
        expandableListView.setAdapter(new PersonExpandableListAdaptor(eventListItems, personListItems, inflater));
        return v;
    }
}