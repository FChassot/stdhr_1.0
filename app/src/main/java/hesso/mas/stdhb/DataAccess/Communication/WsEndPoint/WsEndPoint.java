package hesso.mas.stdhb.DataAccess.Communication.WsEndPoint;

import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 20.06.2016.
 *
 * This class is the base class for a Web service endpoint
 */
public abstract class WsEndPoint {

    // Server Uri
    private String mServerUri = MyString.EMPTY_STRING;

    // Repository Name
    private String mRepositoryName = MyString.EMPTY_STRING;

    // Constructor
    public WsEndPoint() {}

    // Setter
    public void ServerUri(String citizenServerUri) {
        mServerUri = citizenServerUri;
    }

    // Getter
    public String ServerUri(){
        return mServerUri;
    }

    // Setter
    public void Repository(String repositoryName) {
        mRepositoryName = repositoryName;
    }

    // Getter
    public String Repository(){
        return mRepositoryName;
    }

}
