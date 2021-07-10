/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.quizapps.newwallwithfavourates;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.quizapps.newwallwithfavourates.AppModels.Pack;
import com.quizapps.newwallwithfavourates.AppModels.Sticker;
import com.quizapps.newwallwithfavourates.Datas.AppDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.quizapps.newwallwithfavourates.Constants.android_play_store_link;
import static com.quizapps.newwallwithfavourates.Constants.publisher;
import static com.quizapps.newwallwithfavourates.Constants.stickerPack_licenseAgreementWebsite;
import static com.quizapps.newwallwithfavourates.Constants.stickerPack_privacyPolicyWebsite;
import static com.quizapps.newwallwithfavourates.Constants.stickerPack_publisherEmail;
import static com.quizapps.newwallwithfavourates.Constants.stickerPack_publisherWebsite;
import static com.quizapps.newwallwithfavourates.Datas.AppDatabase.getjsonString;


public class WaProvider extends ContentProvider {

    public static final String STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier";
    public static final String STICKER_PACK_NAME_IN_QUERY = "sticker_pack_name";
    public static final String STICKER_PACK_PUBLISHER_IN_QUERY = "sticker_pack_publisher";
    public static final String STICKER_PACK_ICON_IN_QUERY = "sticker_pack_icon";
    public static final String ANDROID_APP_DOWNLOAD_LINK_IN_QUERY = "android_play_store_link";
    public static final String IOS_APP_DOWNLOAD_LINK_IN_QUERY = "ios_app_download_link";
    public static final String PUBLISHER_EMAIL = "sticker_pack_publisher_email";
    public static final String PUBLISHER_WEBSITE = "sticker_pack_publisher_website";
    public static final String PRIVACY_POLICY_WEBSITE = "sticker_pack_privacy_policy_website";
    public static final String LICENSE_AGREENMENT_WEBSITE = "sticker_pack_license_agreement_website";
    public static final String IMAGE_DATA_VERSION = "image_data_version";
    public static final String AVOID_CACHE = "whatsapp_will_not_cache_stickers";
    public static final String ANIMATED_STICKER_PACK = "animated_sticker_pack";

    public static final String STICKER_FILE_NAME_IN_QUERY = "sticker_file_name";
    public static final String STICKER_FILE_EMOJI_IN_QUERY = "sticker_emoji";


    public static final Uri AUTHORITY_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath(WaProvider.METADATA).build();

    /**
     * Do not change the values in the UriMatcher because otherwise, WhatsApp will not be able to fetch the stickers from the ContentProvider.
     */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String METADATA = "metadata";
    private static final int METADATA_CODE = 1;

    private static final int METADATA_CODE_FOR_SINGLE_PACK = 2;

    static final String STICKERS = "stickers";
    private static final int STICKERS_CODE = 3;

    public static final String STICKERS_ASSET = "stickers_asset";
    private static final int STICKERS_ASSET_CODE = 4;

    private static final int STICKER_PACK_TRAY_ICON_CODE = 5;




    private List<Pack> stickerPackList;

    @Override
    public boolean onCreate() {



        final String authority = BuildConfig.CONTENT_PROVIDER_AUTHORITY;
        if (!authority.startsWith(Objects.requireNonNull(getContext()).getPackageName())) {
            throw new IllegalStateException("your authority (" + authority + ") for the content provider should start with your package name: " + getContext().getPackageName());
        }


        //the call to get the metadata for the sticker packs.
        MATCHER.addURI(authority, METADATA, METADATA_CODE);

        //the call to get the metadata for single sticker pack. * represent the identifier
        MATCHER.addURI(authority, METADATA + "/*", METADATA_CODE_FOR_SINGLE_PACK);

        //gets the list of stickers for a sticker pack, * respresent the identifier.
        MATCHER.addURI(authority, STICKERS + "/*", STICKERS_CODE);


        for (Pack stickerPack : getStickerPackList()) {
            MATCHER.addURI(authority, STICKERS_ASSET + "/" + stickerPack.getIdentifier() + "/" + stickerPack.getTrayImageFile(), STICKER_PACK_TRAY_ICON_CODE);
            for (Sticker sticker : stickerPack.getStickers()) {
                MATCHER.addURI(authority, STICKERS_ASSET + "/" + stickerPack.getIdentifier() + "/" + sticker.imageFile, STICKERS_ASSET_CODE);
            }
        }





        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == METADATA_CODE) {
            return getPackForAllStickerPacks(uri);
        } else if (code == METADATA_CODE_FOR_SINGLE_PACK) {
            return getCursorForSingleStickerPack(uri);
        } else if (code == STICKERS_CODE) {
            return getStickersForAStickerPack(uri);
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        final int matchCode = MATCHER.match(uri);
        if (matchCode == STICKERS_ASSET_CODE || matchCode == STICKER_PACK_TRAY_ICON_CODE) {
            return new AssetFileDescriptor(ParcelFileDescriptor.open(getImageAsset(uri), ParcelFileDescriptor.MODE_READ_ONLY), 0, AssetFileDescriptor.UNKNOWN_LENGTH);
        }
        return null;
    }



