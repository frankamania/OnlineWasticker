package com.quizapps.newwallwithfavourates.ui.Viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.quizapps.newwallwithfavourates.AppModels.Pack;
import com.quizapps.newwallwithfavourates.Datas.DataRespitory;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {


    private final DataRespitory mRepository;
    private final LiveData<List<Pack>> packs;


    public HomeViewModel(Application application) {
        super(application);

        mRepository = new DataRespitory(application);
        packs = mRepository.getAllPacks();
    }



    public LiveData<List<Pack>> getAllpacks() {
        return packs;
    }


    public void Update(Pack pack) {
        mRepository.Update(pack);
    }







}