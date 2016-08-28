package hesso.mas.stdhb.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import hesso.mas.stdhb.Base.CitizenEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Communication.Androjena.JenaSparqlWsClient;
import hesso.mas.stdhb.Communication.Rdf4j.Rdf4jSparqlWsClient;

/**
 * Created by chf on 20.06.2016.
 *
 * This class represents a thread used to retrieve Data of the City-Stories
 * Endpoint Sparql.
 */
public class RetrieveCitizenDataAsyncTask2 extends AsyncTask<String, Void, String> {

    private Exception mException;

    private static final String TAG = "RetrCityStoriesDataTask";
    public static final String HTTP_RESPONSE = "httpResponse";

    private Context mContext;

    private String mAction;

    private ProgressDialog mProgress;

    public RetrieveCitizenDataAsyncTask2(
            Context aContext,
            String aAction)
    {
        mContext = aContext;
        mAction = aAction;
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        mProgress = ProgressDialog.show(mContext, "Downloading Data..", "Please wait");
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
        String lDate = urls[1];

        try {
            CitizenEndPoint lEndPointWs = new CitizenEndPoint();
            lEndPointWs.CitizenServerUri("http://dbpedia.org/sparql");

            String lQuery = "select distinct ?Concept where {[] a ?Concept} LIMIT 1";

            Rdf4jSparqlWsClient lRdf4jSparqlWsClient =
                    new Rdf4jSparqlWsClient(lEndPointWs, lQuery);

            try {
                lResponse =
                        lRdf4jSparqlWsClient.DoRequest(
                                lEndPointWs,
                                MyString.EMPTY_STRING);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            this.mException = e;
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

        Intent lIntent = new Intent();

        lIntent.setAction(mAction);
        lIntent.putExtra(HTTP_RESPONSE, aResult);

        // clear the progress indicator
        if (mProgress != null)
        {
            mProgress.dismiss();
        }

        // broadcast the completion
        mContext.sendBroadcast(lIntent);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        //setProgressPercent(progress[0]);
    }
}