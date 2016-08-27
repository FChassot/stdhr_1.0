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
    public CitizenEndPoint(String aServerUri, String aRepository) {

        mCitizenServerUri = aServerUri;
        mCitizenRepository = aRepository;

    }

    // Setter
    public String CitizenServerUri(){return mCitizenServerUri;}

    // Getter
    public void CitizenServerUri(String aCitizenServerUri) {mCitizenServerUri = aCitizenServerUri;}

}
