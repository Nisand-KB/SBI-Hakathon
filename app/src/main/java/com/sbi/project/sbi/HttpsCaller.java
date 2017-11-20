package com.sbi.project.sbi;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by NISAND on 08-11-2017.
 */

public class HttpsCaller implements Runnable
{
    public StringBuffer response=new StringBuffer();
    public String urls="";
    public String api_key="";
    public String team_id="";
    public String acc_num="";
    public String nam="";
    public String mime_type="";
    public String aadhar_no="";
    public String encoding="";
    public String img_size="";
    public String i_date="";
    public byte[] image;
    Context context;
    HttpsCaller(String Urls,String Api_key, String Team_id, String Acc_num,String Nam, String Mime_type, String Aadhar_no, String Encoding, String Img_size, String I_date,byte[] Image) {
        this.urls = Urls;
        Log.e("Check","classurls="+Urls);
        this.api_key = Api_key;
        this.team_id = Team_id;
        this.acc_num = Acc_num;
        this.nam = Nam;
        this.mime_type = Mime_type;
        this.aadhar_no = Aadhar_no;
        this.encoding = Encoding;
        this.i_date = I_date;
        this.image = Image;
    }
    public HttpsCaller(Context context)
    {
        this.context=context;
    }
    @Override
    public void run()
    {
//        byte[] data = this.image.getBytes(Charset.forName("UTF-8"));
//        int data_legth = data.length;
        URL url=null;
        HttpURLConnection urlConnection=null;
        try
        {
            url = new URL(this.urls);
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.e("Check","classconnection"+urlConnection);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("api-key", api_key);
            urlConnection.setRequestProperty("TEAM_ID", team_id);
            urlConnection.setRequestProperty("ACCOUNT_NO", acc_num);
            urlConnection.setRequestProperty("NAME", nam);
            urlConnection.setRequestProperty("MIME_TYPE", mime_type);
            urlConnection.setRequestProperty("AADHAAR_NO", aadhar_no);
            urlConnection.setRequestProperty("ENCODING", encoding);
            urlConnection.setRequestProperty("IMG_SIZE", img_size);
            urlConnection.setRequestProperty("I_DATE", i_date);

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
//            byte[] image_data = image.getBytes();

            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
            out.write(image);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
            String line="";
            while((line=in.readLine())!=null)
            {
                response.append(line);
                Log.e("Check","classrespons="+response);
            }
            out.flush();
            out.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            Log.e("Check","classexception="+e.toString());
        } finally
        {
            urlConnection.disconnect();
        }
    }
    public void Starter()
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