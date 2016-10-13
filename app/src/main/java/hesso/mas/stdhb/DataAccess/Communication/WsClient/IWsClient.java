package hesso.mas.stdhb.DataAccess.Communication.WsClient;

import hesso.mas.stdhb.DataAccess.QueryEngine.CitizenQueryResult;

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
