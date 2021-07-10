package com.quizapps.newwallwithfavourates.ui.Viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.quizapps.newwallwithfavourates.AppModels.Pack;
import com.quizapps.newwallwithfavourates.Datas.DataRespitory;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {


    private final DataRespitory mRepository;
    private final LiveData<List<Pack>> packs;


    public DashboardViewModel(Application application) {
        super(application);

        mRepository = new DataRespitory(application);
        packs = mRepository.getAllfavPacks();
    }

    public LiveData<List<Pack>> getAllpacks() {
        return packs;
    }


    public void Update(Pack pack) {
        mRepository.Update(pack);
    }







}