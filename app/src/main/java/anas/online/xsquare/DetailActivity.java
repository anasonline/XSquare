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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

import anas.online.xsquare.model.Venue;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

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
    String photoId;
    private Venue mVenueData;

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

        Log.v("DetailActivity", builtUri.toString());

        return url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mVenueData = getIntent().getExtras().getParcelable("EXTRA_MOVIE");

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
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(PHOTO_LOADER_ID, null, this);

        } else {

            //TODO Show loading indicator over the image + error message
            //showErrorMessage();

            Log.v("DetailActivity", "Loading Error");
        }

        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + addressDetails.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
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
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    public void openMap(String address) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + addressDetails.getText().toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
