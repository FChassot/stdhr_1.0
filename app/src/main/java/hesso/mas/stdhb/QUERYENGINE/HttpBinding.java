package hesso.mas.stdhb.QUERYENGINE;

import java.io.*;
import java.net.*;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by frede on 11.05.2016.
 */
public class HttpBinding {

    public static void DoHttpBinding(String aUrl) {

        String lUrl="http://www.android.com/";

        try {
            URL url = new URL(aUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //readStream(in);
            }
            finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
