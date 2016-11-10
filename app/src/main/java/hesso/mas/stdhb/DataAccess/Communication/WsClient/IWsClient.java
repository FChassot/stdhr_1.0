package hesso.mas.stdhb.DataAccess.Communication.WsClient;

import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;

/**
 * Created by chf on 23.08.2016.
 */
public interface IWsClient {

    /**
     *
     * @param aQuery The query to execute
     *
     * @return The result of the query
     */
    CitizenQueryResult executeRequest(String aQuery);
}
