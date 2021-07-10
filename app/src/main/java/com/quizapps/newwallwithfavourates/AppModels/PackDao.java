package com.quizapps.newwallwithfavourates.AppModels;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PackDao {
    @Query("SELECT * FROM pack")
    LiveData<List<Pack>> getAllPacks();

    @Query("SELECT * FROM pack")
    List<Pack> ContentPacks();


    @Query("SELECT * FROM pack WHERE isfavourate = 1")
    LiveData<List<Pack>> loadAlFavourates();

    @Query("SELECT * FROM pack WHERE ispopular = 1")
    LiveData<List<Pack>> loadAlPopular();

    @Query("SELECT * FROM pack WHERE istrending = 1")
    LiveData<List<Pack>> loadAlTrending();

    @Query("SELECT * FROM pack WHERE animatedStickerPack = 1")
    LiveData<List<Pack>> loadAlAnimated();

    @Query("SELECT * FROM pack  WHERE identifier = :id")
    LiveData<Pack> getOne(String id);



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Pack... packs);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertone(Pack pack);


    @Update(onConflict = OnConflictStrategy.IGNORE)
    void Updateone(Pack pack);

    @Delete
    void delete(Pack pack);
}