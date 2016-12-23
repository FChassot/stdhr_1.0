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

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Geolocation.GpsLocationListener;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Client.Gui.Cityzen.CityZenActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarActivity;
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

    // The current location of the app's user
    private Location mCurrentUserLocation;

    private RadarMarker mCurrentUserMarker;

    private RadarMarker mSelectedMarker;

    private List<RadarMarker> mCulturalObjectMarkers;

    private boolean mWithoutOnStop = false;

    // Dependencies
    private GpsLocationListener mGeolocationServices;

    private NetworkConnectivity mConnectivity;

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
        Intent intent = this.getIntent();

        // The bundle object contains a mapping from String keys
        // to various Parcelable values.
        Bundle bundle = intent.getExtras();

        mWithoutOnStop = false;

        if (bundle != null) {
            // To retrieve the current user marker
            mCurrentUserMarker = bundle.getParcelable(USER_MARKER);
            // To retrieve the cultural object selected in the radar view
            mSelectedMarker = bundle.getParcelable(RADAR_MARKER);
            // To retrieve all cultural objects found in the radar but not selected
            mCulturalObjectMarkers = bundle.getParcelableArrayList(RADAR_MARKER_ARRAY);
        }
        else {
            if (mCurrentUserLocation != null){
                mCurrentUserMarker = new RadarMarker();
                mCurrentUserMarker.setLatitude(mCurrentUserLocation.getLatitude());
                mCurrentUserMarker.setLongitude(mCurrentUserLocation.getLongitude());
                mCurrentUserMarker.setTitle(BaseConstants.Attr_Citizen_User_Text);
            }
            else {
                mCurrentUserMarker = new RadarMarker();
                mCurrentUserMarker.setLatitude(46.2333);
                mCurrentUserMarker.setLongitude(7.35);
                mCurrentUserMarker.setTitle(BaseConstants.Attr_Citizen_User_Text);
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
     * When the system calls onPause() for your activity, it technically means your activity is still
     * partially visible, but most often is an indication that the user is leaving the activity and it
     * will soon enter the Stopped state. You should usually use the onPause() callback to:
     *
     * - Check if the activity is visible; if it is not, stop animations or other ongoing actions
     * that could consume CPU.
     * Remember, beginning with Android 7.0, a paused app might be running in multi-window mode.
     * In this case, you would not want to stop animations or video playback.
     * - Commit unsaved changes, but only if users expect such changes to be permanently saved when
     * they leave (such as a draft email).
     * - Release system resources, such as broadcast receivers, handles to sensors (like GPS), or
     * any resources
     * that may affect battery life while your activity is paused and the user does not need them.
     */
    @Override
    public void onPause() {
        super.onPause();

        if (!mWithoutOnStop) {
            Intent intent = new Intent(this, RadarActivity.class);

            Bundle bundle = new Bundle();

            bundle.putParcelable(MapsActivity.USER_MARKER, mSelectedMarker);

            intent.putExtras(bundle);

            this.startActivity(intent);
        }
    }

    /**
     * Called when you are no longer visible to the user. You will next receive either onRestart(),
     * onDestroy(), or nothing, depending on later user activity.
     */
    @Override
    public void onStop() {
        super.onStop();

        if (!mWithoutOnStop) {
            Intent intent = new Intent(this, RadarActivity.class);

            Bundle bundle = new Bundle();

            bundle.putParcelable(MapsActivity.RADAR_MARKER, mSelectedMarker);

            intent.putExtras(bundle);

            this.startActivity(intent);
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

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        LatLng latLngCurrentUserLocation =
            new LatLng(
                mCurrentUserMarker.getLatitude(),
                mCurrentUserMarker.getLongitude());

        builder.include(latLngCurrentUserLocation);

        // Add a marker in the marker location
        mMapFragment.addMarker(
                new MarkerOptions()
                        .position(latLngCurrentUserLocation)
                        .title(mCurrentUserMarker.getTitle()));

        if (mSelectedMarker != null) {
            LatLng latLngCulturalObjectLocation =
                new LatLng(
                        mSelectedMarker.getLatitude(),
                        mSelectedMarker.getLongitude());

            builder.include(latLngCulturalObjectLocation);

            // Add a marker in the current location and move the camera
            Marker marker = mMapFragment.addMarker(
                new MarkerOptions()
                    .position(latLngCulturalObjectLocation)
                    .snippet(mSelectedMarker.getObjectId())
                    .title(mSelectedMarker.getTitle()));

            marker.showInfoWindow();
        }

        // Add all markers non selected as well
        if (mCulturalObjectMarkers != null) {
            for (RadarMarker lMarker : mCulturalObjectMarkers) {
                LatLng latLngCulturalObjectLocation =
                    new LatLng(
                        lMarker.getLatitude(),
                        lMarker.getLongitude());

                builder.include(latLngCulturalObjectLocation);

                // Add a marker in the current location and move the camera
                mMapFragment.addMarker(
                    new MarkerOptions()
                        .position(latLngCulturalObjectLocation)
                        .title(lMarker.getTitle())
                        .snippet(lMarker.getObjectId())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_museum)));
            }
        }

        // Returns a CameraUpdate that transforms the camera such that the specified
        // latitude/longitude bounds are centered on screen at the greatest possible zoom level.
        mMapFragment.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 900, 900, 2));
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

        if (aMarker.getTitle().equals(BaseConstants.Attr_Citizen_User_Text)) { return true; }

        Location location = new Location(MyString.EMPTY_STRING);
        location.setLatitude(aMarker.getPosition().latitude);
        location.setLongitude(aMarker.getPosition().longitude);

        RadarMarker selectedMarker = new RadarMarker();

        selectedMarker.setLocation(location);
        selectedMarker.setTitle(aMarker.getTitle());
        selectedMarker.setObjectId(aMarker.getSnippet());

        Intent intent = new Intent(this, CityZenActivity.class);

        // The bundle object contains a mapping from String keys
        // to various Parcelable values.
        Bundle bundle = new Bundle();

        bundle.putParcelable(MapsActivity.RADAR_MARKER, selectedMarker);

        intent.putExtras(bundle);

        this.startActivity(intent);

        mWithoutOnStop = true;

        return true;
    }

}
