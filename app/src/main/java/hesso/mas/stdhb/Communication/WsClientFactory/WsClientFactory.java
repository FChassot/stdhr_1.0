package hesso.mas.stdhb.Communication.WsClientFactory;

import hesso.mas.stdhb.Communication.WsEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Models.Enum.EnumClientServerCommunication;

import hesso.mas.stdhb.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.Communication.WsClient.Androjena.JenaSparqlWsClient;
import hesso.mas.stdhb.Communication.WsClient.Rdf4j.Rdf4jSparqlWsClient;

/**
 * Created by chf on 23.08.2016.
 *
 * Factory to create IWsClient objects.
 */
public class WsClientFactory implements IWsClientFactory {

    /**
     * @see
     *
     * @param aClientServerCommunication
     * @param aCitizenEndPoint
     *
     * @return
     */
    public IWsClient Create(
        EnumClientServerCommunication aClientServerCommunication,
        CitizenEndPoint aCitizenEndPoint) {

        IWsClient lWsClient  = null;

        if (aClientServerCommunication == EnumClientServerCommunication.ANDROJENA) {
            lWsClient = new JenaSparqlWsClient(aCitizenEndPoint);
        }

        if (aClientServerCommunication == EnumClientServerCommunication.RDF4J) {
            lWsClient = new Rdf4jSparqlWsClient(aCitizenEndPoint);
        }

        if (aClientServerCommunication == EnumClientServerCommunication.REST) {
            lWsClient = new Rdf4jSparqlWsClient(aCitizenEndPoint);
        }

        if (lWsClient == null) {
            lWsClient = new JenaSparqlWsClient(aCitizenEndPoint);
        }

        return lWsClient;
    }
}
