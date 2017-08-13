package anas.online.xsquare;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import anas.online.xsquare.model.Venue;

/**
 * Created by anas on 12.08.17.
 */

final class QueryUtils {
    private static final String TAG = QueryUtils.class.getSimpleName();

    // Private constructor - no one should create a QueryUtils object.
    private QueryUtils() {
    }

    public static List<Venue> fetchVenueData(URL requestUrl) {
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(requestUrl);
        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response, create a list of movies and return it

        return extractFeatureFromJson(jsonResponse);
    }

    // Make an HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the Movie JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Convert the {@link InputStream} into a String which contains the
    // whole JSON response from the server.

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    // Return a list of Venue objects that has been built up from parsing the given JSON response.

    private static List<Venue> extractFeatureFromJson(String venueJSON) {

        String address;

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(venueJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding venues to
        List<Venue> venues = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject root = new JSONObject(venueJSON);
            JSONObject response = root.getJSONObject("response");

            JSONArray venuesArray = response.getJSONArray("venues");

            // For each venue in the venuesArray, create a Venue object
            for (int i = 0; i < venuesArray.length(); i++) {
                // Get a single venue item at position i within the list of venues

                JSONObject currentVenue = venuesArray.getJSONObject(i);

                // Extract the venue id
                String id = currentVenue.getString("id");

                // Extract the venue name
                String name = currentVenue.getString("name");

                // Extract the location object to get the location data
                JSONObject locationObject = currentVenue.getJSONObject("location");

                // Extract the address

                if (locationObject.isNull("address")) {
                    address = "Address is not available";
                } else {

                    address = locationObject.getString("address");
                }
                // Extract the distance
                String distance = locationObject.getString("distance");

                // Create a new Venue object with the data extracted from JSON response
                Venue venue = new Venue(id, name, address, distance);

                // Add the new Venue to the list of venues
                venues.add(venue);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the venue JSON results", e);
        }
        // Return the list of movies
        return venues;
    }

}
