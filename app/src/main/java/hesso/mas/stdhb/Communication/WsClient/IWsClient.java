package hesso.mas.stdhb.Communication.WsClient;

import hesso.mas.stdhb.Base.QueryBuilder.Response.CitizenQueryResult;

/**
 * Created by chf on 23.08.2016.
 */
public interface IWsClient {

    /**
     *
     * @param aQuery
     * @return
     */
    CitizenQueryResult executeRequest(String aQuery);
}
