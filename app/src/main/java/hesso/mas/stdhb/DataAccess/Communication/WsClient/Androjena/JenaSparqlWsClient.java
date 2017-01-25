package hesso.mas.stdhb.DataAccess.Communication.WsClient.Androjena;

import android.util.Log;

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

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenQueryResult;
import hesso.mas.stdhb.Base.Tools.MyString;

import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CityZenEndPoint;

/**
 * Created by chf on 27.08.2016.
 *
 * This class represents a Web service Client (using androjena)
 */
public class JenaSparqlWsClient implements IWsClient {

    private CityZenEndPoint mWsEndpoint;

    // It's not possible to use the default constructor to instanciate this class
    private JenaSparqlWsClient() {}

    // Constructor
    public JenaSparqlWsClient(CityZenEndPoint wsEndpoint) {

        Checks.AssertNotNull(wsEndpoint, "wsEndpoint");

        mWsEndpoint = wsEndpoint;
    }

    /**
     * This method allows to execute a request on the Sparql endpoint
     *
     * @param query
     *
     * @return The result of the request
     */
    public CityZenQueryResult executeRequest(String query) {

        CityZenQueryResult cityZenQueryResult = new CityZenQueryResult();

        System.out.println(query);

        try {
            Query lQuery = QueryFactory.create(query);

            String service = mWsEndpoint.Service();

            QueryExecution queryExecution =
                    QueryExecutionFactory.sparqlService(
                            service,
                            lQuery);

            ResultSet results = queryExecution.execSelect();

            List<String> resultsVar = results.getResultVars();

            while (results.hasNext())
            {
                CityZenDbObject cityZenDbObject = new CityZenDbObject();
                QuerySolution binding = results.nextSolution();

                for (String variable : resultsVar) {
                    cityZenDbObject.put(variable, GetValue(binding, variable));
                }

                cityZenQueryResult.Add(cityZenDbObject);
            }
        }
        catch (Exception aException) {
            Log.e("JenaClient", aException.getMessage());
        }
        finally {
        }

        return cityZenQueryResult;
    }

    /**
     * Get the value
     *
     * @param binding
     * @param fieldValue
     *
     * @return
     */
    private String GetValue(
        QuerySolution binding,
        String fieldValue) {

        String literal = TryGetLiteral(binding, "?" + fieldValue);
        String uri = TryGetResource(binding, "?" + fieldValue);

        if (!literal.equals(MyString.EMPTY_STRING)) {
            return literal;
        }

        if (!uri.equals(MyString.EMPTY_STRING)) {
            return uri;
        }

        return null;
    }

    /**
     *
     * @param binding
     * @param value
     *
     * @return
     */
    private String TryGetLiteral(
        QuerySolution binding,
        String value) {

        RDFNode rdfNode = binding.get(value);

        if (rdfNode != null) {
            if (rdfNode.isLiteral()) {
                Literal lLiteral = binding.getLiteral(value);
                return lLiteral.toString();
            }
        }

        return MyString.EMPTY_STRING;
    }

    /**
     *
     * @param binding
     * @param value
     *
     * @return
     */
    private String TryGetResource(
        QuerySolution binding,
        String value) {

        RDFNode rdfNode = binding.get(value);

        if (rdfNode != null) {
            if (rdfNode.isResource()) {
                Resource lResource = binding.getResource(value);
                return lResource.getURI();
            }
        }

        return MyString.EMPTY_STRING;
    }
}
