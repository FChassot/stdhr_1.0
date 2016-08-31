package hesso.mas.stdhb.Communication.WsClient.Rdf4j;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

import hesso.mas.stdhb.Base.SparqlEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhb.Communication.WsClient.IWsClient;

/**
 * Created by chf on 15.07.2016.
 *
 * This class represents a Sparql Web service Client.
 */
public class Rdf4jSparqlWsClient implements IWsClient {

    private CitizenEndPoint mSparqlEndPoint;

    // Constructor
    public Rdf4jSparqlWsClient(CitizenEndPoint aSparqlEndPoint) {
        mSparqlEndPoint = aSparqlEndPoint;
    }

    /**
     * Do a request to the sparql End Point
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

        try {
            Repository lCitizenRepository =
                    new HTTPRepository(
                            mSparqlEndPoint.CitizenServerUri(),
                            mSparqlEndPoint.CitizenRepository());

            RepositoryConnection lRepositoryConnection = null;

            try {
                lRepositoryConnection = lCitizenRepository.getConnection();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

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

        return lResult;
    }
}
