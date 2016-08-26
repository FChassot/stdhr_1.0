package hesso.mas.stdhb.Base.CitizenEndPoint;

import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 20.06.2016.
 *
 * It represents the Citizen Sparql Endpoint
 */
public class CitizenEndPoint {

    // Citizen Server Uri
    private String mCitizenServer = MyString.EMPTY_STRING;

    // Citizen Repository Name
    private String mCitizenRepository = MyString.EMPTY_STRING;

    // Constructor
    public CitizenEndPoint() {
        mCitizenServer = "http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/";
        mCitizenRepository = "CityZenDM";
    }

    // Uri
    static String mUri = MyString.EMPTY_STRING;

    // Setter
    public String CitizenServer(){return mCitizenServer;}

    // Getter
    public void CitizenServer(String aCitizenServer) {mCitizenServer = aCitizenServer;}

    // Setter
    public String Service(){return mUri;}

    // Getter
    public void Service(String aUri) {mUri = aUri;}

}
