package hesso.mas.stdhb.DataAccess.Communication.WsClientFactory;

import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;
import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;

/**
 * Created by chf on 23.08.2016.
 *
 * Factory to create IWsClient objects.
 */
public interface IWsClientFactory {

    /**
     * Factory for the generation of IWsClient instance.
     *
     * @param clientServerCommunication
     * @param citizenEndPoint
     *
     * @return
     */
    IWsClient Create(
            EnumClientServerCommunication clientServerCommunication,
            CitizenEndPoint citizenEndPoint);
}
