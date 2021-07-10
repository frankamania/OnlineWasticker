package com.quizapps.newwallwithfavourates.ui.frags;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appodeal.ads.Appodeal;
import com.quizapps.newwallwithfavourates.AppModels.Pack;
import com.quizapps.newwallwithfavourates.Intefaces.ButtonClickListener;
import com.quizapps.newwallwithfavourates.R;
import com.quizapps.newwallwithfavourates.Viewholders.PackAdaptor;
import com.quizapps.newwallwithfavourates.databinding.FragmentDashboardBinding;
import com.quizapps.newwallwithfavourates.ui.Activitys.StickerPack;
import com.quizapps.newwallwithfavourates.ui.Viewmodels.DashboardViewModel;

import java.util.Objects;

public class DashboardFragment extends Fragment implements ButtonClickListener {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Appodeal.setBannerViewId(R.id.appodealBannerView);
        Appodeal.show(requireActivity(), Appodeal.BANNER_VIEW);


        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        final PackAdaptor adapter = new PackAdaptor(new PackAdaptor.WordDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.getAllpacks().observe(getViewLifecycleOwner(), adapter::submitList);

        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Appodeal.isLoaded(Appodeal.BANNER)){
            Appodeal.show(requireActivity(), Appodeal.BANNER_VIEW);
        }

    }



    @Override
    public void onWallpaperButtonClicked(Pack pack) {
        Intent i = new Intent(getContext(), StickerPack.class);
        i.putExtra("pack_id",  pack.getIdentifier());
        startActivity(i);
    }
}