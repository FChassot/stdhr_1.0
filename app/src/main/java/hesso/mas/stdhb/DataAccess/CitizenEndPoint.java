package hesso.mas.stdhb.DataAccess;

import hesso.mas.stdhb.Common.myString;

/**
 * Created by Frédéric Chassot on 20.06.2016.
 * It represent the Sparql Endpoint
 */
public class CitizenEndPoint {

    static String mUri = myString.Empty();

    // Setter
    public String Service(){return mUri;}

    // Getter
    public void Service(String aUri) {mUri = aUri;}
}
