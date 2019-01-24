package tki.fer.hr.dementia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import io.reactivex.schedulers.Schedulers;
import tki.fer.hr.dementia.database.AppDatabase;
import tki.fer.hr.dementia.models.Contact;
import tki.fer.hr.dementia.utility.AsyncTaskHelper;
import tki.fer.hr.dementia.utility.ContactPicker;
import io.reactivex.Observable;

/**
 * Created by lucija on 02.03.18..
 */

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.MyViewHolder> {
    private final AppDatabase appDatabase = App.getDatabase();

    @lombok.NonNull
    private Context context;
    private final List<ContactPicker.ContactDetails> contactList;


    public ContactsListAdapter(Context context, List<ContactPicker.ContactDetails> contactList) {
        this.contactList = contactList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phoneNumber;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phoneNumber = view.findViewById(R.id.number);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ContactPicker.ContactDetails item = contactList.get(position);
        holder.name.setText(item.getName());
        holder.phoneNumber.setText(item.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void removeItem(int position, ContactPicker.ContactDetails item) {
        contactList.remove(position);

        AsyncTaskHelper.instance().runOnBackground(()->{
            Contact contact = appDatabase.contactsDao().getContact(item.getName());
            appDatabase.contactsDao().delete(contact);
        });

        notifyItemRemoved(position);
    }

    public void restoreItem(ContactPicker.ContactDetails item, int position) {
        addItem(item, position);
    }

    private Contact getContact(ContactPicker.ContactDetails item) {
        return new Contact(item.getName(), item.getPhoneNumber(), item.getPhoto());
    }

    public void addItem(ContactPicker.ContactDetails contactDetails, int position) {
        if (contactList.contains(contactDetails)) {
            return;
        }
        contactList.add(position, contactDetails);
        notifyItemInserted(position);

        Contact contact = getContact(contactDetails);
        AsyncTaskHelper.instance().runOnBackground(()->{
            appDatabase.contactsDao().insertContact(contact);
        });
    }

    public ContactPicker.ContactDetails getItem(int i) {
        return contactList.get(i);
    }
}
