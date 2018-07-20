package com.example.brown.ecard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Brown on 5/4/2018.
 */

public class UploadProfile extends AsyncTask<String,Void,String> {

    private String method;
    public String id;
    public String fName,lName,email,password;

    AlertDialog alertDialog;
    ProgressDialog pdLoading;
    Context context;

    public String UPLOAD_URL = "http://mikeb4771.000webhostapp.com/insert.php";
    public String LOGIN_URL = "http://mikeb4771.000webhostapp.com/log_in.php";

    public UploadProfile(Context context){this.context = context;}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login");

        pdLoading = new ProgressDialog(context);
        pdLoading.setMessage("\tSigning In...");
        pdLoading.setCancelable(false);
        pdLoading.show();
    }

    @Override
    protected void onPostExecute(String s) {
        pdLoading.dismiss();

        try{
            JSONObject pull = new JSONObject(s);
            JSONObject user_data = pull.getJSONObject("user_data");
            String name = user_data.getString("first_name");

            if(name.equals("")){
                if(!((Activity)context).isFinishing()) {
                    alertDialog.setMessage("Incorrect Email or Password");
                    alertDialog.show();
                    alertDialog.dismiss();
                }
            }else {
                alertDialog.setMessage("Welcome " + name);
                alertDialog.show();
                alertDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String doInBackground(String... params) {
        method = params[0];

        if(method.equals("register")){
            id = params[1];
            fName = params[2];
            lName = params[3];
            email = params[4];
            password = params[5];

            try {
                URL url = new URL(UPLOAD_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data_string = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                        URLEncoder.encode("first", "UTF-8") + "=" + URLEncoder.encode(fName, "UTF-8") + "&" +
                        URLEncoder.encode("last", "UTF-8") + "=" + URLEncoder.encode(lName, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();

                httpURLConnection.disconnect();
                return "Registration Success...";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("log-in")){
            email = params[1];
            password = params[2];

            try {
                URL url = new URL(LOGIN_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("login_email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("login_pass", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                //String response = "";
                StringBuilder response = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine())!=null)
                {
                    //response+= line;
                    response.append(line);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return(response.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
