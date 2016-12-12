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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.IntegerUtil;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Geolocation.GpsLocationListener;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Notifications.Notifications;

import hesso.mas.stdhb.Business.Spatial.SpatialGeometryServices;

import hesso.mas.stdhb.Client.Gui.Config.SettingsActivity;
import hesso.mas.stdhb.Client.Gui.Main.MainActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarHelper;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarMarker;
import hesso.mas.stdhb.Client.Gui.Radar.RenderView.RadarView;

import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;
import hesso.mas.stdhb.DataAccess.QueryEngine.Sparql.CitizenRequests;
import hesso.mas.stdhb.DataAccess.Communication.AsyncTask.RetrieveCitizenDataAsyncTask;
import hesso.mas.stdhbtests.R;

import static hesso.mas.stdhb.Client.Gui.GoogleMap.MapsActivity.RADAR_MARKER;

/**
 * Created by chf on 11.07.2016.
 *
 * Activity for the radar functionality
 */
public class RadarActivity
        extends AppCompatActivity
        implements SensorEventListener,
        View.OnClickListener {

    // Constant
    private static final String TAG = "RadarActivity";

    // Member variables
    private RadarView mRadarView;

    private Preferences mPreferences;

    private Button mBtnStopRadar;

    private TextView mNbrOfCulturalObjectsDetected = null;

    // An handler allows you to send and process message
    // and Runnable objects associated with a thread's MessageQueue.
    Handler mHandler = new android.os.Handler();

    private Receiver mReceiver;

    private GestureDetector mGestureDetector;

    private GpsLocationListener mGeolocationServices;

    private SpatialGeometryServices mSpatialGeometryServices;

    private Location mCurrentUserLocation;

    // SensorManager let access the device's sensors.
    private SensorManager mSensorManager;

    // Sensors for the orientation
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    private float[] mLastAccelerometerValue = new float[3];
    private float[] mLastMagnetometerValue = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private boolean mHasInterference = false;

    private float[] mInclinationMatrix = new float[16];
    private float[] mRotationMatrix = new float[9];

    private float[] mOrientation = new float[3];
    private float[] mOldOrientation = new float[3];

    private String[] mOrientationString =  new String[3];
    private String[] mOldOrientationString =  new String[3];

    private double mRadius;
    private int mRoll = 0;
    private int mPitch = 0;
    private int mAzimuth = 0;

    /**
     * Called when the activity is first created. This is where you should do all of your normal
     * static set up:create views, bind data to lists, etc. This method also provides you with a
     * Bundle containing the activity's previously frozen state, if there was one. Always followed
     * by onStart().
     *
     * The app uses the device's magnetometer (compass).
     *
     * @param aSavedInstanceState
     */
    @Override
    protected void onCreate(Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);

        // Set the activity content to an explicit view
        setContentView(R.layout.activity_display);

        mPreferences = new Preferences(this);

        mGeolocationServices = new GpsLocationListener(this);

        mSpatialGeometryServices = new SpatialGeometryServices();

        if (!mGeolocationServices.isLocationRetrievePossible()) {
            mGeolocationServices.showSettingsAlert();
        }

        // Find the views that was identified by an id attribute
        mRadarView = (RadarView) findViewById(R.id.radarView);
        mBtnStopRadar = (Button)findViewById(R.id.mBtnStopRadar);
        mNbrOfCulturalObjectsDetected = (TextView)findViewById(R.id.mDTxtViewNbrObject);

        ImageView lImgBack = (ImageView)findViewById(R.id.mImgBack);
        ImageView lImgRadarInfo = (ImageView)findViewById(R.id.mImgRadarInfo);
        TextView lRadiusInfo = (TextView)findViewById(R.id.mDtxtRadiusInfo);
        ImageButton lImgBtnZoom = (ImageButton)findViewById(R.id.imgBtnZoom);
        ImageButton lImgBtnZoomReset = (ImageButton)findViewById(R.id.imgBtnReset);
        RadarView lRadarView = (RadarView)findViewById(R.id.radarView);

        lImgBtnZoom.setEnabled(true);
        lImgBtnZoomReset.setEnabled(true);

        // Initialize the android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get the default sensor for the given type.
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Broadcast receivers enable applications to receive intents that are broadcast
        // by the system or by other applications, even when other components of the application
        // are not running.
        mReceiver = new Receiver();

        IntentFilter lFilter = new IntentFilter(RetrieveCitizenDataAsyncTask.ACTION2);
        this.registerReceiver(mReceiver, lFilter);

        mRadius =
            mPreferences.getMyIntPref(
                this,
                BaseConstants.Attr_Radius_Search,
                BaseConstants.Attr_Default_Radius_Search);

        updateInfoTxtView();

        mRadarView.Radius(mRadius);

        this.startRadar(mRadarView);

        mNbrOfCulturalObjectsDetected.setText(getResources().getString(R.string.txt_radar_doing_first_search));

        assert mBtnStopRadar != null;
        mBtnStopRadar.setOnClickListener(this);

        assert lImgBack != null;
        lImgBack.setOnClickListener(this);

        assert lImgRadarInfo != null;
        lImgRadarInfo.setOnClickListener(this);

        assert lRadiusInfo != null;
        lRadiusInfo.setOnClickListener(this);

        assert lImgBtnZoom != null;
        lImgBtnZoom.setOnClickListener(this);

        assert lImgBtnZoomReset != null;
        lImgBtnZoomReset.setOnClickListener(this);

        assert lRadarView != null;
        lRadarView.setOnClickListener(this);

        mGestureDetector = new GestureDetector(this, new GestureListener());
    }

    /**
     * When the user resumes your activity from the Paused state, the system calls the onResume()
     * method.
     *
     * Be aware that the system calls this method every time your activity comes into the foreground,
     * including when it's created for the first time. As such, you should implement onResume() to
     * initialize components that you release during onPause() and perform any other initializations
     * that must occur each time the activity enters the Resumed state (such as begin animations and
     * initialize components only used while the activity has user focus).
     */
    @Override
    protected void onResume() {

        super.onResume();

        // Update the radar's information
        this.updateInfoTxtView();

        // An intent is an abstract description of an operation to be performed.
        // getIntent returns the intent that started this activity.
        Intent lIntent = this.getIntent();

        if (lIntent != null) {
            // The bundle object contains a mapping from String keys to various Parcelable values.
            Bundle lBundle = lIntent.getExtras();

            if (lBundle != null) {
                // To retrieve the cultural object selected in the radar view
                RadarMarker mSelectedMarker = lBundle.getParcelable(RADAR_MARKER);

                if (mSelectedMarker != null) {
                    mRadarView.mSelectedMarker = mSelectedMarker;
                }
            }
        }

        // Start the radar
        mRadarView.startRadar();

        // For the system's orientation sensor registered listeners
        mSensorManager.registerListener(
            this,
            mAccelerometer,
            SensorManager.SENSOR_DELAY_GAME);

        mSensorManager.registerListener(
            this,
            mMagnetometer,
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
            mSensorManager.unregisterListener(this);
            //mWakeLock.release();                      //keep screen on

        } catch (Exception aExc) {
            //Log.e(MatabbatManager.TAG, getClass() + " Releasing receivers-" + e.getMessage());
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

        // Stop the radar. When the radar is stopped, other services are stopped as well,
        // like the update of the markers and the update of the orientation.
        this.stopRadar(mRadarView);

        // Stop the listener and save battery
        mSensorManager.unregisterListener(this);
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    //region UpdateTextFields

        /**
         * Update the text displayed on the mBtnStopRadar Button
         */
        private void updateButtonText() {
            Button lBtnStopRadar = (Button)findViewById(R.id.mBtnStopRadar);

            if (lBtnStopRadar.getText() == getResources().getString(R.string.txt_btn_stop_radar))
            {
                lBtnStopRadar.setText(getResources().getString(R.string.txt_btn_continue_radar));
            }
            else {
                lBtnStopRadar.setText(getResources().getString(R.string.txt_btn_stop_radar));
            }
        }

        /**
         * This method updates the radiusinfo field
         */
        private void updateInfoTxtView() {

            TextView mRadiusInfo = (TextView)findViewById(R.id.mDtxtRadiusInfo);

            if (mRadius < 1000) {
                String lText = getResources().getString(R.string.txt_radius_of_search) + ": " + mRadius + " [m]";
                lText += "      " + "Azimuth: " + IntegerUtil.roundToDecimal(mAzimuth);

                mRadiusInfo.setText(lText);
            }
            else {
                String lText = getResources().getString(R.string.txt_radius_of_search) + ": " + (mRadius/1000) + " [km]";
                lText += "      " + "Azimuth: " + IntegerUtil.roundToDecimal(mAzimuth);

                mRadiusInfo.setText(lText);
            }
        }

        /**
         * Update the TextView which informs about the number of objects in proximity
         *
         * @param aTextView
         */
        private void updateRadarText(TextView aTextView) {
            if (mRadarView.getMarkers() != null) {
                aTextView.setText(
                        mRadarView.getMarkers().size() +
                                " " + getResources().getString(R.string.txt_cultural_objects_in_proximity));
            }
        }

    //endregion

    //region OnClickListener

        /**
         * The onClick() method is called when a button is actually clicked (or tapped).
         * This method is called by the listener.
         */
        public void onClick(View aView){
            if (aView.getId()==R.id.mBtnStopRadar){
                if (mBtnStopRadar.getText() == getResources().getString(R.string.txt_btn_stop_radar)) {
                    this.stopRadar(this.mRadarView);
                }
                else {
                    this.startRadar(this.mRadarView);
                }

                this.updateButtonText();
            }
            if (aView.getId()==R.id.mImgBack){
                this.stopRadar(this.mRadarView);

                Intent lIntent = new Intent(RadarActivity.this, MainActivity.class);
                startActivity(lIntent);
            }
            if (aView.getId()==R.id.mDtxtRadiusInfo){
                Intent lIntent = new Intent(RadarActivity.this, SettingsActivity.class);
                startActivity(lIntent);
            }
            if (aView.getId()==R.id.imgBtnZoom){
                if (mBtnStopRadar.getText().equals(getResources().getString(R.string.txt_btn_continue_radar))) {return;}

                if ((mRadius / 2) <= 1) {
                    mRadius = 1;
                    updateRadius(mRadius);
                } else {
                    mRadius = (mRadius / 2);
                    updateRadius(mRadius);
                }

                mRadarView.stopRadar();
                mRadarView.startRadar();
                startAsyncSearch();
                updateInfoTxtView();
            }
            if (aView.getId()==R.id.imgBtnReset){
                if (mBtnStopRadar.getText().equals(getResources().getString(R.string.txt_btn_continue_radar))) {return;}
                    mRadius =
                            mPreferences.getMyIntPref(
                                    this,
                                    BaseConstants.Attr_Radius_Search,
                                    BaseConstants.Attr_Default_Radius_Search);

                mRadarView.Radius(mRadius);
                mRadarView.stopRadar();
                mRadarView.startRadar();

                startAsyncSearch();
                updateInfoTxtView();
            }
            if (aView.getId()==R.id.radarView){
                if (mBtnStopRadar.getText().equals(getResources().getString(R.string.txt_btn_continue_radar))) {
                    this.startRadar(this.mRadarView);
                    this.updateButtonText();
                }
            }
        }

    //endregion

    //region updateDataRunnable

    Runnable updateDataRunnable = new Runnable() {
        @Override
        public void run() {
            startAsyncSearch();
            // Causes the Runnable r to be added to the message queue, to be run after the specified
            // amount of time elapses (for the radar 5 seconds).
            mHandler.postDelayed(this, 1000);
        }
    };

    /**
     * Update the list of marker (property mMarkers) of the view class.
     *
     *
     * @param aMarkers The list of the markers to be updated on the view
     */
    public synchronized void updateMarkers(List<RadarMarker> aMarkers) {
        if (mRadarView != null) mRadarView.updateMarkers(aMarkers);
    }

    /**
     *
     * @param aAzimuth
     */
    public synchronized void updateAzimuth(int aAzimuth) {
        if (mRadarView != null) mRadarView.Azimuth(aAzimuth);
    }

    /**
     *
     * @param aRadius
     */
    public synchronized void updateRadius(double aRadius) {
        if (mRadarView != null) mRadarView.Radius(aRadius);
    }

    /**
     * This method allows to start the update of the Markers in the view
     */
    public void startUpdateMarkersFromCitizen() {
        mHandler.removeCallbacks(updateDataRunnable);
        mHandler.post(updateDataRunnable);
    }

    /**
     * This method allows to stop the update of the Markers in the view
     */
    public void stopUpdateRadarMarkers() {
        mHandler.removeCallbacks(updateDataRunnable);
    }

    /**
     * Start the radar's animation
     *
     * @param aView
     */
    public void startRadar(View aView) {

        if (mRadarView != null) {
            mRadarView.startRadar();
            startUpdateMarkersFromCitizen();
        }
    }

    /**
     * Stop the radar's animation
     *
     * @param aView
     */
    public void stopRadar(View aView) {

        if (mRadarView != null) {
            mRadarView.stopRadar();
            stopUpdateRadarMarkers();
        }
    }

    //endregion

    //region Orientation

    /**
     * Called when sensor values have changed. The length and contents of the values array
     * vary depending on which sensor is being monitored.
     *
     * @param aEvent
     */
    @Override
    public void onSensorChanged(SensorEvent aEvent) {

        if (aEvent.sensor == mAccelerometer) {
            System.arraycopy(
                    aEvent.values,
                    0,
                    mLastAccelerometerValue,
                    0,
                    aEvent.values.length);

            mLastAccelerometerSet = true;
        }
        else if (aEvent.sensor == mMagnetometer) {
            System.arraycopy(
                    aEvent.values,
                    0,
                    mLastMagnetometerValue,
                    0,
                    aEvent.values.length);

            mLastMagnetometerSet = true;
        }

        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            // Computes the inclination matrix I as well as the rotation matrix R transforming
            // a vector from the device coordinate system to the world's coordinate system which
            // is defined as a direct orthonormal basis, where:
            SensorManager.getRotationMatrix(
                    mRotationMatrix,
                    mInclinationMatrix,
                    mLastAccelerometerValue,
                    mLastMagnetometerValue);

            // Computes the device's orientation based on the rotation matrix.
            SensorManager.getOrientation(
                    mRotationMatrix,
                    mOrientation);

            for(int lIndex =0; lIndex<2; lIndex++){
                //mAccelerometer[i] = Float.toString(mGravs[i]);
                //mMagnetic[i] = Float.toString(mGeoMags[i]);
                mOrientationString[lIndex] = Float.toString(mOrientation[lIndex]);
                mOldOrientationString[lIndex] = Float.toString(mOldOrientation[lIndex]);
            }

            mAzimuth = (int) (Math.toDegrees(mOrientation[0])+360)%360;
            mPitch = (int) Math.round(Math.toDegrees(mOrientation[1]));
            mRoll = (int) Math.round(Math.toDegrees(mOrientation[2]));

            updateAzimuth(mAzimuth);
            updateInfoTxtView();
        }
    }

    /**
     * Called when the accuracy of a sensor has changed.
     *
     * @param aSensor The ID of the sensor being monitored
     * @param aAccuracy The new accuracy (exactitude) of this sensor.
     */
    @Override
    public void onAccuracyChanged(
        Sensor aSensor,
        int aAccuracy) {

        // In the function onAccuracyChanged(Sensor sensor, int accuracy), i can check
        // the accuracy of the device's magnetometer.
        if (aSensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            String lText = aSensor.getName();

            mHasInterference = (aAccuracy < SensorManager.SENSOR_STATUS_ACCURACY_HIGH);

            if (aAccuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH) {
                lText += " " + "Compass sensor seems to work correctly on your mobile!"; //"SENSOR_STATUS_ACCURACY_HIGH";
            } else if (aAccuracy == SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM) {
                lText += " " + "SENSOR_STATUS_ACCURACY_MEDIUM" + " " + "Try to calibrate compass on your Android!\"";
            } else if (aAccuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW) {
                lText += " " + "SENSOR_STATUS_ACCURACY_LOW" + " " + "Try to calibrate compass on your Android!\"";
            } else if (aAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                lText += " " + "SENSOR_STATUS_UNRELIABLE" + " " + "Try to calibrate compass on your Android!";
            }

            Toast.makeText(this, lText, Toast.LENGTH_LONG).show();
        }
    }

    //endregion

    //region GestureListener

    /**
     * Delegate the event to the gesture detector
     */
    @Override
    public boolean onTouchEvent(MotionEvent aEvent) {
        return mGestureDetector.onTouchEvent(aEvent);
    }

    /**
     *
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent aMotionEvent) {
            return true;
        }

        /**
         * Event when double tap occurs
         *
         * @param aMotionEvent
         *
         * @return
         */
        @Override
        public boolean onDoubleTap(MotionEvent aMotionEvent) {
            return true;
        }
    }

    //endregion

    //region AsyncTask

    /**
     * Start an Async search on the endPoint Sparql Server
     *
     */
    private void startAsyncSearch() {

        //todo chf removes for the production
        Location lCurrentUserLocation = null;
                //mGeolocationServices.getCurrentLocation();

        // TODO chf removes when the application works
        if (lCurrentUserLocation == null) {
            mCurrentUserLocation = new Location(MyString.EMPTY_STRING);
            mCurrentUserLocation.setLatitude(46.2333d);
            mCurrentUserLocation.setLongitude(7.35d);
        }

        RetrieveCitizenDataAsyncTask lRetrieveTask =
                new RetrieveCitizenDataAsyncTask(
                        this,
                        RetrieveCitizenDataAsyncTask.ACTION2);

        String lClientServerCommunicationMode =
                mPreferences.getMyStringPref(
                        this,
                        BaseConstants.Attr_ClientServer_Communication,
                        EnumClientServerCommunication.ANDROJENA.toString());

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

        String lCulturalObjectType =
            mPreferences.getMyStringPref(
                this,
                BaseConstants.Attr_TypeOfSearch,
                MyString.EMPTY_STRING);

        int lRadiusOfSearch =
            mPreferences.getMyIntPref(
                this,
                BaseConstants.Attr_Radius_Search,
                BaseConstants.Attr_Default_Radius_Search);

        double lRadius =
            mSpatialGeometryServices.getRadiusInRadian(
                mCurrentUserLocation,
                lRadiusOfSearch);

        String lSubject =
            mPreferences.getMyStringPref(
                this,
                BaseConstants.Attr_Subject_Search_Type,
                MyString.EMPTY_STRING);

        int lLimit = 200;

        String lQuery =
                CitizenRequests.getCulturalObjectsInProximityQuery(
                        lCulturalObjectType,
                        (mCurrentUserLocation.getLatitude() - lRadius),
                        (mCurrentUserLocation.getLatitude() + lRadius),
                        (mCurrentUserLocation.getLongitude() - lRadius),
                        (mCurrentUserLocation.getLongitude() + lRadius),
                        lSubject,
                        lLimit);

        lRetrieveTask.onPreExecuteMessageDisplay = false;

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
            if (!aIntent.getAction().equals(RetrieveCitizenDataAsyncTask.ACTION2)) {
                return;
            }

            CitizenQueryResult lCitizenQueryResult = null;

            Bundle lBundle = aIntent.getExtras();

            try {
                lCitizenQueryResult =
                        lBundle.getParcelable(
                                RetrieveCitizenDataAsyncTask.HTTP_RESPONSE);

            } catch (Exception aExc) {
                Log.i(TAG, aExc.getMessage());
            }

            List<RadarMarker> lMarkers =
                    RadarHelper.getRadarMarkersFromResponse(
                            lCitizenQueryResult,
                            mAzimuth,
                            mCurrentUserLocation,
                            mRadius,
                            mRadarView.getHeight(),
                            mRadarView.getWidth(),
                            true);

            updateMarkers(lMarkers);
            updateRadarText(mNbrOfCulturalObjectsDetected);
        }
    }

    //endregion

}
