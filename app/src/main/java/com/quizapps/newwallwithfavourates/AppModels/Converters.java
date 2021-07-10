package com.quizapps.newwallwithfavourates.AppModels;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {


    @TypeConverter
    public static String fromStickerList(List<Sticker> stickers) {
        Gson gson = new Gson();
        return gson.toJson( stickers);
    }

    @TypeConverter
    public static List<Sticker> toStickerList(String value) {
        Type listType = new TypeToken<List<Sticker>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

}
