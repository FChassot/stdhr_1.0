package hesso.mas.stdhb.DataAccess.Communication.WsClient.Rest.HttpUrlConnection;

import java.io.*;
import java.net.*;
import java.net.URL;
import java.io.InputStream;

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.Base.Constants.BaseConstants;
import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CityZenEndPoint;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CityZenQueryResult;

/**
 * Created by chf on 11.05.2016.
 *
 * Class
 */
public class RestWsClient implements IWsClient {

    private CityZenEndPoint mWsEndpoint;

    // It's not possible to use the default constructor to instanciate this class
    private RestWsClient() {}

    // Constructor
    public RestWsClient(CityZenEndPoint wsEndpoint) {

        Checks.AssertNotNull(wsEndpoint, "wsEndpoint");

        mWsEndpoint = wsEndpoint;
    }

    /**
     *
     * @param query The (sparql) request
     *
     * @return result from the request
     */
    public CityZenQueryResult executeRequest(String query) {

        String urlStr = mWsEndpoint.Service() + "?query = " + query;

        String result = "no response!";

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(urlStr);

            urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                result = readStream(inputStream);
            }
            finally {
                urlConnection.disconnect();
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        CityZenQueryResult cityzenResult = new CityZenQueryResult();
        CityZenDbObject object = new CityZenDbObject();
        object.put("test", result);
        cityzenResult.Results().add(object);

        return new CityZenQueryResult();
    }

    /**
     * Method to read the result of the request
     *
     * @param inputStream
     *
     * @return
     *
     * @throws IOException
     */
    private static String readStream(
        InputStream inputStream) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 1000);

        for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()){
            stringBuilder.append(line);
        }

        inputStream.close();

        return stringBuilder.toString();
    }
}
