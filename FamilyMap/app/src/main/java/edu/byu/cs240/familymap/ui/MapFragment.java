package edu.byu.cs240.familymap.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import edu.byu.cs240.familymap.R;
import edu.byu.cs240.familymap.model.DataCache;
import edu.byu.cs240.familymap.model.MapColor;
import edu.byu.cs240.familymap.model.Settings;
import edu.byu.cs240.familymap.model.SettingsResult;
import model.Event;
import model.Person;

/**
 * Map fragment for the main activity and event activity
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private static String ARG_EVENT_ID = "eventID";
    private static String ARG_CAMERA_POS = "camera-pos";
    private static String PERSON_ID = "personID";

    private ImageView genderImageView;
    private TextView personNameTextView;
    private TextView personEventTextView;

    private String gender;
    private String name;
    private String description;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private Event selectedEvent;
    private Map<Marker, Event> markersToEvents;
    private List<Polyline> lines;

    /**
     * Creates a new instance of map fragment
     *
     * @return map fragment
     */
    public static MapFragment newInstance() {
        System.out.println("Map Fragment: In newInstance");
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        System.out.println("Map Fragment: Out newInstance");
        return fragment;
    }

    /**
     * Creates a new instance of map fragment based on eventID
     *
     * @return map fragment
     */
    public static MapFragment newInstance(String eventID) {
        System.out.println("Map Fragment: In newInstance eventID");
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventID);
        fragment.setArguments(args);
        System.out.println("Map Fragment: Out newInstance eventID");
        return fragment;
    }

    public MapFragment() {}

    /**
     * Creates a new map fragment and initializes option menu
     *
     * @param savedInstanceState bundle holding arguments
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Map Fragment: In onCreate");
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        if (hasEventIdArgument()) {
            System.out.println("Map Fragment: Has EventID");
            setHasOptionsMenu(false);
        } else {
            System.out.println("Map Fragment: Does Not Have EventID");
            setHasOptionsMenu(true);
        }
        gender = null;
        name = "Click on a marker to see event";
        description = "details";

        if (hasEventIdArgument()) {
            selectedEvent = DataCache.getEventByID(Objects.requireNonNull(getArguments()).getString(ARG_EVENT_ID));
            String eventID = getArguments().getString(ARG_EVENT_ID);
            gender = DataCache.getPersonByID(DataCache.getEventByID(eventID).getPersonID()).getGender();
            name = DataCache.getPersonByID(DataCache.getEventByID(eventID).getPersonID()).getFirstName()
                    + " " + DataCache.getPersonByID(DataCache.getEventByID(eventID).getPersonID())
                    .getLastName();
            description = DataCache.getEventByID(eventID).getEventType().toUpperCase() + ": "
                    + DataCache.getEventByID(eventID).getCity() + ", "
                    + DataCache.getEventByID(eventID).getCountry() + " ("
                    + DataCache.getEventByID(eventID).getYear() + ")";
        } else {
            gender = null;
            name = "Click on a marker to see event";
            description = "details";
        }
        System.out.println("Map Fragment: Out onCreate");
    }

    /**
     * Creates a new view for the map fragment
     *
     * @param layoutInflater inflater for view
     * @param container view context
     * @param savedInstanceState bundle
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, final Bundle savedInstanceState) {
        System.out.println("Map Fragment: In onCreateView");
        super.onCreateView(layoutInflater, container, savedInstanceState);

        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);

        genderImageView = (ImageView) Objects.requireNonNull(view).findViewById(R.id.genderImageView);
        personNameTextView = (TextView) view.findViewById(R.id.personNameTextView);
        personEventTextView = (TextView) view.findViewById(R.id.personEventTextView);
        if (hasEventIdArgument()) {
            selectedEvent = DataCache.getEventByID(Objects.requireNonNull(getArguments()).getString(ARG_EVENT_ID));
            if (DataCache.getPersonByID(selectedEvent.getPersonID()).getGender().equals("m")) {
                Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                        colorRes(R.color.teal_200).sizeDp(40);
                genderImageView.setImageDrawable(genderIcon);
            } else {
                Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                        colorRes(R.color.purple_200).sizeDp(40);
                genderImageView.setImageDrawable(genderIcon);
            }
        } else {
            selectedEvent = null;
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).
                    colorRes(R.color.teal_700).sizeDp(40);
            genderImageView.setImageDrawable(genderIcon);
        }

        personNameTextView.setText(name);
        personEventTextView.setText(description);


        if (hasEventIdArgument()) {
            setPersonEventInfoClickListener(genderImageView);
            setPersonEventInfoClickListener(personNameTextView);
            setPersonEventInfoClickListener(personEventTextView);
        }

        markersToEvents = new HashMap<>();
        lines = new ArrayList<>();


        ((SupportMapFragment) Objects.requireNonNull(getChildFragmentManager().findFragmentById(R.id.map))).getMapAsync(
                (googleMap) -> {
                    map = googleMap;
                    map.setOnMarkerClickListener(markerClickListener);

                    populateMap(true);

                    if (savedInstanceState == null) {
                        if (selectedEvent != null) {
                            LatLng eventPosition = new LatLng(selectedEvent.getLatitude(),
                                    selectedEvent.getLongitude());
                            map.moveCamera(CameraUpdateFactory.newLatLng(eventPosition));
                        }
                    } else {
                        if (savedInstanceState.containsKey(ARG_CAMERA_POS)) {
                            CameraPosition cameraPosition = savedInstanceState.getParcelable(ARG_CAMERA_POS);
                            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    }
                }
        );

        System.out.println("Map Fragment: Out onCreateView");

        return view;
    }

    /**
     * If the person item is selected, goes to person activity
     *
     * @param view view
     */
    private void setPersonEventInfoClickListener(View view) {
        view.setOnClickListener(v1 -> {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(PERSON_ID, selectedEvent.getPersonID());
            startActivity(intent);
        });
    }

    /**
     * Creates option menu and initializes items on menu
     *
     * @param menu menu
     * @param inflater inflator
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        System.out.println("Map Fragment: In onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        Drawable searchIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_search).
                colorRes(R.color.white).sizeDp(25);
        searchItem.setIcon(searchIcon);
        searchItem.setEnabled(true);
        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        Drawable settingsIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear).
                colorRes(R.color.white).sizeDp(25);
        settingsItem.setIcon(settingsIcon);
        settingsItem.setEnabled(true);
        System.out.println("Map Fragment: Out onCreateOptionsMenu");
    }

    /**
     * Determines where to go when an option item is selected
     *
     * @param item menu item
     * @return true if item is handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("Map Fragment: In onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                Intent intent2 = new Intent(getActivity(), SettingsActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * If the settings have changed, update the map
     *
     * @param result whether the settings have changed
     */
    public void onSettingsChanged(SettingsResult result) {
        if (result.isEventChanges()) {
            selectedEvent = null;
            populateMap(true);
        } else if (result.isLineChanges()) {
            populateMap(false);
        }
    }

    /**
     * If a marker is clicked, go to new event activity and update map
     */
    private GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            selectedEvent = markersToEvents.get(marker);
            populateMap(false);
            Intent intent = new Intent(getActivity(), EventActivity.class);
            intent.putExtra(ARG_EVENT_ID, selectedEvent.getEventID());
            startActivity(intent);
            return true;
        }
    };


    private void populateMap(boolean eventsChanged) {
        map.clear();
        if (hasEventIdArgument()) {
            if (eventsChanged) {
                DataCache.endSync();
                for (Event event: DataCache.getAllFilteredEvents()) {
                    LatLng eventLocation = new LatLng(event.getLatitude(), event.getLongitude());
                    MapColor mapColor = DataCache.getEventTypeColors().
                            get(DataCache.getEventByID(event.getEventID()).getEventType().toLowerCase());
                    switch (Objects.requireNonNull(mapColor)) {
                        case BLUE:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))),
                                    event);
                            break;
                        case AZURE:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))),
                                    event);
                            break;
                        case CYAN:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))),
                                    event);
                            break;
                        case RED:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))),
                                    event);
                            break;
                        case ROSE:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))),
                                    event);
                            break;
                        case GREEN:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))),
                                    event);
                            break;
                        case ORANGE:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))),
                                    event);
                            break;
                        case VIOLET:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))),
                                    event);
                            break;
                        case YELLOW:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))),
                                    event);
                            break;
                        case MAGENTA:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))),
                                    event);
                            break;
                    }
                }
            }

            Person person = DataCache.getPersonByID(DataCache.getEventByID
                    (Objects.requireNonNull(getArguments()).getString(ARG_EVENT_ID)).getPersonID());
            Event birth = null;
            boolean married = false;
            List<LatLng> lifeEventsPositions = new ArrayList<>();
            for (Event event: DataCache.getPersonEvents(person)) {
               lifeEventsPositions.add(new LatLng(event.getLatitude(), event.getLongitude()));
               if (event.getEventType().equalsIgnoreCase("birth")) {
                   birth = event;
               }
               if (event.getEventType().equalsIgnoreCase("marriage")) {
                   married = true;
               }
            }
            if (DataCache.getSettings().isLifeStoryLines()) {
                map.addPolyline(new PolylineOptions().addAll(lifeEventsPositions)).setColor(Color.GREEN);
            }
            LatLng thisEventLocation = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
            if (married) {
                Event spouseBirth = null;
                for (Event event: DataCache.getPersonEvents
                        (DataCache.getPersonByID(person.getSpouseID()))) {
                    if (event.getEventType().equalsIgnoreCase("birth")) {
                        spouseBirth = event;
                        break;
                    } else {
                        spouseBirth = event;
                    }
                }
                if (spouseBirth != null) {
                    LatLng spouseBirthLocation = new LatLng(spouseBirth.getLatitude(),
                            spouseBirth.getLongitude());
                    if (DataCache.getSettings().isSpouseLines()) {
                        map.addPolyline(new PolylineOptions().add(thisEventLocation,
                                spouseBirthLocation)).setColor(Color.BLUE);
                    }
                }
            }
            if (DataCache.getSettings().isFamilyTreeLines()) {
                familyLine();
            }
        } else {
            if (eventsChanged) {
                for (Event event: DataCache.getAllFilteredEvents()) {
                    LatLng eventLocation = new LatLng(event.getLatitude(), event.getLongitude());
                    MapColor mapColor = DataCache.getEventTypeColors().
                            get(DataCache.getEventByID(event.getEventID()).getEventType().toLowerCase());
                    switch (Objects.requireNonNull(mapColor)) {
                        case BLUE:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))),
                                    event);
                            break;
                        case AZURE:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))),
                                    event);
                            break;
                        case CYAN:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))),
                                    event);
                            break;
                        case RED:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))),
                                    event);
                            break;
                        case ROSE:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))),
                                    event);
                            break;
                        case GREEN:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))),
                                    event);
                            break;
                        case ORANGE:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))),
                                    event);
                            break;
                        case VIOLET:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))),
                                    event);
                            break;
                        case YELLOW:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))),
                                    event);
                            break;
                        case MAGENTA:
                            markersToEvents.put(map.addMarker(new MarkerOptions().position(eventLocation).
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))),
                                    event);
                            break;
                    }
                }
            }
        }
    }

    /**
     * Recursively determines the lines to draw for the family tree of the
     * person selected
     */
    public void familyLine() {
        float polyWidth = 10.0f;
        LatLng position = new LatLng(selectedEvent.getLatitude(),
                selectedEvent.getLongitude());
        Person person = DataCache.getPersonByID(selectedEvent.getPersonID());
        if (person.getFatherID() != null) {
            Event fBirth = null;
            for (Event event: DataCache.getPersonEvents(DataCache.getPersonByID(person.getFatherID()))) {
                if (event.getEventType().equalsIgnoreCase("birth")) {
                    fBirth = event;
                    break;
                } else {
                    fBirth = event;
                }
            }
            LatLng pos2 = null;
            if (fBirth != null) {
                pos2 = new LatLng(fBirth.getLatitude(),
                        fBirth.getLongitude());
                map.addPolyline(new PolylineOptions().add(position, pos2)
                        .width(polyWidth)).setColor(Color.BLACK);
                familyLine(fBirth, polyWidth);
            }
        }
        if (person.getMotherID() != null) {
            Event mBirth = null;
            for (Event event: DataCache.getPersonEvents(DataCache.getPersonByID(person.getMotherID()))) {
                if (event.getEventType().equalsIgnoreCase("birth")) {
                    mBirth = event;
                    break;
                } else {
                    mBirth = event;
                }
            }
            LatLng pos2 = null;
            if (mBirth != null) {
                pos2 = new LatLng(mBirth.getLatitude(),
                        mBirth.getLongitude());
                map.addPolyline(new PolylineOptions().add(position, pos2)
                        .width(polyWidth)).setColor(Color.BLACK);
                familyLine(mBirth, polyWidth);
            }
        }
    }

    /**
     * Recursively determines the lines to draw for the family tree of the
     * person selected
     *
     * @param birthEvent event of the current person's birth
     */
    public void familyLine(Event birthEvent, Float width) {
        Float polyWidth = (float)((double)width * 0.75);
        LatLng position = new LatLng(birthEvent.getLatitude(),
                birthEvent.getLongitude());
        Person person = DataCache.getPersonByID(birthEvent.getPersonID());
        if (person.getFatherID() != null) {
            Event fBirth = null;
            for (Event event: DataCache.getPersonEvents(DataCache.getPersonByID(person.getFatherID()))) {
                if (event.getEventType().equalsIgnoreCase("birth")) {
                    fBirth = event;
                    break;
                } else {
                    fBirth = event;
                }
            }
            LatLng pos2 = null;
            if (fBirth != null) {
                pos2 = new LatLng(fBirth.getLatitude(),
                        fBirth.getLongitude());
                map.addPolyline(new PolylineOptions().add(position, pos2)
                        .width(polyWidth)).setColor(Color.BLACK);
                familyLine(fBirth, polyWidth);
            }
        }
        if (person.getMotherID() != null) {
            Event mBirth = null;
            for (Event event: DataCache.getPersonEvents(DataCache.getPersonByID(person.getMotherID()))) {
                if (event.getEventType().equalsIgnoreCase("birth")) {
                    mBirth = event;
                    break;
                } else {
                    mBirth = event;
                }
            }
            LatLng pos2 = null;
            if (mBirth != null) {
                pos2 = new LatLng(mBirth.getLatitude(),
                        mBirth.getLongitude());
                map.addPolyline(new PolylineOptions().add(position, pos2)
                        .width(polyWidth)).setColor(Color.BLACK);
                familyLine(mBirth, polyWidth);
            }
        }
    }

    /**
     * Sets up google map when ready
     *
     * @param googleMap map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("Map Fragment: In onMapReady");
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setOnMapLoadedCallback(this);
        System.out.println("Map Fragment: Out onMapReady");

        if (hasEventIdArgument()) {
            LatLng location = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLng(location));
        }
    }

    /**
     * Set up for when the map is loaded
     */
    @Override
    public void onMapLoaded() {
        System.out.println("Map Fragment: In onMapLoaded");
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }

    /**
     * Checks if there is an eventID given
     *
     * @return true if there was an eventID argument
     */
    private boolean hasEventIdArgument() {
        return (getArguments() != null && getArguments().containsKey(ARG_EVENT_ID));
    }
}