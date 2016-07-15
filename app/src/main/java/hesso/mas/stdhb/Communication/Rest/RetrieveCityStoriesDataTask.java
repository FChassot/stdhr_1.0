package hesso.mas.stdhb.Communication.Rest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Frédéric Chassot on 20.06.2016.
 *
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

    /**
     * Override this method to perform a computation on a background thread.
     * The specified parameters are the parameters passed to execute(Params...) by the caller
     * of this task.
     * This method can call publishProgress(Progress...) to publish updates on the UI thread.
     * @param urls
     */
    protected String doInBackground(String... urls) {

        String lResponse;

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

    /*
     * Cet événement est appelé sur le UI thread directement après l'exécution
     * de la méthode doInBackground(). Le paramètre result est la valeur retourné par
     * la méthode doInBackground.
     *
     * Runs on the UI thread after doInBackground(Params...).
     *The specified result is the value returned by doInBackground(Params...).
     */
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