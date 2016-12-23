package hesso.mas.stdhb.DataAccess.Communication.WsClient.Retrofit;

import android.support.v7.util.SortedList;
import android.util.Log;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 * Created by chf on 23.12.2016.
 */

/*public class Communicator {
    private static  final String TAG = "Communicator";
    private static final String SERVER_URL = "http://127.0.0.1/retrofit";

    public void loginPost(String username, String password){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        ResourceFactory.Interface communicatorInterface = restAdapter.create(ResourceFactory.Interface.class);
        SortedList.Callback<ServerResponse> callback = new SortedList.Callback<ServerResponse>() {
            @Override
            public void success(ServerResponse serverResponse, GetRecentContextCall.Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceServerEvent(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(), serverResponse.getMessage()));
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.postData("login", username, password, callback);
    }

    public void loginGet(String username, String password){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        ResourceFactory.Interface communicatorInterface = restAdapter.create(ResourceFactory.Interface.class);
        SortedList.Callback<ServerResponse> callback = new SortedList.Callback<ServerResponse>() {
            @Override
            public void success(ServerResponse serverResponse, GetRecentContextCall.Response response2) {
                if(serverResponse.getResponseCode() == 0){
                    BusProvider.getInstance().post(produceServerEvent(serverResponse));
                }else{
                    BusProvider.getInstance().post(produceErrorEvent(serverResponse.getResponseCode(), serverResponse.getMessage()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error != null ){
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
                BusProvider.getInstance().post(produceErrorEvent(-200,error.getMessage()));
            }
        };
        communicatorInterface.getData("login", username, password, callback);
    }
}*/
