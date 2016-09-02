package hesso.mas.stdhb.Communication.WsEndPoint;

import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 20.06.2016.
 *
 * This class represents a Web service endpoint
 */
public abstract class WsEndPoint {

    // Citizen Server Uri
    private String mServerUri = MyString.EMPTY_STRING;

    // Constructor
    public WsEndPoint() {}

    // Setter
    public void CitizenServerUri(String aCitizenServerUri) {
        mServerUri = aCitizenServerUri;
    }

    // Getter
    public String CitizenServerUri(){
        return mServerUri;
    }

}
