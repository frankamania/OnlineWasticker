package com.quizapps.newwallwithfavourates.ui.Viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.quizapps.newwallwithfavourates.AppModels.Pack;
import com.quizapps.newwallwithfavourates.Datas.DataRespitory;

public class StickerPackViewModel extends AndroidViewModel {


    private final DataRespitory mRepository;
    private final LiveData<Pack> packs;


    public StickerPackViewModel(Application application,String id) {
        super(application);

        mRepository = new DataRespitory(application);
        packs = mRepository.GetOne(id);
    }

    public LiveData<Pack> getpack() {
        return packs;
    }


    public void Update(Pack pack) {
        mRepository.Update(pack);
    }







}