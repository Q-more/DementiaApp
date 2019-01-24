package tki.fer.hr.dementia.utility;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Created by lucija on 21.02.18..
 */

public class SetConverter {

    private static final Type type = new TypeToken<Set<String>>() {
    }.getType();

    @TypeConverter
    public static Set<String> returnSet(String number) {
        return new Gson().fromJson(number, type);
    }

    @TypeConverter
    public static String returnNumber(Set<String> set) {
        return new Gson().toJson(set, type);
    }
}
