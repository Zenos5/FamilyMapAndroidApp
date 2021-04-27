package edu.byu.cs240.familymap.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.byu.cs240.familymap.R;
import edu.byu.cs240.familymap.model.EventRecyclerItem;
import edu.byu.cs240.familymap.model.PersonRecyclerItem;

/**
 * Adaptor for search recycler
 */
public class SearchRecyclerAdaptor extends RecyclerView.Adapter<SearchRecyclerViewHolder> {
    private static final int PERSON_RECYCLER_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_RECYCLER_ITEM_VIEW_TYPE = 1;
    private final List<PersonRecyclerItem> personRecyclerItems;
    private final List<EventRecyclerItem> eventRecyclerItems;
    private final LayoutInflater layoutInflater;

    public SearchRecyclerAdaptor(List<PersonRecyclerItem> personRecyclerItems,
                                 List<EventRecyclerItem> eventRecyclerItems,
                                 LayoutInflater layoutInflater) {
        this.personRecyclerItems = personRecyclerItems;
        this.eventRecyclerItems = eventRecyclerItems;
        this.layoutInflater = layoutInflater;
    }

    /**
     * Gets item view type
     *
     * @param position where the view is in the list
     * @return view type in the form of an int
     */
    @Override
    public int getItemViewType(int position) {
        return position < personRecyclerItems.size() ? PERSON_RECYCLER_ITEM_VIEW_TYPE : EVENT_RECYCLER_ITEM_VIEW_TYPE;
    }

    /**
     * When a view holder for the recycler is created, inflates the recycler list
     *
     * @param parent context
     * @param viewType type of view
     * @return search recycler view holder
     */
    @NonNull
    @Override
    public SearchRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == PERSON_RECYCLER_ITEM_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.person_recycler_item, parent, false);
        } else {
            view = layoutInflater.inflate(R.layout.event_recycler_item, parent, false);
        }

        return new SearchRecyclerViewHolder(view, viewType);
    }

    /**
     * When the holder is made, binds objects to it
     *
     * @param holder holder
     * @param position where to put the object
     */
    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerViewHolder holder, int position) {
        if (position < personRecyclerItems.size()) {
            holder.bind(personRecyclerItems.get(position));
        } else {
            holder.bind(eventRecyclerItems.get(position - personRecyclerItems.size()));
        }
    }

    /**
     * Number of items in the recycler
     *
     * @return item count
     */
    @Override
    public int getItemCount() {
        return eventRecyclerItems.size() + personRecyclerItems.size();
    }
}
