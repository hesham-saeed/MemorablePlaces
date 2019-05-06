package com.example.memorableplaces.utilities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public final class GeocodingUtils {

    public static String getAddressAsString(Context context, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context);
        StringBuilder addressFragments = new StringBuilder();
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
            if (addressList != null) {
                Address address = addressList.get(0);
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.append(address.getAddressLine(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressFragments.toString();
    }

}
