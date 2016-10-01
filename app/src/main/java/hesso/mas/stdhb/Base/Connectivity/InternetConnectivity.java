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
public class InternetConnectivity {

    private final Context mContext;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    // Constructor
    public InternetConnectivity(Context context) {
        this.mContext = context;
    }

    /**
     * Check if the internet connection is available
     *
     * @return Returns true when the connection is available
     */
    public boolean IsActive() {

        ConnectivityManager lConnManager =
            (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo lNetworkInfo = lConnManager.getActiveNetworkInfo();

        if (lNetworkInfo != null && lNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    /**
     * Check if the gps is available
     *
     * @return Returns true when the connection is available
     */
    public boolean IsGpsEnabled() {

        boolean lIsGpsEnabled = false;

        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            lIsGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lIsGpsEnabled;
    }

    /**
     * Check if the internet connection is available
     *
     * @return Returns true when the connection is available
     */
    public boolean IsNetworkAvailable() {

        boolean lIsNetworkAvailable = false;

        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting network status
            lIsNetworkAvailable = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lIsNetworkAvailable;
    }
}
