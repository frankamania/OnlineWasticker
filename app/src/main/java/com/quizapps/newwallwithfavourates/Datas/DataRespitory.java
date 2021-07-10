package com.quizapps.newwallwithfavourates.Datas;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.quizapps.newwallwithfavourates.AppModels.Pack;
import com.quizapps.newwallwithfavourates.AppModels.PackDao;

import java.util.List;

public class DataRespitory {


    private final PackDao packDao;
    private final LiveData<List<Pack>> Allpacks;
    private final LiveData<List<Pack>> favpacks;

    private final LiveData<List<Pack>> Animatedpacks;



    public DataRespitory(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        packDao = db.packDao();
        Allpacks = packDao.getAllPacks();
        favpacks = packDao.loadAlFavourates();
        Animatedpacks = packDao.loadAlAnimated();
        //Randomwallpapers= Collections.shuffle(wallpaperDao.getAllWallpapers())

    }

    public LiveData<List<Pack>> getAllPacks() {
        return Allpacks;
    }

    public LiveData<List<Pack>> getAllfavPacks() {
        return favpacks;
    }

    public LiveData<List<Pack>> getAllAnimatedpacks() {
        return Animatedpacks;
    }

    public void insert(Pack pack) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            packDao.insertone(pack);
        });
    }


    public void Update(Pack pack) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            packDao.Updateone(pack);
        });
    }

    public LiveData<Pack> GetOne(String id) {

        return packDao.getOne(id);
    }




}
