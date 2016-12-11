package hesso.mas.stdhb.Client.Gui.Search;

import java.util.Date;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Notifications.Notifications;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Base.Validation.ValidationDescCollection;

import hesso.mas.stdhb.Client.Gui.Citizen.CityZenActivity;
import hesso.mas.stdhb.Client.Gui.GoogleMap.MapsActivity;
import hesso.mas.stdhb.Client.Gui.Main.MainActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarMarker;
import hesso.mas.stdhb.DataAccess.Communication.Handler.SearchHandler;
import hesso.mas.stdhb.DataAccess.Communication.Handler.SearchThread;
import hesso.mas.stdhb.Client.Gui.Validation.Validator;

import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;
import hesso.mas.stdhb.DataAccess.QueryEngine.Sparql.CitizenRequests;
import hesso.mas.stdhb.DataAccess.Communication.Services.RetrieveCitizenDataAsyncTask;
import hesso.mas.stdhb.DataAccess.Communication.Services.RetrieveCitizenDataAsyncTask2;

import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 11.06.2016.
 *
 * This is the Activity in which the user will be able to request the
 * semantic data stored on the external triplestore
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    // Variable of type OkHttpClient
    //OkHttpClient mOkHttpClient;

    // An handler allows you to send and process message
    // and Runnable objects associated with a thread's MessageQueue.
    private SearchHandler mSearchHandler;

    private SearchThread mSearchThread;

    // Constant
    private static final String TAG = "SearchActivity";

    // Member variables
    private Preferences mPrefs;

    private Receiver mReceiver;

    private ProgressDialog progress;

    private PowerManager.WakeLock mWakeLock;

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
        setContentView(R.layout.activity_search);

        mPrefs = new Preferences(this);

        // To retrieve the button in that UI that you need to interact with programmatically
        Button mBtnSearch = (Button) findViewById(R.id.mBtnSearch);
        ImageView lImgBack = (ImageView) findViewById(R.id.mImgBack);

        // Finds the views that was identified by an id attribute
        final TextView mTxtPlace = (TextView) findViewById(R.id.mTxtPlace);
        final TextView mTxtPeriod = (TextView) findViewById(R.id.mTxtPeriod);

        // Set a listener of this button
        assert mBtnSearch != null;
        mBtnSearch.setOnClickListener(this);

        mTxtPlace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mTxtPlace.getText().toString().equals("")) {
                        mTxtPlace.setText("Place");
                    }
                } else {
                    if (mTxtPlace.getText().toString().equals("Place")) {
                        mTxtPlace.setText(MyString.EMPTY_STRING);
                    }
                }
            }
        });

        mTxtPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mTxtPeriod.getText().toString().equals("")) {
                        mTxtPeriod.setText("Period");
                    }
                } else {
                    if (mTxtPeriod.getText().toString().equals("Period")) {
                        mTxtPeriod.setText(MyString.EMPTY_STRING);
                    }
                }
            }
        });

        Spinner lCboSubject = (Spinner) findViewById(R.id.mDcboSujet);

        // The Handler ist create in the UI Thread
        this.mSearchHandler = new SearchHandler(lCboSubject, this);
        // SearchThread share the Handler with the activity
        this.mSearchThread = new SearchThread(this.mSearchHandler);
        // The Thread is started
        this.mSearchThread.start();

        assert lImgBack != null;
        lImgBack.setOnClickListener(this);

        mReceiver = new Receiver();

        IntentFilter lFilter = new IntentFilter(RetrieveCitizenDataAsyncTask2.ACTION1);
        this.registerReceiver(mReceiver, lFilter);

        PowerManager lPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = lPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "My Tag");
        mWakeLock.acquire();

    }

    /**
     * When the user resumes your activity from the Paused state, the system calls the onResume() method.
     * <p>
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
    public void onClick(View aView) {

        if (aView.getId() == R.id.mBtnSearch) {
            // Get the technology used for the communication between the
            // client and the server. This is configured in the shared-preferences.
            String lClientServerCommunicationMode =
                    mPrefs.getMyStringPref(
                            this,
                            BaseConstants.Attr_ClientServer_Communication,
                            EnumClientServerCommunication.ANDROJENA.toString());

            TextView mTxtPlace = (TextView) findViewById(R.id.mTxtPlace);
            TextView mTxtPeriod = (TextView) findViewById(R.id.mTxtPeriod);

            ValidationDescCollection lValDescCollection =
                    Validator.ValidateSearch(
                            mTxtPlace.getText().toString(),
                            mTxtPeriod.getText().toString());

            if (lValDescCollection.any()) {
                Notifications.ShowMessageBox(
                        this,
                        lValDescCollection,
                        "Warning",
                        "Ok"
                );

                return;
            }

            String lPlace = "Essence pour les vins";
            //mTxtPlace.getText().toString()
            String lRequest =
                    CitizenRequests.getCulturalObjectQueryByTitleAndDate(
                            lPlace,
                            new Date(19000101),
                            new Date(99990101));

            startAsyncSearch(
                lRequest,
                lClientServerCommunicationMode,
                true);
        }
        if (aView.getId() == R.id.mImgBack) {
            Intent lIntent = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(lIntent);
        }
    }

    //region asyncTask (to request the Citizen Endpoint)

    /**
     * Start an Async Search on a Sparql endPoint
     *
     * @param aRequest                  represents the sparql request
     * @param aClientServerArchitecture provides the type of architecture choosen
     *                                  for the communication with the server
     * @param aDisplaySearchmsg         when true a wait-message will be displayed on the
     *                                  screen until the response has been received from the
     *                                  server
     */
    private void startAsyncSearch(
            String aRequest,
            String aClientServerArchitecture,
            boolean aDisplaySearchmsg) {

        if (aClientServerArchitecture.equals(EnumClientServerCommunication.ANDROJENA)) {
            RetrieveCitizenDataAsyncTask lTask =
                    new RetrieveCitizenDataAsyncTask(
                            this,
                            RetrieveCitizenDataAsyncTask.HTTP_CITIZEN_DATA);

            lTask.onPreExecuteMessageDisplay = aDisplaySearchmsg;

            lTask.execute(
                    aRequest,
                    aClientServerArchitecture);

            return;
        }

        if (aClientServerArchitecture.equals(EnumClientServerCommunication.RDF4J)) {
            RetrieveCitizenDataAsyncTask2 lTask =
                    new RetrieveCitizenDataAsyncTask2(
                            this,
                            RetrieveCitizenDataAsyncTask2.HTTP_CITIZEN_DATA);

            lTask.onPreExecuteMessageDisplay = aDisplaySearchmsg;

            lTask.execute(
                    aRequest,
                    aClientServerArchitecture);

            return;
        } else {
            RetrieveCitizenDataAsyncTask lTask =
                    new RetrieveCitizenDataAsyncTask(
                            this,
                            RetrieveCitizenDataAsyncTask2.HTTP_CITIZEN_DATA);

            lTask.onPreExecuteMessageDisplay = aDisplaySearchmsg;

            lTask.execute(
                    aRequest,
                    aClientServerArchitecture);

            return;
        }
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
        public void onReceive(Context aContext, Intent aIntent) {

            // The bundle object contains a mapping from String keys to various Parcelable values.
            Bundle lBundle = aIntent.getExtras();

            CitizenQueryResult lCitizenQueryResult = null;

            try {
                // The bundle should contain the SPARQL Result
                lCitizenQueryResult =
                        lBundle.getParcelable(
                                RetrieveCitizenDataAsyncTask.HTTP_CITIZEN_DATA);

            } catch (Exception aExc) {
                Log.i(TAG, aExc.getMessage());
            }

            if (lCitizenQueryResult != null && lCitizenQueryResult.Count() > 0) {
                CitizenDbObject lCulturalObject = lCitizenQueryResult.Results().get(0);

                String lTitle = lCulturalObject.GetValue("title");
                String lResourceUri = lCulturalObject.GetValue("image_url");

                RadarMarker lSelectedMarker = new RadarMarker();
                lSelectedMarker.setTitle(lTitle);
                lSelectedMarker.setObjectId(lResourceUri);

                Intent lIntent = new Intent(aContext, CityZenActivity.class);

                // The bundle object contains a mapping from String keys
                // to various Parcelable values.
                lBundle = new Bundle();

                lBundle.putParcelable(MapsActivity.RADAR_MARKER, lSelectedMarker);

                lIntent.putExtras(lBundle);

                aContext.startActivity(lIntent);
            }
            else {
                Notifications.ShowMessageBox(
                        aContext,
                        "None object found! Try with other parameters!",
                        "Information",
                        "Ok");
            }
        }
    }

    //endregion
}
