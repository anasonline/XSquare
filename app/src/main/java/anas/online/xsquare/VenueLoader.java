package anas.online.xsquare;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.net.URL;
import java.util.List;

import anas.online.xsquare.model.Venue;

/**
 * Created by anas on 12.08.17.
 */

public class VenueLoader extends AsyncTaskLoader<List<Venue>> {

    private final URL mUrl;

    public VenueLoader(Context context, URL url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Venue> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.fetchVenueData(mUrl);
    }
}

