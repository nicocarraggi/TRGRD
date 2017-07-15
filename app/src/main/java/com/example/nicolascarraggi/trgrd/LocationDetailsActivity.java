package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.rulesys.Location;

public class LocationDetailsActivity extends RuleSystemBindingActivity {

    private String locationId;
    private Location location;

    private TextView tvName, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Location");

        Intent intent = getIntent();
        this.locationId = intent.getStringExtra("locationid");
        // TODO IF empty string -> ID ERROR?

        tvName = (TextView) findViewById(R.id.tvLocationDetailsName);
        tvAddress = (TextView) findViewById(R.id.tvLocationDetailsAddress);

    }

    @Override
    protected void onBound() {
        super.onBound();
        location = ruleSystemService.getLocationManager().getLocation(locationId);
        tvName.setText(location.getName());
        tvAddress.setText(location.getAddress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_location_edit) {
            Intent intent = new Intent(LocationDetailsActivity.this, CreateLocationActivity.class);
            intent.putExtra("iscreate",false);
            intent.putExtra("locationid",locationId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
