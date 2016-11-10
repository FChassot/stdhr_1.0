package hesso.mas.stdhb.DataAccess.Communication.WsClient.Androjena;

import android.util.Log;

import junit.framework.Assert;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.List;

import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;
import hesso.mas.stdhb.Base.Tools.MyString;

import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CitizenEndPoint;

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
    public CitizenQueryResult executeRequest(String aQuery) {

        CitizenQueryResult lCitizenQueryResult = new CitizenQueryResult();

        System.out.println(aQuery);

        try {
            Query lQuery = QueryFactory.create(aQuery);

            String lService = mWsEndpoint.Service();

            QueryExecution lQueryExecution =
                    QueryExecutionFactory.sparqlService(
                            lService,
                            lQuery);

            ResultSet lResults = lQueryExecution.execSelect();

            List<String> lResultsVar = lResults.getResultVars();

            while (lResults.hasNext())
            {
                CitizenDbObject lCitizenDbObject = new CitizenDbObject();
                QuerySolution lBinding = lResults.nextSolution();

                for (String lVariable : lResultsVar) {
                    lCitizenDbObject.put(lVariable, GetValue(lBinding, lVariable));
                }

                lCitizenQueryResult.Add(lCitizenDbObject);
            }
        }
        catch (Exception aException) {
            Log.e("JenaClient", aException.getMessage());
        }
        finally {
        }

        return lCitizenQueryResult;
    }

    private String GetValue(
        QuerySolution lBinding,
        String aFieldValue) {

        String lLiteral = TryGetLiteral(lBinding, "?" + aFieldValue);
        String lUri = TryGetResource(lBinding, "?" + aFieldValue);

        if (!lLiteral.equals(MyString.EMPTY_STRING)) {
            return lLiteral;
        }

        if (!lUri.equals(MyString.EMPTY_STRING)) {
            return lUri;
        }

        return null;
    }

    /**
     *
     * @param aBinding
     * @param aValue
     * @return
     */
    private String TryGetLiteral(
            QuerySolution aBinding,
            String aValue) {

        RDFNode lRDFNode = aBinding.get(aValue);

        if (lRDFNode != null) {
            if (lRDFNode.isLiteral()) {
                Literal lLiteral = aBinding.getLiteral(aValue);
                return lLiteral.toString();
            }
        }

        return MyString.EMPTY_STRING;
    }

    /**
     *
     * @param aBinding
     * @param aValue
     * @return
     */
    private String TryGetResource(QuerySolution aBinding, String aValue) {

        RDFNode lRDFNode = aBinding.get(aValue);

        if (lRDFNode != null) {
            if (lRDFNode.isResource()) {
                Resource lResource = aBinding.getResource(aValue);
                return lResource.getURI();
            }
        }

        return MyString.EMPTY_STRING;
    }
}
