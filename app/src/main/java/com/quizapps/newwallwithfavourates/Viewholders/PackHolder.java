package com.quizapps.newwallwithfavourates.Viewholders;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.quizapps.newwallwithfavourates.AppModels.Pack;
import com.quizapps.newwallwithfavourates.BuildConfig;
import com.quizapps.newwallwithfavourates.Intefaces.ButtonClickListener;
import com.quizapps.newwallwithfavourates.R;
import com.quizapps.newwallwithfavourates.WaProvider;

import java.io.File;

public class PackHolder extends RecyclerView.ViewHolder{

    //private final SimpleDraweeView wallpaperThumbnail;
    private final TextView PackName,count,size;

    private final SimpleDraweeView[] imageRowView;
    private final View holderview;
    private final LinearLayout add_bton;

    private PackHolder(View itemView) {
        super(itemView);
        holderview = itemView;

        PackName = itemView.findViewById(R.id.PackName);
        add_bton = itemView.findViewById(R.id.add_bton);
        size = itemView.findViewById(R.id.size);
        count = itemView.findViewById(R.id.count);
        imageRowView = new SimpleDraweeView[]{itemView.findViewById(R.id.image_1) ,itemView.findViewById(R.id.image_2) ,itemView.findViewById(R.id.image_3) ,itemView.findViewById(R.id.image_4)   };
    }

    @SuppressLint("DefaultLocale")
    public void bind(Pack pack, ButtonClickListener setfavbuttonlicklistner) {


        PackName.setText(pack.packname);
        size.setText(pack.filesizePretty);
        count.setText(String.format("%d", pack.getStickers().size()));


        for (int i = 0; i < 4; i++) {

                File imgfile = new File(holderview.getContext().getFilesDir()+ "/" + WaProvider.STICKERS_ASSET+"/"+pack.identifier+"/"+pack.getStickers().get(i).thumb);
                if (imgfile.exists()){
                    imageRowView[i].setImageURI(Uri.fromFile(imgfile));
                    System.out.println("loaded from File");
                }else {
                    imageRowView[i].setImageURI(BuildConfig.SERVER_PATH+pack.getIdentifier()+"/"+pack.getStickers().get(i).thumb);
                    System.out.println("loaded from url");
                }


        }

        holderview.setOnClickListener(v->{
            setfavbuttonlicklistner.onWallpaperButtonClicked(pack);

        });

        add_bton.setOnClickListener(v->{
            setfavbuttonlicklistner.onWallpaperButtonClicked(pack);

        });






    }

    static PackHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaperthumbholder, parent, false);
        return new PackHolder(view);
    }
}
