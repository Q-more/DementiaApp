package tki.fer.hr.dementia;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import tki.fer.hr.dementia.database.AppDatabase;
import tki.fer.hr.dementia.models.Contact;
import tki.fer.hr.dementia.models.User;
import tki.fer.hr.dementia.service.InformationData;
import tki.fer.hr.dementia.service.ListenerService;
import tki.fer.hr.dementia.utility.AsyncTaskHelper;
import tki.fer.hr.dementia.utility.GeoConstants;
import tki.fer.hr.dementia.utility.Locator;
import tki.fer.hr.dementia.utility.MessageService;
import tki.fer.hr.dementia.utility.PermissionProvider;


/**
 * Created by lucija on 01.02.18..
 */

public class App extends Application implements MessageClient.OnMessageReceivedListener {
    private static AppDatabase appDatabase;

    private static final String WATCH_PATH = "/wear";
    private static final String PHONE_PATH = "/mobile";
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private FusedLocationProviderClient mFusedLocationClient;
    private SharedPreferences settingsPreference;
    private String nodeId;

    private final Logger LOG = Logger.getLogger(ListenerService.class.getSimpleName());

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        Wearable.getMessageClient(this).addListener(this);
        settingsPreference = PreferenceManager.getDefaultSharedPreferences(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
    }

    public static AppDatabase getDatabase() {
        return appDatabase;
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        LOG.info("Message recived from " + messageEvent.getSourceNodeId());
        LOG.info("Message path: " + messageEvent.getPath());
        if (messageEvent.getPath().equals(PHONE_PATH)) {
            nodeId = messageEvent.getSourceNodeId();

            fetchAddress();
        }
    }

    private byte[] getData(String location, String name, String lastName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsBytes(new InformationData(location, name, lastName));
    }

    private class AddressResultReceiver extends ResultReceiver {

        private AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            boolean messageAllowed = settingsPreference.getBoolean("key_send_message", false);

            AsyncTaskHelper.instance().runOnBackground(() -> {
                List<User> allUsers = appDatabase.userDao().getAllUsers();
                if (allUsers == null || allUsers.isEmpty()) return;
                User user = allUsers.get(0);
                String location = resultData.getString(GeoConstants.RESULT_DATA_KEY);
                try {
                    Wearable.getMessageClient(App.this).sendMessage(nodeId, WATCH_PATH, getData(user.firstName, user.lastName, location));
                    LOG.info("Message send to wear...");
                    if (PermissionProvider.checkSmsPermission(App.this) && messageAllowed) {
                        String message = user.getMassage() + location;

                        AsyncTaskHelper.instance().runOnBackground(() -> {
                            List<Contact> allContacts = appDatabase.contactsDao().getAllContacts();
                            if (allContacts == null) return;
                            allContacts.forEach(c -> MessageService.sendSMS(c.getPhoneNumber(), message));
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
    }

    protected void startIntentService() {
        Intent intent = new Intent(this, Locator.class);
        intent.putExtra(GeoConstants.RECEIVER, mResultReceiver);
        intent.putExtra(GeoConstants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }


    public void fetchAddress() {

        if (!PermissionProvider.checkLocationPermission(this)) {
            return;
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            mLastLocation = location;

            // In some rare cases the location returned can be null
            if (mLastLocation == null) {
                return;
            }

            if (!Geocoder.isPresent()) {
                return;
            }

            // Start service and update UI to reflect new location
            startIntentService();
        });
    }

}
