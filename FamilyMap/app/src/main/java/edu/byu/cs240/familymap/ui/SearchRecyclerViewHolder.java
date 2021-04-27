package edu.byu.cs240.familymap.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import edu.byu.cs240.familymap.R;
import edu.byu.cs240.familymap.model.EventRecyclerItem;
import edu.byu.cs240.familymap.model.PersonRecyclerItem;
import edu.byu.cs240.familymap.ui.EventActivity;
import edu.byu.cs240.familymap.ui.MainActivity;
import edu.byu.cs240.familymap.ui.PersonActivity;
import edu.byu.cs240.familymap.ui.SearchActivity;
import edu.byu.cs240.familymap.ui.SearchFragment;

/**
 * Holder for the search recycler view
 */
public class SearchRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int PERSON_RECYCLER_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_RECYCLER_ITEM_VIEW_TYPE = 1;

    private final ImageView icon;
    private final TextView name;
    private final TextView description;
    private final Context context;

    private final int viewType;
    private PersonRecyclerItem personRecyclerItem;
    private EventRecyclerItem eventRecyclerItem;

    SearchRecyclerViewHolder(View view, int viewType) {
        super(view);
        this.viewType = viewType;

        context = view.getContext();

        itemView.setOnClickListener(this);

        if (viewType == PERSON_RECYCLER_ITEM_VIEW_TYPE) {
            icon = itemView.findViewById(R.id.genderIconRecycler);
            name = itemView.findViewById(R.id.personRecyclerItemNameTextView);
            description = null;
        } else {
            icon = itemView.findViewById(R.id.eventIconRecycler);
            name = itemView.findViewById(R.id.eventRecyclerItemNameTextView);
            description = itemView.findViewById(R.id.eventRecyclerItemDescriptionTextView);
        }
    }

    /**
     * When clicked, changes to a different activity,
     * either to the person or event view
     *
     * @param v view
     */
    @Override
    public void onClick(View v) {
        if (viewType == PERSON_RECYCLER_ITEM_VIEW_TYPE) {
            Intent intent = new Intent(v.getContext(), PersonActivity.class);
            intent.putExtra("personID", personRecyclerItem.getPersonID());
            v.getContext().startActivity(intent);
        } else {
            Intent intent = new Intent(v.getContext(), EventActivity.class);
            intent.putExtra("eventID", eventRecyclerItem.getEventID());
            v.getContext().startActivity(intent);
        }
    }

    /**
     * Binds properties to the person recycler item
     *
     * @param personRecyclerItem person item
     */
    public void bind(PersonRecyclerItem personRecyclerItem) {
        this.personRecyclerItem = personRecyclerItem;
        if (personRecyclerItem.getGender().equals("m")) {
            Drawable genderIcon = new IconDrawable(context, FontAwesomeIcons.fa_male).
                    colorRes(R.color.teal_200).sizeDp(40);
            icon.setImageDrawable(genderIcon);
        } else {
            Drawable genderIcon = new IconDrawable(context, FontAwesomeIcons.fa_female).
                    colorRes(R.color.purple_200).sizeDp(40);
            icon.setImageDrawable(genderIcon);
        }
        name.setText(personRecyclerItem.getName());
    }

    /**
     * Binds properties to the event recycler item
     *
     * @param eventRecyclerItem event item
     */
    public void bind(EventRecyclerItem eventRecyclerItem) {
        this.eventRecyclerItem = eventRecyclerItem;
        Drawable genderIcon = new IconDrawable(context, FontAwesomeIcons.fa_map_marker).
                colorRes(R.color.black).sizeDp(40);
        icon.setImageDrawable(genderIcon);
        name.setText(eventRecyclerItem.getName());
        description.setText(eventRecyclerItem.getDescription());
    }
}