    @Override
    public String getType(@NonNull Uri uri) {
        final int matchCode = MATCHER.match(uri);
        switch (matchCode) {
            case METADATA_CODE:
                return "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case METADATA_CODE_FOR_SINGLE_PACK:
                return "vnd.android.cursor.item/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case STICKERS_CODE:
                return "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + STICKERS;
            case STICKERS_ASSET_CODE:
                return "image/webp";
            case STICKER_PACK_TRAY_ICON_CODE:
                return "image/png";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }



    private List<Pack> getStickerPackList() {
        if (stickerPackList == null) {
            try {
                stickerPackList = Arrays.asList(new Gson().fromJson(getjsonString(R.raw.pack,getContext()), Pack[].class));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stickerPackList;


    }

    private Cursor getPackForAllStickerPacks(@NonNull Uri uri) {
        return getStickerPackInfo(uri, getStickerPackList());
    }

    private Cursor getCursorForSingleStickerPack(@NonNull Uri uri) {
        final String identifier = uri.getLastPathSegment();
        for (Pack stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.getIdentifier())) {

                ArrayList<Pack> myas = new ArrayList<>();
                myas.add(stickerPack);

                return getStickerPackInfo(uri, myas);
            }
        }

        return getStickerPackInfo(uri, new ArrayList<>());
    }

    @NonNull
    private Cursor getStickerPackInfo(@NonNull Uri uri, @NonNull List<Pack> stickerPackList) {
        MatrixCursor cursor = new MatrixCursor(

                new String[]{
                        STICKER_PACK_IDENTIFIER_IN_QUERY,
                        STICKER_PACK_NAME_IN_QUERY,
                        STICKER_PACK_PUBLISHER_IN_QUERY,
                        STICKER_PACK_ICON_IN_QUERY,
                        ANDROID_APP_DOWNLOAD_LINK_IN_QUERY,
                        IOS_APP_DOWNLOAD_LINK_IN_QUERY,
                        PUBLISHER_EMAIL,
                        PUBLISHER_WEBSITE,
                        PRIVACY_POLICY_WEBSITE,
                        LICENSE_AGREENMENT_WEBSITE,
                        IMAGE_DATA_VERSION,
                        AVOID_CACHE,
                        ANIMATED_STICKER_PACK,
                });
        for (Pack stickerPack : stickerPackList) {
            MatrixCursor.RowBuilder builder = cursor.newRow();
            builder.add(stickerPack.getIdentifier());
            builder.add(stickerPack.packname);
            builder.add(publisher);
            builder.add(stickerPack.trayImageFile);
            builder.add(android_play_store_link);
            builder.add("");
            builder.add(stickerPack_publisherEmail);
            builder.add(stickerPack_publisherWebsite);
            builder.add(stickerPack_privacyPolicyWebsite);
            builder.add(stickerPack_licenseAgreementWebsite);
            builder.add(stickerPack.imageDataVersion);
            builder.add(stickerPack.avoidCache ? 1 : 0);
            builder.add(stickerPack.animatedStickerPack ? 1 : 0);




        }
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @NonNull
    private Cursor getStickersForAStickerPack(@NonNull Uri uri) {
        final String identifier = uri.getLastPathSegment();
        MatrixCursor cursor = new MatrixCursor(new String[]{STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY});
        for (Pack stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.getIdentifier())) {
                for (Sticker sticker : stickerPack.getStickers()) {
                    cursor.addRow(new Object[]{sticker.imageFile, TextUtils.join(",", sticker.getEmojis())});
                }
            }
        }
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    private File getImageAsset(Uri uri) throws IllegalArgumentException {

        final List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() != 3) {
            throw new IllegalArgumentException("path segments should be 3, uri is: " + uri);
        }
        String fileName = pathSegments.get(pathSegments.size() - 1);
        final String identifier = pathSegments.get(pathSegments.size() - 2);

        File packfile = new File(getContext().getFilesDir()+ "/"+STICKERS_ASSET +"/" +identifier + "/" + fileName);
        if (TextUtils.isEmpty(identifier)) {
            throw new IllegalArgumentException("identifier is empty, uri: " + uri);
        }
        if (TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("file name is empty, uri: " + uri);
        }
        //making sure the file that is trying to be fetched is in the list of stickers.
        for (Pack stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.getIdentifier())) {
                if (fileName.equals(stickerPack.getTrayImageFile())) {
                    return packfile;
                } else {
                    for (Sticker sticker : stickerPack.getStickers()) {
                        if (fileName.equals(sticker.imageFile)) {
                            return packfile;
                        }
                    }
                }
            }
        }
        return null;
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }







}
