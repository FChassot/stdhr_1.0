package hesso.mas.stdhb.Communication.Androjena;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;

import hesso.mas.stdhb.Base.CitizenEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 27.08.2016.
 *
 */
public class JenaSparqlWsClient {

    private CitizenEndPoint mSparqlEndPoint;
    private String mSparqlQuery;

    // Constructor
    public JenaSparqlWsClient(
            CitizenEndPoint aSparqlEndPoint,
            String aSparqlQuery
    ) {
        mSparqlEndPoint = aSparqlEndPoint;
        mSparqlQuery = aSparqlQuery;
    }

    /**
     *
     * @return
     */
    public String DoRequest() {

        // DBpedia Request using Androjena
        //String strings = "London";

        String lSparqlEndPoint = MyString.EMPTY_STRING;

        if (mSparqlEndPoint.CitizenServerUri() == MyString.EMPTY_STRING) {
            lSparqlEndPoint = "http://dbpedia.org/sparql";

        } else {lSparqlEndPoint = mSparqlEndPoint.CitizenServerUri();}

        String lStrSparqlQuery;

        if (mSparqlQuery.equals(MyString.EMPTY_STRING)) {
            /*lStrSparqlQuery =
                    "PREFIX dbo:<http://dbpedia.org/ontology/>"
                    + "PREFIX : <http://dbpedia.org/resource/>"
                    + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#/>"
                    + "select ?URI where {?URI rdfs:label London.}";*/

            lStrSparqlQuery = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";

        } else {lStrSparqlQuery = mSparqlQuery;}

        String lResult = "nothing";

        System.out.println(lStrSparqlQuery);

        try {
            Query lQuery = QueryFactory.create(lStrSparqlQuery);

            QueryExecution lQueryExecution =
                    QueryExecutionFactory.sparqlService(
                            lSparqlEndPoint,
                            lQuery);

            ResultSet lResults = lQueryExecution.execSelect();

            while (lResults.hasNext())
            {
                QuerySolution lBinding = lResults.nextSolution();
                Resource lSubject = (Resource) lBinding.get("Subject");
                System.out.println("Subject: "+lSubject.getURI());
            }
        } catch (Exception aException) {
            aException.printStackTrace();
        }
        finally {
        }

        return lResult;
    }
}
