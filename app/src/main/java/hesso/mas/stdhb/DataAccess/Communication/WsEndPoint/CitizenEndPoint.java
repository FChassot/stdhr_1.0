package hesso.mas.stdhb.DataAccess.Communication.WsEndPoint;

/**
 * Created by chf on 20.06.2016.
 *
 * This class represents the Citizen Endpoint.
 */
public class CitizenEndPoint extends WsEndPoint {

    // Constructor
    public CitizenEndPoint(
        String aServerUri,
        String aRepositoryName) {

        this.ServerUri(aServerUri);
        this.Repository(aRepositoryName);
    }

    /**
     * Returns the service of the citizen Endpoint
     * Queries on a specific sesame repository with ID <ID> can be evaluated by sending requests to:
     * <SESAME_URL>/repositories/<ID>.
     * This resource represents a SPARQL query endpoint.
     *
     * http://graphdb.ontotext.com/sesame/system/ch08.html
     *
     * @return the resource which represents the SPARQL query endpoint
     */
    public String Service(){
        return this.ServerUri() + "repositories/" + this.Repository();
    }

}
