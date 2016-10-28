package hesso.mas.stdhb.Base.Storage.Local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
        return PreferenceManager.getDefaultSharedPreferences(aContext);
    }

    // Constructor
    public Preferences(Context aContext) {
        mContext = aContext;
    }

    public static void setMyIntPref(Context aContext, String aKey, int aValue) {
        getPrefs(aContext).edit().putInt(aKey, aValue).commit();
    }

    @NonNull
    public static Integer getMyIntPref(Context aContext, String aKey, int aDefaultValue) {
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
