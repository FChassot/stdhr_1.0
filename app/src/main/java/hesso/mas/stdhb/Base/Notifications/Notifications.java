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
     *
     * @param aContext
     * @param aMessage
     * @param aTitle
     * @param aPositiveButtonText
     */
    public static void ShowMessageBox(
        Context aContext,
        String aMessage,
        String aTitle,
        String aPositiveButtonText) {

        AlertDialog.Builder lAlertDialog = new AlertDialog.Builder(aContext);

        lAlertDialog.setMessage(aMessage);
        lAlertDialog.setTitle(aTitle);
        lAlertDialog.setPositiveButton(aPositiveButtonText, null);
        lAlertDialog.setCancelable(true);
        lAlertDialog.create().show();
    }

    /**
     *
     * @param aContext
     * @param aValDescCollection
     * @param aTitle
     * @param aPositiveButtonText
     */
    public static void ShowMessageBox(
        Context aContext,
        ValidationDescCollection aValDescCollection,
        String aTitle,
        String aPositiveButtonText) {

        AlertDialog.Builder lAlertDialog = new AlertDialog.Builder(aContext);

        String lFormattedMsg = MyString.EMPTY_STRING;

        for (String lMessage : aValDescCollection.values()) {
            lFormattedMsg += lMessage;
        }

        lAlertDialog.setMessage(lFormattedMsg);
        lAlertDialog.setTitle(aTitle);
        lAlertDialog.setPositiveButton(aPositiveButtonText, null);
        lAlertDialog.setCancelable(true);
        lAlertDialog.create().show();
    }

    /**
     *
     * @param aContext
     * @param aMessage
     * @param aTitle
     * @param aPositiveText
     */
    public static void ShowMessageBoxWithSettingsOption(
            final Context aContext,
            String aMessage,
            String aTitle,
            String aPositiveText,
            String aSettingsAction,
            String aNegativeButtonText) {

        AlertDialog.Builder lAlertDialog = new AlertDialog.Builder(aContext);

        lAlertDialog.setMessage(aMessage);
        lAlertDialog.setTitle(aTitle);

        // On pressing Settings button
        lAlertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface aDialog, int which) {
                        Intent lIntent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        aContext.startActivity(lIntent);
                    }
                });

        // on pressing cancel button
        lAlertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface aDialog, int which) {
                        aDialog.cancel();
                    }
                });

        lAlertDialog.create().show();
    }
}
