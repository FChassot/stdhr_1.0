package hesso.mas.stdhb.Client.Gui.Citizen;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import hesso.mas.stdhb.Base.Connectivity.NetworkConnectivity;
import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Notifications.Notifications;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Base.Tools.StringUtil;
import hesso.mas.stdhb.Business.Spatial.SpatialGeometryServices;
import hesso.mas.stdhb.Client.Gui.GoogleMap.MapsActivity;
import hesso.mas.stdhb.Client.Gui.Main.MainActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarMarker;;
import hesso.mas.stdhb.DataAccess.Communication.AsyncTask.RetrieveCitizenDataAsyncTask;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;
import hesso.mas.stdhb.DataAccess.QueryEngine.Sparql.CitizenRequests;
import hesso.mas.stdhbtests.R;

import static hesso.mas.stdhbtests.R.id.imageView;

public class CityZenActivity extends AppCompatActivity implements View.OnClickListener {

    // Constant
    private static final String TAG = "SearchActivity";

    // Member variables
    private Preferences mPrefs;

    private Receiver mReceiver;

    private ProgressDialog progress;

    private String mDescription = MyString.EMPTY_STRING;
    private String mTitle = MyString.EMPTY_STRING;

    private PowerManager.WakeLock mWakeLock;

    boolean lIsImageFitToScreen;
    ImageView mImageView;

    private RadarMarker mCulturalObjectMarker;

