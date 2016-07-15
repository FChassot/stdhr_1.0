package hesso.mas.stdhb.Base.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

/**
 * Created by frede on 14.07.2016.
 * This class wrapps the sharedPrerences
 */
public class StdhrPreferences {

    Context lContext;

    public StdhrPreferences(Context aContext) {

        lContext = aContext;
    }

    /**
     *
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
     *
     */
    public Location getValue(String aKey) {

        if (aKey.equals("radar_Rayon")) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(lContext);

            StringBuilder builder = new StringBuilder();

            int lRayon = sharedPrefs.getInt(aKey, 0);

            builder.append("\n Rayon: " + lRayon);

            //TextView rayonSettingsView = (TextView) findViewById(R.id.textViewRayon);

            //rayonSettingsView.setText(builder.toString());

        } else {

            Location lCoordonates = new Location("");

            // coordonnées GPS de Ch. de Cuquerens à Bulle
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
