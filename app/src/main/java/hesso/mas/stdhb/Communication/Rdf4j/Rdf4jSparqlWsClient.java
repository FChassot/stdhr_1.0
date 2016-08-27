package hesso.mas.stdhb.Communication.Rdf4j;

//import org.eclipse.rdf4j.*;

import hesso.mas.stdhb.Base.CitizenEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 15.07.2016.
 *
 *
 */
public class Rdf4jSparqlWsClient {

    /**
     *
     * @param aCitizenEndPoint
     *
     * @return
     */
    public String DoRequest(
            CitizenEndPoint aCitizenEndPoint,
            String aSparqlQuery) {

        return "no response from the " + aCitizenEndPoint.CitizenServerUri();
    }
}
