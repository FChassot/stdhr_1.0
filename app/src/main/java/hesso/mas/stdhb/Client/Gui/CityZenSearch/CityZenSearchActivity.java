package hesso.mas.stdhb.Client.Gui.CityZenSearch;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import hesso.mas.stdhb.Base.Connectivity.NetworkConnectivity;
import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Notifications.Notifications;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Validation.ValidationDescCollection;

import hesso.mas.stdhb.Client.Gui.CityZen.CityZenActivity;
import hesso.mas.stdhb.Client.Gui.Config.SettingsActivity;
import hesso.mas.stdhb.Client.Gui.GoogleMap.MapsActivity;
import hesso.mas.stdhb.Client.Gui.Main.MainActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarMarker;
import hesso.mas.stdhb.DataAccess.Communication.Handler.RetrieveCityZenDataHandler;
import hesso.mas.stdhb.DataAccess.Communication.Handler.RetrieveCityzenDataThread;
import hesso.mas.stdhb.Client.Gui.Validation.Validator;

import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenQueryResult;
import hesso.mas.stdhb.DataAccess.QueryEngine.Sparql.CityZenRequests;
import hesso.mas.stdhb.DataAccess.Communication.AsyncTask.RetrieveCityZenDataAsyncTask;

import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 11.06.2016.
 *
 * This is the Activity in which the user will be able to request the
 * semantic data stored on the external triplestore
 */
public class CityZenSearchActivity extends AppCompatActivity implements View.OnClickListener {

    // Variable of type OkHttpClient
    //OkHttpClient mOkHttpClient;

    // An handler allows you to send and process message
    // and Runnable objects associated with a thread's MessageQueue.
    private RetrieveCityZenDataHandler mRetrieveCityZenDataHandler;

    private RetrieveCityzenDataThread mRetrieveCityzenDataThread;

    // Constant
    private static final String TAG = "CityZenSearchActivity";

    public static final String AsyncTaskAction = "AsyncTask_for_SearchActivity";

    // Member variables
    private Preferences mPrefs;

    private NetworkConnectivity mConnectivity;

    private Receiver mReceiver;

    private ProgressDialog progress;

    private PowerManager.WakeLock mWakeLock;

    private CityZenQueryResult mCityZenQueryResult;

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

        mConnectivity = new NetworkConnectivity(this);

        // To retrieve the button in that UI that you need to interact with programmatically
        Spinner lCboSubject = (Spinner) findViewById(R.id.mDcboSujet);
        Button lBtnSearch = (Button) findViewById(R.id.mBtnSearch);
        ImageView lImgBack = (ImageView) findViewById(R.id.mImgBack);

        // Find the views that was identified by an id attribute
        final TextView mTxtPlace = (TextView) findViewById(R.id.mTxtPlace);
        final TextView mTxtPeriod = (TextView) findViewById(R.id.mTxtPeriod);

        lBtnSearch.requestFocus();

