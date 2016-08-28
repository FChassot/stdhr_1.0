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
 *
 */
public class Rdf4jSparqlWsClient {

    private CitizenEndPoint mSparqlEndPoint;
    private String mSparqlQuery;

    // Constructor
    public Rdf4jSparqlWsClient(
            CitizenEndPoint aSparqlEndPoint,
            String aSparqlQuery
    ) {
        mSparqlEndPoint = aSparqlEndPoint;
        mSparqlQuery = aSparqlQuery;
    }

    /**
     *
     * @param aCitizenEndPoint
     *
     * @return
     */
    public String DoRequest(
            CitizenEndPoint aCitizenEndPoint,
            String aSparqlQuery) {

        // Request Sparql using Rdf4j
        System.out.println(aSparqlQuery);

        String lResult = MyString.EMPTY_STRING;

        Repository lCitizenRepository =
                new HTTPRepository(
                        aCitizenEndPoint.CitizenServerUri(),
                        aCitizenEndPoint.CitizenRepository());

        TupleQueryResult lResponse =
                lCitizenRepository.getConnection().prepareTupleQuery(
                        QueryLanguage.SPARQL,
                        aSparqlQuery).evaluate();

        while (lResponse.hasNext())
        {
            BindingSet lBindingSet = lResponse.next();
            lResult += lBindingSet.toString();
        }

        return lResult;
    }
}
