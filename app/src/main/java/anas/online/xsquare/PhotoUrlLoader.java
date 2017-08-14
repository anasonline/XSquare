package anas.online.xsquare;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.net.URL;

/**
 * AsyncTaskLoader to connect to the photos endpoint for a venue, parse the json, and return
 * the full photo url (in our case we want to show only one photo for each venue
 * so we get only the first photo returned by the API.
 */

class PhotoUrlLoader extends AsyncTaskLoader<String> {

    private final URL mUrl;

    public PhotoUrlLoader(Context context, URL url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.fetchPhotoUrl(mUrl);
    }
}
