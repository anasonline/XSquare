package anas.online.xsquare;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.net.URL;
import java.util.List;

/**
 * AsyncTaskLoader that connects to the venues endpoint, parse the json, and return
 * a list of venues.
 */

class VenueLoader extends AsyncTaskLoader<List<Venue>> {

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

