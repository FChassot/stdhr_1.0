package hesso.mas.stdhb.DataAccess.QueryEngine;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;

import hesso.mas.stdhb.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.Communication.WsClientFactory.IWsClientFactory;
import hesso.mas.stdhb.Communication.WsClientFactory.WsClientFactory;
import hesso.mas.stdhb.Communication.WsEndPoint.CitizenEndPoint;

import java.util.List;

/**
 * Created by chf on 07.10.2016.
 */
public class QueryEngine {

    public static List<CitizenDbObject> request(String aQuery) {

        String lQuery = aQuery;

        EnumClientServerCommunication lClientServerCommunicationMode =
                EnumClientServerCommunication.ANDROJENA;

        CitizenEndPoint lEndPointWs =
                new CitizenEndPoint(
                        BaseConstants.Attr_Citizen_Server_URI,
                        BaseConstants.Attr_Citizen_Repository_NAME);

        hesso.mas.stdhb.DataAccess.QueryEngine.CitizenQueryResult lResponse = null;

        IWsClientFactory lFactory = new WsClientFactory();

        IWsClient lWsClient =
                lFactory.Create(
                        lClientServerCommunicationMode,
                        lEndPointWs);

        try {
            lResponse = lWsClient.executeRequest(lQuery);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return lResponse.Results();
    }
}
