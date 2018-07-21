package com.example.brown.ecard;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.BufferedReader;

public class MainActivity extends Activity {

    SQLiteDatabase db;
    cardDatabase cardHelper;

    public SharedPreferences preferences;
    public SharedPreferences.Editor edit;

    AlertDialog.Builder alert;
    cardDatabase cardDatabase;

    public String firststring,laststring,emailstring,passstring;
    private EditText first,last,email,pass;
    private Button submit;

    public String METHOD = "register";
    private String code;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openData();
        first = (EditText)findViewById(R.id.firstname);
        last = (EditText)findViewById(R.id.lastname);
        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);

        submit = (Button)findViewById(R.id.submitprofile);

    }

    public void openData(){
        cardHelper = new cardDatabase(this);
        db = cardHelper.helper.getWritableDatabase();
        cardHelper.open();
    }

    public void nameStore(String name){

        preferences = getSharedPreferences("profileInfo", MODE_PRIVATE);
        edit = preferences.edit();
        edit.putString("name", name);
        edit.commit();
    }

    public void idStore(String id){

        preferences = getSharedPreferences("profileInfo", MODE_PRIVATE);
        edit = preferences.edit();
        edit.putString("id", id);
        edit.commit();
    }

    public void toLogin(View view){
        Intent toLogin = new Intent(this,Login.class);
        startActivity(toLogin);
    }

    public void sendMail(View view) {
        firststring = first.getText().toString();
        laststring = last.getText().toString();
        emailstring = email.getText().toString();
        passstring = pass.getText().toString();

        String subject = "Student Connect Account Registration.";
        int rand = (int) (Math.random() * 9000) + 1000;
        final String ran = Integer.toString(rand);
        code = "A0" + ran;

        idStore(code);
        SendMail sm = new SendMail(MainActivity.this, emailstring, subject, code);

        sm.execute();

        alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Verify Account");
        alert.setMessage("Enter account verification code");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        input.setLayoutParams(lp);
        alert.setView(input);

        alert.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String vc = input.getText().toString();

                    if (vc.equals(code)) {
                        cardHelper.insertData(code, firststring, laststring, emailstring);

                        UploadProfile uploadProfile = new UploadProfile(MainActivity.this);
                        UploadCardInfo uploadCardInfo = new UploadCardInfo(MainActivity.this);
                        uploadProfile.execute(METHOD, code, firststring, laststring, emailstring, passstring);
                        uploadCardInfo.execute(METHOD, code);

                        alert.setTitle("Verification Successful!");
                        alert.setMessage("Account Registration Complete.");
                        dialog.dismiss();
                    } else if(!vc.equals(code)){
                        alert.setTitle("Incorrect Code!");
                        alert.setMessage("Please try again or resubmit email address.");
                    }
            }
        }) ;

    /*    cardHelper.insertData(code, firststring, laststring, emailstring);
        UploadProfile uploadProfile = new UploadProfile(MainActivity.this);
        UploadCardInfo uploadCardInfo = new UploadCardInfo(MainActivity.this);
        uploadProfile.execute(METHOD, code, firststring, laststring, emailstring, passstring);
        uploadCardInfo.execute(METHOD, code);

        Log.d("Sub",firststring);

        Intent toSetup = new Intent(MainActivity.this,Setup.class);
        startActivity(toSetup); */
//        sm.execute();
//        //regAlert(METHOD,code,firststring,laststring,emailstring,passstring);
//        if (emailstring.contains(".com")) {
//            alert = new AlertDialog.Builder(MainActivity.this);
//
//            alert.setTitle("Verify Account");
//            alert.setMessage("Enter account verification code");
//
//            final EditText input = new EditText(MainActivity.this);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT);
//            input.setLayoutParams(lp);
//            alert.setView(input);
//
//            alert.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    String vc = input.getText().toString();
//
//                    if (vc.equals(code)) {
//                        cardHelper.insertData(code, firststring, laststring, emailstring);
//
//                        UploadProfile uploadProfile = new UploadProfile(MainActivity.this);
//                        UploadCardInfo uploadCardInfo = new UploadCardInfo(MainActivity.this);
//                        uploadProfile.execute(METHOD, code, firststring, laststring, emailstring, passstring);
//                        uploadCardInfo.execute(METHOD, code);
//
//                        alert.setTitle("Verification Successful!");
//                        alert.setMessage("Account Registration Complete.");
//                        dialog.dismiss();
//
////                            Intent toHome = new Intent(MainActivity.this,HomePage.class);
////                            startActivity(toHome);
//
//                        finish();
//                    } else if (!vc.equals(code)) {
//                        alert.setTitle("Incorrect Code!");
//                        alert.setMessage("Please try again or resubmit email address.");
//                    }
//                }
//            });
//
//            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//
//            alert.show();
//            nameStore(firststring);
//        }
    }

    public void sendEmail(String email){
        String subject = "Student Connect Account Registration.";
        int rand = (int)(Math.random()*9000)+1000;
        final String ran = Integer.toString(rand);
        code = "A0"+ran;

        idStore(code);
        SendMail sm = new SendMail(this, email, subject, code);
        sm.execute();
    }

    public void regAlert(final String method,final String id, final String fname, final String lname, final String mail, final String password){
        alert = new AlertDialog.Builder(this);

        alert.setTitle("Verify Account");
        alert.setMessage("Enter account verification code");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alert.setView(input);

        alert.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String vc = input.getText().toString();

                if(vc.equals(code)){
                    cardHelper.insertData(id,fname,lname,mail);

                    UploadProfile uploadProfile = new UploadProfile(MainActivity.this);
                    UploadCardInfo uploadCardInfo = new UploadCardInfo(MainActivity.this);
                    uploadProfile.execute(method,id,fname,lname,mail,password);
                    uploadCardInfo.execute(method,id);

                    alert.setTitle("Verification Successful!");
                    alert.setMessage("Account Registration Complete.");
                    dialog.dismiss();

//                    Intent toHome = new Intent(MainActivity.this,HomePage.class);
//                    startActivity(toHome);

                    finish();
                }else if(!vc.equals(code)){
                    alert.setTitle("Incorrect Code!");
                    alert.setMessage("Please try again or resubmit email address.");
                }
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();
    }
}
