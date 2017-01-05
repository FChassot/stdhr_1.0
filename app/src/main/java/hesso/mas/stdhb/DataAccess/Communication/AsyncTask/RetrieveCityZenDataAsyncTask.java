package hesso.mas.stdhb.DataAccess.Communication.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenQueryResult;

import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CityZenEndPoint;
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
public class RetrieveCityZenDataAsyncTask extends AsyncTask<String, CityZenQueryResult, CityZenQueryResult> {

    private Exception mException;

    public Boolean onPreExecuteMessageDisplay = false;

    private static final String TAG = "RetrieveCityZenDataTask";

    private Context mContext;

    private String mAction;

    private ProgressDialog mProgress;

    /**
     *
     * @param context
     * @param action
     */
    public RetrieveCityZenDataAsyncTask(
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
                    mContext.getResources().getString(R.string.cityZen_search),
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
    public CityZenQueryResult doInBackground(String... urls) {

        String query = urls[0];

        EnumClientServerCommunication clientServerCommunicationMode =
                EnumClientServerCommunication.valueOf(urls[1]);

        CityZenEndPoint endPointWs =
            new CityZenEndPoint(
                BaseConstants.Attr_CityZen_Server_URI,
                BaseConstants.Attr_CityZen_Repository_NAME);

        CityZenQueryResult response = null;

        try {
            IWsClientFactory factory = new WsClientFactory();

            IWsClient wsClient =
                factory.Create(
                    clientServerCommunicationMode,
                    endPointWs);

            try {
                response = wsClient.executeRequest(query);

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
    protected void onPostExecute(
        CityZenQueryResult aCityZenQueryResult) {

        // TODO: check this.exception
        // TODO: do something with the feed
        Log.i(TAG, "RESULT = " + aCityZenQueryResult);

        Intent intent = new Intent();

        intent.setAction(mAction);

        Bundle bundle = new Bundle();

        bundle.putParcelable(mAction, aCityZenQueryResult);
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