package hesso.mas.stdhb.DataAccess.Communication.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;

import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsClientFactory.IWsClientFactory;
import hesso.mas.stdhb.DataAccess.Communication.WsClientFactory.WsClientFactory;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 20.06.2016.
 *
 * This class represents a thread used to retrieve Data of the City-Stories
 * Endpoint Sparql.
 */
public class RetrieveCitizenDataAsyncTask extends AsyncTask<String, CitizenQueryResult, CitizenQueryResult> {

    private Exception mException;

    public Boolean onPreExecuteMessageDisplay = false;

    private static final String TAG = "RetrieveCitizenDataTask";

    public static final String ACTION1 = "EXECUTE_REQUEST";

    public static final String ACTION2 = "SEARCH_CULTURAL_OBJECTS";

    public static final String ACTION3 = "SEARCH_SUBJECTS";

    public static final String HTTP_RESPONSE = "httpResponse";

    public static final String HTTP_CITYZEN_DATA = "SEARCH_CITYZEN_DATA";

    private Context mContext;

    private String mAction;

    private ProgressDialog mProgress;

    /**
     *
     * @param context
     * @param action
     */
    public RetrieveCitizenDataAsyncTask(
        Context context,
        String action)
    {
        mContext = context;
        mAction = action;
    }

    /**
     * Runs on the UI thread before doInBackground(Params...).
     */
    @Override
    protected void onPreExecute() {
        if (onPreExecuteMessageDisplay) {
            mProgress =
                ProgressDialog.show(
                    mContext,
                    mContext.getResources().getString(R.string.citizen_search),
                    mContext.getResources().getString(R.string.wait));
        }
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
    public CitizenQueryResult doInBackground(String... urls) {

        String lQuery = urls[0];

        EnumClientServerCommunication clientServerCommunicationMode =
                EnumClientServerCommunication.valueOf(urls[1]);

        CitizenEndPoint endPointWs =
            new CitizenEndPoint(
                BaseConstants.Attr_Citizen_Server_URI,
                BaseConstants.Attr_Citizen_Repository_NAME);

        CitizenQueryResult response = null;

        try {
            IWsClientFactory factory = new WsClientFactory();

            IWsClient wsClient =
                factory.Create(
                    clientServerCommunicationMode,
                    endPointWs);

            try {
                response = wsClient.executeRequest(lQuery);

            } catch (Exception e) {
                    e.printStackTrace();
            }
        } catch (Exception e) {
                this.mException = e;
                return null;
        }

        return response;
    }

    /*
     * Runs on the UI thread after doInBackground(Params...).
     * The specified result is the value returned by doInBackground(Params...).
     */
    protected void onPostExecute(CitizenQueryResult aCitizenQueryResult) {
        // TODO: check this.exception
        // TODO: do something with the feed
        Log.i(TAG, "RESULT = " + aCitizenQueryResult);

        Intent intent = new Intent();

        intent.setAction(mAction);

        Bundle bundle = new Bundle();

        bundle.putParcelable(RetrieveCitizenDataAsyncTask.HTTP_RESPONSE, aCitizenQueryResult);
        intent.putExtras(bundle);

        // clear the progress indicator
        if (mProgress != null)
        {
            mProgress.dismiss();
        }

        try{
            mContext.sendBroadcast(intent);

        } catch (Exception e){
            Log.i(TAG, e.getMessage());
        } finally {

        }
    }
}