package com.sbi.project.sbi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
public class RegisterActivity extends AppCompatActivity {
    private ImageView imageView=null;
    private TextInputEditText user_name=null,account_number=null,aadhar_number=null;
    private FloatingActionButton upload_image=null;
    //private static String imageData=null;
    private int IMG_REQUEST=1;
    private int CAMERA_REQUEST=0;
    private Bitmap bitmap=null;
    private TextInputLayout user_layout=null,account_layout=null,aadharno_layout=null;
    private boolean isOK=false;
    public byte[] imgbyte;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sampleregister);
        Toolbar toolbar=findViewById(R.id.sample_register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registration Page");
        initializeViews();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp(RegisterActivity.this);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String accountno=account_number.getEditableText().toString();
                String aadharno=aadhar_number.getEditableText().toString();
                String username=user_name.getEditableText().toString();
                isOK=validate(username,accountno,aadharno);
                if(!isOK){
                    Toast.makeText(RegisterActivity.this, "Fill all the details", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    new Task(RegisterActivity.this).execute(username,accountno,aadharno,imgbyte);
                }
            }
        });

    }

    private void accessCamera(){
        Intent a = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(a, CAMERA_REQUEST);
    }

    private void accessSdCard(){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    private void imagetostring(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        imgbyte= byteArrayOutputStream.toByteArray();
//        imageData=convertToBinary(imgbyte);
//        Toast.makeText(this, ""+imageData, Toast.LENGTH_SHORT).show();
    }

    private boolean validate(String username,String accountno,String aadharno){
        int count=0;

        if(username==null||username.equals("")){
            user_layout.setErrorEnabled(true);
            user_layout.setError("enter username");
        }else{
            user_layout.setErrorEnabled(false);
            user_layout.setError("");
            count++;
        }
        if(accountno==null||accountno.equals("")||accountno.length()!=11){
            account_layout.setErrorEnabled(true);
            account_layout.setError("enter accountnumber");
        }else{
            account_layout.setErrorEnabled(false);
            account_layout.setError("");
            count++;
        }
        if(aadharno==null||aadharno.equals("")||aadharno.length()!=16){
            aadharno_layout.setErrorEnabled(true);
            aadharno_layout.setError("enter aadharnumber");
        }else{
            aadharno_layout.setErrorEnabled(false);
            aadharno_layout.setError("");
            count++;
        }
        if (imgbyte!=null && imgbyte.length>0){
            count++;
        }else{
            Toast.makeText(this, "Select an image", Toast.LENGTH_SHORT).show();
        }
       if(count==4){
            return true;
       }
       return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST&&resultCode==RESULT_OK&&data!=null){
            Uri path=data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView.setImageBitmap(bitmap);
                imagetostring(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode==CAMERA_REQUEST&&resultCode==RESULT_OK&&data!=null){
             bitmap = (Bitmap) data.getExtras().get("data");
             imageView.setImageBitmap(bitmap);
             imagetostring(bitmap);
        }
    }

    public String convertToBinary(byte[] byteArray){
       /* StringBuilder sb = new StringBuilder();
        for (byte by : byteArray)
            sb.append(Integer.toBinaryString(by & 0xFF));
        return sb.toString();
        */
       return new String(byteArray, Charset.forName("UTF-8"));
    }

    public String insertToDB(String userName,String accountNumber,String aadharNo,byte[] imagetext){
        String urls = "http://apiplatformcloudse-gseapicssbisecond-uqlpluu8.srv.ravcloud.com:8001/FaceRecogCreate";
        String api_key = "4d874dba-9aa0-4eb9-b0bb-78086dee1ac9";
        String team_id = "594432169";
        String acc_num = accountNumber;
        String nam = userName;
        String mime_type = "image/jpeg";
        String aadhar_no = null;
        String encoding = "";
        String img_size = "";
        String i_date = "";
        Log.e("check","=="+imagetext);
        //String image = imagetext;
        HttpsCaller httpRetrive = new HttpsCaller(urls, api_key, team_id, acc_num, nam, mime_type, aadhar_no, encoding, img_size, i_date, imagetext);
        httpRetrive.Starter();
        String result =httpRetrive.response.toString();
       Log.e("Check","resultvalue"+result);
        return result;
    }

    private void initializeViews(){
        imageView=findViewById(R.id.upload_img);
        user_name=findViewById(R.id.username_edittext);
        account_number=findViewById(R.id.accountno_edittext);
        aadhar_number=findViewById(R.id.aadharno_edittext);
        upload_image=findViewById(R.id.save_button);
        user_layout=findViewById(R.id.name_layout);
        aadharno_layout=findViewById(R.id.aadharno_layout);
        account_layout=findViewById(R.id.accountno_layout);
        InputFilter.LengthFilter account=new InputFilter.LengthFilter(11);
        InputFilter.LengthFilter aadhar=new InputFilter.LengthFilter(16);
        account_number.setFilters(new InputFilter[]{account});
        aadhar_number.setFilters(new InputFilter[]{aadhar});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopUp(Context context) {
        PopupMenu popupMenu =new PopupMenu(context,imageView);
        popupMenu.getMenuInflater().inflate(R.menu.picselect,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.choose){
                    accessSdCard();
                }else if (menuItem.getItemId() == R.id.capture){
                    accessCamera();
                }
                return true;
            }
        });
        popupMenu.show();
    }

    class Task extends AsyncTask<Object,String,String>{
        private Context context=null;
        private ProgressDialog dialog=null;
        public  Task(Context context){
            this.context=context;
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Registering...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(Object... strings) {
          //  String result=insertToDB(strings[0].toString(),strings[1].toString(),strings[2].toString(),strings[3]);
           // return result;
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Log.e("Check",s);
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
