package hesso.mas.stdhb.Client.Gui.CityZenSearch;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.support.v4.app.ActivityCompat;
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
import hesso.mas.stdhb.Base.Permissions.PermissionUtil;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Base.Validation.ValidationDescCollection;

import hesso.mas.stdhb.Client.Gui.CityZen.CityZenActivity;
import hesso.mas.stdhb.Client.Gui.Config.SettingsActivity;
import hesso.mas.stdhb.Client.Gui.GoogleMap.MapsActivity;
import hesso.mas.stdhb.Client.Gui.Main.MainActivity;
import hesso.mas.stdhb.Client.Gui.Radar.RadarHelper.RadarMarker;
import hesso.mas.stdhb.DataAccess.Communication.Handler.RetrieveCityZenDataHandler;
import hesso.mas.stdhb.DataAccess.Communication.Handler.RetrieveCityZenDataThread;
import hesso.mas.stdhb.Client.Gui.Validation.Validator;

import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenQueryResult;
import hesso.mas.stdhb.DataAccess.QueryEngine.Sparql.SparqlRequests;
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

    private RetrieveCityZenDataThread mRetrieveCityZenDataThread;

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

    final int PERMISSION_ALL = 1;

    private Boolean NoPermission = true;

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

        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(!PermissionUtil.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        // Set the activity content to an explicit view
        setContentView(R.layout.activity_cityzen_search);

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
                        mTxtPlace.setText("Keyword");
                    }
                } else {
                    if (mTxtPlace.getText().toString().equals("Keyword")) {
                        mTxtPlace.setText("");
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

        // RetrieveCityZenDataThread share the Handler with the activity
        this.mRetrieveCityZenDataThread = new RetrieveCityZenDataThread(this.mRetrieveCityZenDataHandler);

        // The Thread is started
        this.mRetrieveCityZenDataThread.start();

        // Set a listener of this button
        assert lBtnSearch != null;
        lBtnSearch.setOnClickListener(this);

        assert lImgBack != null;
        lImgBack.setOnClickListener(this);

        mReceiver = new Receiver();

        IntentFilter filter = new IntentFilter(AsyncTaskAction);
        this.registerReceiver(mReceiver, filter);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, TAG);
        mWakeLock.acquire();

    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_ALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    NoPermission = false;
                    // permission was granted! Do the task you need to do.
                } else {
                    NoPermission = true;
                    // permission denied! Disable the functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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

            if (NoPermission == true) {
                String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if(!PermissionUtil.hasPermissions(this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                    NoPermission = true;
                } {
                    NoPermission = false;
                }

                return;
            }

            // Get the technology used for the communication between the
            // client and the server. This is configured in the shared-preferences.
            String clientServerCommunicationMode =
                    mPrefs.getMyStringPref(
                            this,
                            BaseConstants.Attr_ClientServer_Communication,
                            EnumClientServerCommunication.ANDROJENA.toString());

            TextView txtViewKeyword = (TextView) findViewById(R.id.mTxtPlace);
            TextView txtViewPeriod = (TextView) findViewById(R.id.mTxtPeriod);
            Spinner lSubjectSpinner = (Spinner) findViewById(R.id.mDcboSujet);

            String subject = lSubjectSpinner.getSelectedItem().toString();
            String keyWord = txtViewKeyword.getText().toString();
            String period = txtViewPeriod.getText().toString();

            ValidationDescCollection valDescCollection =
                    Validator.ValidateSearch(
                            keyWord,
                            period,
                            subject);

            String examplePeriod = getExamplePeriod(period);

            if (!examplePeriod.equals(MyString.EMPTY_STRING)) {txtViewPeriod.setText(examplePeriod);}

            if (valDescCollection.any()) {
                Notifications.ShowMessageBox(
                        this,
                        valDescCollection,
                        "Warning",
                        "Ok"
                );

                return;
            }

            String periodBegin = txtViewPeriod.getText().toString().substring(0, 4);
            String periodEnd = txtViewPeriod.getText().toString().substring(5, 9);

            String request =
                    SparqlRequests.getCulturalObjectQueryByTitleAndDate(
                            subject,
                            keyWord,
                            Integer.parseInt(periodBegin),
                            Integer.parseInt(periodEnd));

            //Notifications.ShowMessageBox(this, request, "test", "test");

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

    /**
     *
     * @param aPeriod
     * @return
     */
    private String getExamplePeriod(String aPeriod) {
        if (!aPeriod.equals(MyString.EMPTY_STRING)) {
            if (aPeriod.length() != 9) {
                return "1900-2016";
            } else {
                return MyString.EMPTY_STRING;
            }
        } else {return "1900-2016";}
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
        Debug.startMethodTracing("STDHR_Trace");

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

            CityZenQueryResult cityzenQueryResult = null;

            try {
                // The bundle should contain the SPARQL Result
                cityzenQueryResult =
                        bundle.getParcelable(AsyncTaskAction);

            } catch (Exception aExc) {
                Log.i(TAG, aExc.getMessage());
            }

            if (cityzenQueryResult != null && cityzenQueryResult.Count() > 0) {
                CityZenDbObject culturalObject = cityzenQueryResult.Results().get(0);

                String title = culturalObject.GetValue("title");
                String objectId = culturalObject.GetValue("culturalInterest");
                String start = culturalObject.GetValue("start");
                String end = culturalObject.GetValue("end");
                String latitude = culturalObject.GetValue("lat");
                String longitude = culturalObject.GetValue("long");
                Double lDblLat = 0.0;
                Double lDblLong = 0.0;

                if (!latitude.equals(MyString.EMPTY_STRING)) {
                    lDblLat  = Double.parseDouble(latitude);
                }
                if (!longitude.equals(MyString.EMPTY_STRING)) {
                    lDblLong  = Double.parseDouble(longitude);
                }

                RadarMarker selectedMarker = new RadarMarker();
                selectedMarker.setTitle(title);
                selectedMarker.setObjectId(objectId);
                selectedMarker.setStart(start);
                selectedMarker.setEnd(end);
                selectedMarker.setLatitude(lDblLat);
                selectedMarker.setLongitude(lDblLong);

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
