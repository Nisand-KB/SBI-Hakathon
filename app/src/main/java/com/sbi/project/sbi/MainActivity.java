package com.sbi.project.sbi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Toast;

import com.stephentuso.welcome.WelcomeHelper;

public class MainActivity extends AppCompatActivity {
WelcomeHelper helper;
    Button registerButton,signInButton;
    Toolbar toolbar;
    TypewriterView typewriterView;
int no=0;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper=new WelcomeHelper(this,WelcomeClass.class);
        helper.show(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        initializeViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FACIAL RECOGNITION PLATFORM");
        InputMethodManager inputMethodManager= (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(typewriterView.getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY);
        if (TypewriterView.ex==1){
            typewriterView.setText(R.string.typingtext);
        }else {
            TypewriterView.ex=1;
            typewriterView.pause()
                    .type("SBI ")
                    .type("Hackathon ")
                    .type("Facial ")
                    .type("Recognition ")
                    .type("Platform ")
                    .run(new Runnable() {
                        @Override
                        public void run() {
                            // Finalize the text if user fiddled with it during animation.
                            typewriterView.setText(R.string.typingtext);
                        }
                    });
        }
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertAccountNo(MainActivity.this);
            }
        });
    }

    public void initializeViews(){
        registerButton= findViewById(R.id.register_button);
        typewriterView = findViewById(R.id.tagline_typewriter);
        toolbar = findViewById(R.id.main_page_toolbar);
        signInButton= findViewById(R.id.recognize);
    }

    public void alertAccountNo(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.accountpopup,null);
        builder.setView(view);
        final EditText editText=view.findViewById(R.id.accountverifyedittext);
        InputFilter.LengthFilter account=new InputFilter.LengthFilter(11);
        editText.setFilters(new InputFilter[]{account});
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String s=editText.getEditableText().toString().trim();
                if(validate(s)){
                    Intent intent= new Intent(MainActivity.this,CameraActivity.class);
                    intent.putExtra("accountno",s);
                    startActivity(intent);
                }else{
                    Toast.makeText(context, "Check Account number", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public boolean validate(String accountno){
        boolean a=false;
        if(accountno==null||accountno.equals("")||accountno.length()!=11){
            Toast.makeText(this, "Enter account number", Toast.LENGTH_SHORT).show();
        }else{
            a=true;
        }
        return a;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        helper.onSaveInstanceState(outState);
    }
}
