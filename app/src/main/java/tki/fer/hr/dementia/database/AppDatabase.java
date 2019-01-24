package tki.fer.hr.dementia.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import tki.fer.hr.dementia.dao.ContactsDao;
import tki.fer.hr.dementia.dao.UserDao;
import tki.fer.hr.dementia.models.Contact;
import tki.fer.hr.dementia.models.User;


/**
 * Application database. Two tables User and Contact. 
 */

@Database(entities = {User.class, Contact.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "appDatabase.db";

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }

        return instance;
    }


    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract UserDao userDao();

    public abstract ContactsDao contactsDao();
}
