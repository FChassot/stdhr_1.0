package hesso.mas.stdhb.DataAccess.QueryEngine;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;

import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsClientFactory.IWsClientFactory;
import hesso.mas.stdhb.DataAccess.Communication.WsClientFactory.WsClientFactory;
import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;

import java.util.List;

/**
 * Created by chf on 07.10.2016.
 *
 * This class allows to initialize a type of client-server communication and to
 * send a request towards an endoint
 */
public class QueryEngine {

    public static List<CitizenDbObject> request(String aQuery) {

        EnumClientServerCommunication lClientServerCommunicationMode =
                EnumClientServerCommunication.ANDROJENA;

        CitizenEndPoint lEndPointWs =
                new CitizenEndPoint(
                        BaseConstants.Attr_Citizen_Server_URI,
                        BaseConstants.Attr_Citizen_Repository_NAME);

        CitizenQueryResult lResponse;

        IWsClientFactory lFactory = new WsClientFactory();

        IWsClient lWsClient =
                lFactory.Create(
                        lClientServerCommunicationMode,
                        lEndPointWs);

        try {
            lResponse = lWsClient.executeRequest(aQuery);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return lResponse.Results();
    }
}
