package hesso.mas.stdhb.QueryEngine;

/**
 * Created by frede on 11.05.2016.
 */
class RestClientold
{
    /*public enum RequestMethod
    {
        GET,
        POST
    }
    public int responseCode=0;
    public String message;
    public String response;
    public void Execute(RequestMethod method,String url,ArrayList<NameValuePair> headers,ArrayList<NameValuePair> params) throws Exception
    {
        switch (method)
        {
            case GET:
            {
                // add parameters
                String combinedParams = "";
                if (params!=null)
                {
                    combinedParams += "?";
                    for (NameValuePair p : params)
                    {
                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
                        if (combinedParams.length() > 1)
                            combinedParams += "&" + paramString;
                        else
                            combinedParams += paramString;
                    }
                }
                HttpGet request = new HttpGet(url + combinedParams);
                // add headers
                if (headers!=null)
                {
                    headers=addCommonHeaderField(headers);
                    for (NameValuePair h : headers)
                        request.addHeader(h.getName(), h.getValue());
                }
                executeRequest(request, url);
                break;
            }
            case POST:
            {
                HttpPost request = new HttpPost(url);
                // add headers
                if (headers!=null)
                {
                    headers=addCommonHeaderField(headers);
                    for (NameValuePair h : headers)
                        request.addHeader(h.getName(), h.getValue());
                }
                if (params!=null)
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                executeRequest(request, url);
                break;
            }
        }
    }
    private ArrayList<NameValuePair> addCommonHeaderField(ArrayList<NameValuePair> _header)
    {
        _header.add(new BasicNameValuePair("Content-Type","application/x-www-form-urlencoded"));
        return _header;
    }
    private void executeRequest(HttpUriRequest request, String url)
    {
        HttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse;
        try
        {
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null)
            {
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                instream.close();
            }
        }
        catch (Exception e)
        { }
    }

    private static String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
        }
        catch (IOException e)
        { }
        return sb.toString();
    }*/
}