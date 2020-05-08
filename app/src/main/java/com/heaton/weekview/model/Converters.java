package com.heaton.weekview.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> list = new Gson().fromJson(value, listType);
        if (list==null) {
            return new ArrayList<String>();
        } else {
            return list;
        }
    }

    @TypeConverter
    public static String fromList(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<Boolean> booleanListFromString(String value) {
        Type listType = new TypeToken<List<Boolean>>() {}.getType();
        List<Boolean> list = new Gson().fromJson(value, listType);
        if (list==null) {
            return new ArrayList<Boolean>();
        } else {
            return list;
        }
    }

    @TypeConverter
    public static String strfromBooleanList(List<Boolean> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}