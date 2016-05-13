package hesso.mas.stdhb.Sensor;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.util.*;

/**
 * Created by frede on 13.05.2016.
 */
/*---------- Listener class to get coordinates ------------- */
class MyLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location loc) {
        //editLocation.setText("");
        //pb.setVisibility(View.INVISIBLE);
        Toast.makeText(
                null,
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v("TAG", longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v("TAG", latitude);

        /*------- To get city name from coordinates -------- */
        String cityName = null;
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
        //editLocation.setText(s);
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}