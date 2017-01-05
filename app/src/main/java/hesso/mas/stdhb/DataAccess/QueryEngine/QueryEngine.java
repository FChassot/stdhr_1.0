package hesso.mas.stdhb.DataAccess.QueryEngine;

import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;

import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsClientFactory.IWsClientFactory;
import hesso.mas.stdhb.DataAccess.Communication.WsClientFactory.WsClientFactory;
import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CityZenEndPoint;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenQueryResult;

import java.util.List;

/**
 * Created by chf on 07.10.2016.
 *
 * This class allows to initialize a type of client-server communication and to
 * send a request towards an endoint
 */
public class QueryEngine {

    public static List<CityZenDbObject> request(String aQuery) {

        EnumClientServerCommunication clientServerCommunicationMode =
                EnumClientServerCommunication.ANDROJENA;

        CityZenEndPoint endPointWs =
                new CityZenEndPoint(
                        BaseConstants.Attr_CityZen_Server_URI,
                        BaseConstants.Attr_CityZen_Repository_NAME);

        CityZenQueryResult response;

        IWsClientFactory lFactory = new WsClientFactory();

        IWsClient wsClient =
                lFactory.Create(
                        clientServerCommunicationMode,
                        endPointWs);

        try {
            response = wsClient.executeRequest(aQuery);

        } catch (Exception aExc) {
            aExc.printStackTrace();
            return null;
        }

        return response.Results();
    }
}
