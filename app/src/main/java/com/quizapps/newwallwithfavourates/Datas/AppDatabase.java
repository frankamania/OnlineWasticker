package com.quizapps.newwallwithfavourates.Datas;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.gson.Gson;
import com.quizapps.newwallwithfavourates.AppModels.Converters;
import com.quizapps.newwallwithfavourates.AppModels.Pack;
import com.quizapps.newwallwithfavourates.AppModels.PackDao;
import com.quizapps.newwallwithfavourates.AppModels.Sticker;
import com.quizapps.newwallwithfavourates.BuildConfig;
import com.quizapps.newwallwithfavourates.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Pack.class, Sticker.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract PackDao packDao();



    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {



                    INSTANCE = Room.databaseBuilder(context,AppDatabase.class, "Packdatabase").allowMainThreadQueries().addCallback(


                            new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);


                                    databaseWriteExecutor.execute(() -> {
                                        // Populate the database in the background.
                                        // If you want to start with more words, just add them.
                                        PackDao dao = INSTANCE.packDao();
                                        dao.insertAll(getwallpaperList(context));
                                    });
                                }}


                    ).build();
                }
            }
        }
        return INSTANCE;
    }




    public static String inputStremeToString(InputStream is)throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is , "utf-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        return writer.toString();

    }

    public static String getjsonString(int resourceid, Context context) throws IOException {

        InputStream is = context.getResources().openRawResource(resourceid);

        return inputStremeToString(is);




    }

    public static Pack[] getwallpaperList(Context context){
        Pack[] wallpapers = new Pack[0];
        /*
        File Wallpaperjsonfile  = new File(context.getFilesDir()+"/"+"wallpapers/", "pack.json");


        if( Wallpaperjsonfile.exists()){

            try {
                String jsonString = inputStremeToString(context.getContentResolver().openInputStream( FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID +".provider", Wallpaperjsonfile)));
                wallpapers = new Gson().fromJson( jsonString, Pack[].class);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else {

            try {
                wallpapers = new Gson().fromJson(getjsonString(R.raw.pack,context), Pack[].class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

         */

        try {
            wallpapers = new Gson().fromJson(getjsonString(R.raw.pack,context), Pack[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return  wallpapers;

    }

}