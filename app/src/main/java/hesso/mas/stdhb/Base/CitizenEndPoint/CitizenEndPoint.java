package hesso.mas.stdhb.Base.CitizenEndPoint;

import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 20.06.2016.
 *
 * It represents the Citizen Sparql Endpoint
 */
public class CitizenEndPoint {

    // Constructor
    public void CitizenEndPoint() {
        CitizenServer = "http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/";
        CitizenRepository = "CityZenDM";
    }

    // Uri
    static String mUri = MyString.EMPTY_STRING;

    // Citizen Server Uri
    public String CitizenServer = MyString.EMPTY_STRING;

    // Citizen Repository Name
    static String CitizenRepository = MyString.EMPTY_STRING;

    // Setter
    public String Service(){return mUri;}

    // Getter
    public void Service(String aUri) {mUri = aUri;}

}
