package tki.fer.hr.dementia.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import lombok.Data;

/**
 * Created by lucija on 01.03.18..
 */

@Entity(tableName = "contacts")
@Data
public class    Contact {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String phoneNumber;
    public String photo;

    public Contact() {
    }

    @Ignore
    public Contact(String name, String phoneNumber, String photo) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
    }

}
