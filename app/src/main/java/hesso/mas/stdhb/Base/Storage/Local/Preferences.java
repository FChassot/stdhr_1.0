package hesso.mas.stdhb.Base.Storage.Local;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.widget.Toast;

import hesso.mas.stdhb.Base.Constants.BaseConstants;

/**
 * Created by chf on 14.07.2016.
 *
 * This class wrapps the sharedPrerences
 */
public class Preferences {

    Context lContext;

    // Constructor
    public Preferences(Context aContext) {
        lContext = aContext;
    }

    /**
     * Set a Value to the preferences.
     *
     * @param aKey The name of the preference to set.
     * @param aValue The value of the preference to set.
     */
    public void setValue(String aKey, int aValue) {

        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(lContext);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(aKey, aValue);

        // commit writes its data to persistent storage immediately, whereas 'apply' will
        // handle it in the background
        editor.apply();
    }

    /**
     * Set a Value to the preferences.
     *
     * @param aKey The name of the preference to set.
     * @param aValue The value of the preference to set.
     */
    public void setValue(String aKey, String aValue) {

        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(lContext);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(aKey, aValue);

        // commit writes its data to persistent storage immediately, whereas 'apply' will
        // handle it in the background
        editor.apply();
    }

    /**
     * Set a Value to the preferences.
     *
     * @param aKey The name of the preference to set.
     * @param aValue The value of the preference to set.
     */
    public void setValue(String aKey, Boolean aValue) {

        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(lContext);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(aKey, aValue);

        // commit writes its data to persistent storage immediately, whereas 'apply' will
        // handle it in the background
        editor.apply();
    }

    /**
     * Retrieve an integer value from the preferences.
     *
     * @param aKey The name of the preference to retrieve.
     * @param aDefaultValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a String.
     *
     * @throws ClassCastException
     */
    public Integer getPrefValue(String aKey, Integer aDefaultValue) {

        Integer lValue = aDefaultValue;

        SharedPreferences lSharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(lContext);

        if (lSharedPrefs != null) {
            if (lSharedPrefs.contains(aKey)) {
                try {
                    lValue = lSharedPrefs.getInt(aKey, aDefaultValue);

                } catch (Exception aExc) {
                    Toast toast = Toast.makeText(null, aExc.getMessage(), Toast.LENGTH_SHORT);;
                    toast.show();
                }
            }
        }

        return lValue;
    }

    /**
     * Retrieve an integer value from the preferences.
     *
     * @param aKey The name of the preference to retrieve.
     * @param aDefaultValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a String.
     *
     * @throws ClassCastException
     */
    public String getPrefValue(String aKey, String aDefaultValue) {

        String lValue = aDefaultValue;

        SharedPreferences lSharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(lContext);

        if (lSharedPrefs != null) {
            if (lSharedPrefs.contains(aKey)) {
                try {
                    lValue = lSharedPrefs.getString(aKey, aDefaultValue);

                } catch (Exception aExc) {
                    Toast toast = Toast.makeText(null, aExc.getMessage(), Toast.LENGTH_SHORT);;
                    toast.show();
                }
            }
        }

        return lValue;
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param aKey The name of the preference to retrieve.
     * @param aDefValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a String.
     *
     * @throws ClassCastException
     */
    public boolean getBooleanPrefValue(String aKey, boolean aDefValue) {

        boolean lValue = aDefValue;

        SharedPreferences lSharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(lContext);

        if (lSharedPrefs != null) {
            if (lSharedPrefs.contains(aKey)) {
                try {
                    lValue = lSharedPrefs.getBoolean(aKey, aDefValue);

                } catch (Exception aExc) {
                    Toast toast = Toast.makeText(null, aExc.getMessage(), Toast.LENGTH_SHORT);;
                    toast.show();
                }
            }
        }

        return lValue;
    }

}
