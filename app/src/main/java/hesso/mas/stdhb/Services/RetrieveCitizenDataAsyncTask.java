package hesso.mas.stdhb.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.Communication.WsClientFactory.IWsClientFactory;
import hesso.mas.stdhb.Communication.WsClientFactory.WsClientFactory;
import hesso.mas.stdhb.Communication.WsEndPoint.CitizenEndPoint;

/**
 * Created by chf on 20.06.2016.
 *
 * This class represents a thread used to retrieve Data of the City-Stories
 * Endpoint Sparql.
 */
public class RetrieveCitizenDataAsyncTask extends AsyncTask<String, Void, String> {

    private Exception mException;

    private static final String TAG = "RetrieveCitizenDataTask";

    public static final String ACTION1 = "EXECUTE_REQUEST";

    public static final String ACTION2 = "SEARCH_CULTURAL_OBJECTS";

    public static final String HTTP_RESPONSE = "httpResponse";

    private Context mContext;

    private String mAction;

    private ProgressDialog mProgress;

    public RetrieveCitizenDataAsyncTask(
            Context aContext,
            String aAction)
    {
        mContext = aContext;
        mAction = aAction;
    }

    /**
     * Runs on the UI thread before doInBackground(Params...).
     */
    @Override
    protected void onPreExecute() {
        mProgress = ProgressDialog.show(mContext, "Searching Data..", "Please wait");
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

        String lPlace = urls[0];
        String lDate = urls[1];
        EnumClientServerCommunication lClientServerCommunicationMode =
                EnumClientServerCommunication.ANDROJENA;
        String lStrSparqlQuery = "select distinct ?Concept where {[] a ?Concept} LIMIT 1";
        String lResponse = MyString.EMPTY_STRING;

        if (mAction == ACTION1) {
            try {
                IWsClientFactory lFactory = new WsClientFactory();

                CitizenEndPoint lEndPointWs = new CitizenEndPoint();
                lEndPointWs.CitizenServerUri("http://dbpedia.org/sparql");

                IWsClient lWsClient =
                        lFactory.Create(
                                lClientServerCommunicationMode,
                                lEndPointWs);

                try {
                    lResponse = lWsClient.executeRequest(lStrSparqlQuery);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                this.mException = e;
                return null;
            }
        }

        if (mAction == ACTION2) {}

        return lResponse;
    }

    /*
     * Cet événement est appelé sur le UI thread directement après l'exécution
     * de la méthode doInBackground(). Le paramètre result est la valeur retourné par
     * la méthode doInBackground.
     *
     * Runs on the UI thread after doInBackground(Params...).
     * The specified result is the value returned by doInBackground(Params...).
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