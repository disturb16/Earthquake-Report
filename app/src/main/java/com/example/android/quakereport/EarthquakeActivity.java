/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.quakereport.Utils.QueryUtils;
import com.example.android.quakereport.loaders.EarthquakeLoader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&limit=10";

    EarthquakeAdapter adapter;
    ProgressBar progress;
    TextView emptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        progress = (ProgressBar) findViewById(R.id.progress);
        emptyList = (TextView) findViewById(R.id.txtEmptyList);

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setEmptyView(emptyList);
        earthquakeListView.setAdapter(adapter);

        LoaderManager loaderManager = getLoaderManager();
        progress.setVisibility(View.VISIBLE);

        if( !QueryUtils.hasInternetConnection(getApplicationContext()) ) {
            progress.setVisibility(View.GONE);
            emptyList.setText(R.string.no_inter_connect);
        }else
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        adapter.clear();

        if(!earthquakes.isEmpty() || earthquakes != null){
            emptyList.setText(R.string.no_earthquakes);
            adapter.addAll(earthquakes);
        }

        progress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        adapter.clear();
    }

    private class FetchEarthquakesTask extends AsyncTask<String, Void, List<Earthquake>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<Earthquake> doInBackground(String... params) {

            if(params[0] == null || params.length < 1)
                return null;


            List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(params[0]);
            return earthquakes;
        }


        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            super.onPostExecute(earthquakes);


        }
    }
}
