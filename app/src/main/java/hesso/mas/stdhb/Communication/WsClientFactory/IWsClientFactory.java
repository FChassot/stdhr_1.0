package hesso.mas.stdhb.Communication.WsClientFactory;

import hesso.mas.stdhb.Base.SparqlEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Models.EnumClientServerCommunication;
import hesso.mas.stdhb.Communication.WsClient.IWsClient;

/**
 * Created by chf on 23.08.2016.
 *
 * Factory for creating IWsClient objects.
 */
public interface IWsClientFactory {

    /**
     * Factory for the generation of IWsClient instance.
     *
     * @param aClientServerCommunication
     * @param aCitizenEndPoint
     * @return
     */
    IWsClient Create(
            EnumClientServerCommunication aClientServerCommunication,
            CitizenEndPoint aCitizenEndPoint);
}
