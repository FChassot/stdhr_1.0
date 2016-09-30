package hesso.mas.stdhb.Gui.GoogleMap;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import hesso.mas.stdhb.Base.Connectivity.InternetConnectivity;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Gui.Radar.RadarMarker;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 11.07.2016.
 *
 * Activity for the google Map function
 */
public class MapsActivity extends Activity implements OnMapReadyCallback {

    // GoogleMap instance
    private GoogleMap mMapFragment;

    public static final String RADAR_MARKER = "RADAR_MARKER";

    public RadarMarker mCurrentUserMarker;
    public RadarMarker mCulturalObjectMarker;

    public double mCulturalObjectMarkerLatitude;
    public double mCulturalObjectMarkerLongitude;
    public double mCurrentUserLatitude;
    public double mCurrentUserLongitude;

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

        if (this.getIntent().hasExtra("lat")) {
            Bundle bundle = getIntent().getExtras();
            // to retrieve the cultural object selected in the radar view
            //mCulturalObjectMarker = getIntent().getParcelableExtra(RADAR_MARKER);
            mCulturalObjectMarkerLatitude = bundle.getDouble("lat");
            mCulturalObjectMarkerLongitude = bundle.getDouble("long");
            mCurrentUserLatitude = bundle.getDouble("currentlat");
            mCurrentUserLongitude = bundle.getDouble("currentlong");
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

        mMapFragment.setMyLocationEnabled(true);

        LatLngBounds.Builder lBuilder = new LatLngBounds.Builder();

        // Add a marker in the current location and move the camera
        LatLng lLatLngCurrentUserLocation =
                    new LatLng(
                            mCurrentUserLatitude,
                            mCurrentUserLongitude);

        // Add a marker in the current location
        LatLng lLatLngCulturalObjectLocation =
                    new LatLng(
                            mCulturalObjectMarkerLatitude,
                            mCulturalObjectMarkerLongitude);

        lBuilder.include(lLatLngCulturalObjectLocation).include(lLatLngCurrentUserLocation);

        // Add a marker in the marker location
        mMapFragment.addMarker(
                    new MarkerOptions()
                            .position(lLatLngCurrentUserLocation)
                            .title("CITIZEN RADAR's USER"));

        mMapFragment.addMarker(
                    new MarkerOptions()
                            .position(lLatLngCulturalObjectLocation)
                            .title("MARKER"));

       /* LatLngBounds lBounds = null;

        try{
            lBounds =
                    new LatLngBounds(
                            lLatLngCulturalObjectLocation,
                            lLatLngCurrentUserLocation);

        }
        catch (Exception e){
            Log.i("GoogleMap", e.getMessage());
        }*/

        mMapFragment.moveCamera(CameraUpdateFactory.newLatLngBounds(lBuilder.build(), 2));
    }

    /*@Override
    public void onPause() {
        super.onPause();

    }*/

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

        double lCurrentLatitude = location.getLatitude();
        double lCurrentLongitude = location.getLongitude();

        LatLng lLatLng = new LatLng(lCurrentLatitude, lCurrentLongitude);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(lLatLng)
                .title("I am here!");

        mMapFragment.addMarker(options);
        mMapFragment.moveCamera(CameraUpdateFactory.newLatLng(lLatLng));
    }

    /*@Override
    public boolean onMarkerClick(Marker marker) {
        // TODO Auto-generated method stub
       // if(marker.equals(marker_1)){
            //Log.w("Click", "test");
            return true;
        //}
        return false;
    }*/
}
