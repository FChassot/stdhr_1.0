package hesso.mas.stdhb.Base.Connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by chf on 27.07.2016.
 *
 * This class is used to check internet access connection.
 */
public class InternetConnectivity {
    private final Context mContext;

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

        ConnectivityManager connManager =
            (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo lNetworkInfo = connManager.getActiveNetworkInfo();

        if (lNetworkInfo != null && lNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }
}
