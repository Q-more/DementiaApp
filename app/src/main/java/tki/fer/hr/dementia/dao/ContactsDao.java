package tki.fer.hr.dementia.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import tki.fer.hr.dementia.models.Contact;

/**
 * Created by lucija on 01.03.18..
 */
@Dao
public interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContact(Contact... contacts);

    @Query("SELECT * FROM contacts WHERE name LIKE :name")
    Contact getContact(String name);

    @Query("SELECT * FROM contacts")
    List<Contact> getAllContacts();

    @Delete
    void delete(Contact contact);
}
