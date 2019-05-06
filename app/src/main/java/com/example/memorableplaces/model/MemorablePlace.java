package com.example.memorableplaces.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MemorablePlace {
    private String address;
    private LatLng location;

    public String getAddress() {
        return address;
    }

    public LatLng getLocation() {
        return location;
    }

    public MemorablePlace(String address, LatLng location) {
        this.address = address;
        this.location = location;
    }

    public static Type getListType(){
        return new TypeToken<ArrayList<MemorablePlace>>() {
        }.getType();
    }

}
