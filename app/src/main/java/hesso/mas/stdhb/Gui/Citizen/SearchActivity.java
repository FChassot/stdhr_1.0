package hesso.mas.stdhb.Gui.Citizen;

import hesso.mas.stdhb.Base.CitizenEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.Communication.WsClientFactory.IWsClientFactory;
import hesso.mas.stdhb.Communication.WsClientFactory.WsClientFactory;
import hesso.mas.stdhb.Services.RetrieveCitizenDataAsyncTask;

import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;

import hesso.mas.stdhb.Services.RetrieveCitizenDataAsyncTask2;
import hesso.mas.stdhbtests.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by chf on 11.06.2016.
 *
 * This is the Activity in which the user will be able to request the
 * semantic datas stored on the external triplestore
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    // Variable of type OkHttpClient
    OkHttpClient mOkHttpClient;
    private boolean mReceiverStarted;
    private Receiver mReceiver;

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

        // Récupération de l'instance bouton préférences
        Button mBtnSearch = (Button)findViewById(R.id.mBtnSearch);

        // Positionner un listener sur ce bouton
        mBtnSearch.setOnClickListener(this);

        mReceiverStarted = true;
        mReceiver = new Receiver();

        IntentFilter lFilter = new IntentFilter("LOAD_DATA");
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
            //mWakeLock.release();//keep screen on
        } catch (Exception e) {
            //Log.e(MatabbatManager.TAG, getClass() + " Releasing receivers-" + e.getMessage());
        }
    }

    /**
     * Méthode déclenchée par le listener lorsqu'un appui sur le bouton se produit
     *
     * @param view
     */
    public void onClick(View view){
        if (view.getId()==R.id.mBtnSearch) {
            // Get the technology for the server communication configured in the settings
            Preferences lPrefs = new Preferences(this);

            TextView mTxtPlace = (TextView)findViewById(R.id.mTxtVille);
            TextView mTxtDate = (TextView)findViewById(R.id.mTxtDate);

            String lPlace = mTxtPlace.getText().toString();
            String lDate = mTxtDate.getText().toString();

            String lCommTechnology =
                    lPrefs.getPrefValue(
                            BaseConstants.Attr_Comm_Technology,
                            MyString.EMPTY_STRING);

            /*IWsClientFactory lFactory = new WsClientFactory();

            CitizenEndPoint lCitizenEndPoint = new CitizenEndPoint();
            lCitizenEndPoint.CitizenServerUri("http://dbpedia.org/sparql");

            IWsClient lWsClient =
                    lFactory.Create(
                        EnumClientServerCommunication.ANDROJENA,
                        lCitizenEndPoint);

            String lQuery = "\"select distinct ?Concept where {[] a ?Concept} LIMIT 1\"";

            String lResponse = lWsClient.DoRequest(lQuery);

            Context context = getApplicationContext();
            CharSequence lTextToDisplay = lResponse;

            Toast toast = Toast.makeText(context, lTextToDisplay, Toast.LENGTH_SHORT);
            toast.show();*/

            EnumClientServerCommunication lTechnology = EnumClientServerCommunication.ANDROJENA;

            if (lCommTechnology.equals(EnumClientServerCommunication.RDF4J.toString())){
                lTechnology = EnumClientServerCommunication.RDF4J;
            }

            startAsyncSearch(
                    lPlace,
                    lDate,
                    lTechnology);

            //return;
            // Rest-Client using library OkHttp
            /*if (lCommTechnology.equals(EnumClientServerCommunication.OKHTTP.toString())) {

                mOkHttpClient = new OkHttpClient();

                // Do a post through the HttpOk Rest-Client
                HttpClientPost(mOkHttpClient);

                TextView mResult = (TextView)findViewById(R.id.editText);
                mResult.setText(ourTextView);

                return;
            }*/

            // Rest-Client using standard HttpUrlConnection library
            /*if (lCommTechnology.equals(EnumClientServerCommunication.REST.toString())) {

                RestclientWithHttpUrlConnection lRequest =
                        new RestclientWithHttpUrlConnection();

                CitizenEndPoint lCitizenEndPoint = new CitizenEndPoint("http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/","CityZenDM");

                String lResponse = lRequest.DoHttpBinding(lCitizenEndPoint);
                Toast.makeText(this, lResponse, Toast.LENGTH_SHORT).show();

                return;
            }*/

            // RDF4J
            /*if (lCommTechnology.equals(EnumClientServerCommunication.RDF4J.toString())) {
                String lCitizenServerUri = "http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/";
                String lCitizenRepository = "CityZenDM";
                String lQuery = "\"select distinct ?Concept where {[] a ?Concept} LIMIT 1\"";

                CitizenEndPoint lCitizenEndPoint =
                        new CitizenEndPoint();

                lCitizenEndPoint.CitizenServerUri(lCitizenServerUri);
                lCitizenEndPoint.CitizenRepository(lCitizenRepository);

                Rdf4jSparqlWsClient lRdf4jSparqlWsClient =
                        new Rdf4jSparqlWsClient(lCitizenEndPoint, lQuery);

                String lResponse = lRdf4jSparqlWsClient.DoRequest(
                        lCitizenEndPoint,
                        MyString.EMPTY_STRING);

                Context context = getApplicationContext();
                CharSequence lTextToDisplay = lResponse;

                Toast toast = Toast.makeText(context, lTextToDisplay, Toast.LENGTH_SHORT);
                toast.show();

                return;
            }*/

            // ANDROJENA
            /*if (lCommTechnology.equals(EnumClientServerCommunication.ANDROJENA.toString())) {
                CitizenEndPoint lCitizenEndPoint = new CitizenEndPoint();

                lCitizenEndPoint.CitizenServerUri("http://dbpedia.org/sparql");

                String lStrSparqlQuery =
                        "PREFIX dbo:<http://dbpedia.org/ontology/>"
                                + "PREFIX : <http://dbpedia.org/resource/>"
                                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#/>"
                                + "select ?URI where {?URI rdfs:label " + "London" + ".}";

                JenaSparqlWsClient lJenaSparqlWsClient = new JenaSparqlWsClient(lCitizenEndPoint, lStrSparqlQuery);

                String lResponse = lJenaSparqlWsClient.DoRequest();

                Context context = getApplicationContext();
                CharSequence text = lResponse;

                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();

                return;
            }*/

            // SOAP
            /*if (lCommTechnology.equals(EnumClientServerCommunication.SOAP.toString())) {
                CitizenEndPoint lCitizenEndPoint = new CitizenEndPoint();

                lCitizenEndPoint.CitizenServerUri("http://dbpedia.org/sparql");

                String lStrSparqlQuery =
                        "PREFIX dbo:<http://dbpedia.org/ontology/>"
                                + "PREFIX : <http://dbpedia.org/resource/>"
                                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#/>"
                                + "select ?URI where {?URI rdfs:label " + "London" + ".}";

                JenaSparqlWsClient lJenaSparqlWsClient = new JenaSparqlWsClient(lCitizenEndPoint);

                String lResponse = lJenaSparqlWsClient.DoRequest(lStrSparqlQuery);

                Context context = getApplicationContext();
                CharSequence text = lResponse;

                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();

                return;
            }*/

            // means technology not implemented
            /*Context context = getApplicationContext();
            CharSequence text = "The type of server communication " + lCommTechnology + " has not been yet implemented!";

            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();

            startAsyncSearch(lPlace, lDate, lCommTechnology);*/
        }
    }

    ProgressDialog progress;

    private String ourTextView;

    private static final String TAG = "AATestFragment";

    /**
     * Start an Async Search on the endPoint Sparql Server
     *
     * @param aPlace
     * @param aDate
     */
    private void startAsyncSearch(
            String aPlace,
            String aDate,
            EnumClientServerCommunication aClientServerCommunication) {

        RetrieveCitizenDataAsyncTask lTask = null;

        if (aClientServerCommunication.equals(EnumClientServerCommunication.ANDROJENA)) {
            lTask = new RetrieveCitizenDataAsyncTask(this, "LOAD_DATA");
            lTask.execute(aPlace, aDate);
            return;
        }

        if (aClientServerCommunication.equals(EnumClientServerCommunication.RDF4J)) {
            RetrieveCitizenDataAsyncTask2 lTask2 = new RetrieveCitizenDataAsyncTask2(this, "LOAD_DATA");
            lTask2.execute(aPlace, aDate);
            return;
        }
    }

    /**
     *
     */
    public void HttpClientPost(OkHttpClient aOkHttpClient){
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
                        ourTextView = text;
                        TextView mResult = (TextView)findViewById(R.id.editText);
                        mResult.setText(ourTextView);
                    }
                });
            }
        });
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

            ourTextView = lResponse;

            TextView mResult = (TextView)findViewById(R.id.editText);

            mResult.setText(ourTextView);

            Log.i(TAG, "RESPONSE = " + lResponse);
            //
            // my old json code was here. this is where you will parse it.
            //
        }
    }
}
