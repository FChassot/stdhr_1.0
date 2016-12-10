package hesso.mas.stdhb.Client.Gui.Search.Handler;

import android.os.Bundle;
import android.os.Message;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsClientFactory.IWsClientFactory;
import hesso.mas.stdhb.DataAccess.Communication.WsClientFactory.WsClientFactory;
import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;
import hesso.mas.stdhb.DataAccess.QueryEngine.Sparql.CitizenRequests;

/**
 * Created by chf on 10.12.2016.
 *
 * Thread wo want to do a long task and to give the answer to the UI Thread.
 */
public class SearchThread extends Thread {

    // Dependencie
    private SearchHandler mSearchHandler;

    private boolean mContinue = true;

    /**
     *
     * @param aSearchHandler
     */
    public SearchThread(SearchHandler aSearchHandler) {
        this.mSearchHandler = aSearchHandler;
    }

    public void run() {
        Message lMessage = null;

        Bundle lBundle = new Bundle();

        int i=0;

        //while (mContinue){

            // Permet d'obtenir du Handler un Message dans lequel on va «glisser» les informations
            // à transmettre (à la fonction handleMessage).
            lMessage = mSearchHandler.obtainMessage();

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

            } catch (Exception e) {
                e.printStackTrace();
            }

            String lItem = "no data";

            if (lResponse != null) {
                for (CitizenDbObject lObjet : lResponse.Results()) {
                    lItem = lObjet.GetValue("subject");
                }
            }

            lBundle.putString("Sesame Data", lItem);

            lMessage.setData(lBundle);

            // permet à un Thread partageant ce Handler (avec un autre Thread (E.g.
            // ThreadCreateur)) de déposer (FIFO) un Message dans la file de Messages.
            // Généralement cette méthode sera appelée à partir du run() (de cet autre
            //Thread).
            mSearchHandler.sendMessage(lMessage);
            //try { Thread.sleep(10000L); } catch (InterruptedException e) {}
            i++;
        //}
    }

}