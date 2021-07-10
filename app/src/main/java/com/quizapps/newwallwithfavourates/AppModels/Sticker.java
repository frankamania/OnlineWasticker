package com.quizapps.newwallwithfavourates.AppModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

@Entity(indices = { @Index(value = { "id"},unique = true) })
public class Sticker {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("image_file")
    @ColumnInfo(name = "image_file")
    @Expose
    public String imageFile;


    @SerializedName("thumb")
    @ColumnInfo(name = "thumb")
    @Expose
    public String thumb;

    public String getImageFile() {
        return imageFile;
    }


    public String getThumb() {
        return thumb;
    }

    public Sticker() {

    }

    public List<String> getEmojis(){
        return Arrays.asList("☕", "🙂", "💕");
    }
}