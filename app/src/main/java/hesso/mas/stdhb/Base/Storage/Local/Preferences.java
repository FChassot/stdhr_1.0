package hesso.mas.stdhb.Base.Storage.Local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by chf on 14.07.2016.
 *
 * This class wrapps the sharedPrerences
 */
public class Preferences {

    // interface to global information about the application environment.
    Context mContext;

    private static SharedPreferences getPrefs(Context aContext) {
        return aContext.getSharedPreferences("myprefs", 0);
    }

    // Constructor
    public Preferences(Context aContext) {
        mContext = aContext;
    }

    /**
     * Set a Value to the preferences.
     *
     * @param aKey The name of the preference to set.
     * @param aValue The value of the preference to set.
     */
    public void setValue(String aKey, int aValue) {

        SharedPreferences lSharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(mContext);

        SharedPreferences.Editor lEditor = lSharedPrefs.edit();
        lEditor.putInt(aKey, aValue);

        // commit writes its data to persistent storage immediately, whereas 'apply' will
        // handle it in the background
        lEditor.apply();
    }

    /**
     * Set a Value to the preferences.
     *
     * @param aKey The name of the preference to set.
     * @param aValue The value of the preference to set.
     */
    public void setValue(String aKey, Set<String> aValue) {

        SharedPreferences lSharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(mContext);

        SharedPreferences.Editor lEditor = lSharedPrefs.edit();
        lEditor.putStringSet(aKey, aValue);

        // commit writes its data to persistent storage immediately, whereas 'apply' will
        // handle it in the background
        lEditor.apply();
    }

    /**
     * Set a Value to the preferences.
     *
     * @param aKey The name of the preference to set.
     * @param aValue The value of the preference to set.
     */
    public void setValue(String aKey, String aValue) {

        SharedPreferences lSharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(mContext);

        SharedPreferences.Editor lEditor = lSharedPrefs.edit();
        lEditor.putString(aKey, aValue);

        // commit writes its data to persistent storage immediately, whereas 'apply' will
        // handle it in the background
        lEditor.apply();
    }

    /**
     * Set a Value to the preferences.
     *
     * @param aKey The name of the preference to set.
     * @param aValue The value of the preference to set.
     */
    public void setValue(String aKey, Boolean aValue) {

        SharedPreferences lSharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(mContext);

        SharedPreferences.Editor lEditor = lSharedPrefs.edit();
        lEditor.putBoolean(aKey, aValue);

        // commit writes its data to persistent storage immediately, whereas 'apply' will
        // handle it in the background
        lEditor.apply();
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
            PreferenceManager.getDefaultSharedPreferences(mContext);

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
                PreferenceManager.getDefaultSharedPreferences(mContext);

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
                PreferenceManager.getDefaultSharedPreferences(mContext);

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
    public Set<String> getSetPrefValue(String aKey, Set<String> aDefValue) {

        Set<String> lValue = null;

        SharedPreferences lSharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(mContext);

        if (lSharedPrefs != null) {
            if (lSharedPrefs.contains(aKey)) {
                try {
                    lValue = lSharedPrefs.getStringSet(aKey, aDefValue);

                } catch (Exception aExc) {
                    Toast toast = Toast.makeText(null, aExc.getMessage(), Toast.LENGTH_SHORT);;
                    toast.show();
                }
            }
        }

        return lValue;
    }

    public static void setMyIntPref(Context aContext, String aKey, int aValue) {
        getPrefs(aContext).edit().putInt(aKey, aValue).commit();
    }

    public static Integer getMyIntPref(Context aContext, String aKey, Integer aDefaultValue) {
        return getPrefs(aContext).getInt(aKey, aDefaultValue);
    }

    public static void setMyStringPref(Context aContext, String aKey, String aValue) {
        getPrefs(aContext).edit().putString(aKey, aValue).commit();
    }

    public static String getMyStringPref(Context aContext, String aKey, String aDefaultValue) {
        return getPrefs(aContext).getString(aKey, aDefaultValue);
    }

    public static void setMyBooleanPref(Context aContext, String aKey, Boolean aValue) {
        getPrefs(aContext).edit().putBoolean(aKey, aValue).commit();
    }

    public Boolean getMyBooleanPref(Context aContext, String aKey, Boolean aDefaultValue) {
        return getPrefs(aContext).getBoolean(aKey, aDefaultValue);
    }

    public static void setMySetPref(Context aContext, String aKey, Set<String> aValue) {
        getPrefs(aContext).edit().putStringSet(aKey, aValue).commit();
    }

    public static Set<String> getMySetPref(Context aContext, String aKey, Set<String> aDefaultValue) {
        return getPrefs(aContext).getStringSet(aKey, aDefaultValue);
    }


}
