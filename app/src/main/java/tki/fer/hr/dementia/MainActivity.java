package tki.fer.hr.dementia;

import android.annotation.SuppressLint;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import tki.fer.hr.dementia.database.AppDatabase;
import tki.fer.hr.dementia.models.User;
import tki.fer.hr.dementia.service.InformationData;
import tki.fer.hr.dementia.service.ListenerService;
import tki.fer.hr.dementia.utility.AsyncTaskHelper;
import tki.fer.hr.dementia.utility.PermissionProvider;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppDatabase appDatabase = App.getDatabase();
    private User user;
    private TextView personalInfo;
    private TextView addressInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

        if (!preferences.getBoolean("activity_executed", false)) {
            this.startActivity(new Intent(this, LaunchingActivity.class));
            finish();
        }

        if (preferences.getBoolean("message_permission", true)) {
            preferences.edit().putBoolean("message_permission", true).apply();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startNewActivity(SendMessageActivity.class));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        getSupportActionBar().setTitle("Dementia");

        personalInfo = hView.findViewById(R.id.personalInfo);
        addressInfo = hView.findViewById(R.id.addressInfo);


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
            personalInfo.setText(user.firstName + " " + user.lastName);
            addressInfo.setText(user.address);
        });

    }

    @SuppressLint("MissingPermission")
    public void fetchAddressButtonHandler(View view) {

        if (!PermissionProvider.checkLocationPermission(this)) {
            Toast.makeText(getApplicationContext(), "Morate dozvoliti pritup lokaciji za dohvat lokacije", Toast.LENGTH_LONG).show();
        }

        if (PermissionProvider.enableGPS(this)) {
            startNewActivity(InformationActivity.class);
        }

    }

    private void startNewActivity(Class<?> cls) {
        Intent detailsIntent = new Intent(this, cls);
        TaskStackBuilder.create(this).addNextIntentWithParentStack(detailsIntent).startActivities();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        //return true if exists
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.personal_data) {
            startNewActivity(LaunchingActivity.class);
        } else if (id == R.id.nav_contact) {
            startNewActivity(ContactsActivity.class);
        } else if (id == R.id.nav_settings) {
            startNewActivity(SettingsActivity.class);
            //  startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_send) {
            startNewActivity(SendMessageActivity.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
