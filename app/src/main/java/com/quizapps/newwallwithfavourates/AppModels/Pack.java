package com.quizapps.newwallwithfavourates.AppModels;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
@Entity(indices = { @Index(value = { "id"},unique = true) })
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class Pack  implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("identifier")
    @ColumnInfo(name = "identifier")
    @Expose
    public String identifier;

    @SerializedName("name")
    @ColumnInfo(name = "packname")
    @Expose
    public String packname;

    @SerializedName("tray_image_file")
    @ColumnInfo(name = "trayImageFile")
    @Expose
    public String trayImageFile;

    @TypeConverters(Converters.class)
    @SerializedName("stickers")
    @Expose
    public List<Sticker> stickers;


    @NonNull
    @SerializedName("filesize_bytes")
    @ColumnInfo(name = "filesizebytes",defaultValue = "100000")
    public Integer filesizeBytes;

    @NonNull
    @SerializedName("filesize_pretty")
    @ColumnInfo(name = "filesizepretty",defaultValue = "100kb")
    public String filesizePretty;

    @NonNull
    @SerializedName("image_data_version")
    @ColumnInfo(name = "imageDataVersion",defaultValue = "1" )
    @Expose
    public String imageDataVersion;

    @NonNull
    @SerializedName("avoid_cache")
    @ColumnInfo(name = "avoidCache",defaultValue = "false" )
    @Expose
    public Boolean avoidCache;

    @NonNull
    @SerializedName("animated_sticker_pack")
    @ColumnInfo(name = "animatedStickerPack",defaultValue = "false" )
    @Expose
    public Boolean animatedStickerPack;

    @NonNull
    @ColumnInfo(name = "isfavourate",defaultValue = "false" )
    public Boolean isfavourate;

    @NonNull
    @SerializedName("ispopular")
    @ColumnInfo(name = "ispopular",defaultValue = "false" )
    public Boolean ispopular;

    @NonNull
    @SerializedName("istrending")
    @ColumnInfo(name = "istrending",defaultValue = "false" )
    public Boolean istrending;





    public Pack() {
        filesizeBytes = 100000;
        filesizePretty = "100kb";
        imageDataVersion = "1";
        avoidCache = false;
        animatedStickerPack = false;
        isfavourate = false;
        ispopular = false;
        istrending = false;
    }

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return packname;
    }

    public String getTrayImageFile() {

        return trayImageFile;
    }

    public List<Sticker> getStickers() {
        return stickers;
    }

    @NonNull
    public Integer getFilesizeBytes() {
        return filesizeBytes;
    }

    @NonNull
    public String getFilesizePretty() {
        return filesizePretty;
    }

    @NonNull
    public String getImageDataVersion() {
        return imageDataVersion;
    }

    @NonNull
    public Boolean getAvoidCache() {
        return avoidCache;
    }

    @NonNull
    public Boolean getAnimatedStickerPack() {
        return animatedStickerPack;
    }

    @NonNull
    public Boolean getIsfavourate() {
        return isfavourate;
    }

    @NonNull
    public Boolean getIspopular() {
        return ispopular;
    }

    @NonNull
    public Boolean getIstrending() {
        return istrending;
    }


    protected Pack(Parcel in) {
        identifier = in.readString();
        packname = in.readString();
        trayImageFile = in.readString();
        if (in.readByte() == 0x01) {
            stickers = new ArrayList<Sticker>();
            in.readList(stickers, Sticker.class.getClassLoader());
        } else {
            stickers = null;
        }
        filesizeBytes = in.readByte() == 0x00 ? null : in.readInt();
        filesizePretty = in.readString();
        imageDataVersion = in.readString();
        byte avoidCacheVal = in.readByte();
        avoidCache = avoidCacheVal == 0x02 ? null : avoidCacheVal != 0x00;
        byte animatedStickerPackVal = in.readByte();
        animatedStickerPack = animatedStickerPackVal == 0x02 ? null : animatedStickerPackVal != 0x00;
        byte isfavourateVal = in.readByte();
        isfavourate = isfavourateVal == 0x02 ? null : isfavourateVal != 0x00;
        byte ispopularVal = in.readByte();
        ispopular = ispopularVal == 0x02 ? null : ispopularVal != 0x00;
        byte istrendingVal = in.readByte();
        istrending = istrendingVal == 0x02 ? null : istrendingVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identifier);
        dest.writeString(packname);
        dest.writeString(trayImageFile);
        if (stickers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(stickers);
        }
        if (filesizeBytes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(filesizeBytes);
        }
        dest.writeString(filesizePretty);
        dest.writeString(imageDataVersion);
        if (avoidCache == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (avoidCache ? 0x01 : 0x00));
        }
        if (animatedStickerPack == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (animatedStickerPack ? 0x01 : 0x00));
        }
        if (isfavourate == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isfavourate ? 0x01 : 0x00));
        }
        if (ispopular == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (ispopular ? 0x01 : 0x00));
        }
        if (istrending == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (istrending ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Pack> CREATOR = new Parcelable.Creator<Pack>() {
        @Override
        public Pack createFromParcel(Parcel in) {
            return new Pack(in);
        }

        @Override
        public Pack[] newArray(int size) {
            return new Pack[size];
        }
    };




}