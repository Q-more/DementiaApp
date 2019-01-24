package tki.fer.hr.dementia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import tki.fer.hr.dementia.database.AppDatabase;
import tki.fer.hr.dementia.models.User;
import tki.fer.hr.dementia.utility.AsyncTaskHelper;
import tki.fer.hr.dementia.utility.PermissionProvider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LaunchingActivity extends Activity {

    private final AppDatabase appDatabase = App.getDatabase();
    private SharedPreferences preferences;

    private EditText firstName;
    private EditText lastName;
    private EditText address;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        firstName = findViewById(R.id.name);
        lastName = findViewById(R.id.surnameText);
        address = findViewById(R.id.address);

        preferences = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

        if (!preferences.getBoolean("activity_executed", false)) {

            int Permission_All = 1;

            String[] Permissions = {
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.READ_CONTACTS,
                    android.Manifest.permission.SEND_SMS};
            if (!hasPermissions(this, Permissions)) {
                ActivityCompat.requestPermissions(this, Permissions, Permission_All);
            }
        }

        AsyncTaskHelper.instance().runOnBackground(() -> {
            List<User> allUsers = appDatabase.userDao().getAllUsers();
            if (allUsers == null || allUsers.isEmpty()) return;

            user = allUsers.get(0);
            updateUI();
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateUI() {
        runOnUiThread(() -> {
            firstName.setText(user.firstName);
            lastName.setText(user.lastName);
            address.setText(user.address);
        });

    }


    public void saveUserInfo(View view) {
        if (isEmpty()) {
            Toast.makeText(this, "Ispunite sva polja", Toast.LENGTH_LONG).show();
            return;
        }

        String name = getText(firstName);
        String surname = getText(lastName);
        String addr = getText(address);

        if (!preferences.getBoolean("activity_executed", false)) {

            user = new User(name, surname, addr);

            AsyncTaskHelper.instance().runOnBackground(() -> {
                appDatabase.userDao().insertUsers(user);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("activity_executed", true);
                editor.apply();
                startNewActivity();
                finish();
            });

        } else {
            user.setAddress(addr);
            user.setFirstName(name);
            user.setLastName(surname);

            AsyncTaskHelper.instance().runOnBackground(() -> {
                appDatabase.userDao().insertUsers(user);
                startNewActivity();
            });
        }
    }

    @NonNull
    private String getText(EditText element) {

        return element.getText().toString();
    }


    private void startNewActivity() {
        this.startActivity(new Intent(this, MainActivity.class));
    }

    private boolean isEmpty() {
        return getText(firstName).isEmpty() || getText(lastName).isEmpty() || getText(address).isEmpty();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean hasPermissions(Context context, String... permissions) {

        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }
}
