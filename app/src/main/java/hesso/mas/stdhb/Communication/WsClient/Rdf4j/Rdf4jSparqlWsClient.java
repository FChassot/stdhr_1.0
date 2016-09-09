package hesso.mas.stdhb.Communication.WsClient.Rdf4j;

import junit.framework.Assert;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.Base.Tools.MyString;

import hesso.mas.stdhb.Communication.WsEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Communication.WsClient.IWsClient;

/**
 * Created by chf on 15.07.2016.
 *
 * This class represents a Web service Client (using Rdf4j)
 */
public class Rdf4jSparqlWsClient implements IWsClient {

    private CitizenEndPoint mWsEndpoint;

    // It's not possible to use the default constructor to instanciate this class
    private Rdf4jSparqlWsClient() {}

    // Constructor
    public Rdf4jSparqlWsClient(CitizenEndPoint aWsEndpoint) {

        Checks.AssertNotNull(aWsEndpoint);

        mWsEndpoint = aWsEndpoint;
    }

    /**
     * This method allows to execute a request on the Sparql endpoint
     *
     * @param aQuery The (sparql) request
     *
     * @return The result of the request
     */
    public String executeRequest(
        String aQuery) {

        // Request Sparql using Rdf4j
        System.out.println(aQuery);

        String lResult = MyString.EMPTY_STRING;

        Repository lCitizenRepository =
                new HTTPRepository(
                        mWsEndpoint.CitizenServerUri(),
                        mWsEndpoint.CitizenRepositoryName());

        RepositoryConnection lRepositoryConnection = null;

        try {
            lRepositoryConnection = lCitizenRepository.getConnection();

            lRepositoryConnection.begin();

            TupleQueryResult lResponse =
                    lRepositoryConnection.prepareTupleQuery(
                            QueryLanguage.SPARQL,
                            aQuery).evaluate();

            while (lResponse.hasNext())
            {
                BindingSet lBindingSet = lResponse.next();
                lResult += lBindingSet.toString();
            }
        } catch(Exception aException) {
            aException.printStackTrace();
        }
        finally
        {
            if(lRepositoryConnection != null)
            {
                lRepositoryConnection.close();
            }
        }

        return lResult;
    }
}
