

package com.quizapps.newwallwithfavourates.ui.Activitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.quizapps.newwallwithfavourates.BuildConfig;
import com.quizapps.newwallwithfavourates.R;
import com.quizapps.newwallwithfavourates.WhatsappStickers.WhitelistCheck;




public abstract class AddStickerPackActivity extends BaseActivity {
    private static final int ADD_PACK = 200;
    private static final String TAG = "AddStickerPackActivity";

    protected void addStickerPackToWhatsApp(String identifier, String stickerPackName) {

        try {
            //if neither WhatsApp Consumer or WhatsApp Business is installed, then tell user to install the apps.
            if (!WhitelistCheck.isWhatsAppConsumerAppInstalled(getPackageManager()) && !WhitelistCheck.isWhatsAppSmbAppInstalled(getPackageManager())) {
                Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();

                Log.e(TAG, "app not installed");
                return;
            }
            final boolean stickerPackWhitelistedInWhatsAppConsumer = WhitelistCheck.isStickerPackWhitelistedInWhatsAppConsumer(this, identifier);
            final boolean stickerPackWhitelistedInWhatsAppSmb = WhitelistCheck.isStickerPackWhitelistedInWhatsAppSmb(this, identifier);
            if (!stickerPackWhitelistedInWhatsAppConsumer && !stickerPackWhitelistedInWhatsAppSmb) {
                //ask users which app to add the pack to.
                Log.e(TAG, "chooose app");
                launchIntentToAddPackToChooser(identifier, stickerPackName);
            } else if (!stickerPackWhitelistedInWhatsAppConsumer) {

                Log.e(TAG, "chooose app consumer");
                launchIntentToAddPackToSpecificPackage(identifier, stickerPackName, WhitelistCheck.CONSUMER_WHATSAPP_PACKAGE_NAME);
            } else if (!stickerPackWhitelistedInWhatsAppSmb) {

                Log.e(TAG, "chooose app buissness");
                launchIntentToAddPackToSpecificPackage(identifier, stickerPackName, WhitelistCheck.SMB_WHATSAPP_PACKAGE_NAME);
            } else {
                Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "error adding sticker pack to WhatsApp", e);
            Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
        }

    }

    private void launchIntentToAddPackToSpecificPackage(String identifier, String stickerPackName, String whatsappPackageName) {
        Log.e(TAG, "intent louched");
        Intent intent = createIntentToAddStickerPack(identifier, stickerPackName);
        intent.setPackage(whatsappPackageName);
        try {
            startActivityForResult(intent, ADD_PACK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
        }
    }

    //Handle cases either of WhatsApp are set as default app to handle this intent. We still want users to see both options.
    private void launchIntentToAddPackToChooser(String identifier, String stickerPackName) {
        Intent intent = createIntentToAddStickerPack(identifier, stickerPackName);
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.add_to_whatsapp)), ADD_PACK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    private Intent createIntentToAddStickerPack(String identifier, String stickerPackName) {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra("sticker_pack_id", identifier);
        intent.putExtra("sticker_pack_authority", BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        intent.putExtra( "sticker_pack_name", stickerPackName);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PACK) {
            if (resultCode == Activity.RESULT_CANCELED) {
                if (data != null) {
                    final String validationError = data.getStringExtra("validation_error");
                    if (validationError != null) {
                        if (BuildConfig.DEBUG) {
                            //validation error should be shown to developer only, not users.
                            MessageDialogFragment.newInstance(R.string.title_validation_error, validationError).show(getSupportFragmentManager(), "validation error");
                        }
                        Log.e(TAG, "Validation failed:" + validationError);
                    }
                } else {
                    new StickerPackNotAddedMessageFragment().show(getSupportFragmentManager(), "sticker_pack_not_added");
                }
            }
        }
    }

    public static final class StickerPackNotAddedMessageFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity())
                    .setMessage(R.string.add_pack_fail_prompt_update_whatsapp)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> dismiss())
                    .setNeutralButton(R.string.add_pack_fail_prompt_update_play_link, (dialog, which) -> launchWhatsAppPlayStorePage());

            return dialogBuilder.create();
        }

        private void launchWhatsAppPlayStorePage() {
            if (getActivity() != null) {
                final PackageManager packageManager = getActivity().getPackageManager();
                final boolean whatsAppInstalled = WhitelistCheck.isPackageInstalled(WhitelistCheck.CONSUMER_WHATSAPP_PACKAGE_NAME, packageManager);
                final boolean smbAppInstalled = WhitelistCheck.isPackageInstalled(WhitelistCheck.SMB_WHATSAPP_PACKAGE_NAME, packageManager);
                final String playPackageLinkPrefix = "http://play.google.com/store/apps/details?id=";
                if (whatsAppInstalled && smbAppInstalled) {
                    launchPlayStoreWithUri("https://play.google.com/store/apps/developer?id=WhatsApp+Inc.");
                } else if (whatsAppInstalled) {
                    launchPlayStoreWithUri(playPackageLinkPrefix + WhitelistCheck.CONSUMER_WHATSAPP_PACKAGE_NAME);
                } else if (smbAppInstalled) {
                    launchPlayStoreWithUri(playPackageLinkPrefix + WhitelistCheck.SMB_WHATSAPP_PACKAGE_NAME);
                }
            }
        }

        private void launchPlayStoreWithUri(String uriString) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uriString));
            intent.setPackage("com.android.vending");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), R.string.cannot_find_play_store, Toast.LENGTH_LONG).show();
            }
        }
    }
}
