package anas.online.xsquare;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final int PHOTO_LOADER_ID = 1; // Constant value for the Venue loader ID.
    private static final String BASE_URL = "https://api.foursquare.com/v2/venues";
    private final static String CLIENT_ID = "ES0C40SIQINKXBKND2HKNSFIRQRW2R2SUEB0KJ2VESBDABEG";
    private final static String CLIENT_SECRET = "ZNZT4LKNLQU3CTOZQSR403YAPH02WFBRMT12J10003QGL3HN";
    private final static String VERSION = "20170810";
    private final static String METHOD = "foursquare";

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.address_details)
    TextView addressDetails;
    @BindView(R.id.distance_value)
    TextView distanceValue;
    @BindView(R.id.btn_map)
    Button mapButton;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    String photoId;

    private static URL buildUrl(String photoId) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(photoId)
                .appendPath("photos")
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

        return url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // Get Venue data from the intent
        Venue mVenueData = getIntent().getExtras().getParcelable("EXTRA_VENUE");

        photoId = mVenueData.getId();
        name.setText(mVenueData.getName());
        addressDetails.setText(mVenueData.getAddress());
        distanceValue.setText(mVenueData.getDistance());

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
            // the bundle. Pass in this activity for the LoaderCallbacks parameter.
            loaderManager.initLoader(PHOTO_LOADER_ID, null, this);

        } else {
            showErrorMessage();
            Log.v(TAG, "Loading Error");
        }

        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // IF address is not available for this venue, then show a Toast message
                if (addressDetails.getText().equals(getResources().getString(R.string.no_address_available))) {
                    Toast.makeText(DetailActivity.this, getResources().getString(R.string.no_address_available),
                            Toast.LENGTH_SHORT).show();
                } else {
                    openMap();
                }
            }
        });
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        return new PhotoUrlLoader(this, buildUrl(photoId));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        Picasso.with(this).load(s).placeholder(R.drawable.placeholder).into(photo);
        mLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    public void openMap() {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + addressDetails.getText());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        //Check first if Google Maps is available on the device
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            //Prevent a crash if Google Maps is not available on the device
            Toast.makeText(DetailActivity.this, "Google Maps is Not Available",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.GONE);
        Toast.makeText(this, getResources().getString(R.string.no_connection_error),
                Toast.LENGTH_SHORT).show();
    }
}