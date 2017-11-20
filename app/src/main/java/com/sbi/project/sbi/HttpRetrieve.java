package com.sbi.project.sbi;

/**
 * Created by Niru.R on 11-13-2017.
 */


import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class HttpRetrieve implements Runnable{
    public StringBuffer response=new StringBuffer();
    public ArrayList<HashMap<String,String>> lab;
    public String urls="";

    public String api_key="";


    public HttpRetrieve(String Urls,String Api_key) {
        this.urls = Urls;
        this.api_key = Api_key;
    }
    Context context;
    HttpRetrieve(Context context)
    {
        this.context=context;
    }
    @Override
    public void run()
    {

        URL url=null;
        HttpURLConnection urlConnection = null;
        try
        {
            url = new URL(this.urls);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("api-key", api_key);

            urlConnection.setDoOutput(false);
            urlConnection.setDoInput(true);


            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
            String line;

            while((line=in.readLine())!=null)
            {
                response.append(line);

            }

        } catch(Exception e)
        {
            e.printStackTrace();
        } finally
        {
            urlConnection.disconnect();
        }
    }
    public void starter()
    {
        try {
            Thread runs = new Thread(this);
            runs.start();
            runs.join();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
