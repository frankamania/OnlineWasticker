package com.quizapps.newwallwithfavourates.ui.Viewmodels;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;




public class MyViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final String id;


    public MyViewModelFactory(Application application, String param) {
        mApplication = application;
        id = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new StickerPackViewModel(mApplication, id);
    }
}