package tki.fer.hr.dementia;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import tki.fer.hr.dementia.database.AppDatabase;
import tki.fer.hr.dementia.models.Contact;
import tki.fer.hr.dementia.utility.AsyncTaskHelper;
import tki.fer.hr.dementia.utility.GeoConstants;
import tki.fer.hr.dementia.utility.Locator;
import tki.fer.hr.dementia.utility.MessageService;
import tki.fer.hr.dementia.utility.PermissionProvider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SendMessageActivity extends AppCompatActivity {

    private final AppDatabase appDatabase = App.getDatabase();
    private SharedPreferences settingsPreference;
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ActionBar actionBar = getActionBar();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mResultReceiver = new AddressResultReceiver(new Handler());

        settingsPreference = PreferenceManager.getDefaultSharedPreferences(this);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendSMS(View view) {

        if (!PermissionProvider.checkLocationPermission(this)) {
            Toast.makeText(this,
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
                        Toast.makeText(this,
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
            String location = resultData.getString(GeoConstants.RESULT_DATA_KEY);

            EditText messageText = findViewById(R.id.message_text);
            View viewMessage = findViewById(R.id.message_layout);


            boolean messageAllowed = settingsPreference.getBoolean("key_send_message", false);

            if (!messageAllowed) {
                Snackbar snackbar = Snackbar.make(viewMessage, "Zabranili ste slanje poruke u postavkama aplikacije.", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return;
            }

            if (!PermissionProvider.checkSmsPermission(getApplicationContext())) {
                Snackbar snackbar = Snackbar.make(viewMessage, "Zabranili ste slanje poruke u postavkama mobitela.", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return;
            }

            AsyncTaskHelper.instance().runOnBackground(() -> {
                List<Contact> allContacts = appDatabase.contactsDao().getAllContacts();
                if (allContacts == null) return;
                allContacts.forEach(c -> MessageService.sendSMS(c.getPhoneNumber(), messageText.getText().toString() + " " + location));
                runOnUiThread(() -> messageText.setText(""));
            });
        }
    }

    protected void startIntentService() {
        Intent intent = new Intent(this, Locator.class);
        intent.putExtra(GeoConstants.RECEIVER, mResultReceiver);
        intent.putExtra(GeoConstants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }
}
