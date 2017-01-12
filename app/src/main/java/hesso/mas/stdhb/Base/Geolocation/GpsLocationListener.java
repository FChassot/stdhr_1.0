package hesso.mas.stdhb.Base.Geolocation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

/**
 * Created by chf on 13.05.2016.
 *
 * This class is used for receiving notifications from the LocationManager when
 * the location has changed. These methods are called if the LocationListener has
 * been registered with the location manager service
 * using the {@link LocationManager#requestLocationUpdates(String, long, float, LocationListener)}
 * method.
 */
public final class GpsLocationListener implements LocationListener {

    private final Context mContext;

    // Declare a Location Manager
    private LocationManager mLocationManager;

    // location
    private Location mLocation;

    // flag for GPS status
    private boolean mIsGpsEnabled = false;

    // flag for network status
    private boolean mIsNetworkEnabled = false;

    // flag for GPS status
    private boolean mGetLocationPossible = false;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;  // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1;              // 1 minute

    // Constructor
    public GpsLocationListener(Context context) {

        this.mContext = context;
        this.mLocation = getCurrentLocation();
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     * */
    public boolean isLocationRetrievePossible() {
        return this.mGetLocationPossible;
    }

    /**
     *
     *
     * @return
     */
    private boolean isGpsEnabled() { return this.mIsGpsEnabled; }

    /**
     *
     *
     * @return
     */
    private boolean isNetworkEnabled() { return this.mIsGpsEnabled; }

    /**
     * Function to get the user's current location
     *
     * @return the user's current location
     */
    public Location getCurrentLocation() {

        Location location = null;

        try {
            mLocationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);

            // get GPS status
            mIsGpsEnabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // get network status
            mIsNetworkEnabled = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (mIsGpsEnabled || mIsNetworkEnabled) {
                this.mGetLocationPossible = true;

                if (mIsNetworkEnabled) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (mLocationManager != null) {
                        location = mLocationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (mIsGpsEnabled) {
                    location = null;

                    if (location == null) {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (mLocationManager != null) {
                            location = mLocationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }

        } catch (Exception aExc) {
            aExc.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     * */
    public void stopUsingGPS() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(GpsLocationListener.this);
        }
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * launch Settings Options
     * */
    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog =
            new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface aDialog, int which) {
                        aDialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();

        /*Notifications.ShowMessageBox(
                this.mContext,
                "GPS is not enabled. Do you want to go to settings menu?",
                "GPS is setting",
                "");*/
    }

    /**
     *
     * @param aLocation
     */
    @Override
    public void onLocationChanged(Location aLocation) {
    }

    /**
     *
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider) {
    }

    /**
     *
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider) {
    }

    /**
     *
     * @param provider
     * @param status
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}