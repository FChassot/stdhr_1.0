package hesso.mas.stdhb.DataAccess.Communication.WsClient.Rest.HttpUrlConnection;

import java.io.*;
import java.net.*;
import java.net.URL;
import java.io.InputStream;

import hesso.mas.stdhb.DataAccess.Communication.WsEndPoint.CitizenEndPoint;

/**
 * Created by chf on 11.05.2016.
 *
 * Class
 */
public class RestWsClient {

    /**
     *
     * @param aWsEndpoint represents the WsEndpoint on which the request will be done
     *
     * @return result from the request
     */
    public static String executeRequest(CitizenEndPoint aWsEndpoint) {

        String lUrlStr = "http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=select+*+where+%7B%0D%0A+++%3Fcategorie+rdfs%3Alabel+%22%C5%92uvre+conserv%C3%A9e+au+Louvre%22%40fr+.%0D%0A+++%3Foeuvre+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2FwikiPageWikiLink%3E+%3Fcategorie%0D%0A+%7D+LIMIT+1000&format=text%2Fhtml&CXML_redir_for_subjs=121&CXML_redir_for_hrefs=&timeout=30000&debug=o";

        String lResult = "no response!";
        HttpURLConnection lUrlConnection = null;

        try {
            URL lUrl = new URL(lUrlStr);

            lUrlConnection = (HttpURLConnection) lUrl.openConnection();

            try {
                InputStream lInputStream = new BufferedInputStream(lUrlConnection.getInputStream());
                lResult = readStream(lInputStream);
            }
            finally {
                lUrlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (lUrlConnection != null) {
                lUrlConnection.disconnect();
            }
        }

        return lResult;
    }

    /**
     * Method to read the result of the request
     *
     * @param aInputStream
     *
     * @return
     *
     * @throws IOException
     */
    private static String readStream(InputStream aInputStream) throws IOException {

        StringBuilder lStringBuilder = new StringBuilder();
        InputStreamReader lInputStreamReader = new InputStreamReader(aInputStream);

        BufferedReader lBufferedReader = new BufferedReader(lInputStreamReader,1000);

        for (String lLine = lBufferedReader.readLine(); lLine != null; lLine =lBufferedReader.readLine()){
            lStringBuilder.append(lLine);
        }

        aInputStream.close();

        return lStringBuilder.toString();
    }
}
