package hesso.mas.stdhb.Communication.WsClient.Androjena;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;

import hesso.mas.stdhb.Base.CitizenEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Communication.WsClient.IWsClient;

/**
 * Created by chf on 27.08.2016.
 *
 */
public class JenaSparqlWsClient implements IWsClient {

    private CitizenEndPoint mSparqlEndPoint;

    // Constructor
    public JenaSparqlWsClient(
            CitizenEndPoint aSparqlEndPoint
    ) {
        mSparqlEndPoint = aSparqlEndPoint;
    }

    /**
     *
     * @return
     */
    public String DoRequest(String aQuery) {

        String lResult = "";

        // DBpedia Request using Androjena
        System.out.println(aQuery);

        try {
            Query lQuery = QueryFactory.create(aQuery);

            QueryExecution lQueryExecution =
                    QueryExecutionFactory.sparqlService(
                            mSparqlEndPoint.CitizenServerUri(),
                            lQuery);

            ResultSet lResults = lQueryExecution.execSelect();

            while (lResults.hasNext())
            {
                QuerySolution lBinding = lResults.nextSolution();
                Resource lSubject = (Resource) lBinding.get("Subject");
                lResult += lSubject.getURI();
                //System.out.println("Subject: "+lSubject.getURI());
            }
        } catch (Exception aException) {
            aException.printStackTrace();
        }
        finally {
        }

        return lResult;
    }
}
