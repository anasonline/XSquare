package anas.online.xsquare;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
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
            jsonResponse = ConnectionUtils.makeHttpRequest(requestUrl);
        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response, create a list of movies and return it

        return extractVenueDataFromJson(jsonResponse);
    }

    // Return a list of Venue objects that has been built up from parsing the given JSON response.

    private static List<Venue> extractVenueDataFromJson(String venueJSON) {

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

    public static String fetchPhotoUrl(URL requestUrl) {
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = ConnectionUtils.makeHttpRequest(requestUrl);
        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response, create a list of movies and return it

        return extractPhotoUrlFromJson(jsonResponse);
    }

    private static String extractPhotoUrlFromJson(String photoJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(photoJSON)) {
            return null;
        }

        // The variable that stores the full PhotoUrl
        String photoUrl = null;

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject root = new JSONObject(photoJSON);
            JSONObject response = root.getJSONObject("response");
            JSONObject photosObject = response.getJSONObject("photos");

            int count = photosObject.getInt("count");

            if (count == 0) {
                Log.v("QueryUtils", "no photos");
            } else {
                JSONArray itemsArray = photosObject.getJSONArray("items");

            /*In our case, we only want to show one photo for each venue, so we get the
            first photo object in the array.*/
                JSONObject photoObject = itemsArray.getJSONObject(0);
                String UrlPrefix = photoObject.getString("prefix");
                String UrlSuffix = photoObject.getString("suffix");

                // Create the full photo url

                photoUrl = UrlPrefix + "original" + UrlSuffix;
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the venue JSON results", e);
        }
        // Return the list of movies
        return photoUrl;
    }


}
