package hesso.mas.stdhb.Base.Storage.Local;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by chf on 02.10.2016.
 */
public class SharedPreferencesLoader extends AsyncTaskLoader<SharedPreferences>
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences mPrefs = null;

    public static void persist(final SharedPreferences.Editor editor) {
        editor.apply();
    }

    public SharedPreferencesLoader(Context context) {
        super(context);
    }

    // Load the data asynchronously
    @Override
    public SharedPreferences loadInBackground() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mPrefs.registerOnSharedPreferenceChangeListener(this);

        return (mPrefs);
    }

    @Override
    public void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences,
            String key) {

        // notify loader that content has changed
        onContentChanged();
    }

    /**
     * starts the loading of the data
     * once result is ready the onLoadFinished method is called
     * in the main thread. It loader was started earlier the result
     * is return directly

     * method must be called from main thread.
     */
    @Override
    protected void onStartLoading() {
        if (mPrefs != null) {
            deliverResult(mPrefs);
        }

        if (takeContentChanged() || mPrefs == null) {
            forceLoad();
        }
    }
}
