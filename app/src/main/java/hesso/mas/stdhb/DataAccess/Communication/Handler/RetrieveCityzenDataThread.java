package hesso.mas.stdhb.DataAccess.Communication.Handler;

import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;

import java.util.ArrayList;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsClientFactory.IWsClientFactory;
import hesso.mas.stdhb.DataAccess.Communication.WsClientFactory.WsClientFactory;
import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CityZenEndPoint;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenQueryResult;
import hesso.mas.stdhb.DataAccess.QueryEngine.Sparql.CityZenRequests;

/**
 * Created by chf on 10.12.2016.
 *
 * A thread is a thread of execution in a program. The Java Virtual Machine allows an
 * application to have multiple threads of execution running concurrently.
 * Thread wo want to do a long task and to give the answer to the UI Thread.
 */
public class RetrieveCityzenDataThread extends Thread {

    // Dependency
    private RetrieveCityZenDataHandler mRetrieveCityZenDataHandler;

    public static String CityZenData = "CityZenData";

    /**
     * Public constructor
     *
     * @param retrieveCityZenDataHandler
     */
    public RetrieveCityzenDataThread(RetrieveCityZenDataHandler retrieveCityZenDataHandler) {
        this.mRetrieveCityZenDataHandler = retrieveCityZenDataHandler;
    }

    /**
     * If this thread was constructed using a separate Runnable run object, then that
     * Runnable object's run method is called; otherwise, this method does nothing and returns.
     */
    public void run() {

        Bundle bundle = new Bundle();

        // Permet d'obtenir du Handler un Message dans lequel on va «glisser» les informations
        // à transmettre (à la fonction handleMessage).
        // Returns a new Message from the global message pool.
        Message message = mRetrieveCityZenDataHandler.obtainMessage();

        // Populate a CityZen Search
        IWsClientFactory factory = new WsClientFactory();

        String query = CityZenRequests.getSubjectQuery();

        CityZenEndPoint endPointWs =
            new CityZenEndPoint(
                BaseConstants.Attr_CityZen_Server_URI,
                BaseConstants.Attr_CityZen_Repository_NAME);

        CityZenQueryResult response = null;

        IWsClient wsClient =
            factory.Create(
                EnumClientServerCommunication.ANDROJENA,
                endPointWs);

        try {
            response = wsClient.executeRequest(query);
        }
        catch (Exception aExc) {
            aExc.printStackTrace();
        }

        bundle.putParcelableArrayList(
            CityZenData,
            (ArrayList<? extends Parcelable>)response.Results());

        message.setData(bundle);

        // Allow to deposit (FIFO) a message in the message's queue.
        mRetrieveCityZenDataHandler.sendMessage(message);
    }

}