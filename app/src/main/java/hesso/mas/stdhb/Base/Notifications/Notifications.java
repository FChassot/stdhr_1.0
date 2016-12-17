package hesso.mas.stdhb.Base.Notifications;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Base.Validation.ValidationDescCollection;

/**
 * Created by chf on 12.09.2016.
 *
 * This class provides methods for the display of messages of different types
 * on the app's user.
 */
public final class Notifications {

    /**
     * Display a dialog Box with the alert message
     *
     * @param context
     * @param message
     * @param title
     * @param positiveButtonText
     */
    public static void ShowMessageBox(
        Context context,
        String message,
        String title,
        String positiveButtonText) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setMessage(message);
        alertDialog.setTitle(title);
        alertDialog.setPositiveButton(positiveButtonText, null);
        alertDialog.setCancelable(true);
        alertDialog.create().show();
    }

    /**
     * Display a dialog Box with the alert message
     *
     * @param context
     * @param valDescCollection
     * @param title
     * @param positiveButtonText
     */
    public static void ShowMessageBox(
        Context context,
        ValidationDescCollection valDescCollection,
        String title,
        String positiveButtonText) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        String formattedMsg = MyString.EMPTY_STRING;

        for (String lMessage : valDescCollection.values()) {
            formattedMsg += lMessage;
        }

        alertDialog.setMessage(formattedMsg);
        alertDialog.setTitle(title);
        alertDialog.setPositiveButton(positiveButtonText, null);
        alertDialog.setCancelable(true);
        alertDialog.create().show();
    }

    /**
     * Display a dialog Box with the alert message
     *
     * @param context
     * @param message
     * @param title
     * @param positiveText
     */
    public static void ShowMessageBoxWithSettingsOption(
            final Context context,
            String message,
            String title,
            String positiveText,
            String settingsAction,
            String negativeButtonText) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setMessage(message);
        alertDialog.setTitle(title);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface aDialog, int which) {
                        Intent lIntent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(lIntent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface aDialog, int which) {
                        aDialog.cancel();
                    }
                });

        alertDialog.create().show();
    }
}
