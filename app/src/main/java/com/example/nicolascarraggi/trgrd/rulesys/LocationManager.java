package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

class LocationManager {
    private static final LocationManager ourInstance = new LocationManager();

    static LocationManager getInstance() {
        return ourInstance;
    }

    private LocationManager() {
    }
}
