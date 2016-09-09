package hesso.mas.stdhb.Communication.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.Communication.WsClientFactory.IWsClientFactory;
import hesso.mas.stdhb.Communication.WsClientFactory.WsClientFactory;
import hesso.mas.stdhb.Communication.WsEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 20.06.2016.
 *
 * This class represents a thread used to retrieve Data of the Citizen
 * Server.
 */
public class RetrieveCitizenDataAsyncTask2 extends AsyncTask<String, Void, String> {

    private Exception mException;

    private static final String TAG = "RetrieveCitizenDataTask";

    public static final String ACTION1 = "EXECUTE_REQUEST";

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
    public String doInBackground(String... urls) {

        String lPlace = urls[0];
        String lPeriod = urls[1];
        String lQuery = urls[2];
        EnumClientServerCommunication lClientServerCommunicationMode =
                EnumClientServerCommunication.valueOf(urls[3]);

        String lResponse = MyString.EMPTY_STRING;

        try {
            IWsClientFactory lFactory = new WsClientFactory();

            CitizenEndPoint lEndPointWs =
                    new CitizenEndPoint(
                            "http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/",
                            "CityZenDM");

            IWsClient lWsClient =
                    lFactory.Create(
                            lClientServerCommunicationMode,
                            lEndPointWs);

            try {
                lResponse = lWsClient.executeRequest(lQuery);

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