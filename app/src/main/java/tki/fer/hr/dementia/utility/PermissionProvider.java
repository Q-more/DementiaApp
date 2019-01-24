package tki.fer.hr.dementia.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by lucija on 23.02.18..
 */

public class PermissionProvider {
    public final static int LOCATION_CODE = 1;
    public final static int MESSAGE_CODE = 2;

    public static void buildAlertMessageNoGps(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("GPS je ugašen, želite li ga aktivirati?")
                .setCancelable(false)
                .setPositiveButton("Da", (dialog, id) -> context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("Ne", (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();

    }

    public static boolean checkLocationPermission(Context context) {
        boolean hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        Log.d("CheckPerm", "checkLocationPermission: " + hasPermission);
        return hasPermission;
    }

    public static boolean checStoragePermission(Context context) {
        boolean hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Log.d("CheckPerm", "checkStoragePermission: " + hasPermission);
        return hasPermission;
    }


    public static boolean checkSmsPermission(Context context) {
        boolean hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        Log.d("CheckPerm", "checkSmsPermission: " + hasPermission);
        return hasPermission;
    }

    public static boolean checkContactPermission(Context context) {
        boolean hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        Log.d("CheckPerm", "checkContactPermission: " + hasPermission);
        return hasPermission;
    }


    public static void requestSmsPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, 123);
    }

    public static void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                123);
    }

    public static boolean enableGPS(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            PermissionProvider.buildAlertMessageNoGps(context);
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context.getApplicationContext(), "Niste uključili lokaciju!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}