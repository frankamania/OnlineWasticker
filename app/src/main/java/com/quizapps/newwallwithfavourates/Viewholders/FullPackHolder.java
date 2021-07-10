package com.quizapps.newwallwithfavourates.Viewholders;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.quizapps.newwallwithfavourates.AppModels.Sticker;
import com.quizapps.newwallwithfavourates.BuildConfig;
import com.quizapps.newwallwithfavourates.R;
import com.quizapps.newwallwithfavourates.WaProvider;

import java.io.File;

public class FullPackHolder extends RecyclerView.ViewHolder{

    private final SimpleDraweeView imageRowView;

    private FullPackHolder(View itemView) {
        super(itemView);

        imageRowView = itemView.findViewById(R.id.image_1);
    }

    public void bind(Sticker pack,String identifier) {



        File imgfile = new File(itemView.getContext().getFilesDir()+ "/" + WaProvider.STICKERS_ASSET+"/"+identifier+"/"+pack.thumb);
        if (imgfile.exists()){
            imageRowView.setImageURI(Uri.fromFile(imgfile));

            System.out.println("loaded from File");
        }else {
            imageRowView.setImageURI(BuildConfig.SERVER_PATH+identifier+"/"+pack.thumb);
            System.out.println("loaded from url");
        }


    }

    static FullPackHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_packs_list_image_item, parent, false);
        return new FullPackHolder(view);
    }
}
