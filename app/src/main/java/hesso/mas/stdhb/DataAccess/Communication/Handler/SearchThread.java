package hesso.mas.stdhb.DataAccess.Communication.Handler;

import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsClientFactory.IWsClientFactory;
import hesso.mas.stdhb.DataAccess.Communication.WsClientFactory.WsClientFactory;
import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;
import hesso.mas.stdhb.DataAccess.QueryEngine.Sparql.CitizenRequests;

import java.util.*;

/**
 * Created by chf on 10.12.2016.
 *
 * A thread is a thread of execution in a program. The Java Virtual Machine allows an
 * application to have multiple threads of execution running concurrently.
 * Thread wo want to do a long task and to give the answer to the UI Thread.
 */
public class SearchThread extends Thread {

    // Dependency
    private SearchHandler mSearchHandler;

    public static String CityZenData = "CityZenData";

    /**
     * Public constructor
     *
     * @param aSearchHandler
     */
    public SearchThread(SearchHandler aSearchHandler) {
        this.mSearchHandler = aSearchHandler;
    }

    /**
     * If this thread was constructed using a separate Runnable run object, then that
     * Runnable object's run method is called; otherwise, this method does nothing and returns.
     */
    public void run() {

        Bundle lBundle = new Bundle();

        // Permet d'obtenir du Handler un Message dans lequel on va «glisser» les informations
        // à transmettre (à la fonction handleMessage).
        // Returns a new Message from the global message pool.
        Message lMessage = mSearchHandler.obtainMessage();

        // Do the CityZen Search
        IWsClientFactory lFactory = new WsClientFactory();

        String lQuery = CitizenRequests.getSubjectQuery();

        CitizenEndPoint lEndPointWs =
            new CitizenEndPoint(
                BaseConstants.Attr_Citizen_Server_URI,
                BaseConstants.Attr_Citizen_Repository_NAME);

        CitizenQueryResult lResponse = null;

        IWsClient lWsClient =
            lFactory.Create(
                EnumClientServerCommunication.ANDROJENA,
                lEndPointWs);

        try {
            lResponse = lWsClient.executeRequest(lQuery);
        }
        catch (Exception aExc) {
            aExc.printStackTrace();
        }

        lBundle.putParcelableArrayList(
            CityZenData,
            (ArrayList<? extends Parcelable>)lResponse.Results());

        lMessage.setData(lBundle);

        // Allow to deposit (FIFO) a message in the message's queue.
        mSearchHandler.sendMessage(lMessage);
    }

}