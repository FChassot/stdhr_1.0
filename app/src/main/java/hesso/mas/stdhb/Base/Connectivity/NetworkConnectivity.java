package hesso.mas.stdhb.Base.Connectivity;

import android.content.Context;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by chf on 27.07.2016.
 *
 * This class is used to check internet access connection.
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

        ConnectivityManager lConnectivityManager =
            (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo lNetworkInfo = lConnectivityManager.getActiveNetworkInfo();

        if (lNetworkInfo != null && lNetworkInfo.isConnected()) {
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

        boolean lIsNetworkAvailable = false;

        try {
            LocationManager lLocationManager =
                (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // get network status
            lIsNetworkAvailable = lLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception aExc) {
            aExc.printStackTrace();
        }

        return lIsNetworkAvailable;
    }

    /**
     * Check if the GPS is available
     *
     * @return returns true when the gps system is available
     */
    public boolean isGpsEnabled() {

        boolean lIsGpsEnabled = false;

        try {
            // the locationManager class provides access to the system location services.
            LocationManager lLocationManager =
                (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // get GPS status
            lIsGpsEnabled = lLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception aExc) {
            aExc.printStackTrace();
        }

        return lIsGpsEnabled;
    }
}
