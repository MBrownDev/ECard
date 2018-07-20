package com.example.brown.ecard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Activity {

    EditText UName,Password;
    String uname, password;
    public String name, pw, NAME;
    public SharedPreferences preferences;
    public SharedPreferences.Editor edit;
    public AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UName = (EditText)findViewById(R.id.email);
        Password = (EditText)findViewById(R.id.password);

        NAME = "";

    }
    public void loginStore(String username,String password){

        preferences = getSharedPreferences("logInfo", MODE_PRIVATE);
        edit = preferences.edit();
        edit.putString("username",username);
        edit.putString("password",password);
        edit.commit();
        //preferences = getSharedPreferences("logInfo", MODE_PRIVATE).getString("password",password);
    }

    public void login(View view){
        uname = UName.getText().toString();
        password = Password.getText().toString();
        String method = "login";

        loginStore(uname,password);

        BackgroundLogin backgroundLogin = new BackgroundLogin(this);

        //backgroundTask.execute(method, uname, password);
        backgroundLogin.execute(uname,password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alertDialog.dismiss();
    }

    class BackgroundLogin extends AsyncTask<String,Void,String> {

        //AlertDialog alertDialog;
        ProgressDialog pdLoading;
        String login_url;
        Context context;

        public BackgroundLogin(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Login");

            pdLoading = new ProgressDialog(context);
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

            login_url = "https://mikeb4771.000webhostapp.com/log_in.php";
        }

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL(login_url);
                String urlParams = "login_email="+email+"&login_pass="+password;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pdLoading.dismiss();
            JSONPull(s);
            /*
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                NAME = user_data.getString("name");

                MessageToast.message(context,"works");
                if(NAME.equals("")){
                        alertDialog.setMessage("Incorrect Email or Password");
                        alertDialog.show();
                    MessageToast.message(context,"done");

                }else {
                    alertDialog.setMessage("Welcome " + NAME);
                    alertDialog.show();
                    MessageToast.message(context,"done");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
        }

        public void JSONPull(String result){
            try {
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                NAME = user_data.getString("first_name");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(NAME.equals("")){
                alertDialog.setMessage("Incorrect Email or Password");
                alertDialog.show();
                //alertDialog.dismiss();
                //MessageToast.message(context,"done");
            }else {
                alertDialog.setMessage("Welcome " + NAME);
                alertDialog.show();
                //alertDialog.dismiss();
                //nameStore(NAME);
                //MessageToast.message(context, "done");

                Intent intent = new Intent(Login.this,HomePage.class);
                startActivity(intent);
                finish();
            }
        }

    }
}
