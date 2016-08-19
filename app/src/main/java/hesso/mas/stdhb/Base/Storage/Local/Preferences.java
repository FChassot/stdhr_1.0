package hesso.mas.stdhb.Base.Storage.Local;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import hesso.mas.stdhb.Base.Constants.BaseConstants;

/**
 * Created by chf on 14.07.2016.
 *
 * This class wrapps the sharedPrerences
 */
public class Preferences {

    Context lContext;

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

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(lContext);

        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putInt(aKey, aValue);

        // commit writes its data to persistent storage immediately, whereas 'apply' will handle it
        // in the background
        editor.apply();
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
    public String getPrefValue(String aKey, String aDefValue) {

        String lValue = aDefValue;

        SharedPreferences lSharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(lContext);

        if (lSharedPrefs != null) {
            if (lSharedPrefs.contains(aKey)) {
                //try {
                    lValue = lSharedPrefs.getString(aKey, aDefValue);
                //} catch (ClassCastException)
            }
        }

        return lValue;
    }

    /**
     * This method allow to get the value corresponding to the given Key
     *
     * @param aKey The name of the preference to retrieve.
     */
    public Location getValue(String aKey) {

        if (aKey.equals(BaseConstants.Attr_Rayon_Radar)) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(lContext);

            StringBuilder builder = new StringBuilder();

            int lRayon = sharedPrefs.getInt(aKey, 0);

            builder.append("\n Rayon: " + lRayon);

            //TextView rayonSettingsView = (TextView) findViewById(R.id.textViewRayon);

            //rayonSettingsView.setText(builder.toString());

        } else {

            Location lCoordonates = new Location("");

            // coordonnées GPS
            lCoordonates.setAltitude(829);
            lCoordonates.setLatitude(46.6092369);
            lCoordonates.setLongitude(7.029020100000025);

            // accuracy (exactitude)
            lCoordonates.setAccuracy(100);

            //GpsLocationListener lGpsServices = new GpsLocationListener(this);

            //lCoordonates = lGpsServices.getLocation();
            return lCoordonates;

        }

        return null;
    }
}
