package anas.online.xsquare;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.net.URL;

/**
 * Created by anas on 13.08.17.
 */

public class PhotoUrlLoader extends AsyncTaskLoader<String> {

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
