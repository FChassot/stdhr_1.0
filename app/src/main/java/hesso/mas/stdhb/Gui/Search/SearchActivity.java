package hesso.mas.stdhb.Gui.Search;

import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Communication.Rest.RetrieveCityStoriesDataTask;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;

import hesso.mas.stdhbtests.R;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Frédéric Chassot on 11.05.2016.
 * This is the Activity in which the user will be able to request the
 * sematic datas stored on the external triplestore
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Récupération de l'instance bouton préférences
        Button mBtnSearch = (Button)findViewById(R.id.btnRSearch);

        // Positionner un listener sur ce bouton
        mBtnSearch.setOnClickListener(this);
    }

    // Méthode déclenchée par le listener lorsqu'un appui sur le bouton se produit
    public void onClick(View view){
        if (view.getId()==R.id.btnRSearch) { // C'est notre bouton ? oui, alors affichage d'un message
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
            String lUrls = MyString.EMPTY_STRING;
            startAsyncSearch(lUrls);

            /*Toast.makeText(this, lResponse, Toast.LENGTH_SHORT).show();*/
        }
    }

    /**
     * Start an Async Search on the endPoint Sparql Server
     * @param urls
     */
    private void startAsyncSearch(String... urls) {

        new RetrieveCityStoriesDataTask(this, MyString.EMPTY_STRING).execute(urls);
    }

    ProgressDialog progress;
    private TextView ourTextView;
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

            ourTextView.setText(response);

            Log.i(TAG, "RESPONSE = " + response);
            //
            // my old json code was here. this is where you will parse it.
            //
        }
    };
}
