package hesso.mas.stdhb.Communication.Rest;

import android.os.AsyncTask;

import hesso.mas.stdhb.Base.myString;
import hesso.mas.stdhb.QueryEngine.RestClient;

/**
 * Created by frede on 23.05.2016.
 */
class RetrieveCityStoriesDataTask extends AsyncTask<String, Void, String> {

    private Exception exception;

    protected String doInBackground(String... urls) {
        try {
            RestClient client = new RestClient("http://dbpedia.org/sparql");
            client.AddParam("service", "http://dbpedia.org/sparql");
            String query = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";
            client.AddParam("query", query);

            try {
                client.Execute(RestClient.RequestMethod.GET);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String lResponse = client.getResponse();
        } catch (Exception e) {
            this.exception = e;
            return null;
        }

        return myString.Empty();

    }

    protected void onPostExecute(String feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}