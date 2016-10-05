package hesso.mas.stdhb.Client.Gui.Radar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Geolocation.GpsLocationListener;
import hesso.mas.stdhb.Base.Models.Basemodel;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Notifications.Notifications;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;
import hesso.mas.stdhb.DataAccess.QueryEngine.Request.CitizenRequests;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Client.Gui.MainActivity;
import hesso.mas.stdhb.Communication.Services.RetrieveCitizenDataAsyncTask;
import hesso.mas.stdhbtests.R;

public class RadarActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    RadarView mRadarView = null;

    private static final String TAG = "RadarActivity";

    // A Handler allows you to send and process Message
    // and Runnable objects associated with a thread's MessageQueue.
    Handler mHandler = new android.os.Handler();

    private GpsLocationListener mGeolocationServices;

    private Receiver mReceiver;

    private Location CurrentUserLocation;
    private double Radius;

    // device sensor manager
    private SensorManager mSensorManager;

    private Sensor mCompass;

    // record the compass picture angle turned
    private float mCurrentDegree = 0f;

    Button mBtnStopRadar = null;

    TextView mNbrOfCulturalObjectsDetected = null;

    /**
     * Called when the activity is first created. This is where you should do all of your normal static set up:
     * create views, bind data to lists, etc. This method also provides you with a Bundle containing the activity's
     * previously frozen state, if there was one. Always followed by onStart().
     *
     * The app uses the device's magnetometer (compass).
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);

        mGeolocationServices = new GpsLocationListener(this);

        if (mGeolocationServices.canGetLocation() == false) {mGeolocationServices.showSettingsAlert();}

        mRadarView = (RadarView) findViewById(R.id.radarView);
        mBtnStopRadar = (Button)findViewById(R.id.mBtnStopRadar);
        mNbrOfCulturalObjectsDetected = (TextView)findViewById(R.id.mDTxtViewNbrObject);

        ImageView mImgBack = (ImageView)findViewById(R.id.mImgBack);
        ImageView mImgRadarInfo = (ImageView)findViewById(R.id.mImgRadarInfo);
        TextView mRadiusInfo = (TextView)findViewById(R.id.mDtxtRadiusInfo);

        mReceiver = new Receiver();

        // initialize the android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mCompass = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        IntentFilter lFilter =
                new IntentFilter(RetrieveCitizenDataAsyncTask.ACTION2);

        this.registerReceiver(mReceiver, lFilter);

        startUpdateMarkersFromCitizen();

        this.startRadar(mRadarView);

        Preferences lPrefs = new Preferences(this);

        double lRadiusOfSearch =
                lPrefs.getPrefValue(
                        BaseConstants.Attr_Search_Radius,
                        Basemodel.NULL_KEY);

        Radius = lRadiusOfSearch;

        if (lRadiusOfSearch < 1000) {
            mRadiusInfo.setText(getResources().getString(R.string.txt_radius_of_search) + ": " + lRadiusOfSearch + " [m]");

        } else {
            mRadiusInfo.setText(getResources().getString(R.string.txt_radius_of_search) + ": " + (lRadiusOfSearch/1000) + " [km]");
        }

        mNbrOfCulturalObjectsDetected.setText(getResources().getString(R.string.txt_radar_doing_first_search));

        assert mBtnStopRadar != null;
        mBtnStopRadar.setOnClickListener(this);

        assert mImgBack != null;
        mImgBack.setOnClickListener(this);

        assert mImgBack != null;
        mImgRadarInfo.setOnClickListener(this);
    }

    /**
     *
     */
    @Override
    protected void onResume() {

        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(
                this,
                mSensorManager.getDefaultSensor(
                        Sensor.TYPE_ORIENTATION),
                        SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * The final call you receive before your activity is destroyed.
     * This can happen either because the activity is finishing (someone called finish() on it,
     * or because the system is temporarily destroying this instance of the activity to save space.
     * You can distinguish between these two scenarios with the isFinishing() method.
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        try {
            this.unregisterReceiver(mReceiver);
            //mWakeLock.release();                      //keep screen on

        } catch (Exception e) {
            //Log.e(MatabbatManager.TAG, getClass() + " Releasing receivers-" + e.getMessage());
        }
    }

    /**
     * When the system calls onPause() for your activity, it technically means your activity is still
     * partially visible, but most often is an indication that the user is leaving the activity and it
     * will soon enter the Stopped state. You should usually use the onPause() callback to:
     *
     * - Check if the activity is visible; if it is not, stop animations or other ongoing actions that could consume CPU.
     * Remember, beginning with Android 7.0, a paused app might be running in multi-window mode. In this case, you would
     * not want to stop animations or video playback.
     * - Commit unsaved changes, but only if users expect such changes to be permanently saved when they leave
     * (such as a draft email).
     * - Release system resources, such as broadcast receivers, handles to sensors (like GPS), or any resources
     * that may affect battery life while your activity is paused and the user does not need them.
     */
    @Override
    public void onPause() {
        super.onPause();

        this.stopRadar(mRadarView);
        this.stopUpdateMarkersFromCitizen();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);

    }

    /**
     * Start the radar's animation
     *
     * @param aView
     */
    public void startRadar(View aView) {
        if (mRadarView != null) mRadarView.startRadar();
    }

    /**
     * Updates the property mMarkers of the view class
     *
     * @param aMarkers The list of the markers to be updated on the view
     */
    public synchronized void updateMarkers(List<RadarMarker> aMarkers) {
        if (mRadarView != null) mRadarView.updateMarkers(aMarkers);
    }

    /**
     * Stop the radar's animation
     *
     * @param aView
     */
    public void stopRadar(View aView) {
        if (mRadarView != null) mRadarView.stopRadar();
    }

    Runnable updateData = new Runnable() {
        @Override
        public void run() {
            startAsyncSearch();
            mHandler.postDelayed(this, 5000);
        }
    };

    /**
     * Update the TextView which informs about the number of objects in proximity
     *
     * @param aTextView
     */
    private void updateRadarText(TextView aTextView) {
        if (mRadarView.getMarkers() != null) {
            aTextView.setText(
                    mRadarView.getMarkers().size() + " " + getResources().getString(R.string.txt_cultural_objects_in_proximity));
        }
    }

    /**
     * This method allows to start the update of the Markers in the view
     */
    public void startUpdateMarkersFromCitizen() {
        mHandler.removeCallbacks(updateData);
        mHandler.post(updateData);
    }

    /**
     * This method allows to stop the update of the Markers in the view
     */
    public void stopUpdateMarkersFromCitizen() {
        mHandler.removeCallbacks(updateData);
    }

    /**
     *
     * @param aEvent
     */
    @Override
    public void onSensorChanged(SensorEvent aEvent) {
        // get the angle around the z-axis rotated
        mCurrentDegree = Math.round(aEvent.values[0]);
    }

    /**
     *
     * @param aSensor
     * @param aAccuracy
     */
    @Override
    public void onAccuracyChanged(Sensor aSensor, int aAccuracy) {
    }

    /**
     * Start an Async search on the endPoint Sparql Server
     *
     */
    private void startAsyncSearch() {

        Location lCurrentUserLocation =
                mGeolocationServices.getUserCurrentLocation();

        // TODO removes when the application works
        if (lCurrentUserLocation == null) {
            CurrentUserLocation = new Location(MyString.EMPTY_STRING);
            CurrentUserLocation.setLatitude(46.2333d);
            CurrentUserLocation.setLongitude(7.35d);
        }

        RetrieveCitizenDataAsyncTask lRetrieveTask =
                new RetrieveCitizenDataAsyncTask(
                        this,
                        RetrieveCitizenDataAsyncTask.ACTION2);

        Preferences lPrefs = new Preferences(this);

        String lClientServerCommunicationMode =
                lPrefs.getPrefValue(
                        BaseConstants.Attr_ClientServer_Communication,
                        MyString.EMPTY_STRING);

        EnumClientServerCommunication lEnumValue =
                EnumClientServerCommunication.valueOf(
                        lClientServerCommunicationMode);

        if (lEnumValue != EnumClientServerCommunication.ANDROJENA) {
            Notifications.ShowMessageBox(
                    this,
                    getResources().getString(R.string.txt_radar_possible_mode),
                    getResources().getString(R.string.Warning),
                    getResources().getString(R.string.Ok));

            return;
        }

        Integer lRadiusOfSearch =
            lPrefs.getPrefValue(
                BaseConstants.Attr_Search_Radius,
                    0);

        String lCulturalObjectType =
                lPrefs.getPrefValue(
                        BaseConstants.Attr_TypeOfSearch,
                        MyString.EMPTY_STRING);

        lRetrieveTask.onPreExecuteMessageDisplay = false;

        String lQuery =
                CitizenRequests.GetCulturalObjectsInProximityQuery(
                        lCulturalObjectType,
                        CurrentUserLocation,
                        lRadiusOfSearch);

        lRetrieveTask.execute(
            lQuery,
            lClientServerCommunicationMode);
    }

    /**
     * Our Broadcast Receiver. We get notified that the data is ready this way.
     */
    private class Receiver extends BroadcastReceiver {

        /**
         * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
         * During this time you can use the other methods on BroadcastReceiver to view/modify
         * the current result values. This method is always called within the main thread of
         * its process, unless you explicitly asked for it to be scheduled on a different thread
         * using registerReceiver(BroadcastReceiver, IntentFilter, String, android.os.Handler).
         * When it runs on the main thread you should never perform long-running operations in it
         * (there is a timeout of 10 seconds that the system allows before considering the receiver
         * to be blocked and a candidate to be killed). You cannot launch a popup dialog in your
         * implementation of onReceive().
         */
        @Override
        public void onReceive(Context aContext, Intent aIntent)
        {
            Bundle lBundle = aIntent.getExtras();

            CitizenQueryResult lCitizenQueryResult = null;

            try {
                lCitizenQueryResult =
                        lBundle.getParcelable(
                                RetrieveCitizenDataAsyncTask.HTTP_RESPONSE);

            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }

            List<RadarMarker> lMarkers =
                    RadarHelper.getRadarMarkersFromResponse(
                            mCurrentDegree,
                            CurrentUserLocation,
                            Radius,
                            lCitizenQueryResult,
                            mRadarView);

            updateMarkers(lMarkers);
            updateRadarText(mNbrOfCulturalObjectsDetected);
        }
    }

    /**
     * Update the text displayed on the mBtnStopRadar Button
     */
    private void updateButtonText() {
        if (mBtnStopRadar.getText() == getResources().getString(R.string.txt_btn_stop_radar))
        {
            mBtnStopRadar.setText(getResources().getString(R.string.txt_btn_continue_radar));
        }
        else {
            mBtnStopRadar.setText(getResources().getString(R.string.txt_btn_stop_radar));
        }
    }

    /**
     * The onClick() method is called when a button is actually clicked (or tapped).
     * This method is called by the listener.
     */
    public void onClick(View view){
        if (view.getId()==R.id.mBtnStopRadar){
            if (mBtnStopRadar.getText() == getResources().getString(R.string.txt_btn_stop_radar)) {
                this.stopRadar(this.mRadarView);
            } else {
                this.startRadar(this.mRadarView);
            }

            this.updateButtonText();
        }
        if (view.getId()==R.id.mImgBack){
            this.stopRadar(this.mRadarView);

            Intent intent = new Intent(RadarActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}