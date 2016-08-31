package hesso.mas.stdhb.Communication.WsClient.Rest.HttpUrlConnection;

import java.io.*;
import java.net.*;
import java.io.InputStream;
import java.net.URL;

import hesso.mas.stdhb.Base.SparqlEndPoint.CitizenEndPoint;

/**
 * Created by chf on 11.05.2016.
 *
 * Class
 */
public class RestclientWithHttpUrlConnection {

    /**
     *
     * @param aCitizenEndPoint
     *
     * @return
     */
    public static String DoHttpBinding(CitizenEndPoint aCitizenEndPoint) {

        String lUrlStr = "http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=select+*+where+%7B%0D%0A+++%3Fcategorie+rdfs%3Alabel+%22%C5%92uvre+conserv%C3%A9e+au+Louvre%22%40fr+.%0D%0A+++%3Foeuvre+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2FwikiPageWikiLink%3E+%3Fcategorie%0D%0A+%7D+LIMIT+1000&format=text%2Fhtml&CXML_redir_for_subjs=121&CXML_redir_for_hrefs=&timeout=30000&debug=o";

        String lResult = "nothing";

        try {
            URL lUrl = new URL(lUrlStr);

            HttpURLConnection lUrlConnection = (HttpURLConnection) lUrl.openConnection();

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
