package hesso.mas.stdhb.DataAccess.Communication.WsClientFactory;

import hesso.mas.stdhb.DataAccess.Communication.WsClient.Rest.HttpUrlConnection.RestWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CityZenEndPoint;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;

import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsClient.Androjena.JenaSparqlWsClient;

/**
 * Created by chf on 23.08.2016.
 *
 * Factory to create IWsClient objects.
 */
public class WsClientFactory implements IWsClientFactory {

    /**
     * @see
     *
     * @param clientServerCommunication
     * @param cityZenEndPoint
     *
     * @return a IWsClient Instance
     */
    public IWsClient Create(
        EnumClientServerCommunication clientServerCommunication,
        CityZenEndPoint cityZenEndPoint) {

        IWsClient wsClient  = null;

        if (clientServerCommunication == EnumClientServerCommunication.ANDROJENA) {
            wsClient = new JenaSparqlWsClient(cityZenEndPoint);
        }

        if (clientServerCommunication == EnumClientServerCommunication.REST) {
            wsClient = new RestWsClient(cityZenEndPoint);
        }

        if (wsClient == null) {
            wsClient = new JenaSparqlWsClient(cityZenEndPoint);
        }

        return wsClient;
    }
}
