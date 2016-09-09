package hesso.mas.stdhb.Communication.WsClient.Androjena;

import junit.framework.Assert;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import com.hp.hpl.jena.rdf.model.Resource;

import hesso.mas.stdhb.Base.Tools.MyString;

import hesso.mas.stdhb.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.Communication.WsEndPoint.CitizenEndPoint;

/**
 * Created by chf on 27.08.2016.
 *
 * This class represents a Web service Client (using androjena)
 */
public class JenaSparqlWsClient implements IWsClient {

    private CitizenEndPoint mWsEndpoint;

    // It's not possible to use the default constructor to instanciate this class
    private JenaSparqlWsClient() {}

    // Constructor
    public JenaSparqlWsClient(CitizenEndPoint aWsEndpoint) {

        Assert.assertNotNull(aWsEndpoint);

        mWsEndpoint = aWsEndpoint;
    }

    /**
     * This method allows to execute a request on the Sparql endpoint
     *
     * @param aQuery
     *
     * @return The result of the request
     */
    public String executeRequest(String aQuery) {

        String lResult = MyString.EMPTY_STRING;

        System.out.println(aQuery);

        try {
            Query lQuery = QueryFactory.create(aQuery);

            String lService = mWsEndpoint.Service();

            QueryExecution lQueryExecution =
                    QueryExecutionFactory.sparqlService(
                            lService,
                            lQuery);

            ResultSet lResults = lQueryExecution.execSelect();

            while (lResults.hasNext())
            {
                QuerySolution lBinding = lResults.nextSolution();

                Resource lSubject = (Resource) lBinding.get("Concept");

                if (lSubject != null){
                    lResult += lSubject.getURI();
                }
            }
        } catch (Exception aException) {
            lResult = aException.getMessage();
        }
        finally {
        }

        return lResult;
    }
}
