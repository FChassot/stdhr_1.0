package hesso.mas.stdhb.Gui.CitizenSearch;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.EnumClientServerCommTechnology;
import hesso.mas.stdhb.Base.Storage.Local.Preferences;
import hesso.mas.stdhb.Base.Tools.Basemodel;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Communication.Rest.RetrieveCityStoriesDataTask;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;

import hesso.mas.stdhb.Services.SearchTask;
import hesso.mas.stdhbtests.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by chf on 11.05.2016.
 *
 * This is the Activity in which the user will be able to request the
 * semantic datas stored on the external triplestore
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Récupération de l'instance bouton préférences
        Button mBtnSearch = (Button)findViewById(R.id.mBtnSearch);

        this.okHttpClient = new OkHttpClient();

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
            TextView mTxtLieu = (TextView)findViewById(R.id.mTxtVille);
            TextView mTxtDate = (TextView)findViewById(R.id.mTxtDate);

            Preferences lPrefs = new Preferences(this);

            // Affiche les coordonnées GPS actuel de l'appareil
            Integer lCommTechnology = lPrefs.getPrefValue(BaseConstants.Attr_Comm_Technology, Basemodel.NULL_KEY);

            if (lCommTechnology == 5) {
                HttpClientPost();
                TextView mResult = (TextView)findViewById(R.id.editText);
                mResult.setText(ourTextView);
            } else {

                Context context = getApplicationContext();
                CharSequence text = "The type of server communication has not been yet implemented!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }


            /*HttpBinding request = new HttpBinding();
            CitizenEndPoint lCitizenEndPoint = new CitizenEndPoint();

            lCitizenEndPoint.Service("http://dbpedia.org/sparql");

            //Toast.makeText(this,"Requête HTTP!", Toast.LENGTH_SHORT).show();
            String lResponse = request.DoHttpBinding(lCitizenEndPoint);*/

            /*OkHttpRClient lClient = new OkHttpRClient();
            String getResponse = myString.Empty();

            try {
                getResponse = lClient.doGetRequest("http://www.vogella.com");
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            /*OkHttpRClient lClient = new OkHttpRClient();
            String postResponse = myString.Empty();
            String json = lClient.bowlingJson("Jesse", "Jake");

            try {
                postResponse = lClient.doPostRequest("http://www.roundsapp.com/post", json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(postResponse);*/

            /*RestClient client = new RestClient("http://dbpedia.org/sparql");
            client.AddParam("service", "http://dbpedia.org/sparql");
            String query = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";
            client.AddParam("query", query);

            try {
                client.Execute(RestClient.RequestMethod.GET);
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
    public void HttpClientPost(){

        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
        String myJson = "{}";

        //.url("https://api.github.com/users/florent37")
        //post Request
        Request myGetRequest = new Request.Builder()
                .url("http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/")
                .post(RequestBody.create(JSON_TYPE, myJson))
                .build();

        okHttpClient.newCall(myGetRequest).enqueue(new Callback() {
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
