package anas.online.xsquare;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import anas.online.xsquare.model.Venue;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Venue>> {

    private final static String TAG = MainActivity.class.getSimpleName();
    private static final int VENUE_LOADER_ID = 1; // Constant value for the Venue loader ID.

    private static final String BASE_URL = "https://api.foursquare.com/v2/venues";
    private final static String LL = "52.500342,13.425170";
    private final static String CLIENT_ID = "ES0C40SIQINKXBKND2HKNSFIRQRW2R2SUEB0KJ2VESBDABEG";
    private final static String CLIENT_SECRET = "ZNZT4LKNLQU3CTOZQSR403YAPH02WFBRMT12J10003QGL3HN";
    private final static String VERSION = "20170810";
    private final static String METHOD = "foursquare";

    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    private VenueAdapter mVenueAdapter;
    private List<Venue> mVenues;

    private int mPosition = RecyclerView.NO_POSITION;

    private static URL buildUrl() {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("search")
                .appendQueryParameter("ll", LL)
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("client_secret", CLIENT_SECRET)
                .appendQueryParameter("v", VERSION)
                .appendQueryParameter("m", METHOD)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v("MainActivity", builtUri.toString());

        return url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLoadingIndicator.setVisibility(View.VISIBLE);

        mRecyclerView = (RecyclerView) findViewById(R.id.venues_recycler_view);
        setupGridLayout();

        mVenueAdapter = new VenueAdapter(mVenues, R.layout.item_venue, getApplicationContext());
        mRecyclerView.setAdapter(mVenueAdapter);
        mVenueAdapter.swapList(mVenues);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(VENUE_LOADER_ID, null, this);

        } else {

            showErrorMessage();

            Log.v("MainActivity", "Loading Error");
        }
    }

    public void setupGridLayout() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
    }

    @Override
    public Loader<List<Venue>> onCreateLoader(int i, Bundle bundle) {
        return new VenueLoader(this, buildUrl());
    }

    @Override
    public void onLoadFinished(Loader<List<Venue>> loader, List<Venue> venues) {
        mLoadingIndicator.setVisibility(View.GONE);

        //   venues.clear();

        if (venues != null && !venues.isEmpty()) {
            mVenueAdapter.swapList(venues);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Venue>> loader) {
        mVenues.clear();
    }

    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
