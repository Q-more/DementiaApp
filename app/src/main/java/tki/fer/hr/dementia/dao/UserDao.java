package tki.fer.hr.dementia.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import tki.fer.hr.dementia.models.User;

/**
 * Created by lucija on 31.01.18..
 */
@Dao
public interface UserDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(User... users);

    @Query("SELECT * FROM user WHERE firstName LIKE :firstName AND lastName LIKE :lastName")
    User getUser(String firstName, String lastName);

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT massage FROM user WHERE firstName LIKE :firstName AND lastName LIKE :lastName")
    String getMassage(String firstName, String lastName);

}
