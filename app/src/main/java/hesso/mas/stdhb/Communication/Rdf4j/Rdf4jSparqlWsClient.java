package hesso.mas.stdhb.Communication.Rdf4j;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

import hesso.mas.stdhb.Base.CitizenEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 15.07.2016.
 *
 * This class represents a Sparql Web service Client.
 */
public class Rdf4jSparqlWsClient {

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
    public String DoRequest(
        String aSparqlQuery) {

        // Request Sparql using Rdf4j
        System.out.println(aSparqlQuery);

        String lResult = MyString.EMPTY_STRING;

        try {
            Repository lCitizenRepository =
                    new HTTPRepository(
                            mSparqlEndPoint.CitizenServerUri(),
                            mSparqlEndPoint.CitizenRepository());

            TupleQueryResult lResponse =
                    lCitizenRepository.getConnection().prepareTupleQuery(
                            QueryLanguage.SPARQL,
                            aSparqlQuery).evaluate();

            while (lResponse.hasNext())
            {
                BindingSet lBindingSet = lResponse.next();
                lResult += lBindingSet.toString();
            }
        } catch(Exception e) {
            String lMessage = e.getMessage();
        }

        return lResult;
    }
}
