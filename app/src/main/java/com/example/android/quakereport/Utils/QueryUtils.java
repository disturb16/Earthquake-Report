package com.example.android.quakereport.Utils;

import android.util.Log;

import com.example.android.quakereport.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {


    public static URL createUrl(String _url){
        URL url = null;

        try {
            url = new URL(_url);
        } catch (MalformedURLException e) {
            Log.e("ConnectHelper error:", "Problem building the URL ", e);
        }

        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String response = "";

        if (url == null)
            return response;

        HttpURLConnection conn = null;
        InputStream istream = null;

        try{

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("GET");
            conn.connect();


            if (conn.getResponseCode() == 200){
                istream = conn.getInputStream();
                response = readFromStream(istream);

            }else
                Log.e("Query connection error", "Response error:" + conn.getResponseCode());

        }catch (IOException e){
            Log.e("Query Error", "Problem retrieving the earthquake JSON results.", e);
        }finally {
            if (conn != null)
                conn.disconnect();

            if (istream != null)
                istream.close();
        }

        return response;
    }

    public static String readFromStream(InputStream istream) throws IOException {
        StringBuilder result = new StringBuilder();

        if (istream == null)
            return  result.toString();

        InputStreamReader isreader = new InputStreamReader(istream, Charset.forName("UTF-8"));
        BufferedReader breader = new BufferedReader(isreader);

        String line = null;
        line = breader.readLine();

        while(line != null){
            result.append(line);
            line = breader.readLine();
        }

        return  result.toString();
    }

    public static ArrayList<Earthquake> extractEarthquakes(String jsonstr) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject root = new JSONObject(jsonstr);
            JSONArray features = root.optJSONArray("features");


            for(int i = 0;i< features.length(); i++ ){
                JSONObject feature = features.getJSONObject(i);
                JSONObject featureEarthquake = feature.getJSONObject("properties");

                Earthquake quake = new Earthquake(
                        featureEarthquake.optDouble("mag"),
                        featureEarthquake.optString("place"),
                        featureEarthquake.optLong("time"),
                        featureEarthquake.optString("url")
                );
                earthquakes.add(quake);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    public static List<Earthquake> fetchEarthquakeData(String requestUrl){

        List<Earthquake> result = null;

        URL url = createUrl(requestUrl);

        String response = "";

        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        result = extractEarthquakes(response);

        return result;
    }
}