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

    private NetworkConnectivity mConnectivity;

    // The current location of the app's user
    private Location mCurrentUserLocation;

    // GoogleMap instance
    private GoogleMap mMapFragment;

    /**
     * Called when the activity is first created. This is where you should do all of your normal static set up: create views,
     * bind data to lists, etc. This method also provides you with a Bundle containing the activity's previously frozen state,
     * if there was one. Always followed by onStart().
     *
     * @param aSavedInstanceState
     */
    @Override
    protected void onCreate(Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);

        // Set the activity content to an explicit view
        setContentView(R.layout.activity_maps);

        // ToDo chf: this dependencies has to be injected
        mGeolocationServices = new GpsLocationListener(this);
        mConnectivity = new NetworkConnectivity(this);
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

        // The google Map is acquired using getMapAsync(OnMapReadyCallback)
        mMapFragment.getMapAsync(this);

        // Check if map is created successfully or not
        if (mMapFragment == null) {
            if (!mConnectivity.isActive()) {
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

        mMapFragment.setOnMarkerClickListener(this);
        mMapFragment.setMyLocationEnabled(true);

        LatLngBounds.Builder lBuilder = new LatLngBounds.Builder();

        LatLng lLatLngCurrentUserLocation =
            new LatLng(
                mCurrentUserMarker.getLatitude(),
                mCurrentUserMarker.getLongitude());

        lBuilder.include(lLatLngCurrentUserLocation);

        // Add a marker in the marker location
        mMapFragment.addMarker(
                new MarkerOptions()
                        .position(lLatLngCurrentUserLocation)
                        .title(mCurrentUserMarker.getTitle()));

        if (mCulturalObjectMarkerSelected != null) {
            LatLng lLatLngCulturalObjectLocation =
                new LatLng(
                    mCulturalObjectMarkerSelected.getLatitude(),
                    mCulturalObjectMarkerSelected.getLongitude());

            lBuilder.include(lLatLngCulturalObjectLocation);

            // Add a marker in the current location and move the camera
            Marker lMarker = mMapFragment.addMarker(
                new MarkerOptions()
                    .position(lLatLngCulturalObjectLocation)
                    .snippet(mCulturalObjectMarkerSelected.getObjectId())
                    .title(mCulturalObjectMarkerSelected.getTitle()));

            lMarker.showInfoWindow();
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
                        .snippet(lMarker.getObjectId())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_museum)));
            }
        }

        // Returns a CameraUpdate that transforms the camera such that the specified
        // latitude/longitude bounds are centered on screen at the greatest possible zoom level.
        mMapFragment.moveCamera(CameraUpdateFactory.newLatLngBounds(lBuilder.build(), 900, 900, 2));
    }

    /**
     * Called when a marker has been clicked or tapped.
     *
     * @param aMarker The marker that was clicked.
     *
     * @return true if the listener has consumed the event (i.e., the default behavior should
     * not occur); false otherwise (i.e., the default behavior should occur).
     */
    @Override
    public boolean onMarkerClick (Marker aMarker) {

        if (aMarker.getTitle().equals("Citizen radar's user")) { return true; }

        Location lLocation = new Location(MyString.EMPTY_STRING);
        lLocation.setLatitude(aMarker.getPosition().latitude);
        lLocation.setLongitude(aMarker.getPosition().longitude);

        RadarMarker lSelMarker = new RadarMarker();

        lSelMarker.setLocation(lLocation);
        lSelMarker.setTitle(aMarker.getTitle());
        lSelMarker.setObjectId(aMarker.getSnippet());

        Intent lIntent = new Intent(this, SearchActivity.class);

        // The bundle object contains a mapping from String keys
        // to various Parcelable values.
        Bundle lBundle = new Bundle();

        lBundle.putParcelable(MapsActivity.RADAR_MARKER, lSelMarker);

        lIntent.putExtras(lBundle);

        this.startActivity(lIntent);

        return true;
    }

}
