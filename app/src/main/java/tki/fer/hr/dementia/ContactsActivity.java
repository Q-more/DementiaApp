package tki.fer.hr.dementia;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tki.fer.hr.dementia.database.AppDatabase;
import tki.fer.hr.dementia.models.Contact;
import tki.fer.hr.dementia.utility.AsyncTaskHelper;
import tki.fer.hr.dementia.utility.ContactPicker;
import tki.fer.hr.dementia.utility.PermissionProvider;

import static tki.fer.hr.dementia.utility.ContactPicker.RESULT_PICK_CONTACT;

public class ContactsActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private ContactsListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    private final AppDatabase appDatabase = App.getDatabase();
    List<ContactPicker.ContactDetails> contactList = new ArrayList<>();
    private boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (!PermissionProvider.checkContactPermission(this)) {
                Toast.makeText(
                        this,
                        "Dozvolite pristup kontaktima u postavkama telefona",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }
            startActivityForResult(ContactPicker.pickContactIntent(), RESULT_PICK_CONTACT);
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        mAdapter = new ContactsListAdapter(this, contactList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        prepareList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContactPicker.ContactDetails contactDetails = ContactPicker.contactPicked(data, getContentResolver());

        if (contactDetails != null) {
            mAdapter.addItem(contactDetails, mAdapter.getItemCount());
        }
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ContactsListAdapter.MyViewHolder) {
            // backup of removed item for undo purpose
            final ContactPicker.ContactDetails deletedItem = mAdapter.getItem(viewHolder.getAdapterPosition());
            String name = deletedItem.getName();

            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition(), deletedItem);

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout,"Kontakt " + name + " uklonjen s popisa!", Snackbar.LENGTH_LONG);
            snackbar.setAction("Vrati", view -> {

                // undo is selected, restore the deleted item
                mAdapter.restoreItem(deletedItem, deletedIndex);
            });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
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

    private void prepareList() {
        AsyncTaskHelper.instance().runOnBackground(() -> {
            List<Contact> allContacts = appDatabase.contactsDao().getAllContacts();
            if (allContacts == null) {
                return;
            }
            converter(allContacts);
        });
    }

    private void converter(List<Contact> contacts) {
        if (flag) return;
        contactList.clear();
        for (Contact contact : contacts) {
            contactList.add(new ContactPicker.ContactDetails(contact.name, contact.phoneNumber, contact.photo));
            Log.w("CONTACT", contact.getName());
        }

        mAdapter.notifyDataSetChanged();
        flag = true;
    }

}
