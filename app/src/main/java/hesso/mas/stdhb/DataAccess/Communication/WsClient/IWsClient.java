package hesso.mas.stdhb.DataAccess.Communication.WsClient;

import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenQueryResult;

/**
 * Created by chf on 23.08.2016.
 */
public interface IWsClient {

    /**
     * Method used to populate a request on a sparql server
     *
     * @param query The query to execute
     *
     * @return The result of the query
     */
    CityZenQueryResult executeRequest(String query);
}
