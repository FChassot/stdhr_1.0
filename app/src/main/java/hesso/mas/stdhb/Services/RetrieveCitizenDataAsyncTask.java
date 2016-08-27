package hesso.mas.stdhb.Services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import hesso.mas.stdhb.Base.CitizenEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Communication.Androjena.JenaSparqlWsClient;
import hesso.mas.stdhb.Communication.Rdf4j.Rdf4jSparqlWsClient;
import hesso.mas.stdhb.Communication.Rest.HttpClient.RestclientWithHttpClient;

/**
 * Created by chf on 20.06.2016.
 *
 * This class represents a thread used to retrieve Data of the City-Stories
 * Endpoint Sparql.
 */
public class RetrieveCitizenDataAsyncTask extends AsyncTask<String, Void, String> {

    private Exception exception;

    private static final String TAG = "RetrCityStoriesDataTask";
    public static final String HTTP_RESPONSE = "httpResponse";

    private Context mContext;
    private ProgressDialog mProgress;

    public RetrieveCitizenDataAsyncTask(Context aContext)
    {
        mContext = aContext;
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        mProgress = ProgressDialog.show(mContext, "Signing in", "Please wait while we are signing you in..");
    }

    /**
     * Override this method to perform a computation on a background thread.
     * The specified parameters are the parameters passed to execute(Params...) by the caller
     * of this task.
     *
     * This method can call publishProgress(Progress...) to publish updates on the UI thread.
     *
     * @param urls
     */
    protected String doInBackground(String... urls) {

        String lResponse = MyString.EMPTY_STRING;
        String lPlace = urls[0];
        String lDate = urls[0];

        try {
            CitizenEndPoint lEndPointWs = new CitizenEndPoint();

            lEndPointWs.CitizenServerUri("http://dbpedia.org/sparql");

            String lStrSparqlQuery = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";

            JenaSparqlWsClient lJenaSparqlWsClient =
                    new JenaSparqlWsClient(lEndPointWs, lStrSparqlQuery);

            try {
                lResponse = lJenaSparqlWsClient.DoRequest();

            } catch (Exception e) {
                e.printStackTrace();
            }
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
    protected void onPostExecute(String aResult) {
        // TODO: check this.exception
        // TODO: do something with the feed
        Log.i(TAG, "RESULT = " + aResult);

        Intent intent = new Intent();

        intent.putExtra(HTTP_RESPONSE, aResult);

        // broadcast the completion
        mContext.sendBroadcast(intent);
    }

    @Override
    protected void onProgressUpdate(Void... values) {}
}