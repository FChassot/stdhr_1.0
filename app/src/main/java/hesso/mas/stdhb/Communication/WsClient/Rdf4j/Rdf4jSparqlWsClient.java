package hesso.mas.stdhb.Communication.WsClient.Rdf4j;

import junit.framework.Assert;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

import hesso.mas.stdhb.Base.Tools.MyString;

import hesso.mas.stdhb.Communication.WsEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Communication.WsClient.IWsClient;

/**
 * Created by chf on 15.07.2016.
 *
 * This class represents a Web service Client (using Rdf4j)
 */
public class Rdf4jSparqlWsClient implements IWsClient {

    private CitizenEndPoint mSparqlEndPoint;

    // Constructor
    public Rdf4jSparqlWsClient(CitizenEndPoint aSparqlEndPoint) {

        Assert.assertNotNull(aSparqlEndPoint);

        mSparqlEndPoint = aSparqlEndPoint;
    }

    /**
     * This method allows to execute a request on the Sparql endpoint
     *
     * @param aSparqlQuery
     *
     * @return
     */
    public String executeRequest(
        String aSparqlQuery) {

        // Request Sparql using Rdf4j
        System.out.println(aSparqlQuery);

        String lResult = MyString.EMPTY_STRING;

        Repository lCitizenRepository =
                new HTTPRepository(
                        mSparqlEndPoint.CitizenServerUri(),
                        mSparqlEndPoint.CitizenRepositoryName());

        RepositoryConnection lRepositoryConnection = null;

        try {
            lRepositoryConnection = lCitizenRepository.getConnection();

            lRepositoryConnection.begin();

            TupleQueryResult lResponse =
                    lRepositoryConnection.prepareTupleQuery(
                            QueryLanguage.SPARQL,
                            aSparqlQuery).evaluate();

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
