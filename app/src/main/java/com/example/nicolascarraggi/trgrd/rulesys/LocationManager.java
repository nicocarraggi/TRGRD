package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class LocationManager {

    private RuleSystemService mRuleSystemService;

    private HashMap<String,Location> locations;

    public LocationManager(RuleSystemService mRuleSystemService) {
        this.mRuleSystemService = mRuleSystemService;
        this.locations = new HashMap<>();
    }

    public HashSet<Location> getLocations(){
        return new HashSet(locations.values());
    }

    public Location getLocation(String id){
        return locations.get(id);
    }

    public void addLocation(Location location){
        this.locations.put(location.getId(),location);
    }

}
