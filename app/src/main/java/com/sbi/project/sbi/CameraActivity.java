package com.sbi.project.sbi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.VerifyResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.UUID;

public class CameraActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private ImageView capturedImage;
    public  Bitmap bit_image=null;
    private int CAMERA_REQUEST=0;
    ProgressDialog progressDialog;
    private UUID mFaceId0;
    private UUID mFaceId1;
    TextView username,accountno,aadharno;
    String  account_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        progressDialog =new ProgressDialog(this);
        account_no = getIntent().getExtras().getString("accountno");
        capturedImage=findViewById(R.id.capturedimage);
        username=findViewById(R.id.usernametext);
        accountno=findViewById(R.id.accountno_text);
        aadharno=findViewById(R.id.aadharno_text);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Verifying Account Number");
        dialog.setCancelable(false);
        dialog.show();
        new RetrieveImage().execute(account_no);
    }
    class RetrieveImage extends AsyncTask<String,String,Bitmap>{

        public Bitmap sendRequest(String acc){
            String http = "http://apiplatformcloudse-gseapicssbisecond-uqlpluu8.srv.ravcloud.com:8001/FaceRecogInfo/594432169/"+acc+"/FACE_IMAGE";
            String Api_key ="4d874dba-9aa0-4eb9-b0bb-78086dee1ac9 ";
            HttpImage o = new HttpImage(http,Api_key);
            o.starter();
            byte arr[]=o.response;
            if (arr==null){
                return null;
            }
            bit_image = BitmapFactory.decodeByteArray(o.response,0,o.response.length);
            return bit_image;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String accno=strings[0];
            return sendRequest(accno);
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);
            if(s==null){
                Log.e("Check","failed to retrieve");
                Toast.makeText(CameraActivity.this, "Check the account number", Toast.LENGTH_SHORT).show();
                finish();
                dialog.dismiss();
            }else {
                dialog.dismiss();
                detect(bit_image,1);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST&&resultCode==RESULT_OK&&data!=null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            detect(bitmap,0);
        }
    }

    private void accessCamera(){
        Intent a = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(a, CAMERA_REQUEST);
    }

    private class VerificationTask extends AsyncTask<Void, String, VerifyResult> {

        // The IDs of two face to verify.
        private UUID mFaceId0;
        private UUID mFaceId1;

        VerificationTask (UUID faceId0, UUID faceId1) {
            mFaceId0 = faceId0;
            mFaceId1 = faceId1;
            Log.e("Check","abcd"+mFaceId0.toString());
            Log.e("Check","abcd"+mFaceId1.toString());
        }

        @Override
        protected VerifyResult doInBackground(Void... params) {
            // Get an instance of face service client to detect faces in image.
            FaceServiceClient faceServiceClient = SampleApp.getFaceServiceClient();
            try{
                publishProgress("Verifying...");

                // Start verification.
                return faceServiceClient.verify(
                        mFaceId0,      /* The first face ID to verify */
                        mFaceId1);     /* The second face ID to verify */
            }  catch (Exception e) {
                publishProgress(e.getMessage());
                Log.e("Check",e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            Log.e("Check","Request: Verifying face " + mFaceId0 + " and face " + mFaceId1);
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            progressDialog.setMessage(progress[0]);
            //setInfo(progress[0]);
        }

        @Override
        protected void onPostExecute(VerifyResult result) {
            if (result != null) {
                Log.e("Check","Response: Success. Face " + mFaceId0 + " and face "
                        + mFaceId1 + (result.isIdentical ? " " : " don't ")
                        + "belong to the same person");
                if (result.isIdentical){
                    Log.e("Check","success");
                    retrieve(account_no);
                }else{
                    Log.e("Check","failed");
                }
            }

            // Show the result on screen when verification is done.
            setUiAfterVerification(result);
        }
    }

    private void setInfo(String info) {
        TextView textView = (TextView) findViewById(R.id.info);
        textView.setText(info);
    }

    private void setUiAfterVerification(VerifyResult result) {
        progressDialog.dismiss();
        if (result != null) {
            DecimalFormat formatter = new DecimalFormat("#0.00");
            String verificationResult = (result.isIdentical ? "The same person": "Different persons")
                    + ". The confidence is " + formatter.format(result.confidence);
            //setInfo(verificationResult);
        }
        if (result.isIdentical){
            Log.e("Check","success");
            retrieve(account_no);
        }else{
            Log.e("Check","failed");
        }
    }

    private void setUiAfterDetection(Face[] result,int index, boolean succeed) {

        if (succeed) {
            Log.e("Check","Response: Success. Detected "
                    + result.length + " face(s) in image");
            //setInfo(result.length + " face" + (result.length != 1 ? "s": "")  + " detected");
            if (result.length != 0) {
                Log.e("Check1","in");
                if (index==0) {
                    mFaceId0 = result[0].faceId;
                    Log.e("Check1","in");
                    Log.e("Check1","image0 done");
                }
                else {
                    mFaceId1 = result[0].faceId;
                    Log.e("Check1","image1 done");
                }
            }else{
                Log.e("Check1","ouut");

            }
        }

        if (result != null && result.length == 0) {
            //setInfo("No face detected!");
            Toast.makeText(this, "No face detected.Try again", Toast.LENGTH_SHORT).show();
            if(index==0)accessCamera();else finish();
        }

        if (succeed) {
            progressDialog.dismiss();
        }else{
            progressDialog.dismiss();
        }
    }

    private void detect(Bitmap bitmap,int index) {
        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        // Start a background task to detect faces in the image.
        new DetectionTask(index).execute(inputStream);

        // Set the status to show that detection starts.
        //setInfo("Detecting...");
    }

    public void verify() {
        new VerificationTask(mFaceId0, mFaceId1).execute();
    }

    private class DetectionTask extends AsyncTask<InputStream, String, Face[]> {
        private boolean mSucceed = true;
        private int index;
        DetectionTask(int index){
            this.index=index;
        }

        @Override
        protected Face[] doInBackground(InputStream... params) {
            // Get an instance of face service client to detect faces in image.
            FaceServiceClient faceServiceClient = SampleApp.getFaceServiceClient();
            try{
                publishProgress("Detecting...");

                // Start detection.
                return faceServiceClient.detect(
                        params[0],  /* Input stream of image to detect */
                        true,       /* Whether to return face ID */
                        false,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                        null);
            }  catch (Exception e) {
                mSucceed = false;
                publishProgress(e.getMessage());
                Log.e("Check",e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();

        }

        @Override
        protected void onProgressUpdate(String... progress) {
            progressDialog.setMessage(progress[0]);
            //setInfo(progress[0]);
        }

        @Override
        protected void onPostExecute(Face[] result) {
            setUiAfterDetection(result,index, mSucceed);
            if (index==1){
                capturedImage.setImageBitmap(bit_image);
                accessCamera();
            }else if (index == 0){
                verify();
            }
        }
    }

    private void retrieve(String acc_no){
        String http="http://apiplatformcloudse-gseapicssbisecond-uqlpluu8.srv.ravcloud.com:8001/FaceRecogInfo/594432169/"+acc_no;
        String api_key = "4d874dba-9aa0-4eb9-b0bb-78086dee1ac9";
        HttpRetrieve hr = new HttpRetrieve(http,api_key);

        hr.starter();
        String s = hr.response.toString();
        Log.e("check","==="+s);
        try {
            JSONObject jObject = new JSONObject(s);
            JSONArray jsonArr = jObject.getJSONArray("items");

            for(int i =0 ; i<jsonArr.length() ;i++ ) {
                JSONObject jsonObj1 = jsonArr.getJSONObject(i);

                String ad = jsonObj1.getString("aadhaar_no");
                String ac = jsonObj1.getString("account_no");
                String en = jsonObj1.getString("encoding");
                String idate = jsonObj1.getString("i_date");
                String imagesize = jsonObj1.getString("img_size");
                String image = jsonObj1.getString("links");
                String mime = jsonObj1.getString("mime_type");
                String name = jsonObj1.getString("name");
                String teamid = jsonObj1.getString("team_id");
                accountno.setText(ac);
                aadharno.setText(ad);
                username.setText(name);
                Log.e("check","==\\\\="+ad);
                Log.e("check","=\\\\=="+ac);
                Log.e("check","==\\\\="+en);
                Log.e("check","=\\\\=="+idate);
                Log.e("check","==\\\\="+imagesize);
                Log.e("check","=\\\\=="+image);
                Log.e("check","==\\\\="+mime);
                Log.e("check","=\\\\=="+name);
                Log.e("check","=\\\\=="+teamid);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
