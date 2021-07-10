package com.quizapps.newwallwithfavourates.ui.Activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.facebook.drawee.view.SimpleDraweeView;
import com.quizapps.newwallwithfavourates.AppModels.Pack;
import com.quizapps.newwallwithfavourates.AppModels.Sticker;
import com.quizapps.newwallwithfavourates.BuildConfig;
import com.quizapps.newwallwithfavourates.Intefaces.ButtonClickListener;
import com.quizapps.newwallwithfavourates.R;
import com.quizapps.newwallwithfavourates.Viewholders.FullPackAdaptor;
import com.quizapps.newwallwithfavourates.WaProvider;
import com.quizapps.newwallwithfavourates.WhatsappStickers.WhitelistCheck;
import com.quizapps.newwallwithfavourates.databinding.ActivityStickerPackBinding;
import com.quizapps.newwallwithfavourates.ui.Viewmodels.MyViewModelFactory;
import com.quizapps.newwallwithfavourates.ui.Viewmodels.StickerPackViewModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class StickerPack extends AddStickerPackActivity{

    private StickerPackViewModel stickerPackViewModel;
    private ActivityStickerPackBinding binding;
    private String identifier;
    ImageButton setfav;

    ProgressDialog progressDialog;

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityStickerPackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        identifier = getIntent().getExtras().getString("pack_id");





        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final FullPackAdaptor adapter = new FullPackAdaptor(new FullPackAdaptor.WordDiff(), identifier);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        SimpleDraweeView image = findViewById(R.id.image_1);
        LinearLayout add_pack = findViewById(R.id.add_pack);

        TextView PackName  = findViewById(R.id.PackName);
        TextView size  = findViewById(R.id.size);
        TextView count  = findViewById(R.id.count);
        TextView added_pack_text  = findViewById(R.id.add_pack_text);
        setfav = findViewById(R.id.set_fav);


        Appodeal.setBannerViewId(R.id.appodealBannerView);

        if (Appodeal.isLoaded(Appodeal.BANNER)){
            Appodeal.show(this, Appodeal.BANNER_VIEW);
        }






        stickerPackViewModel = new ViewModelProvider(this,new MyViewModelFactory(this.getApplication(), identifier)).get(StickerPackViewModel.class);

        stickerPackViewModel.getpack().observe(this, pack -> {
            adapter.submitList(pack.getStickers());

            image.setImageURI(BuildConfig.SERVER_PATH+pack.getIdentifier()+"/"+pack.trayImageFile);
            PackName.setText(pack.packname);
            size.setText(pack.getFilesizePretty());
            count.setText(String.format("%d", pack.getStickers().size()));


            if(WhitelistCheck.isWhitelisted(getApplicationContext(),pack.identifier)){
                added_pack_text.setText("Pack is Already Added");
                add_pack.setOnClickListener(null);
            }else {
                added_pack_text.setText("+ Add To Whatsapp");


                add_pack.setOnClickListener(v-> {

                    addToWa(pack);
                    Appodeal.show(this, Appodeal.INTERSTITIAL);
                });
            }








            if(pack.getIsfavourate()){
                setfav.setImageResource(R.drawable.favourate_btn_img_clicked);
            }else {
                setfav.setImageResource(R.drawable.favourate_btn_img_notclicked);
            }


            setfav.setOnClickListener(v -> {
                pack.isfavourate = !pack.isfavourate;
               stickerPackViewModel.Update(pack);

                Appodeal.show(this, Appodeal.INTERSTITIAL);
            });





            //WhitelistCheck.isWhitelisted(this, pack.getIdentifier());


            //addStickerPackToWhatsApp(stickerPack.getIdentifier(), stickerPack.getName());





        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Appodeal.isLoaded(Appodeal.BANNER)){
            Appodeal.show(this, Appodeal.BANNER_VIEW);
        }

    }

    public boolean CheckIfallFilesAvoilable(Pack pack){

        if(!new File(getFilesDir()+ "/" + WaProvider.STICKERS_ASSET+"/"+pack.identifier+"/"+pack.trayImageFile).exists()){
            return false;
        }

        for (Sticker stickers: pack.getStickers()) {

            if(!new File(getFilesDir()+ "/" + WaProvider.STICKERS_ASSET+"/"+pack.identifier+"/"+stickers.imageFile).exists()){
                return false;
            }
        }


        return true;
    }

    public void addToWa(Pack pack){

        if(CheckIfallFilesAvoilable(pack)){


            //WhitelistCheck.isWhitelisted(this, pack.getIdentifier())
            addStickerPackToWhatsApp(pack.identifier, pack.packname);

        }else {
            try {
                progressDialog = new ProgressDialog(StickerPack.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.setTitle("featching Stickers");
                progressDialog.setMessage("featching Stickers From Server");
                progressDialog.show();
            }catch (Exception ignored){

            }


            downloadAllPack(pack, pack1 -> {


                try {
                    progressDialog.dismiss();
                }catch (Exception ignored){

                }

                addToWa(pack);
            });
        }

        //File Base_path = new File(getFilesDir() + "/" + WaProvider.STICKERS_ASSET + "/" + );
    }



    public void downloadAllPack(Pack pack, ButtonClickListener buttonClickListener){

        //ProgressBar progressBar = new ProgressBar(getApplicationContext());

        File Base_path = new File(getFilesDir() + "/" + WaProvider.STICKERS_ASSET );
        if(!Base_path.exists()){
            Base_path.mkdirs();
        }


        PRDownloader.download(BuildConfig.SERVER_PATH+pack.getIdentifier()+".zip", String.valueOf(Base_path), pack.getIdentifier()+".zip").build().start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                System.out.println(BuildConfig.SERVER_PATH+pack.getIdentifier()+".zip"+" ---> "+ Base_path+"/"+pack.getIdentifier()+".zip");


                try {
                    unzip(new File(Base_path,pack.getIdentifier()+".zip"), Base_path,buttonClickListener,pack);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Error error) {

                System.out.println(BuildConfig.SERVER_PATH+pack.getIdentifier()+".zip"+" xxxx  "+ Base_path+"/"+pack.getIdentifier()+".zip"+" -->"+error.getServerErrorMessage());
            }
        });



    }

    public static void unzip(File zipFile, File targetDirectory, ButtonClickListener buttonClickListener,Pack pack) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }

        buttonClickListener.onWallpaperButtonClicked(pack);
    }
}