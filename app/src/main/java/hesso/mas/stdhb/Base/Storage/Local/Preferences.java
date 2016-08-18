package hesso.mas.stdhb.Base.Storage.Local;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import hesso.mas.stdhb.Base.Constants.BaseConstants;

/**
 * Created by Frédéric Chassot (chf) on 14.07.2016.
 *
 * This class wrapps the sharedPrerences
 */
public class Preferences {

    Context lContext;

    public Preferences(Context aContext) {

        lContext = aContext;
    }

    /**
     * This method allow to set a value to a dedicated Key
     *
     * @param aKey Key in the File.
     * @param aValue Key in the File.
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
     * This method allow to get the value corresponding to the given Key
     *
     * @param aKey
     */
    public String getPrefValue(String aKey, String aDefaultValue) {

        String lValue = aDefaultValue;

        SharedPreferences lSharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(lContext);

        if (lSharedPrefs != null) {
            if (lSharedPrefs.contains(aKey)) {
                lValue = lSharedPrefs.getString(aKey, aDefaultValue);
            }
        }

        return lValue;
    }

    /**
     * This method allow to get the value corresponding to the given Key
     *
     * @param aKey
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
