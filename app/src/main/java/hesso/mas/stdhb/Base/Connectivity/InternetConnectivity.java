package hesso.mas.stdhb.Base.Connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Frédéric Chassot (chf) on 27.07.2016.
 *
 * Cette classe offre des méthodes nécessaires pour tester
 * la connectivité de l'application.
 */
public class InternetConnectivity {
    private final Context mContext;

    public InternetConnectivity(Context context) {
        this.mContext = context;
    }

    public boolean IsActive() {

        ConnectivityManager connManager =
            (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }

        return false;
    }
}
