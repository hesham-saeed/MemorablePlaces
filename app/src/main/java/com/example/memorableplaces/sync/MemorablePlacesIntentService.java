package com.example.memorableplaces.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.memorableplaces.utilities.GeocodingUtils;
import com.example.memorableplaces.utilities.PreferenceUtils;
import com.google.android.gms.maps.model.LatLng;

public class MemorablePlacesIntentService extends IntentService {
    private static final String TAG = "MemorablePlacesIntentService";

    public static final String ACTION_ADD_MEMORABLE_PLACE = "com.example.memorableplaces.sync.ADD_MEMORABLE_PLACE";
    public static final String ACTION_REMOVE_MEMORABLE_PLACE = "com.example.memorableplaces.sync.REMOVE_MEMORABLE_PLACE";

    private static final String EXTRA_LATLNG = "com.example.memorableplaces.sync.latlng";
    private static final String EXTRA_MEMORABLE_PLACE = "com.example.memorableplaces.sync.memorable_place";

    public static Intent newIntent(Context context, LatLng latLng) {
        Intent intent = new Intent(context, MemorablePlacesIntentService.class);
        intent.putExtra(EXTRA_LATLNG, latLng);
        return intent;
    }

    public static Intent newIntent(Context context, String memorablePlaceAddress) {
        Intent intent = new Intent(context, MemorablePlacesIntentService.class);
        intent.putExtra(EXTRA_MEMORABLE_PLACE, memorablePlaceAddress);
        return intent;
    }

    public MemorablePlacesIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = null;
        if (intent != null) {
            action = intent.getAction();
        }
        if (action == null)
            return;
        if (action.equals(ACTION_ADD_MEMORABLE_PLACE)) {

            LatLng latLng = intent.getParcelableExtra(EXTRA_LATLNG);
            String addressOfMemorablePlace = GeocodingUtils.getAddressAsString(this, latLng);
            PreferenceUtils.addMemorablePlace(this, addressOfMemorablePlace, latLng);
        }
        else if (action.equals(ACTION_REMOVE_MEMORABLE_PLACE)) {

            String memorablePlace = intent.getStringExtra(EXTRA_MEMORABLE_PLACE);
            PreferenceUtils.removeMemorablePlace(this, memorablePlace);
        }
    }
}
