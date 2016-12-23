package hesso.mas.stdhb.DataAccess.Communication.WsClient.Rest.HttpUrlConnection;

import java.io.*;
import java.net.*;
import java.net.URL;
import java.io.InputStream;

import hesso.mas.stdhb.Base.Checks.Checks;
import hesso.mas.stdhb.DataAccess.Communication.WsClient.IWsClient;
import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CitizenEndPoint;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;

/**
 * Created by chf on 11.05.2016.
 *
 * Class
 */
public class RestWsClient implements IWsClient {

    private CitizenEndPoint mWsEndpoint;

    // It's not possible to use the default constructor to instanciate this class
    private RestWsClient() {}

    // Constructor
    public RestWsClient(CitizenEndPoint wsEndpoint) {

        Checks.AssertNotNull(wsEndpoint, "wsEndpoint");

        mWsEndpoint = wsEndpoint;
    }

    /**
     *
     * @param query The (sparql) request
     *
     * @return result from the request
     */
    public CitizenQueryResult executeRequest(String query) {

        String urlStr = "http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=select+*+where+%7B%0D%0A+++%3Fcategorie+rdfs%3Alabel+%22%C5%92uvre+conserv%C3%A9e+au+Louvre%22%40fr+.%0D%0A+++%3Foeuvre+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2FwikiPageWikiLink%3E+%3Fcategorie%0D%0A+%7D+LIMIT+1000&format=text%2Fhtml&CXML_redir_for_subjs=121&CXML_redir_for_hrefs=&timeout=30000&debug=o";

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

        CitizenQueryResult cityzenResult = new CitizenQueryResult();
        CitizenDbObject object = new CitizenDbObject();
        object.put("test", result);
        cityzenResult.Results().add(object);

        return new CitizenQueryResult();
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
