package hesso.mas.stdhb.DataAccess.Communication.WsClient.Rdf4j;

/*import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;*/

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenQueryResult;
import hesso.mas.stdhb.Base.Tools.MyString;

import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CityZenEndPoint;
import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;

/**
 * Created by chf on 15.07.2016.
 *
 * This class represents a Web service Client (using Rdf4j)
 */
public class Rdf4jSparqlWsClient implements IWsClient {

    private CityZenEndPoint mWsEndpoint;

    // It's not possible to use the default constructor to instanciate this class
    private Rdf4jSparqlWsClient() {}

    // Constructor
    public Rdf4jSparqlWsClient(CityZenEndPoint aWsEndpoint) {

        Checks.AssertNotNull(aWsEndpoint, "aWsEndpoint");

        mWsEndpoint = aWsEndpoint;
    }

    /**
     * This method allows to execute a request on the Sparql endpoint
     *
     * @param aQuery The (sparql) request
     *
     * @return The result of the request
     */
    public CityZenQueryResult executeRequest(
        String aQuery) {

        // Request Sparql using Rdf4j
        System.out.println(aQuery);

        return null;
    }
}
