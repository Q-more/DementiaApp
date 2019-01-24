package tki.fer.hr.dementia.utility;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import tki.fer.hr.dementia.InformationActivity;
import tki.fer.hr.dementia.R;

import static android.content.ContentValues.TAG;


/**
 * Created by lucija on 06.02.18..
 */

public class Locator extends IntentService {
    private ResultReceiver mReceiver;

    public Locator() {
        super("locatorService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String errorMessage = "";

        assert intent != null;
        Location location = intent.getParcelableExtra(GeoConstants.LOCATION_DATA_EXTRA);
        mReceiver = intent.getParcelableExtra(GeoConstants.RECEIVER);

        List<Address> addresses = null;

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1); // In this sample, get just a single address.

        } catch (IOException ioException) {
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);

        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.isEmpty()) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }

            deliverResultToReceiver(GeoConstants.FAILURE_RESULT, errorMessage);

        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            Log.i(TAG, getString(R.string.address_found));

            deliverResultToReceiver(GeoConstants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
    }


    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(GeoConstants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
