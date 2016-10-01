package hesso.mas.stdhb.Gui.Citizen;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Notifications.Notifications;
import hesso.mas.stdhb.Base.QueryBuilder.Response.CitizenDbObject;
import hesso.mas.stdhb.Base.QueryBuilder.Response.CitizenQueryResult;
import hesso.mas.stdhb.Base.QueryBuilder.Request.CitizenRequests;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Base.Validation.ValidationDescCollection;
import hesso.mas.stdhb.Communication.Services.RetrieveCitizenDataAsyncTask;

import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;

import hesso.mas.stdhb.Communication.Services.RetrieveCitizenDataAsyncTask2;
import hesso.mas.stdhb.Gui.Validation.GuiValidation;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by chf on 11.06.2016.
 *
 * This is the Activity in which the user will be able to request the
 * semantic datas stored on the external triplestore
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    // Variable of type OkHttpClient
    //OkHttpClient mOkHttpClient;

    private Receiver mReceiver;

    ProgressDialog progress;

    private String mTextView;

    private static final String TAG = "SearchActivity";

    private PowerManager.WakeLock mWakeLock;

    //final View lView = findViewById(R.id.imageView);

    //final Animation lAnimation =
            //AnimationUtils.loadAnimation(this, R.animator.anim);

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

        // set a listener of this button
        mBtnSearch.setOnClickListener(this);

        final TextView mTxtPlace = (TextView)findViewById(R.id.mTxtVille);
        final TextView mTxtPeriod = (TextView)findViewById(R.id.mTxtPeriode);

        mTxtPlace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if (mTxtPlace.getText().toString().equals("")) {
                        mTxtPlace.setText("Lieu");
                    }
                }else {
                    if (mTxtPlace.getText().toString().equals("Lieu")) {
                        mTxtPlace.setText(MyString.EMPTY_STRING);
                    }
                }
            }
        });

        mTxtPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if (mTxtPeriod.getText().toString().equals("")) {
                        mTxtPeriod.setText("Période");
                    }
                }else {
                    if (mTxtPeriod.getText().toString().equals("Période")) {
                        mTxtPeriod.setText(MyString.EMPTY_STRING);
                    }
                }
            }
        });

        /*ImageView lImageView = (ImageView) findViewById(R.id.imageView);

        lImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lView.startAnimation(lAnimation);
            }
        });*/

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
            Log.e(TAG, getClass() + " Releasing receivers-" + e.getMessage());
        }
    }

    /**
     * Method processed by the listener when one button is clicked
     *
     * @param view
     */
    public void onClick(View view){
        if (view.getId()==R.id.mBtnSearch) {

            // Get the technology used for the communication between the
            // client and the server. This is configured in the shared-preferences.
            Preferences lPrefs = new Preferences(this);

            String lClientServerCommunicationMode =
                    lPrefs.getPrefValue(
                            BaseConstants.Attr_ClientServer_Communication,
                            MyString.EMPTY_STRING);

            TextView mTxtPlace = (TextView)findViewById(R.id.mTxtVille);
            TextView mTxtPeriod = (TextView)findViewById(R.id.mTxtPeriode);

            String lPlace = mTxtPlace.getText().toString();
            String lPeriod = mTxtPeriod.getText().toString();

            ValidationDescCollection lValDescCollection =
                    GuiValidation.ValidateSearch(lPlace, lPeriod);

            if (lValDescCollection.count() > 0) {
                Notifications.ShowMessageBox(
                        this,
                        lValDescCollection,
                        "Warning",
                        "Ok"
                );

                return;
            }
            String lRequest =
                    CitizenRequests.GetCulturalObjectQuery(
                            lPlace,
                            new Date(19000101),
                            new Date(99990101));

            startAsyncSearch(
                lRequest,
                lClientServerCommunicationMode,
                true);
        }
    }

    //region asyncTask (to request the Citizen Endpoint)

    /**
     * Start an Async Search on a Sparql endPoint
     *
     * @param aRequest represents the sparql request
     * @param aClientServerArchitecture
     * @param aDisplaySearchmessage
     */
        private void startAsyncSearch(
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
                Bundle lBundle = aIntent.getExtras();

                CitizenQueryResult lCitizenQueryResult = null;

                try {
                    lCitizenQueryResult =
                            lBundle.getParcelable(
                                    RetrieveCitizenDataAsyncTask.HTTP_RESPONSE);

                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }

                if (lCitizenQueryResult != null) {
                    if (lCitizenQueryResult != null && lCitizenQueryResult.Count() > 0) {
                        CitizenDbObject lCulturalInterestObject = lCitizenQueryResult.Results().get(0);
                        //String lImagePreview = lCulturalInterestObject.GetValue("imagePreview");                        String lImagePreview = lCulturalInterestObject.GetValue("imagePreview");
                        String lImagePreview = "https://cave.valais-wallis-digital.ch/media/filer_public/4c/a1/4ca12ada-3fa2-4d61-bdb5-7fa542a0725f/e3f651ad-0671-459b-b256-0332884f47f2.png";

                        if(isNetworkAvailable()){
                            // Creating a new non-ui thread task
                            DownloadTask downloadTask = new DownloadTask();

                            // Starting the task created above
                            downloadTask.execute(lImagePreview);
                        }
                        else{
                            Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }

    //endregion

    //region AsyncTask

    private class DownloadTask extends AsyncTask<String, Integer, Bitmap> {

        Bitmap bitmap = null;

        @Override
        protected Bitmap doInBackground(String... url) {
            try{
                bitmap = downloadUrl(url[0]);

            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap aResult) {
            // Getting a reference to ImageView to display the downloaded image
            ImageView lImageView = (ImageView) findViewById(R.id.imageView);

            // Displaying the downloaded image
            lImageView.setImageBitmap(aResult);

            // Showing a message, on completion of download process
            Toast.makeText(getBaseContext(), "Image downloaded successfully", Toast.LENGTH_SHORT).show();
        }
    }

    //endregion

    /**
     * get is the network is available
     *
     * @return
     */
    private boolean isNetworkAvailable(){

        boolean lIsNetworkAvailable = false;

        // get the system's connectivity service
        ConnectivityManager lConnManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // get the active network interface to get the network's status
        NetworkInfo lNetworkInfo = lConnManager.getActiveNetworkInfo();

        if(lNetworkInfo !=null && lNetworkInfo.isAvailable()) {
            lIsNetworkAvailable = true;
        }

        // return the status of the network
        return lIsNetworkAvailable;
    }

    /**
     *
     *
     * @param strUrl
     * @return
     * @throws IOException
     */
    private Bitmap downloadUrl(String strUrl) throws IOException{

        Bitmap lBitmap=null;

        InputStream lInputStream = null;

        try{
            URL lUrl = new URL(strUrl);

            // create an http connection to communicate with url
            HttpURLConnection lUrlConnection = (HttpURLConnection) lUrl.openConnection();

            // connecting to url
            lUrlConnection.connect();

            // read date from url
            lInputStream = lUrlConnection.getInputStream();

            // create a bitmap from teh stream returned from the url
            lBitmap = BitmapFactory.decodeStream(lInputStream);

        }catch(Exception e){
            Log.d("Exception while downl", e.toString());
        }finally{
            lInputStream.close();
        }
        return lBitmap;
    }

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
