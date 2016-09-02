package hesso.mas.stdhb.Gui.GoogleMap;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import hesso.mas.stdhb.Base.Connectivity.InternetConnectivity;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 11.07.2016.
 *
 * Activity for the google Map function
 */
public class MapsActivity extends Activity implements OnMapReadyCallback {

    // GoogleMap instance
    private GoogleMap mMapFragment;

    public static final String RADAR_EXTRA = "LATLNG";

    public Location mLocationToDisplay;

    /**
     * Called when the activity is first created. This is where you should do all of your normal static set up: create views,
     * bind data to lists, etc. This method also provides you with a Bundle containing the activity's previously frozen state,
     * if there was one. Always followed by onStart().
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        if (this.getIntent().hasExtra(RADAR_EXTRA)) {
            // To retrieve object sent by the radar
            mLocationToDisplay = (Location)getIntent().getSerializableExtra(RADAR_EXTRA);
        }

        InternetConnectivity lInterConnectivity = new InternetConnectivity(this);
        boolean lIsActive = lInterConnectivity.IsActive();

        // Obtain the MapFragment and get notified when the map is ready to be used.
        MapFragment mMapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));

        mMapFragment.getMapAsync(this);

        // check if map is created successfully or not
        if (mMapFragment == null) {
            if (!lIsActive) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps [internet network not active]", Toast.LENGTH_SHORT)
                        .show();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMapFragment = googleMap;

        if (mLocationToDisplay != null) {
            // Add a marker in the position found and move the camera
            LatLng lLatLngBulle = new LatLng(46.6092369, 7.029020100000025);

            mMapFragment.animateCamera(CameraUpdateFactory.newLatLngZoom(lLatLngBulle, 14));
            mMapFragment.addMarker(new MarkerOptions().position(lLatLngBulle).title("Marker in Bulle"));
            mMapFragment.moveCamera(CameraUpdateFactory.newLatLng(lLatLngBulle));

        }
        else {
            // Add a marker in the position found and move the camera
            LatLng lLatLngBulle = new LatLng(46.6092369, 7.029020100000025);

            mMapFragment.animateCamera(CameraUpdateFactory.newLatLngZoom(lLatLngBulle, 14));
            mMapFragment.addMarker(new MarkerOptions().position(lLatLngBulle).title("Marker in Bulle"));
            mMapFragment.moveCamera(CameraUpdateFactory.newLatLng(lLatLngBulle));
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMapFragment} is not null.
     */
    private void setUpMap() {
        mMapFragment.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    /**
     *
     * @param location
     */
    private void handleNewLocation(Location location) {
        //Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMapFragment.addMarker(options);
        mMapFragment.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
