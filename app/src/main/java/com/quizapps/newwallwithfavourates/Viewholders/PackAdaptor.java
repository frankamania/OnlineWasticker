package com.quizapps.newwallwithfavourates.Viewholders;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.quizapps.newwallwithfavourates.AppModels.Pack;
import com.quizapps.newwallwithfavourates.Intefaces.ButtonClickListener;

public class PackAdaptor extends ListAdapter<Pack, PackHolder> {

    private final ButtonClickListener setfavbuttonlicklistner;
    public PackAdaptor(@NonNull DiffUtil.ItemCallback<Pack> diffCallback, ButtonClickListener listener) {
        super(diffCallback);
        this.setfavbuttonlicklistner = listener;
    }

    @Override
    public PackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PackHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(PackHolder holder, int position) {
        Pack current = getItem(position);
        holder.bind(current,setfavbuttonlicklistner);
    }

    public static class WordDiff extends DiffUtil.ItemCallback<Pack> {

        @Override
        public boolean areItemsTheSame(@NonNull Pack oldItem, @NonNull Pack newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Pack oldItem, @NonNull Pack newItem) {
            return oldItem.getIdentifier().equals(newItem.getIdentifier());
        }
    }
}