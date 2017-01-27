package hesso.mas.stdhb.Client.Gui.CityZen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import hesso.mas.stdhb.Base.Connectivity.NetworkConnectivity;
import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Notifications.Notifications;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Base.Tools.StringUtil;
import hesso.mas.stdhb.Business.Spatial.SpatialGeometryServices;
import hesso.mas.stdhb.Client.Gui.CityZenSearch.CityZenSearchActivity;
import hesso.mas.stdhb.Client.Gui.Config.SettingsActivity;
import hesso.mas.stdhb.Client.Gui.GoogleMap.MapsActivity;
import hesso.mas.stdhb.Client.Gui.Main.MainActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarMarker;;
import hesso.mas.stdhb.DataAccess.Communication.AsyncTask.RetrieveCityZenDataAsyncTask;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenQueryResult;
import hesso.mas.stdhb.DataAccess.QueryEngine.Sparql.SparqlRequests;
import hesso.mas.stdhbtests.R;

import static hesso.mas.stdhbtests.R.id.imageView;

public class CityZenActivity extends AppCompatActivity implements View.OnClickListener {

    // Constant
    private static final String TAG = "CityZenSearchActivity";

    public static final String CityZenAction = "AsyncTask_for_CityZenActivity";

    // Member variables
    private Preferences mPrefs;

    private Receiver mReceiver;

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

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set the activity content to an explicit view
        setContentView(R.layout.activity_cityzen);

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

            String request =
                    SparqlRequests.getCulturalObjectInfoByObjectIdQuery(
                            mCulturalObjectMarker.getTitle(),
                            mCulturalObjectMarker.getObjectId());

            startAsyncSearch(
                    request,
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
                }
                else{
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

        IntentFilter filter = new IntentFilter(CityZenAction);
        this.registerReceiver(mReceiver, filter);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "My Tag");
        mWakeLock.acquire();

    }

    /**
     * Method to specify the options menu
     *
     * @param menu
     *
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add the actionmenu entries to the ActionBar
        getMenuInflater().inflate(R.menu.actionsearchmenu, menu);
        return true;
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
            RetrieveCityZenDataAsyncTask task =
                    new RetrieveCityZenDataAsyncTask(
                            this,
                            CityZenAction);

            task.onPreExecuteMessageDisplay = displaySearchmsg;

            task.execute(
                    request,
                    clientServerArchitecture);

            return;
        }
        else {
            RetrieveCityZenDataAsyncTask lTask =
                    new RetrieveCityZenDataAsyncTask(
                            this,
                            CityZenAction);

            lTask.onPreExecuteMessageDisplay = displaySearchmsg;

            lTask.execute(
                    request,
                    clientServerArchitecture);

            return;
        }
    }

    /**
     * Calculate the distance between the mobile system and an CityZen object.
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

            CityZenQueryResult cityZenQueryResult = null;

            try {
                // The bundle should contain the SPARQL Result
                cityZenQueryResult = bundle.getParcelable(CityZenAction);

            } catch (Exception aExc) {
                Log.i(TAG, aExc.getMessage());
            }

            if (cityZenQueryResult != null && cityZenQueryResult.Count() > 0) {
                CityZenDbObject culturalObject = cityZenQueryResult.Results().get(0);

                mDescription = culturalObject.GetValue("description");
                TextView mTxtDescription = (TextView)findViewById(R.id.mTxtDescription);
                mTxtDescription.setText(mDescription);
                TextView mTxtViewPosition = (TextView)findViewById(R.id.mTxtViewPosition);
                mTxtViewPosition.setText(getStrLocation(mCulturalObjectMarker));

                NetworkConnectivity networkConnectivity = new NetworkConnectivity(context);
                String resourceUri = culturalObject.GetValue("image_url");

                if(networkConnectivity.isNetworkAvailable()) {
                    if (true) {
                        // Use of the Picasso library to load images
                        //ImageView imgView = (ImageView) findViewById(imageView);
                        //Picasso.with(context).load(lResourceUri).into(imgView);
                        //Notifications.ShowMessageBox(context, lResourceUri, "Image Url", "ok");
                        // Use of Glide to load images
                        ImageView imgView = (ImageView) findViewById(imageView);
                        Glide.with(context).load(resourceUri).into(imgView);
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

    /**
     * This hook is called whenever an item in your options menu is selected.
     *
     * @param menuItem The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(CityZenActivity.this, SettingsActivity.class);
                startActivity(intent);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_search:
                Intent intent2 = new Intent(CityZenActivity.this, CityZenSearchActivity.class);
                startActivity(intent2);
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
