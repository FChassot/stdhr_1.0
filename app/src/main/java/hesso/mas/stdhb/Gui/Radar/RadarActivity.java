package hesso.mas.stdhb.Gui.Radar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Basemodel;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Gui.Citizen.SearchActivity;
import hesso.mas.stdhb.Gui.Config.SettingsActivity;
import hesso.mas.stdhb.Gui.GoogleMap.MapsActivity;
import hesso.mas.stdhb.Gui.MainActivity;
import hesso.mas.stdhb.Services.RetrieveCitizenDataAsyncTask;
import hesso.mas.stdhbtests.R;

public class RadarActivity extends AppCompatActivity {

    RadarView mRadarView = null;

    android.os.Handler mHandler = null;

    private Receiver mReceiver;

    Button mBtnStopRadar = null;

    TextView mNbrObjectDetected = null;

    /**
     * Called when the activity is first created. This is where you should do all of your normal static set up:
     * create views, bind data to lists, etc. This method also provides you with a Bundle containing the activity's
     * previously frozen state, if there was one. Always followed by onStart().
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);

        mRadarView = (RadarView) findViewById(R.id.radarView);
        mBtnStopRadar = (Button)findViewById(R.id.mBtnStopRadar);
        ImageView mImgBack = (ImageView)findViewById(R.id.mImgBack);
        ImageView mImgRadarInfo = (ImageView)findViewById(R.id.mImgRadarInfo);
        TextView mRadiusInfo = (TextView)findViewById(R.id.mDtxtRadiusInfo);
        mNbrObjectDetected = (TextView)findViewById(R.id.mDTxtViewNbrObject);

        // A Handler allows you to send and process Message
        // and Runnable objects associated with a thread's MessageQueue.
        Handler mHandler = new android.os.Handler();

        mReceiver = new Receiver();

        IntentFilter lFilter =
                new IntentFilter(RetrieveCitizenDataAsyncTask.ACTION2);

        this.registerReceiver(mReceiver, lFilter);

        startAsyncSearch();

        this.startAnimation(mRadarView);

        Preferences lPrefs = new Preferences(this);

        Integer lRadiusOfSearch =
                lPrefs.getPrefValue(
                        BaseConstants.Attr_Search_Radius,
                        Basemodel.NULL_KEY);

        mRadiusInfo.setText("Radius of search: " + lRadiusOfSearch + "[m]");

        updateRadarText(mNbrObjectDetected);

        //startUpdateMarkersFromCitizen();

        //assert mBtnStopRadar != null;
        //mBtnStopRadar.setOnClickListener(this);

        //assert mImgBack != null;
        //mImgBack.setOnClickListener(this);
    }

    private void updateRadarText(TextView aTextView) {
        if (mRadarView.getMarkers() != null) {
            aTextView.setText(
                    mRadarView.getMarkers().length-1 +
                            " cultural objects in the radar");
        }
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

        this.stopAnimation(mRadarView);
    }

    /**
     *
     * @param aView
     */
    public void stopAnimation(View aView) {
        if (mRadarView != null) mRadarView.stopAnimation();
    }

    /**
     *
     * @param aMarkers
     */
    public synchronized void updateMarkers(RadarMarker[] aMarkers) {
        if (mRadarView != null) mRadarView.updateMarkers(aMarkers);
    }

    /**
     *
     * @param aView
     */
    public void startAnimation(View aView) {
        if (mRadarView != null) mRadarView.startAnimation();
    }

    Runnable mTick = new Runnable() {
        @Override
        public void run() {
            startAsyncSearch();
            mHandler.postDelayed(this, 10);
        }
    };

    /**
     * This method allows to start the animation
     */
    public void startUpdateMarkersFromCitizen() {
        mHandler.removeCallbacks(mTick);
        mHandler.post(mTick);
    }

    /**
     * Start an Async Search on the endPoint Sparql Server
     *
     */
    private void startAsyncSearch() {

        RetrieveCitizenDataAsyncTask lRetrieveTask =
                new RetrieveCitizenDataAsyncTask(this, "SEARCH_CULTURAL_OBJECTS");

        lRetrieveTask.execute(MyString.EMPTY_STRING, MyString.EMPTY_STRING);
    }

    private class Receiver extends BroadcastReceiver {
        /**
         * Our Broadcast Receiver. We get notified that the data is ready this way.
         */
        @Override
        public void onReceive(Context aContext, Intent aIntent)
        {
            String lResponse =
                    aIntent.getStringExtra(
                            RetrieveCitizenDataAsyncTask.HTTP_RESPONSE);

            RadarMarker lMarker1 = new RadarMarker(0, 0, Color.BLUE);
            RadarMarker lMarker2 = new RadarMarker(120, 150, Color.RED);
            RadarMarker lMarker3 = new RadarMarker(150, 201, Color.RED);

            RadarMarker lMarkers[] = new RadarMarker[3];

            lMarkers[0] = lMarker3;
            lMarkers[1] = lMarker2;
            lMarkers[2] = lMarker1;

            updateMarkers(lMarkers);
            updateRadarText(mNbrObjectDetected);
        }
    }

    private void updateButtonText() {
        if (mBtnStopRadar.getText() == "STOP RADAR")
        {mBtnStopRadar.setText("START RADAR");}
        else {mBtnStopRadar.setText("STOP RADAR");}
    }

    /**
     * The onClick() method is called when a button is actually clicked (or tapped).
     * This method is called by the listener.
     */
    public void onClick(View view){
        if (view.getId()==R.id.mBtnStopRadar){
            this.stopAnimation(this.mRadarView);
            this.updateButtonText();
        }
        if (view.getId()==R.id.mImgBack){
            Intent intent = new Intent(RadarActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
