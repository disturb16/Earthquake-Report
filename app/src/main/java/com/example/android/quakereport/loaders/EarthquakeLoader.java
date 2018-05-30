package com.example.android.quakereport.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.quakereport.Earthquake;
import com.example.android.quakereport.Utils.QueryUtils;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>>{

    String url;

    public EarthquakeLoader(Context context, String _url) {
        super(context);
        url = _url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if (url == null)
            return null;

        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(url);
        return earthquakes;
    }
}
