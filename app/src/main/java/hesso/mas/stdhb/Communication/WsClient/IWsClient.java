package hesso.mas.stdhb.Communication.WsClient;

/**
 * Created by chf on 23.08.2016.
 */
public interface IWsClient {

    /**
     *
     * @param aQuery
     * @return
     */
    String executeRequest(String aQuery);
}
