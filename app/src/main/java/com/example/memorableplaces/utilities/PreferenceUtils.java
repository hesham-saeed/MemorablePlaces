package com.example.memorableplaces.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.memorableplaces.model.MemorablePlace;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PreferenceUtils {
    private static final String TAG = "PreferenceUtils";
    private static final String KEY_MEMORABLE_PLACE = "key-memorable-place";

    synchronized public static void addMemorablePlace(Context context, String memorablePlace, LatLng location) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String memorablePlacesJson = sharedPreferences.getString(KEY_MEMORABLE_PLACE, "");
        Gson gson = new Gson();

        //Parsing the stored json to an ArrayList of MemorablePlace
        ArrayList<MemorablePlace> memorablePlacesList = gson.fromJson(memorablePlacesJson,
                MemorablePlace.getListType());

        //list maybe empty if json string is empty
        if (memorablePlacesList == null)
            memorablePlacesList = new ArrayList<>();

        //adding the new memorable place in the arraylist
        memorablePlacesList.add(new MemorablePlace(memorablePlace, location));

        //converting the arraylist again to Json string then storing that new json string
        String updatedMemorablePlacesJson = gson.toJson(memorablePlacesList,
                MemorablePlace.getListType());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MEMORABLE_PLACE, updatedMemorablePlacesJson);
        editor.apply();

        Log.d(TAG, "Added memorable place " + memorablePlace);

    }

    public static String getMemorablePlaces(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(KEY_MEMORABLE_PLACE, "");
    }

    synchronized public static void removeMemorablePlace(Context context, String memorablePlace) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String memorablePlacesJson = getMemorablePlaces(context);

        Gson gson = new Gson();

        //parsing the stored json string to ArrayList<MemorablePlace>
        ArrayList<MemorablePlace> memorablePlacesList = gson.fromJson(memorablePlacesJson,
                MemorablePlace.getListType());

        if (memorablePlacesList != null) {

            //Searching by memorable place address
            for (int i = 0; i < memorablePlacesList.size(); i++) {
                if (memorablePlacesList.get(i).getAddress().equals(memorablePlace)) {
                    memorablePlacesList.remove(i);
                    break;
                }
            }
        }

        //Parsing the new arraylist to json string then re-storing
        String newMemorablePlacesJson = gson.toJson(memorablePlacesList, MemorablePlace.getListType());

        editor.putString(KEY_MEMORABLE_PLACE, newMemorablePlacesJson);
        editor.apply();

        Log.d(TAG, "Removed memorable place " + memorablePlace);
    }
}
