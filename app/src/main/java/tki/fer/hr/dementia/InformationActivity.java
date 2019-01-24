package tki.fer.hr.dementia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import tki.fer.hr.dementia.database.AppDatabase;
import tki.fer.hr.dementia.models.Contact;
import tki.fer.hr.dementia.models.User;
import tki.fer.hr.dementia.utility.AsyncTaskHelper;
import tki.fer.hr.dementia.utility.GeoConstants;
import tki.fer.hr.dementia.utility.Locator;
import tki.fer.hr.dementia.utility.MessageService;
import tki.fer.hr.dementia.utility.PermissionProvider;


/**
 * Created by lucija on 31.01.18..
 */

public class InformationActivity extends AppCompatActivity {

    private AppDatabase appDatabase = App.getDatabase();
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private FusedLocationProviderClient mFusedLocationClient;
    private User user;
    private SharedPreferences settingsPreference;


    private TextView locationView;
    private TextView firstName;
    private TextView lastName;
    private TextView address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mResultReceiver = new AddressResultReceiver(new Handler());

        settingsPreference = PreferenceManager.getDefaultSharedPreferences(this);

        locationView = findViewById(R.id.currentLocation);
        firstName = findViewById(R.id.nameData);
        lastName = findViewById(R.id.surnameData);
        address = findViewById(R.id.addressData);

        AsyncTaskHelper.instance().runOnBackground(() -> {
            List<User> allUsers = appDatabase.userDao().getAllUsers();
            if (allUsers == null || allUsers.isEmpty()) return;

            user = allUsers.get(0);
            updateUI();
        });

        getSupportActionBar().setTitle("Dementia");

        fetchAddress();
    }


    @SuppressLint("SetTextI18n")
    private void updateUI() {
        runOnUiThread(() -> {
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            address.setText(user.getAddress());
        });

    }

    protected void startIntentService() {
        Intent intent = new Intent(this, Locator.class);
        intent.putExtra(GeoConstants.RECEIVER, mResultReceiver);
        intent.putExtra(GeoConstants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }


    public void fetchAddress() {

        if (!PermissionProvider.checkLocationPermission(this)) {
            Toast.makeText(InformationActivity.this,
                    "Niste dozvolili pristup lokaciji.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    mLastLocation = location;

                    // In some rare cases the location returned can be null
                    if (mLastLocation == null) {
                        return;
                    }

                    if (!Geocoder.isPresent()) {
                        Toast.makeText(InformationActivity.this,
                                R.string.no_geocoder_available,
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Start service and update UI to reflect new location
                    startIntentService();
                });
    }

    private class AddressResultReceiver extends ResultReceiver {

        private AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            setLocation(resultData.getString(GeoConstants.RESULT_DATA_KEY));
        }
    }

    private void setLocation(String location) {
        locationView.setText(location);
        boolean messageAllowed = settingsPreference.getBoolean("key_send_message", false);

        if (!messageAllowed) {
            Toast.makeText(this, "Zabranili ste slanje poruka u postavkama aplikacije.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!PermissionProvider.checkSmsPermission(getApplicationContext())) {
            Toast.makeText(this, "Zabranili ste slanje poruka u postavkama uređaja.", Toast.LENGTH_LONG).show();
            PermissionProvider.requestSmsPermission(this);
            return;
        }

        if (PermissionProvider.checkSmsPermission(this)) {
            String message = user.getMassage() + location;

            AsyncTaskHelper.instance().runOnBackground(() -> {
                List<Contact> allContacts = appDatabase.contactsDao().getAllContacts();
                if (allContacts == null) return;
                allContacts.forEach(c -> MessageService.sendSMS(c.getPhoneNumber(), message));
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
