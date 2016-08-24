package hesso.mas.stdhb.Services;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import hesso.mas.stdhb.Base.Tools.MyString;
import hesso.mas.stdhbtests.R;

/**
 * Created by chf on 21.08.2016.
 */
public class SearchTask extends AsyncTask<String,Void,String> {

    /**
     *
     *
     * @param urls
     * @return
     */
    @Override
    protected String doInBackground(String... urls) {

        String result = MyString.EMPTY_STRING;
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);

            urlConnection=(HttpURLConnection)url.openConnection();
            InputStream in = urlConnection.getInputStream();

            InputStreamReader reader = new InputStreamReader(in);

            int data=reader.read();

            while (data !=-1){

                char current=(char) data;

                result += current;
                data = reader.read();
            }

            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After download task
     *
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {

            JSONArray jArray=new JSONArray(result);
            JSONObject json_data = jArray.getJSONObject(1);
//Logging data
            Log.i("Podatci: ", "Id: " + json_data.getInt("Id") +
                    ", Name: " + json_data.getString("Name") +
                    ", Years: " + json_data.getString("Age") +
                    ", Email address: " + json_data.getString("Email")
            );
            /*TextView textView = (TextView) findViewById(R.id.textViewName);
            textView.setText("ID: "+", Name: "+  json_data.getInt("Id")+json_data.getString("Name")+json_data.getString("Age")+json_data.getString("Email"));
*/



/*
        String data = jsonObject.getString("Name");
            Log.i("Website content", data);
*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
