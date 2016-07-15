package hesso.mas.stdhb.Base.DataAccess;

import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by Frédéric Chassot on 20.06.2016.
 * It represents the Sparql Endpoint
 */
public class CitizenEndPoint {

    static String mUri = MyString.EMPTY_STRING;

    // Setter
    public String Service(){return mUri;}

    // Getter
    public void Service(String aUri) {mUri = aUri;}
}
