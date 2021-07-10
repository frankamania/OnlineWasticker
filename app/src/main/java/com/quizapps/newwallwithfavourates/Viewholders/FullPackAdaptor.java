package com.quizapps.newwallwithfavourates.Viewholders;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.quizapps.newwallwithfavourates.AppModels.Pack;
import com.quizapps.newwallwithfavourates.AppModels.Sticker;
import com.quizapps.newwallwithfavourates.Intefaces.ButtonClickListener;

public class FullPackAdaptor extends ListAdapter<Sticker, FullPackHolder> {

    String identifier;
    public FullPackAdaptor(@NonNull DiffUtil.ItemCallback<Sticker> diffCallback,String identifier) {
        super(diffCallback);
        this.identifier = identifier;
    }

    @Override
    public FullPackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return FullPackHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(FullPackHolder holder, int position) {
        Sticker current = getItem(position);
        holder.bind(current,identifier);
    }

    public static class WordDiff extends DiffUtil.ItemCallback<Sticker> {

        @Override
        public boolean areItemsTheSame(@NonNull Sticker oldItem, @NonNull Sticker newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Sticker oldItem, @NonNull Sticker newItem) {
            return oldItem.getThumb().equals(newItem.getThumb());
        }
    }
}