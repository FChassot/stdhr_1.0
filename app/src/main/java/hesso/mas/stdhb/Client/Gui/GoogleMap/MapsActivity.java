package hesso.mas.stdhb.Client.Gui.GoogleMap;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import hesso.mas.stdhb.Base.Connectivity.NetworkConnectivity;

import hesso.mas.stdhb.Base.Geolocation.GpsLocationListener;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Client.Gui.Citizen.SearchActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarMarker;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 11.07.2016.
 *
 * Activity for the google Map functionality
 */
public class MapsActivity extends Activity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // Constants
    public static final String USER_MARKER = "USER_MARKER";

    public static final String RADAR_MARKER = "RADAR_MARKER";

    public static final String RADAR_MARKER_ARRAY = "NO_SELECTED_RADAR_MARKER_ARRAY";

    // Member variables
    private RadarMarker mCurrentUserMarker;

    private RadarMarker mCulturalObjectMarkerSelected;

    private List<RadarMarker> mCulturalObjectMarkers;

    private GpsLocationListener mGeolocationServices;

    // The current location of the app's user
    private Location mCurrentUserLocation;

    // GoogleMap instance
    private GoogleMap mMapFragment;

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

        // Set the activity content to an explicit view
        setContentView(R.layout.activity_maps);

        mGeolocationServices = new GpsLocationListener(this);

        mCurrentUserLocation = mGeolocationServices.getCurrentLocation();

        // An intent is an abstract description of an operation to be performed.
        // getIntent returns the intent that started this activity.
        Intent lIntent = this.getIntent();

        // The bundle object contains a mapping from String keys
        // to various Parcelable values.
        Bundle lBundle = lIntent.getExtras();

        if (lBundle != null) {
            // To retrieve the current user marker
            mCurrentUserMarker = lBundle.getParcelable(USER_MARKER);
            // To retrieve the cultural object selected in the radar view
            mCulturalObjectMarkerSelected = lBundle.getParcelable(RADAR_MARKER);
            // To retrieve all cultural objects found in the radar but not selected
            mCulturalObjectMarkers = lBundle.getParcelableArrayList(RADAR_MARKER_ARRAY);
        }
        else {
            if (mCurrentUserLocation != null){
                mCurrentUserMarker = new RadarMarker();
                mCurrentUserMarker.setLatitude(mCurrentUserLocation.getLatitude());
                mCurrentUserMarker.setLongitude(mCurrentUserLocation.getLongitude());
                mCurrentUserMarker.setTitle("Citizen radar's user");
            }
            else {
                mCurrentUserMarker = new RadarMarker();
                mCurrentUserMarker.setLatitude(46.2333);
                mCurrentUserMarker.setLongitude(7.35);
                mCurrentUserMarker.setTitle("Citizen radar's user");
            }
        }

        // Obtain the MapFragment and get notified when the map is ready to be used.
        MapFragment mMapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));

        mMapFragment.getMapAsync(this);

        // Check if map is created successfully or not
        if (mMapFragment == null) {
            NetworkConnectivity lConnectivity = new NetworkConnectivity(this);

            if (!lConnectivity.isActive()) {
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
    public void onMapReady(GoogleMap aGoogleMap) {

        mMapFragment = aGoogleMap;

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

        } else {
            lBuilder.include(lLatLngCurrentUserLocation);
        }

        // Add all markers non selected as well
        if (mCulturalObjectMarkers != null) {
            for (RadarMarker lMarker : mCulturalObjectMarkers) {
                LatLng lLatLngCulturalObjectLocation =
                        new LatLng(
                                lMarker.getLatitude(),
                                lMarker.getLongitude());

                lBuilder.include(lLatLngCulturalObjectLocation);

                // Add a marker in the current location and move the camera
                mMapFragment.addMarker(
                        new MarkerOptions()
                                .position(lLatLngCulturalObjectLocation)
                                .title(lMarker.getTitle())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_museum)));
            }
        }

        // Add a marker in the marker location
        mMapFragment.addMarker(
            new MarkerOptions()
                .position(lLatLngCurrentUserLocation)
                .title(mCurrentUserMarker.getTitle()));

        mMapFragment.moveCamera(CameraUpdateFactory.newLatLngBounds(lBuilder.build(), 2));
    }

    /**
     * Called when a marker has been clicked or tapped.
     *
     * @param aMarker The marker that was clicked.
     *
     * @return true if the listener has consumed the event (i.e., the default behavior should not occur);
     * false otherwise (i.e., the default behavior should occur).
     */
    public boolean onMarkerClick (Marker aMarker) {

        Intent lIntent = new Intent(this, SearchActivity.class);
        Bundle lBundle = new Bundle();

        RadarMarker lSelectedMarker = new RadarMarker();

        Location lSelectedMarkerLocation = new Location(MyString.EMPTY_STRING);

        lSelectedMarkerLocation.setLatitude(aMarker.getPosition().latitude);
        lSelectedMarkerLocation.setLongitude(aMarker.getPosition().longitude);
        lSelectedMarker.setLocation(lSelectedMarkerLocation);

        lBundle.putParcelable(MapsActivity.RADAR_MARKER, lSelectedMarker);

        lIntent.putExtras(lBundle);

        this.startActivity(lIntent);

        return true;
    }

}
