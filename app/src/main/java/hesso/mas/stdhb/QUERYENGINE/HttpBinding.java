package hesso.mas.stdhb.QueryEngine;

import java.io.*;
import java.net.*;
import java.io.InputStream;
import java.net.URL;

import hesso.mas.stdhb.DataAccess.CitizenEndPoint;

/**
 * Created by frede on 11.05.2016.
 */
public class HttpBinding {

    public static String DoHttpBinding(CitizenEndPoint aCitizenEndPoint) {

        // Db Pedia Request (HTTP Binding)
        String strings = "London";
        String service = "http://dbpedia.org/sparql";
        String query = "PREFIX dbo:<http://dbpedia.org/ontology/>"
                + "PREFIX : <http://dbpedia.org/resource/>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#/>"
                + "select ?URI where {?URI rdfs:label "+strings+".}";

        String lResult = "rien";

        try {
            //QueryExecution qE =

            URL url = new URL(aCitizenEndPoint.Service());

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                lResult = readStream(in);
            }
            finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return lResult;
        }
    }

    private static String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
}
