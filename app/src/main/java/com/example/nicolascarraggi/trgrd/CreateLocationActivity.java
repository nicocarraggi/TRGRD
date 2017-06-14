package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.rulesys.Location;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

public class CreateLocationActivity extends RuleSystemBindingActivity {

    private final int REQUEST_CODE_PLACEPICKER = 1;

    private EditText etName;
    private TextView tvAddress;
    private LinearLayout llName, llAddress;
    private Button bPick;
    private boolean isCreate, isPicked;
    private String locationId;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);

        // Get extra int ID! IF real id (>=0), edit location!!!
        this.isCreate = getIntent().getBooleanExtra("iscreate",true);
        this.locationId = getIntent().getStringExtra("locationid");

        llName = (LinearLayout) findViewById(R.id.llCreateLocationName);
        llAddress = (LinearLayout) findViewById(R.id.llCreateLocationAddress);
        etName = (EditText) findViewById(R.id.etCreateLocationName);
        tvAddress = (TextView) findViewById(R.id.tvCreateLocationAddress);
        bPick = (Button) findViewById(R.id.bCreateLocationPick);

        bPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlacePickerActivity();
            }
        });

        isPicked = false;

    }

    @Override
    protected void onBound() {
        super.onBound();

        ActionBar ab = getSupportActionBar();
        if (isCreate) {
            ab.setTitle("Create location");
            if(!isPicked){
                llName.setVisibility(View.GONE);
                llAddress.setVisibility(View.GONE);
            }
        } else {
            ab.setTitle("Edit location");
            this.isPicked = true;

            location = ruleSystemService.getLocation(locationId);
            etName.setText(location.getName());
            tvAddress.setText(location.getAddress());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_location_save) {
            if(isLocationValid()) {
                if(isCreate) {
                    location.setName(etName.getText().toString());
                    ruleSystemService.addLocation(location);
                    Intent intent = new Intent(this, LocationDetailsActivity.class);
                    intent.putExtra("locationid",location.getId());
                    startActivity(intent);
                } else {
                    this.location.setName(etName.getText().toString());
                    // TODO Set all other properties?
                }
                finish();
            } else {
                Toast.makeText(CreateLocationActivity.this, "A location requires a name and an address!", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Check if location name is not empty AND address is not empty!
    private boolean isLocationValid(){
        // show explanation message
        return (!etName.getText().toString().isEmpty() && !tvAddress.getText().toString().isEmpty());
    }

    private void startPlacePickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent,REQUEST_CODE_PLACEPICKER);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE_PLACEPICKER){
            displayPlace(data);
        }
    }

    private void displayPlace(Intent data) {
        if(data != null) {
            isPicked = true;
            llName.setVisibility(View.VISIBLE);
            llAddress.setVisibility(View.VISIBLE);
            bPick.setText("Pick other place!");
            Place selectedPlace = PlacePicker.getPlace(this, data);
            Log.d("TRGRD","CreateLocationActivity displayPlace place = "+selectedPlace);
            String id = selectedPlace.getId();
            String name = selectedPlace.getName().toString();
            String address = "";
            if(selectedPlace.getAddress() != null) address = selectedPlace.getAddress().toString();
            LatLng latLng = selectedPlace.getLatLng();
            etName.setText(name);
            tvAddress.setText(address);
            if(isCreate){
                location = new Location(id,name,address,R.drawable.ic_location_on_black_24dp,latLng);
                ruleSystemService.getDeviceManager().getGeofences().addGeofence(location);
            } else {
                location.setName(name);
                location.setAddress(address);
                location.setLatLng(latLng);
                ruleSystemService.getDeviceManager().getGeofences().refrehGeofence(location);
            }
        }
    }
}
