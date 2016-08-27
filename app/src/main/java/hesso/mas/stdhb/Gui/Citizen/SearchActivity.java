package hesso.mas.stdhb.Gui.Citizen;

import hesso.mas.stdhb.Base.CitizenEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Communication.Androjena.JenaSparqlWsClient;
import hesso.mas.stdhb.Communication.Rdf4j.Rdf4jSparqlWsClient;
import hesso.mas.stdhb.Communication.Rest.HttpUrlConnection.RestclientWithHttpUrlConnection;
import hesso.mas.stdhb.Communication.Rest.RetrieveCityStoriesDataTask;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;

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
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a view
        setContentView(R.layout.activity_search);

        // Récupération de l'instance bouton préférences
        Button mBtnSearch = (Button)findViewById(R.id.mBtnSearch);

        // Positionner un listener sur ce bouton
        mBtnSearch.setOnClickListener(this);
    }

    /**
     * Méthode déclenchée par le listener lorsqu'un appui sur le bouton se produit
     *
     * @param view
     */
    public void onClick(View view){
        if (view.getId()==R.id.mBtnSearch) {
            TextView mTxtPlace = (TextView)findViewById(R.id.mTxtVille);
            TextView mTxtDate = (TextView)findViewById(R.id.mTxtDate);

            // Get the technology for the server communication configured in the settings
            Preferences lPrefs = new Preferences(this);

            String lCommTechnology =
                    lPrefs.getPrefValue(
                            BaseConstants.Attr_Comm_Technology,
                            MyString.EMPTY_STRING);

            // Rest-Client using library OkHttp
            if (lCommTechnology.equals(EnumClientServerCommunication.OKHTTP.toString())) {

                mOkHttpClient = new OkHttpClient();

                // Do a post through the HttpOk Rest-Client
                HttpClientPost(mOkHttpClient);

                TextView mResult = (TextView)findViewById(R.id.editText);
                mResult.setText(ourTextView);

                return;
            }

            // Rest-Client using standard HttpUrlConnection library
            if (lCommTechnology.equals(EnumClientServerCommunication.REST.toString())) {

                RestclientWithHttpUrlConnection lRequest =
                        new RestclientWithHttpUrlConnection();

                CitizenEndPoint lCitizenEndPoint = new CitizenEndPoint("http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/","CityZenDM");

                String lResponse = lRequest.DoHttpBinding(lCitizenEndPoint);
                Toast.makeText(this, lResponse, Toast.LENGTH_SHORT).show();

                return;
            }

            // RDF4J
            if (lCommTechnology.equals(EnumClientServerCommunication.RDF4J.toString())) {
                CitizenEndPoint lCitizenEndPoint = new CitizenEndPoint("http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/","CityZenDM");


                Rdf4jSparqlWsClient lJenaSparqlWsClient = new Rdf4jSparqlWsClient();

                String lResponse = lJenaSparqlWsClient.DoRequest(lCitizenEndPoint, MyString.EMPTY_STRING);

                Context context = getApplicationContext();
                CharSequence lTextToDisplay = lResponse;
                int lDisplayDuration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, lTextToDisplay, lDisplayDuration);
                toast.show();

                return;
            }

            // ANDROJENA
            if (lCommTechnology.equals(EnumClientServerCommunication.ANDROJENA.toString())) {
                CitizenEndPoint lCitizenEndPoint = new CitizenEndPoint("http://dbpedia.org/sparql", "");

                lCitizenEndPoint.CitizenServerUri(MyString.EMPTY_STRING);

                String lStrSparqlQuery =
                        "PREFIX dbo:<http://dbpedia.org/ontology/>"
                                + "PREFIX : <http://dbpedia.org/resource/>"
                                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#/>"
                                + "select ?URI where {?URI rdfs:label " + "London" + ".}";

                JenaSparqlWsClient lJenaSparqlWsClient = new JenaSparqlWsClient(lCitizenEndPoint, lStrSparqlQuery);

                String lResponse = lJenaSparqlWsClient.DoRequest();

                Context context = getApplicationContext();
                CharSequence text = lResponse;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                return;
            }

            // means technology not implemented
            Context context = getApplicationContext();
            CharSequence text = "The type of server communication " + lCommTechnology + " has not been yet implemented!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            /*OkHttpRestClient lClient = new OkHttpRestClient();
            String getResponse = myString.Empty();

            try {
                getResponse = lClient.doGetRequest("http://www.vogella.com");
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            /*OkHttpRestClient lClient = new OkHttpRestClient();
            String postResponse = myString.Empty();
            String json = lClient.bowlingJson("Jesse", "Jake");

            try {
                postResponse = lClient.doPostRequest("http://www.roundsapp.com/post", json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(postResponse);*/

            /*RestclientWithHttpClient client = new RestclientWithHttpClient("http://dbpedia.org/sparql");
            client.AddParam("service", "http://dbpedia.org/sparql");
            String query = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";
            client.AddParam("query", query);

            try {
                client.Execute(RestclientWithHttpClient.RequestMethod.GET);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String lResponse = client.getResponse();*/
            /*String lUrls = MyString.EMPTY_STRING;
            startAsyncSearch(lUrls);*/

            /*SearchTask lSearchTask = new SearchTask();

            lSearchTask.execute(
                    "myserver.com",
                    mTxtLieu.getText().toString(),
                    mTxtDate.getText().toString());*/

            /*Toast.makeText(this, lResponse, Toast.LENGTH_SHORT).show();*/
        }
    }

    /**
     * Start an Async Search on the endPoint Sparql Server
     *
     * @param urls
     */
    private void startAsyncSearch(String... urls) {

        new RetrieveCityStoriesDataTask(this, MyString.EMPTY_STRING).execute(urls);
    }

    ProgressDialog progress;
    private String ourTextView;
    private static final String TAG = "AATestFragment";

    /**
     * Our Broadcast Receiver. We get notified that the data is ready this way.
     */
    private BroadcastReceiver receiver = new BroadcastReceiver()

    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // clear the progress indicator
            if (progress != null)
            {
                progress.dismiss();
            }

            String response = intent.getStringExtra(RetrieveCityStoriesDataTask.HTTP_RESPONSE);

            ourTextView = response;

            Log.i(TAG, "RESPONSE = " + response);
            //
            // my old json code was here. this is where you will parse it.
            //
        }
    };

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
}
