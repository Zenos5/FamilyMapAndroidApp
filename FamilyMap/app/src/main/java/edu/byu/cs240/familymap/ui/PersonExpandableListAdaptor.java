package edu.byu.cs240.familymap.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;
import java.util.Objects;

import edu.byu.cs240.familymap.R;
import edu.byu.cs240.familymap.model.EventListItem;
import edu.byu.cs240.familymap.model.PersonListItem;

/**
 * Adapter for person expandable list
 */
public class PersonExpandableListAdaptor extends BaseExpandableListAdapter {
    private static final String EVENT_ID = "eventID";
    private static final String PERSON_ID = "personID";

    private static final int EVENT_LIST_ITEMS_POSITION = 0;
    private static final int PERSON_LIST_ITEMS_POSITION = 1;

    private final List<EventListItem> eventListItems;
    private final List<PersonListItem> personListItems;
    private final LayoutInflater layoutInflater;

    public PersonExpandableListAdaptor(List<EventListItem> eventListItems,
                                       List<PersonListItem> personListItems,
                                       LayoutInflater layoutInflater) {
        this.eventListItems = eventListItems;
        this.personListItems = personListItems;
        this.layoutInflater = layoutInflater;
    }

    /**
     * Number of list headers
     * @return count
     */
    @Override
    public int getGroupCount() {
        return 2;
    }

    /**
     * Gets the number of event items in each header section
     *
     * @param groupPosition index of header
     * @return number of items in header's list
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        switch (groupPosition) {
            case EVENT_LIST_ITEMS_POSITION:
                return eventListItems.size();
            case PERSON_LIST_ITEMS_POSITION:
                return personListItems.size();
            default:
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
        }
    }

    /**
     * Gets the header by the position in the list
     *
     * @param groupPosition position of header
     * @return header object
     */
    @Override
    public Object getGroup(int groupPosition) {
        switch (groupPosition) {
            case EVENT_LIST_ITEMS_POSITION:
                return R.string.life_events_header;
            case PERSON_LIST_ITEMS_POSITION:
                return R.string.family_header;
            default:
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
        }
    }

    /**
     * Gets the child based on the position of the header and position
     * in the header list
     *
     * @param groupPosition header position
     * @param childPosition item position
     * @return item object
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        switch (groupPosition) {
            case EVENT_LIST_ITEMS_POSITION:
                return eventListItems.get(childPosition);
            case PERSON_LIST_ITEMS_POSITION:
                return personListItems.get(childPosition);
            default:
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
        }
    }

    /**
     * Gets the group position id
     *
     * @param groupPosition group position as int
     * @return group position as long
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Gets the child position id
     *
     * @param groupPosition group position as int
     * @param childPosition child position as int
     * @return child position as long
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Checks if the ids are stable
     *
     * @return false
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Gets the view group for the expandable list group
     *
     * @param groupPosition checks position to put each header
     * @param isExpanded if is expanded or not
     * @param convertView view to inflate each group item
     * @param parent view of parent
     * @return view
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_group, parent, false);
        }

        TextView titleView = convertView.findViewById(R.id.listTitle);

        switch (groupPosition) {
            case EVENT_LIST_ITEMS_POSITION:
                titleView.setText(R.string.life_events_header);
                break;
            case PERSON_LIST_ITEMS_POSITION:
                titleView.setText(R.string.family_header);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
        }

        return convertView;
    }

    /**
     * Gets the view for the expandable list child
     *
     * @param groupPosition checks position to put each header
     * @param childPosition checks position to put each item
     * @param isLastChild checks if it is the last item
     * @param convertView view to inflate each group item
     * @param parent view of parent
     * @return view
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View itemView;

        switch(groupPosition) {
            case EVENT_LIST_ITEMS_POSITION:
                itemView = layoutInflater.inflate(R.layout.event_recycler_item, parent, false);
                initializeEventView(itemView, childPosition);
                break;
            case PERSON_LIST_ITEMS_POSITION:
                itemView = layoutInflater.inflate(R.layout.person_list_item, parent, false);
                initializePersonView(itemView, childPosition);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
        }

        return itemView;
    }

    /**
     * Initializes the event items and where to go when clicked
     *
     * @param eventItemView view of event item
     * @param childPosition position of item
     */
    private void initializeEventView(View eventItemView, final int childPosition) {
        ImageView eventIcon = (ImageView) eventItemView.findViewById(R.id.eventIconRecycler);
        Drawable eventIconify = new IconDrawable(eventItemView.getContext(), FontAwesomeIcons.fa_map_marker).
                colorRes(R.color.black).sizeDp(40);
        eventIcon.setImageDrawable(eventIconify);

        TextView eventView = (TextView) eventItemView.findViewById(R.id.eventRecyclerItemDescriptionTextView);
        eventView.setText(eventListItems.get(childPosition).getDescription());

        TextView eventItemNameView = (TextView) eventItemView.findViewById(R.id.eventRecyclerItemNameTextView);
        eventItemNameView.setText(eventListItems.get(childPosition).getName());

        eventView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EventActivity.class);
            intent.putExtra(EVENT_ID, eventListItems.get(childPosition).getEventID());
            v.getContext().startActivity(intent);
        });
    }

    /**
     * Initializes the person items and where to go when clicked
     *
     * @param personItemView view of person item
     * @param childPosition position of item
     */
    private void initializePersonView(View personItemView, final int childPosition) {
        ImageView personIcon = (ImageView) personItemView.findViewById(R.id.genderIconList);
        PersonListItem personItem = (PersonListItem) getChild(PERSON_LIST_ITEMS_POSITION, childPosition);
        if (personItem.getGender().equals("m")) {
            Drawable genderIcon = new IconDrawable(personItemView.getContext(), FontAwesomeIcons.fa_male).
                    colorRes(R.color.teal_200).sizeDp(40);
            personIcon.setImageDrawable(genderIcon);
        } else {
            Drawable genderIcon = new IconDrawable(personItemView.getContext(), FontAwesomeIcons.fa_female).
                    colorRes(R.color.purple_200).sizeDp(40);
            personIcon.setImageDrawable(genderIcon);
        }

        TextView trailNameView = personItemView.findViewById(R.id.personListItemNameTextView);
        trailNameView.setText(personListItems.get(childPosition).getName());

        TextView trailLocationView = personItemView.findViewById(R.id.personListItemRelationTextView);
        trailLocationView.setText(personListItems.get(childPosition).getRelation());

        personItemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PersonActivity.class);
            intent.putExtra(PERSON_ID, personListItems.get(childPosition).getPersonID());
            v.getContext().startActivity(intent);
        });
    }

    /**
     * Checks if child is selectable
     *
     * @param groupPosition position of header
     * @param childPosition position of item
     * @return true if selectable
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
