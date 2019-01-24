package tki.fer.hr.dementia.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Table entity User. Represents app user.
 */

@Entity(tableName = "user")
@Data
@NoArgsConstructor
public class User {

    @PrimaryKey
    public int id;
    public String firstName;
    public String lastName;
    public String address;

    public String massage;


    public User(String firstName, String lastName, String address) {
        this(firstName, lastName);
        this.address = address;
    }

    @Ignore
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        massage = "Imam napad panike i trenutno se nalazim ovdje: ";
    }


}
