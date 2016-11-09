package hesso.mas.stdhb.Base.Storage.Local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    public static void setMyIntPref(@NonNull Context aContext, @NonNull String aKey, @Nullable int aValue) {
        getPrefs(aContext).edit().putInt(aKey, aValue).commit();
    }

    @NonNull
    public static Integer getMyIntPref(@NonNull Context aContext, @NonNull String aKey, @Nullable int aDefaultValue) {
        return getPrefs(aContext).getInt(aKey, aDefaultValue);
    }

    public static void setMyStringPref(@NonNull Context aContext, @NonNull String aKey, @Nullable String aValue) {
        getPrefs(aContext).edit().putString(aKey, aValue).commit();
    }

    public static String getMyStringPref(@NonNull Context aContext, @NonNull String aKey, @Nullable String aDefaultValue) {
        return getPrefs(aContext).getString(aKey, aDefaultValue);
    }

    public static void setMyBooleanPref(@NonNull Context aContext, @NonNull String aKey, @Nullable Boolean aValue) {
        getPrefs(aContext).edit().putBoolean(aKey, aValue).commit();
    }

    public Boolean getMyBooleanPref(@NonNull Context aContext, @NonNull String aKey, @Nullable Boolean aDefaultValue) {
        return getPrefs(aContext).getBoolean(aKey, aDefaultValue);
    }

    public static void setMySetPref(@NonNull Context aContext, @NonNull String aKey, @Nullable Set<String> aValue) {
        getPrefs(aContext).edit().putStringSet(aKey, aValue).commit();
    }

    public static Set<String> getMySetPref(@NonNull Context aContext, @NonNull String aKey, @Nullable Set<String> aDefaultValue) {
        return getPrefs(aContext).getStringSet(aKey, aDefaultValue);
    }


}
