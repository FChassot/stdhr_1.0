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
    private Context mContext;

    private static SharedPreferences getPrefs(Context aContext) {
        return PreferenceManager.getDefaultSharedPreferences(aContext);
    }

    // Constructor
    public Preferences(Context context) {
        mContext = context;
    }

    /**
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setMyIntPref(@NonNull Context context, @NonNull String key, @Nullable int value) {
        getPrefs(context).edit().putInt(key, value).commit();
    }

    /**
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    @NonNull
    public static Integer getMyIntPref(@NonNull Context context, @NonNull String key, @Nullable int defaultValue) {
        return getPrefs(context).getInt(key, defaultValue);
    }

    /**
     *
     * @param aContext
     * @param aKey
     * @param value
     */
    public static void setMyStringPref(@NonNull Context aContext, @NonNull String aKey, @Nullable String value) {
        getPrefs(aContext).edit().putString(aKey, value).commit();
    }

    /**
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getMyStringPref(@NonNull Context context, @NonNull String key, @Nullable String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }

    /**
     *
     * @param context
     * @param aKey
     * @param value
     */
    public static void setMyBooleanPref(@NonNull Context context, @NonNull String aKey, @Nullable Boolean value) {
        getPrefs(context).edit().putBoolean(aKey, value).commit();
    }

    /**
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public Boolean getMyBooleanPref(@NonNull Context context, @NonNull String key, @Nullable Boolean defaultValue) {
        return getPrefs(context).getBoolean(key, defaultValue);
    }

    /**
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setMySetPref(@NonNull Context context, @NonNull String key, @Nullable Set<String> value) {
        getPrefs(context).edit().putStringSet(key, value).commit();
    }

    /**
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static Set<String> getMySetPref(@NonNull Context context, @NonNull String key, @Nullable Set<String> defaultValue) {
        return getPrefs(context).getStringSet(key, defaultValue);
    }

}
