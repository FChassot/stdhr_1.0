package hesso.mas.stdhb.Communication.WsEndPoint;

import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 20.06.2016.
 *
 * This class represents the Citizen Endpoint
 */
public class CitizenEndPoint extends WsEndPoint {

    // Citizen Server Uri
    private String mCitizenServerUri = MyString.EMPTY_STRING;

    // Citizen Repository Name
    private String mCitizenRepositoryName = MyString.EMPTY_STRING;

    // Constructor
    public CitizenEndPoint() {}

    // Constructor
    public CitizenEndPoint(
            String aServerUri,
            String aRepositoryName) {

        mCitizenServerUri = aServerUri;
        mCitizenRepositoryName = aRepositoryName;
    }

    // Setter
    public void CitizenServerUri(String aCitizenServerUri) {
        mCitizenServerUri = aCitizenServerUri;
    }

    // Getter
    public String CitizenServerUri(){
        return mCitizenServerUri;
    }

    // Setter
    public void CitizenRepositoryName(String aCitizenRepository) {
        mCitizenRepositoryName = aCitizenRepository;
    }

    // Getter
    public String CitizenRepositoryName(){
        return mCitizenRepositoryName;
    }

}
