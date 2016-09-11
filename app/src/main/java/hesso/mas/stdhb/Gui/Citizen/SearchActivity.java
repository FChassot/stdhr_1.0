package hesso.mas.stdhb.Gui.Citizen;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.QueryBuilder.CitizenRequests;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Communication.Services.RetrieveCitizenDataAsyncTask;

import android.content.IntentFilter;
import android.os.PowerManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;

import hesso.mas.stdhb.Communication.Services.RetrieveCitizenDataAsyncTask2;
import hesso.mas.stdhbtests.R;
/*import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;*/

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by chf on 11.06.2016.
 *
 * This is the Activity in which the user will be able to request the
 * semantic datas stored on the external triplestore
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    // Variable of type OkHttpClient
    //OkHttpClient mOkHttpClient;

    private boolean mReceiverStarted;

    private Receiver mReceiver;

    ProgressDialog progress;

    private String mTextView;

    private static final String TAG = "AsynSearch";

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

        // create a view
        setContentView(R.layout.activity_search);

        // to retrieve the button in that UI that you need to interact with programmatically
        Button mBtnSearch = (Button)findViewById(R.id.mBtnSearch);

        // Set a listener of this button
        mBtnSearch.setOnClickListener(this);

        mReceiverStarted = true;
        mReceiver = new Receiver();

        PowerManager lPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = lPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "My Tag");
        mWakeLock.acquire();

        IntentFilter lFilter = new IntentFilter(RetrieveCitizenDataAsyncTask2.ACTION1);
        this.registerReceiver(mReceiver, lFilter);
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
            //Log.e(MatabbatManager.TAG, getClass() + " Releasing receivers-" + e.getMessage());
        }
    }

    /**
     * Method processed by the listener when one button is clicked
     *
     * @param view
     */
    public void onClick(View view){
        if (view.getId()==R.id.mBtnSearch) {

            // Get the technology using for the communication between the
            // client and the server. This is configured in the preferences.
            Preferences lPrefs = new Preferences(this);

            String lClientServerCommunicationMode =
                    lPrefs.getPrefValue(
                            BaseConstants.Attr_ClientServer_Communication,
                            MyString.EMPTY_STRING);

            TextView mTxtPlace = (TextView)findViewById(R.id.mTxtVille);
            TextView mTxtPeriod = (TextView)findViewById(R.id.mTxtPeriode);

            String lPlace = mTxtPlace.getText().toString();
            String lPeriod = mTxtPeriod.getText().toString();

            String lRequest = CitizenRequests.GetCulturalObjectsTyp();

            startAsyncSearch(
                    lPlace,
                    lPeriod,
                    lRequest,
                    lClientServerCommunicationMode,
                    true);
        }
    }

    //region asyncTask (to request the Citizen Endpoint)

        /**
         * Start an Async Search on a Sparql endPoint
         *
         * @param aPlace
         * @param aPeriod
         */
        private void startAsyncSearch(
                String aPlace,
                String aPeriod,
                String aRequest,
                String aClientServerArchitecture,
                Boolean aDisplaySearchmessage) {

            if (aClientServerArchitecture.equals(EnumClientServerCommunication.ANDROJENA)) {
                RetrieveCitizenDataAsyncTask lTask =
                        new RetrieveCitizenDataAsyncTask(
                                this,
                                RetrieveCitizenDataAsyncTask.ACTION1);

                lTask.onPreExecuteMessageDisplay = aDisplaySearchmessage;

                lTask.execute(
                        aPlace,
                        aPeriod,
                        aRequest,
                        aClientServerArchitecture);

                return;
            }

            if (aClientServerArchitecture.equals(EnumClientServerCommunication.RDF4J)) {
                RetrieveCitizenDataAsyncTask2 lTask =
                        new RetrieveCitizenDataAsyncTask2(
                                this,
                                RetrieveCitizenDataAsyncTask2.ACTION1);

                lTask.onPreExecuteMessageDisplay = aDisplaySearchmessage;

                lTask.execute(
                        aPlace,
                        aPeriod,
                        aRequest,
                        aClientServerArchitecture);

                return;
            }

            else {
                RetrieveCitizenDataAsyncTask lTask =
                        new RetrieveCitizenDataAsyncTask(
                                this,
                                RetrieveCitizenDataAsyncTask2.ACTION1);

                lTask.onPreExecuteMessageDisplay = aDisplaySearchmessage;

                lTask.execute(
                        aPlace,
                        aPeriod,
                        aRequest,
                        aClientServerArchitecture);

                return;
            }

            // means technology not implemented
            /*Context context = getApplicationContext();
            CharSequence text = "The type of server communication " + aClientServerCommunication + " has not been yet implemented!";

            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();*/
        }

        private class Receiver extends BroadcastReceiver {
            /**
             * Our Broadcast Receiver. We get notified that the data is ready this way.
             */
            @Override
            public void onReceive(Context aContext, Intent aIntent)
            {
                // clear the progress indicator
                if (progress != null)
                {
                    progress.dismiss();
                }

                String lResponse =
                        aIntent.getStringExtra(
                                RetrieveCitizenDataAsyncTask.HTTP_RESPONSE);

                if (lResponse.equals(MyString.EMPTY_STRING)) {
                    mTextView = "no response from the server!";
                } else {
                    mTextView = lResponse;
                }

                TextView mResult = (TextView)findViewById(R.id.mEditTxtCitizenResult);

                mResult.setText(mTextView);

                Log.i(TAG, "RESPONSE = " + lResponse);
            }
        }

    //endregion (

    /**
     *
     */
   /* public void HttpClientPost(OkHttpClient aOkHttpClient){
        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
        String myJson = "{}";

        //.url("https://api.github.com/users/florent37")
        //post Request
        Request myGetRequest = new Request.Builder()
                .url("http://dbpedia.org/page/Berlin")
                .post(RequestBody.create(JSON_TYPE, myJson))
                .build();

        aOkHttpClient.newCall(myGetRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {
            }

            @Override
            public void onResponse(Call request, Response response) throws IOException {
                //le retour est effectué dans un thread différent
                final String text = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView = text;
                        TextView mResult = (TextView)findViewById(R.id.mEditTxtCitizenResult);
                        mResult.setText(mTextView);
                    }
                });
            }
        });
    }*/
}
