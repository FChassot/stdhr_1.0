package hesso.mas.stdhb.DataAccess.Communication.WsClient.Retrofit;

import android.support.v7.util.SortedList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by chf on 23.12.2016.
 */
public interface IRetrofit {

    //This method is used for "POST"
    @FormUrlEncoded
    @POST("/api.php")
    void postData(@Field("method") String method,
                  @Field("username") String username,
                  @Field("password") String password,
                  SortedList.Callback<ServerResponse> serverResponseCallback);

    //This method is used for "GET"
    @GET("/api.php")
    void getData(@Query("method") String method,
                 @Query("username") String username,
                 @Query("password") String password,
                 SortedList.Callback<ServerResponse> serverResponseCallback);
}
