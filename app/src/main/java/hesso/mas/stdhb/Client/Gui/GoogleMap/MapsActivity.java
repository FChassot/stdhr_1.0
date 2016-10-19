package hesso.mas.stdhb.Client.Gui.GoogleMap;

import java.util.Date;
import java.text.DateFormat;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import android.location.LocationManager;
import android.content.Context;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import hesso.mas.stdhb.Base.Connectivity.NetworkConnectivity;

import hesso.mas.stdhb.Base.Geolocation.GpsLocationListener;
import hesso.mas.stdhb.Base.Notifications.Notifications;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarMarker;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 11.07.2016.
 *
 * Activity for the google Map functionality
 */
public class MapsActivity extends Activity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, LocationListener {

    // GoogleMap instance
    private GoogleMap mMapFragment;

    // constants
    public static final String USER_MARKER = "USER_MARKER";

    public static final String RADAR_MARKER = "RADAR_MARKER";

    public static final String RADAR_MARKER_ARRAY = "NO_SELECTED_RADAR_MARKER_ARRAY";

    public RadarMarker mCurrentUserMarker;

    public RadarMarker mCulturalObjectMarkerSelected;

    public List<RadarMarker> mCulturalObjectMarkers;

    //private GpsLocationListener mGeolocationServices;

    private LocationManager mLocationManager;

    private Location mCurrentUserLocation;

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

        //mGeolocationServices = new GpsLocationListener(this);
        //mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);

        Intent lIntent = getIntent();

        // the bundle object contains a mapping from String keys to various Parcelable values.
        Bundle lBundle = lIntent.getExtras();

        if (lBundle != null) {
            // to retrieve the current user marker
            mCurrentUserMarker = lBundle.getParcelable(USER_MARKER);
            // to retrieve the cultural object selected in the radar view
            mCulturalObjectMarkerSelected = lBundle.getParcelable(RADAR_MARKER);
            // to retrieve the non selected cultural object present in the view
            //mCulturalObjectMarkers = lBundle.getParcelable(RADAR_MARKER_ARRAY);
        }
        else {
            /*Location lCurrentUserLocation =
                mGeolocationServices.getUserCurrentLocation();

            if (lCurrentUserLocation == null){
                Notifications.ShowMessageBox(this,"null", "CurrentLocation", "Ok");
            }

            if (lCurrentUserLocation != null){
                mCurrentUserMarker = new RadarMarker();
                mCurrentUserMarker.setLatitude(lCurrentUserLocation.getLatitude());
                mCurrentUserMarker.setLongitude(lCurrentUserLocation.getLongitude());
                mCurrentUserMarker.setTitle("Citizen radar's user");
            }
            else {*/
                mCurrentUserMarker = new RadarMarker();
                mCurrentUserMarker.setLatitude(46.2333);
                mCurrentUserMarker.setLongitude(7.35);
                mCurrentUserMarker.setTitle("Citizen radar's user");
            //}
        }

        NetworkConnectivity lConnectivity = new NetworkConnectivity(this);
        boolean lIsActive = lConnectivity.IsActive();

        // obtain the MapFragment and get notified when the map is ready to be used.
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

        LatLng lLatLngCurrentUserLocation =
            new LatLng(
                mCurrentUserMarker.getLatitude(),
                mCurrentUserMarker.getLongitude());

        if (mCulturalObjectMarkerSelected != null) {
            LatLng lLatLngCulturalObjectLocation =
                new LatLng(
                    mCulturalObjectMarkerSelected.getLatitude(),
                    mCulturalObjectMarkerSelected.getLongitude());

            lBuilder.include(lLatLngCulturalObjectLocation).include(lLatLngCurrentUserLocation);

            // Add a marker in the current location and move the camera
            mMapFragment.addMarker(
                new MarkerOptions()
                    .position(lLatLngCulturalObjectLocation)
                    .title(mCulturalObjectMarkerSelected.getTitle()));
        } else { lBuilder.include(lLatLngCurrentUserLocation);}

        // Add a marker in the marker location
        mMapFragment.addMarker(
            new MarkerOptions()
                .position(lLatLngCurrentUserLocation)
                .title(mCurrentUserMarker.getTitle()));

        mMapFragment.moveCamera(CameraUpdateFactory.newLatLngBounds(lBuilder.build(), 2));
    }

    public void onMapClick (LatLng point) {
        // Do Something
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

    @Override
    public void onLocationChanged(Location location) {
        mCurrentUserLocation = location;
        //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        //updateUI();
    }
}
