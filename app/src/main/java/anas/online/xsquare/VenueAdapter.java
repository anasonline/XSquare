package anas.online.xsquare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import anas.online.xsquare.model.Venue;

/**
 * Created by anas on 12.08.17.
 */

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.VenueAdapterViewHolder> {
    private final Context mContext;
    private List<Venue> mVenues;
    private int mRowLayout;
    private VenueAdapterOnClickHandler mClickHander;


    public VenueAdapter(List<Venue> venues, int rowLayout, Context context) {

        mVenues = venues;
        mContext = context;
        mRowLayout = rowLayout;
        //mClickHander = clickHandler;
    }

    @Override
    public VenueAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_venue, parent, false);
        view.setFocusable(true);
        return new VenueAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VenueAdapterViewHolder holder, int position) {
        Venue venue = mVenues.get(position);

        String name = venue.getName();
        String address = venue.getAddress();
        String distance = venue.getDistance();

        holder.name.setText(name);
        holder.address.setText(address);
        holder.distance.setText(distance);


    }

    @Override
    public int getItemCount() {
        if (mVenues == null) {
            return -1;
        }
        return mVenues.size();
    }

    public void swapList(List<Venue> venues) {
        mVenues = venues;
        notifyDataSetChanged();
    }

    interface VenueAdapterOnClickHandler {
        void onClick(Venue venue);
    }

    public class VenueAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // final ImageView photo;
        final TextView name;
        final TextView address;
        final TextView distance;


        public VenueAdapterViewHolder(View view) {
            super(view);

            // photo = (ImageView) view.findViewById(R.id.venue_image);
            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.address);
            distance = (TextView) view.findViewById(R.id.distance);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
