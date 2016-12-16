package hesso.mas.stdhb.DataAccess.Communication.WsClientFactory;

import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;

import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsClient.Androjena.JenaSparqlWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsClient.Rdf4j.Rdf4jSparqlWsClient;

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
     * @param citizenEndPoint
     *
     * @return a IWsClient Instance
     */
    public IWsClient Create(
        EnumClientServerCommunication clientServerCommunication,
        CitizenEndPoint citizenEndPoint) {

        IWsClient wsClient  = null;

        if (clientServerCommunication == EnumClientServerCommunication.ANDROJENA) {
            wsClient = new JenaSparqlWsClient(citizenEndPoint);
        }

        /*if (aClientServerCommunication == EnumClientServerCommunication.RDF4J) {
            lWsClient = new Rdf4jSparqlWsClient(aCitizenEndPoint);
        }*/

        if (clientServerCommunication == EnumClientServerCommunication.REST) {
            wsClient = new Rdf4jSparqlWsClient(citizenEndPoint);
        }

        if (wsClient == null) {
            wsClient = new JenaSparqlWsClient(citizenEndPoint);
        }

        return wsClient;
    }
}
