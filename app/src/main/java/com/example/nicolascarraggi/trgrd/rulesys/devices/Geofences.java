package com.example.nicolascarraggi.trgrd.rulesys.devices;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.MainActivity;
import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.EventType;
import com.example.nicolascarraggi.trgrd.rulesys.Location;
import com.example.nicolascarraggi.trgrd.rulesys.LocationEvent;
import com.example.nicolascarraggi.trgrd.rulesys.LocationState;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;
import com.example.nicolascarraggi.trgrd.rulesys.StateType;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by nicolascarraggi on 1/05/17.
 */

public class Geofences extends Device implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String GEOFENCE_RECEIVE_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.GEOFENCE_RECEIVE_ACTION";

    // Clock Broadcast Receiver

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TRGRD","Geofences mReceiver "+action);
            if (action.equals(GEOFENCE_RECEIVE_ACTION)){
                handleIntent(intent);
            }
        }
    };

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * The list of geofences used in this sample.
     */
    protected ArrayList<Geofence> mGeofenceList;

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    /**
     * HashMap with Geofence Request Ids as keys, to save their respective location.
     */
    private HashMap<String,Location> mGeofenceLocations;


    // Views
    private LocationEvent mEvLocationArrivingAt, mEvLocationLeaving;
    private LocationState mStLocationCurrentlyAt;

    public Geofences(RuleSystemService ruleSystemService, DeviceManager deviceManager, EventType evLocationArrivingAt, EventType evLocationLeaving, StateType stLocationCurrentlyAt) {
        super(ruleSystemService.getNewId(), "Locations", "Google", "Android", R.drawable.ic_my_location_black_24dp, ruleSystemService, deviceManager);
        this.eventTypes.put(evLocationArrivingAt.getId(),evLocationArrivingAt);
        this.eventTypes.put(evLocationLeaving.getId(),evLocationLeaving);
        this.stateTypes.put(stLocationCurrentlyAt.getId(),stLocationCurrentlyAt);
        mEvLocationArrivingAt = new LocationEvent(deviceManager.getNewId(),"Arrived at ...",R.drawable.ic_my_location_black_24dp,this,evLocationArrivingAt, LocationEvent.LocationEventType.ARRIVING);
        mEvLocationLeaving = new LocationEvent(deviceManager.getNewId(),"Left ...",R.drawable.ic_my_location_black_24dp,this,evLocationLeaving, LocationEvent.LocationEventType.LEAVING);
        mStLocationCurrentlyAt = new LocationState(deviceManager.getNewId(),"Currently at ...",R.drawable.ic_my_location_black_24dp,this,stLocationCurrentlyAt,false);
        this.events.put(mEvLocationArrivingAt.getId(),mEvLocationArrivingAt);
        this.events.put(mEvLocationLeaving.getId(),mEvLocationLeaving);
        this.states.put(mStLocationCurrentlyAt.getId(),mStLocationCurrentlyAt);

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();
        mGeofenceLocations = new HashMap<>();

        // Initially set the PendingIntent used in addGeofences() and removeGeofences() to null.
        mGeofencePendingIntent = null;

        buildGoogleApiClient();

    }

    // Event & State getters

    public LocationEvent getLocationArrivingAt() {
        return mEvLocationArrivingAt;
    }

    public LocationEvent getLocationLeaving() {
        return mEvLocationLeaving;
    }

    public LocationState getLocationCurrentlyAt() {
        return mStLocationCurrentlyAt;
    }

    public void connectGoogleApiClient(){
        mGoogleApiClient.connect();
    }

    public void disconnectGoogleApiClient(){
        mGoogleApiClient.disconnect();
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(ruleSystemService)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("TRGRD", "Connected to GoogleApiClient");
        // TODO test

        HashSet<Location> locations = ruleSystemService.getLocations();
        for (Location l: locations){
            addGeofence(l);
        }

        this.started = true;
        deviceManager.sendRefreshBroadcast();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("TRGRD", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        Log.i("TRGRD", "Connection suspended");
        this.started = false;
        deviceManager.sendRefreshBroadcast();
        // onConnected() will be called again automatically when the service reconnects
    }

    public void addGeofence(final Location location){
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(ruleSystemService, ruleSystemService.getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Log.d("TRGRD","Geofences addGeofence() for "+location.toString());
            createEventsAndState(location);
            final Geofence geofence = createGeofence(location.getId(),location.getLatLng());
            GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
            builder.addGeofence(geofence);
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    builder.build(),
                    getGeofencePendingIntent()
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.d("TRGRD","Geofences addGeofence of location "+location.getName()+" = "+status.toString());
                    if (status.isSuccess()){
                        location.setGeofence(geofence);
                        Geofences.this.mGeofenceLocations.put(geofence.getRequestId(),location);
                    }
                }
            });

        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    public void refrehGeofence(final Location location){
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(ruleSystemService, ruleSystemService.getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!location.isHasGeofence()){
            Log.e("TRGRD","Geofences refrehGeofence location "+location.getName()+" has no geofence!");
        }
        try {
            Log.d("TRGRD","Geofences refrehGeofence() for "+location.toString());
            List<String> geofenceRequestIds = new ArrayList<>();
            geofenceRequestIds.add(location.getGeofence().getRequestId());
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    geofenceRequestIds
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.d("TRGRD","Geofences refrehGeofence of location "+location.getName()+" = "+status.isSuccess());
                    if (status.isSuccess()){
                        Geofences.this.mGeofenceLocations.remove(location.getGeofence().getRequestId());
                        location.setGeofence(null);
                        addGeofence(location);
                    }
                }
            });
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    public void removeGeofence(final Location location){
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(ruleSystemService, ruleSystemService.getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!location.isHasGeofence()){
            Log.e("TRGRD","Geofences removeGeofence location "+location.getName()+" has no geofence!");
        }
        try {
            Log.d("TRGRD","Geofences removeGeofence() for "+location.toString());
            List<String> geofenceRequestIds = new ArrayList<>();
            geofenceRequestIds.add(location.getGeofence().getRequestId());
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    geofenceRequestIds
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.d("TRGRD","Geofences removeGeofence of location "+location.getName()+" = "+status.isSuccess());
                    if (status.isSuccess()){
                        Geofences.this.mGeofenceLocations.remove(location.getGeofence().getRequestId());
                        location.setGeofence(null);
                    }
                }
            });
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    // use Place ID as key ( to be sure it is unique! )
    public Geofence createGeofence(String key, LatLng latLng){
        Geofence geofence = new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(key)

                // Set the circular region of this geofence.
                .setCircularRegion(
                        latLng.latitude,
                        latLng.longitude,
                        GeofencesConstants.GEOFENCE_RADIUS_IN_METERS
                )

                // Set the expiration duration of the geofence.
                .setExpirationDuration(Geofence.NEVER_EXPIRE)

                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)

                // Create the geofence.
                .build();
        return geofence;
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent == null) {
            Intent intent = new Intent(GEOFENCE_RECEIVE_ACTION);
            this.mGeofencePendingIntent = PendingIntent.getBroadcast(ruleSystemService,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return mGeofencePendingIntent;
    }

    private void logSecurityException(SecurityException securityException) {
        Log.e("TRGRD", "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    /**
     * Handles incoming intents.
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    private void handleIntent(Intent intent){
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(ruleSystemService,
                    geofencingEvent.getErrorCode());
            Log.e("TRGRD", errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    ruleSystemService,
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.
            // sendNotification(geofenceTransitionDetails);
            Log.i("TRGRD", geofenceTransitionDetails);

            for(Geofence g: geofencingEvent.getTriggeringGeofences()){
                Location location = mGeofenceLocations.get(g.getRequestId());
                if (location != null){
                    if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
                        if (location.getArrivingAt() != null) location.getArrivingAt().trigger();
                        if (location.getCurrentlyAt() != null) location.getCurrentlyAt().setState(true);
                    } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
                        if (location.getLeaving() != null) location.getLeaving().trigger();
                        if (location.getCurrentlyAt() != null) location.getCurrentlyAt().setState(false);
                    }
                }
            }

        } else {
            // Log the error.
            Log.e("TRGRD", ruleSystemService.getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param context               The app context.
     * @param geofenceTransition    The ID of the geofence transition.
     * @param triggeringGeofences   The geofence(s) triggered.
     * @return                      The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ",  triggeringGeofencesIdsList);

        Location location = mGeofenceLocations.get(triggeringGeofencesIdsString);

        if(location == null){
            return geofenceTransitionString + ": " + "null (from id "+triggeringGeofencesIdsString+")";
        }
        return geofenceTransitionString + ": " + location.getName();
    }

    /**
     *
     *  TODO remove?! not used!
     *
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private void sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(ruleSystemService, MainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ruleSystemService);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ruleSystemService);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.ic_triggered_notification)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(ruleSystemService.getResources(),
                        R.mipmap.ic_launcher_blue))
                .setColor(Color.rgb(104,159,56))
                .setContentTitle(notificationDetails)
                .setContentText(ruleSystemService.getString(R.string.geofence_transition_notification_text))
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) ruleSystemService.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType    A transition type constant defined in Geofence
     * @return                  A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return ruleSystemService.getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return ruleSystemService.getString(R.string.geofence_transition_exited);
            default:
                return ruleSystemService.getString(R.string.unknown_geofence_transition);
        }
    }

    // Create arriving & leaving events + currently at state
    public void createEventsAndState(Location location){
        if (location.getArrivingAt() == null) {
            location.setArrivingAt(new LocationEvent(deviceManager.getNewId(),mEvLocationArrivingAt,location));
        }
        if (location.getLeaving() == null) {
            location.setLeaving(new LocationEvent(deviceManager.getNewId(),mEvLocationLeaving,location));
        }
        if (location.getCurrentlyAt() == null) {
            location.setCurrentlyAt(new LocationState(deviceManager.getNewId(),mStLocationCurrentlyAt,location));
        }
    }

    // Register & UnRegister Android Phone Broadcast Receiver

    public void registerGeofencesReceiver(){
        IntentFilter filter = new IntentFilter(GEOFENCE_RECEIVE_ACTION);
        ruleSystemService.registerReceiver(mReceiver, filter);
        Log.d("TRGRD","Geofences mReceiver registered!");
    }

    public void unRegisterGeofencesReceiver(){
        ruleSystemService.unregisterReceiver(mReceiver);
        Log.d("TRGRD","Geofences mReceiver unRegistered!");
    }

    @Override
    public void start(){
        this.connectGoogleApiClient();
        this.registerGeofencesReceiver();
    }

    @Override
    public void stop(){
        this.disconnectGoogleApiClient();
        this.unRegisterGeofencesReceiver();
    }

}
