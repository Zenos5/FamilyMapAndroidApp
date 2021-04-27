package edu.byu.cs240.familymap.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs240.familymap.R;
import edu.byu.cs240.familymap.model.DataCache;
import edu.byu.cs240.familymap.model.EventRecyclerItem;
import edu.byu.cs240.familymap.model.PersonRecyclerItem;
import model.Event;
import model.Person;
import edu.byu.cs240.familymap.ui.SearchRecyclerAdaptor;

/**
 * Search fragment for the search activity
 */
public class SearchFragment extends Fragment {

    private static final String ARG_PARAM_SEARCH_STRING = "searchValue";

    private String searchString;
    private SearchView searchView;
    private RecyclerView recyclerView;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Creates new instance of search fragment
     *
     * @return search fragment
     */
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates a new search fragment
     *
     * @param savedInstanceState bundle holding arguments
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates widgets and listeners for search fragment
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
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        //recycler view stuff!

        searchView = (SearchView) v.findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //initiate search
                recyclerView = (RecyclerView) v.findViewById(R.id.searchView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

                searchString = query.toLowerCase();

                //Search all events and people which is being searched
                List<PersonRecyclerItem> personList = new ArrayList<>();
                for (Person person: DataCache.getAllPeople()) {
                    if (person.getFirstName().toLowerCase().contains
                            (searchString)) {
                        personList.add(new PersonRecyclerItem(person.getFirstName(), person.getLastName(), person.getGender(),
                                person.getPersonID()));
                    } else if (person.getLastName().toLowerCase().contains
                            (searchString)) {
                        personList.add(new PersonRecyclerItem(person.getFirstName(), person.getLastName(), person.getGender(),
                                person.getPersonID()));
                    }
                }
                List<EventRecyclerItem> eventList = new ArrayList<>();
                for (Event event: DataCache.getAllFilteredEvents()) {
                    if (event.getCountry().toLowerCase().contains
                            (searchString)) {
                        eventList.add(new EventRecyclerItem(DataCache.getPersonByID(event.getPersonID()).getFirstName(),
                                DataCache.getPersonByID(event.getPersonID()).getLastName(),
                                event.getEventType(), event.getCity(), event.getCountry(), event.getYear(), event.getEventID()));
                    } else if (event.getCity().toLowerCase().contains
                            (searchString)) {
                        eventList.add(new EventRecyclerItem(DataCache.getPersonByID(event.getPersonID()).getFirstName(),
                                DataCache.getPersonByID(event.getPersonID()).getLastName(),
                                event.getEventType(), event.getCity(), event.getCountry(), event.getYear(), event.getEventID()));
                    } else if (event.getEventType().toLowerCase().contains
                            (searchString)) {
                        eventList.add(new EventRecyclerItem(DataCache.getPersonByID(event.getPersonID()).getFirstName(),
                                DataCache.getPersonByID(event.getPersonID()).getLastName(),
                                event.getEventType(), event.getCity(), event.getCountry(), event.getYear(), event.getEventID()));
                    } else if ((event.getYear() + " ").contains
                            (searchString)) {
                        eventList.add(new EventRecyclerItem(DataCache.getPersonByID(event.getPersonID()).getFirstName(),
                                DataCache.getPersonByID(event.getPersonID()).getLastName(),
                                event.getEventType(), event.getCity(), event.getCountry(), event.getYear(), event.getEventID()));
                    }
                }
                SearchRecyclerAdaptor adapter = new SearchRecyclerAdaptor(personList, eventList, inflater);
                recyclerView.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });

        return v;
    }
}