package hesso.mas.stdhb.Base.CitizenEndPoint;

import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 20.06.2016.
 *
 * It represents the Citizen Sparql Endpoint
 */
public class CitizenEndPoint {

    // Citizen Server Uri
    private String mCitizenServerUri = MyString.EMPTY_STRING;

    // Citizen Repository Name
    private String mCitizenRepository = MyString.EMPTY_STRING;

    // Constructor
    public CitizenEndPoint() {

        mCitizenServerUri = ""; //"http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/";
        mCitizenRepository = ""; //"CityZenDM";

    }

    // Setter
    public String CitizenServerUri(){return mCitizenServerUri;}

    // Getter
    public void CitizenServerUri(String aCitizenServerUri) {mCitizenServerUri = aCitizenServerUri;}

}
