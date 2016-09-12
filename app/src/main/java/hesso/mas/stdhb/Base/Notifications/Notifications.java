package hesso.mas.stdhb.Base.Notifications;

import android.content.Context;
import android.support.v7.app.AlertDialog;

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
}
