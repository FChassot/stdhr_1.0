package hesso.mas.stdhb.Base.CitizenEndPoint;

import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by Frédéric Chassot (chf) on 20.06.2016.
 *
 * It represents the Citizen Sparql Endpoint
 */
public class CitizenEndPoint {

    // Uri
    static String mUri = MyString.EMPTY_STRING;

    // Citizen Server Uri
    public static final String CitizenServer =
        "http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/";

    // Citizen Server Name
    static final String CitizenRepository = "CityZenDM";

    // Setter
    public String Service(){return mUri;}

    // Getter
    public void Service(String aUri) {mUri = aUri;}
}
