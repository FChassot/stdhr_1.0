package hesso.mas.stdhb.Communication.Rest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import hesso.mas.stdhb.Base.MyString;
import hesso.mas.stdhb.QueryEngine.RestClient;

/**
 * Created by Frédéric Chassot on 20.06.2016.
 * This class represents a thread used to retrieve Data of the City-Stories
 * Endpoint Sparql.
 */
public class RetrieveCityStoriesDataTask extends AsyncTask<String, Void, String> {

    private Exception exception;

    private static final String TAG = "AARestTask";
    public static final String HTTP_RESPONSE = "httpResponse";

    private Context mContext;
    private HttpClient mClient;
    private String mAction;

    public RetrieveCityStoriesDataTask(Context context, String action)
    {
        mContext = context;
        mAction = action;
        mClient = new DefaultHttpClient();
    }

    public RetrieveCityStoriesDataTask(Context context, String action, HttpClient client)
    {
        mContext = context;
        mAction = action;
        mClient = client;
    }

    protected String doInBackground(String... urls) {

        String lResponse = MyString.Empty();

        try {
            RestClient client = new RestClient("http://dbpedia.org/sparql");
            client.AddParam("service", "http://dbpedia.org/sparql");
            String query = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";
            client.AddParam("query", query);

            try {
                client.Execute(RestClient.RequestMethod.GET);
            } catch (Exception e) {
                e.printStackTrace();
            }

            lResponse = client.getResponse();

        } catch (Exception e) {
            this.exception = e;
            return null;
        }

        return lResponse;
    }

    protected void onPostExecute(String result) {
        // TODO: check this.exception
        // TODO: do something with the feed
        Log.i(TAG, "RESULT = " + result);

        Intent intent = new Intent(mAction);

        intent.putExtra(HTTP_RESPONSE, result);

        // broadcast the completion
        mContext.sendBroadcast(intent);
    }
}