    /**
     * Called when the activity is first created. This is where you should do all of your
     * normal static set up: create views, bind data to lists, etc. This method also provides
     * you with a Bundle containing the activity's previously frozen state, if there was one.
     * Always followed by onStart().
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity content to an explicit view
        setContentView(R.layout.activity_city_zen);

        mPrefs = new Preferences(this);

        // Finds the views that was identified by an id attribute
        TextView mTitle = (TextView)findViewById(R.id.textView);
        TextView mTxtDescription = (TextView)findViewById(R.id.mTxtDescription);
        mTxtDescription.setSingleLine(false);
        mImageView = (ImageView) findViewById(imageView);
        ImageView lImgBack = (ImageView)findViewById(R.id.mImgBack);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            // To retrieve the cultural object selected in the radar view
            mCulturalObjectMarker = bundle.getParcelable(MapsActivity.RADAR_MARKER);

            if (mCulturalObjectMarker != null) {
                mTitle.setText(mCulturalObjectMarker.getTitle());

                if (!StringUtil.isNullOrBlank(mCulturalObjectMarker.getDescription())) {
                    mTxtDescription.setText(mCulturalObjectMarker.getDescription());
                }

                mDescription = mCulturalObjectMarker.getDescription();
            }

            String clientServerCommunicationMode =
                    mPrefs.getMyStringPref(
                            this,
                            BaseConstants.Attr_ClientServer_Communication,
                            EnumClientServerCommunication.ANDROJENA.toString());

            String lRequest =
                    CitizenRequests.getCulturalObjectInfoByObjectIdQuery(
                            mCulturalObjectMarker.getTitle(),
                            mCulturalObjectMarker.getObjectId());

            startAsyncSearch(
                    lRequest,
                    clientServerCommunicationMode,
                    false);
        }

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lIsImageFitToScreen) {
                    lIsImageFitToScreen=false;
                    mImageView.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                    mImageView.setAdjustViewBounds(true);
                }else{
                    lIsImageFitToScreen=true;
                    mImageView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
                    mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });

        assert mTxtDescription != null;
        mTxtDescription.setOnClickListener(this);

        assert lImgBack != null;
        lImgBack.setOnClickListener(this);

        mReceiver = new Receiver();

        IntentFilter lFilter = new IntentFilter(RetrieveCitizenDataAsyncTask.ACTION1);
        this.registerReceiver(mReceiver, lFilter);

        PowerManager lPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = lPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "My Tag");
        mWakeLock.acquire();

    }

    /**
     * When the user resumes your activity from the Paused state, the system calls the onResume() method.
     *
     * Be aware that the system calls this method every time your activity comes into the foreground,
     * including when it's created for the first time. As such, you should implement onResume() to initialize
     * components that you release during onPause() and perform any other initializations that must occur each time
     * the activity enters the Resumed state (such as begin animations and initialize components only used while
     * the activity has user focus).
     */
    @Override
    public void onResume() {
        super.onResume();
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
            mWakeLock.release();                      //keep screen on

        } catch (Exception e) {
            Log.e(TAG, getClass() + " Releasing receivers-" + e.getMessage());
        }
    }

    /**
     * Method processed by the OnClickListener when one button is clicked
     *
     * @param aView
     */
    public void onClick(View aView){

        if (aView.getId()==R.id.mTxtDescription) {
            Notifications.ShowMessageBox(
                    this,
                    mDescription,
                    mTitle,
                    "Close");
        }

        if (aView.getId()== imageView) {

        }
        if (aView.getId()==R.id.mImgBack){
            Intent lIntent = new Intent(CityZenActivity.this, MainActivity.class);
            startActivity(lIntent);
        }
    }

    //region asyncTask (to request the Citizen Endpoint)

    /**
     * Start an Async Search on a Sparql endPoint
     *
     * @param request represents the sparql request
     * @param clientServerArchitecture provides the type of architecture choosen
     *                                  for the communication with the server
     * @param displaySearchmsg when true a wait-message will be displayed on the
     *                          screen until the response has been received from the
     *                          server
     */
    private void startAsyncSearch(
            String request,
            String clientServerArchitecture,
            boolean displaySearchmsg) {

        if (clientServerArchitecture.equals(EnumClientServerCommunication.ANDROJENA)) {
            RetrieveCitizenDataAsyncTask task =
                    new RetrieveCitizenDataAsyncTask(
                            this,
                            RetrieveCitizenDataAsyncTask.ACTION1);

            task.onPreExecuteMessageDisplay = displaySearchmsg;

            task.execute(
                    request,
                    clientServerArchitecture);

            return;
        }
        else {
            RetrieveCitizenDataAsyncTask lTask =
                    new RetrieveCitizenDataAsyncTask(
                            this,
                            RetrieveCitizenDataAsyncTask.ACTION1);

            lTask.onPreExecuteMessageDisplay = displaySearchmsg;

            lTask.execute(
                    request,
                    clientServerArchitecture);

            return;
        }
    }

    /**
     *
     * @param currentLocation
     * @param culturalObjectMarker
     * @return
     */
    private double getDistance(
            Location currentLocation,
            Location culturalObjectMarker) {

        return SpatialGeometryServices.getDistanceBetweenTwoPoints(
                0,
                0,
                culturalObjectMarker.getLatitude(),
                culturalObjectMarker.getLongitude(),
                0,
                0);

    }

    /**
     *
     * @param culturalObjectMarker
     * @return
     */
    private String getStrLocation(
            RadarMarker culturalObjectMarker) {

        return "Position Lat " + culturalObjectMarker.getLatitude() + " Lon " + culturalObjectMarker.getLongitude();

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
        public void onReceive(Context context, Intent intent) {

            // The bundle object contains a mapping from String keys to various Parcelable values.
            Bundle bundle = intent.getExtras();

            CitizenQueryResult citizenQueryResult = null;

            try {
                // The bundle should contain the SPARQL Result
                citizenQueryResult =
                        bundle.getParcelable(
                                RetrieveCitizenDataAsyncTask.HTTP_RESPONSE);

            } catch (Exception aExc) {
                Log.i(TAG, aExc.getMessage());
            }

            if (citizenQueryResult != null && citizenQueryResult.Count() > 0) {
                CitizenDbObject culturalObject = citizenQueryResult.Results().get(0);

                mDescription = culturalObject.GetValue("description");
                TextView mTxtDescription = (TextView)findViewById(R.id.mTxtDescription);
                mTxtDescription.setText(mDescription);
                TextView mTxtViewPosition = (TextView)findViewById(R.id.mTxtViewPosition);
                mTxtViewPosition.setText(getStrLocation(mCulturalObjectMarker));

                NetworkConnectivity lNetworkConnectivity = new NetworkConnectivity(context);
                String lResourceUri = culturalObject.GetValue("image_url");

                if(lNetworkConnectivity.isNetworkAvailable()) {
                    if (true) {
                        // Use of the Picasso library to load images
                        ImageView lImageView = (ImageView) findViewById(imageView);
                        Picasso.with(context).load(lResourceUri).into(lImageView);
                    }
                    else {
                            /*VideoView lVideoView = (VideoView) findViewById(R.id.video_view);

                            Uri lVideoUri = null;

                            lVideoView.setVideoURI(lVideoUri);*/
                    }
                }
                else{
                    Toast.makeText(
                            getBaseContext(),
                            "Network is not available",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //endregion

    //region AsyncTask

    //region Handler

    public void startProgress(View view) {
        // do something long
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 10; i++) {
                    final int value = i;

                    doFakeWork();

                    /*progress.post(new Runnable() {
                        @Override
                        public void run() {
                            text.setText("Updating");
                            progress.setProgress(value);
                        }
                    });*/
                }
            }
        };

        new Thread(runnable).start();
    }

    // Simulating something timeconsuming
    private void doFakeWork() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //endregion
}
