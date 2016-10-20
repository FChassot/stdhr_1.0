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
    protected LocationManager mLocationManager;

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
    public GpsLocationListener(Context aContext) {

        this.mContext = aContext;
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

        Location lLocation = null;

        try {
            mLocationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);

            // get GPS status
            mIsGpsEnabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

           // Log.v("isGPSEnabled", "=" + mIsGpsEnabled);

            // get network status
            mIsNetworkEnabled = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //Log.v("isNetworkEnabled", "=" + mIsNetworkEnabled);

            if (mIsGpsEnabled || mIsNetworkEnabled) {
                this.mGetLocationPossible = true;

                if (mIsNetworkEnabled) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    //Log.d("Network", "Network");

                    if (mLocationManager != null) {
                        lLocation = mLocationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (mIsGpsEnabled) {
                    lLocation = null;

                    if (lLocation == null) {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        //Log.d("GPS Enabled", "GPS Enabled");

                        if (mLocationManager != null) {
                            lLocation = mLocationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }

        } catch (Exception aExc) {
            aExc.printStackTrace();
        }

        /*if (lLocation != null) {
            Notifications.ShowMessageBox(this.mContext, "Location " + lLocation.getLatitude() + "" + lLocation.getLongitude(), "info", "ok");
        }*/

        return lLocation;
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
        //editLocation.setText("");
        //pb.setVisibility(View.INVISIBLE);
        /*Toast.makeText(
                null,
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v("TAG", longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v("TAG", latitude);*/

        /*------- To get city name from coordinates -------- */
        /*String cityName = null;
        Geocoder gcd = new Geocoder(null, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        String s = longitude + "\n" + latitude + "\n\nMy Current City is: " + cityName;
        //editLocation.setText(s);*/
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}