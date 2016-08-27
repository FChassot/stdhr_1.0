package hesso.mas.stdhb.Communication.Rest.HttpClient;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;

import java.util.ArrayList;
import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.net.URLEncoder;

import org.apache.http.HttpEntity;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * Created by chf on 11.05.2016.
 */
public class RestclientWithHttpClient {

    public enum RequestMethod
    {
        GET,
        POST
    }

    private ArrayList <NameValuePair> params;
    private ArrayList <NameValuePair> headers;

    private String url;

    private int responseCode;
    private String message;

    private String response;

    public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public RestclientWithHttpClient(String url)
    {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }

    public void AddParam(String name, String value)
    {
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value)
    {
        headers.add(new BasicNameValuePair(name, value));
    }

    public void Execute(RequestMethod method) throws Exception
    {
        switch(method) {
            case GET:
            {
                //add parameters
                String combinedParams = "";

                if(!params.isEmpty()){
                    combinedParams += "?";
                    for(NameValuePair p : params)
                    {
                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
                        if(combinedParams.length() > 1)
                        {
                            combinedParams  +=  "&" + paramString;
                        }
                        else
                        {
                            combinedParams += paramString;
                        }
                    }
                }

                HttpGet request = new HttpGet(url + combinedParams);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                executeRequest(request, url);
                break;
            }
            case POST:
            {
                HttpPost request = new HttpPost(url);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                if(!params.isEmpty()){
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                }

                executeRequest(request, url);
                break;
            }
        }
    }

    /**
     * Method to execute the request
     *
     * @param request
     * @param aUrl
     */
    private void executeRequest(HttpUriRequest request, String aUrl)
    {
        HttpClient lHttpClient = new DefaultHttpClient();

        HttpResponse lHttpResponse;

        try {
            lHttpResponse = lHttpClient.execute(request);
            responseCode = lHttpResponse.getStatusLine().getStatusCode();
            message = lHttpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = lHttpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);

                // Closing the input stream will trigger connection release
                instream.close();
            }

        } catch (ClientProtocolException e)  {
            lHttpClient.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            lHttpClient.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }

    /**
     *
     * @param aInputStream
     * @return
     */
    private static String convertStreamToString(InputStream aInputStream) {

        BufferedReader lBufferedReader = new BufferedReader(new InputStreamReader(aInputStream));
        StringBuilder lStringBuilder = new StringBuilder();

        String line = null;

        try {
            while ((line = lBufferedReader.readLine()) != null) {
                lStringBuilder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                aInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lStringBuilder.toString();
    }
}