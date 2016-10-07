package hesso.mas.stdhb.Base.Notifications;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Base.Validation.ValidationDescCollection;

/**
 * Created by chf on 12.09.2016.
 *
 * This class provides methods for the display of messages and comments
 * for the app's user.
 */
public final class Notifications {

    /**
     *
     * @param aContext
     * @param aMessage
     * @param aTitle
     * @param aPositiveBtnText
     */
    public static void ShowMessageBox(
        Context aContext,
        String aMessage,
        String aTitle,
        String aPositiveBtnText) {

        AlertDialog.Builder lAlertDialog =
            new AlertDialog.Builder(aContext);

        lAlertDialog.setMessage(aMessage);
        lAlertDialog.setTitle(aTitle);
        lAlertDialog.setPositiveButton(aPositiveBtnText, null);
        lAlertDialog.setCancelable(true);
        lAlertDialog.create().show();
    }

    /**
     *
     * @param aContext
     * @param aValDescCollection
     * @param aTitle
     * @param aPositiveBtnText
     */
    public static void ShowMessageBox(
        Context aContext,
        ValidationDescCollection aValDescCollection,
        String aTitle,
        String aPositiveBtnText) {

        AlertDialog.Builder lAlertDialog =
            new AlertDialog.Builder(aContext);

        String lFormattedMsg = MyString.EMPTY_STRING;

        for (String lMessage : aValDescCollection.mListOfValidationDesc) {
            lFormattedMsg += lMessage;
        }

        lAlertDialog.setMessage(lFormattedMsg);
        lAlertDialog.setTitle(aTitle);
        lAlertDialog.setPositiveButton(aPositiveBtnText, null);
        lAlertDialog.setCancelable(true);
        lAlertDialog.create().show();
    }
}