        mTxtPlace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mTxtPlace.getText().toString().equals("")) {
                        mTxtPlace.setText("Place");
                    }
                } else {
                    if (mTxtPlace.getText().toString().equals("Place")) {
                        mTxtPlace.setText("Louvie");
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
                        mTxtPeriod.setText("1900-2016");
                    }
                }
            }
        });

        // The Handler ist create in the UI Thread
        this.mRetrieveCityZenDataHandler =
                new RetrieveCityZenDataHandler(
                        lCboSubject,
                        mCityZenQueryResult,
                        this);

        // RetrieveCityzenDataThread share the Handler with the activity
        this.mRetrieveCityzenDataThread = new RetrieveCityzenDataThread(this.mRetrieveCityZenDataHandler);

        // The Thread is started
        this.mRetrieveCityzenDataThread.start();

        // Set a listener of this button
        assert lBtnSearch != null;
        lBtnSearch.setOnClickListener(this);

        assert lImgBack != null;
        lImgBack.setOnClickListener(this);

        mReceiver = new Receiver();

        IntentFilter filter = new IntentFilter(AsyncTaskAction);
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

        Button mBtnSearch = (Button) findViewById(R.id.mBtnSearch);
        mBtnSearch.requestFocus();
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
     * @param view
     */
    public void onClick(View view) {

        if (view.getId() == R.id.mBtnSearch) {
            if (!mConnectivity.isActive() || !mConnectivity.isNetworkAvailable()) {
                Notifications.ShowMessageBox(this, "Sorry! unable to search CityZen Data [internet network not active]", "Warning", "Ok");
                return;
            }

            // Get the technology used for the communication between the
            // client and the server. This is configured in the shared-preferences.
            String clientServerCommunicationMode =
                    mPrefs.getMyStringPref(
                            this,
                            BaseConstants.Attr_ClientServer_Communication,
                            EnumClientServerCommunication.ANDROJENA.toString());

            TextView txtViewPlace = (TextView) findViewById(R.id.mTxtPlace);
            TextView txtViewPeriod = (TextView) findViewById(R.id.mTxtPeriod);

            ValidationDescCollection valDescCollection =
                    Validator.ValidateSearch(
                            txtViewPlace.getText().toString(),
                            txtViewPeriod.getText().toString());

            if (valDescCollection.any()) {
                Notifications.ShowMessageBox(
                        this,
                        valDescCollection,
                        "Warning",
                        "Ok"
                );

                return;
            }

            Spinner lSubjectSpinner = (Spinner) findViewById(R.id.mDcboSujet);
            String lPlace = txtViewPlace.getText().toString();
            String lBegin = txtViewPeriod.getText().toString().substring(0, 4);
            String lEnd = txtViewPeriod.getText().toString().substring(5, 9);

            String request =
                    CityZenRequests.getCulturalObjectQueryByTitleAndDate(
                            lPlace,
                            Integer.parseInt(lBegin),
                            Integer.parseInt(lEnd),
                            lSubjectSpinner.getSelectedItem().toString());

            startAsyncSearch(
                request,
                clientServerCommunicationMode,
                true);
        }
        if (view.getId() == R.id.mImgBack) {
            Intent intent = new Intent(CityZenSearchActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    //region asyncTask (to request the Citizen Endpoint)

    /**
     * Start an Async Search on a Sparql endPoint
     *
     * @param request                  represents the sparql request
     * @param clientServerArchitecture provides the type of architecture choosen
     *                                  for the communication with the server
     * @param displaySearchmsg         when true a wait-message will be displayed on the
     *                                  screen until the response has been received from the
     *                                  server
     */
    private void startAsyncSearch(
            String request,
            String clientServerArchitecture,
            boolean displaySearchmsg) {

        // Start method tracing with default log name and buffer size.
        Debug.startMethodTracing("myapp_stdhr");

        if (clientServerArchitecture.equals(EnumClientServerCommunication.ANDROJENA.toString())) {
            RetrieveCityZenDataAsyncTask retrieveTask =
                    new RetrieveCityZenDataAsyncTask(
                            this,
                            AsyncTaskAction);

            retrieveTask.onPreExecuteMessageDisplay = displaySearchmsg;

            retrieveTask.execute(
                    request,
                    clientServerArchitecture);

            return;
        }
        else {
            RetrieveCityZenDataAsyncTask retrieveTask =
                    new RetrieveCityZenDataAsyncTask(
                            this,
                            AsyncTaskAction);

            retrieveTask.onPreExecuteMessageDisplay = displaySearchmsg;

            retrieveTask.execute(
                    request,
                    clientServerArchitecture);

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
        public void onReceive(Context context, Intent aIntent) {

            // Stop method tracing.
            Debug.stopMethodTracing();

            // The bundle object contains a mapping from String keys to various Parcelable values.
            Bundle bundle = aIntent.getExtras();

            CityZenQueryResult citizenQueryResult = null;

            try {
                // The bundle should contain the SPARQL Result
                citizenQueryResult =
                        bundle.getParcelable(AsyncTaskAction);

            } catch (Exception aExc) {
                Log.i(TAG, aExc.getMessage());
            }

            if (citizenQueryResult != null && citizenQueryResult.Count() > 0) {
                CityZenDbObject culturalObject = citizenQueryResult.Results().get(0);

                String title = culturalObject.GetValue("title");
                String objectId = culturalObject.GetValue("culturalInterest");

                RadarMarker selectedMarker = new RadarMarker();
                selectedMarker.setTitle(title);
                selectedMarker.setObjectId(objectId);

                Intent intent = new Intent(context, CityZenActivity.class);

                // The bundle object contains a mapping from String keys
                // to various Parcelable values.
                bundle = new Bundle();

                bundle.putParcelable(MapsActivity.RADAR_MARKER, selectedMarker);

                intent.putExtras(bundle);

                context.startActivity(intent);
            }
            else {
                Notifications.ShowMessageBox(
                        context,
                        "No results found! Try giving other parameters!",
                        "Information",
                        "Ok");
            }
        }
    }

    //endregion

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
                Intent intent = new Intent(CityZenSearchActivity.this, SettingsActivity.class);
                startActivity(intent);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(menuItem);
        }
    }


}