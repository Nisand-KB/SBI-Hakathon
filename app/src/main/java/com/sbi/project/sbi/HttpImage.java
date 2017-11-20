package com.sbi.project.sbi;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpImage implements Runnable{
    public byte[] response;
    public String urls="";
    InputStream is=null;
    public String api_key="";
    public String TEAM_ID="";
    public String CHQ_NUM= "";
    public HttpImage(String Urls,String Api_key) {
        this.urls = Urls;
        this.api_key = Api_key;
    }
    Context context;
    HttpImage(Context context)
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
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            byte[] buffer = new byte[8192];
            int byteRead;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while((byteRead = in.read(buffer))!=-1)
            {
                out.write(buffer,0,byteRead);
            }
            response=out.toByteArray();
            Log.e("Check",""+response.toString());
        } catch(Exception e)
        {
            Log.e("Check",e.getMessage());
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