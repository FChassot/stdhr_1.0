package hesso.mas.stdhb.Base.Connectivity;

import android.content.Context;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class is used to check internet access connection.
 *
 * @author chf
 * @version Version 1.0
 * @since 27.07.2016
 */
public class NetworkConnectivity {

    // Member variable
    private final Context mContext;

    // Constructor
    public NetworkConnectivity(Context context) {
        this.mContext = context;
    }

    /**
     * Check if the internet connection is available
     *
     * @return returns true when the connection is available
     */
    public boolean isActive() {

        ConnectivityManager connectivityManager =
            (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    /**
     * Check if the internet connection is available
     *
     * @return returns true when the connection is available
     */
    public boolean isNetworkAvailable() {

        boolean isNetworkAvailable = false;

        try {
            LocationManager locationManager =
                (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // get network status
            isNetworkAvailable = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception aExc) {
            aExc.printStackTrace();
        }

        return isNetworkAvailable;
    }

    /**
     * Check if the GPS is available
     *
     * @return returns true when the gps system is available
     */
    public boolean isGpsEnabled() {

        boolean isGpsEnabled = false;

        try {
            // the locationManager class provides access to the system location services.
            LocationManager locationManager =
                (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // get GPS status
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception aExc) {
            aExc.printStackTrace();
        }

        return isGpsEnabled;
    }
}
