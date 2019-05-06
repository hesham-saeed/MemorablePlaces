package com.example.memorableplaces;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.memorableplaces.model.MemorablePlace;
import com.example.memorableplaces.sync.MemorablePlacesIntentService;
import com.example.memorableplaces.utilities.PreferenceUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private ArrayList<String> addressList = new ArrayList<>();
    private ArrayList<MemorablePlace> memorablePlacesList = new ArrayList<>();
    private fetchMemorablePlacesTask fetchItemsTask;
    private ListView placesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressList.add("Add a memorable place");

        placesListView = findViewById(R.id.places_list_view);

        placesListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList));

        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { //Add a new memorable place option

                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(intent);

                } else { //Navigate to such location
                    String address = addressList.get(position);

                    //(position -1) for the extra Add a memorable place choice in listview
                    LatLng location = memorablePlacesList.get(position - 1).getLocation();

                    Intent intent = MapActivity.newIntent(MainActivity.this,
                            address,
                            location);

                    startActivity(intent);

                }
            }
        });

        placesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String address = memorablePlacesList.get(position-1).getAddress();
                Intent intent = MemorablePlacesIntentService.newIntent(MainActivity.this, address);
                intent.setAction(MemorablePlacesIntentService.ACTION_REMOVE_MEMORABLE_PLACE);
                startService(intent);
                return true;
            }
        });

        fetchItemsTask = new fetchMemorablePlacesTask();
        fetchItemsTask.execute();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        fetchItemsTask = (fetchMemorablePlacesTask) new fetchMemorablePlacesTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fetchItemsTask.cancel(true);
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    class fetchMemorablePlacesTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            return PreferenceUtils.getMemorablePlaces(MainActivity.this);

        }

        @Override
        protected void onPostExecute(String memorablePlacesJson) {
            Gson gson = new Gson();
            memorablePlacesList = gson.fromJson(memorablePlacesJson, MemorablePlace.getListType());

            addressList = new ArrayList<>();
            addressList.add("Add a memorable place");

            if (memorablePlacesList != null) {
                ArrayList<String> keys = new ArrayList<>();
                for (MemorablePlace memorablePlace : memorablePlacesList)
                    keys.add(memorablePlace.getAddress());
                addressList.addAll(keys);
            }

            placesListView.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    addressList));
        }
    }
}

