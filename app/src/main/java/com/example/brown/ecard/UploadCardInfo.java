package com.example.brown.ecard;

import android.content.Context;
import android.os.AsyncTask;

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
 * Created by Brown on 5/9/2018.
 */

public class UploadCardInfo extends AsyncTask<String,Void,String> {

    String method;
    String id,comp,occ,wemail,wnumb,fax;

    Context context;

    public String REG_URL = "http://mikeb4771.000webhostapp.com/insert_card.php";
    public String CARD_URL = "http://mikeb4771.000webhostapp.com/card_push.php";

    public UploadCardInfo(Context context){this.context = context;}
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... params) {
        method = params[0];

        if(method.equals("register")){
            id = params[1];

            try {
                URL url = new URL(REG_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data_string = URLEncoder.encode("id_num", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
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
        else if(method.equals("upload")){
            comp = params[1];
            occ = params[2];
            wnumb = params[3];
            wemail = params[4];
            fax = params[5];
            id = params[6];

            try {
                URL url = new URL(CARD_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("id_num", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                        URLEncoder.encode("company", "UTF-8") + "=" + URLEncoder.encode(comp, "UTF-8") + "&" +
                        URLEncoder.encode("occupation", "UTF-8") + "=" + URLEncoder.encode(occ, "UTF-8") + "&" +
                        URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(wnumb, "UTF-8") + "&" +
                        URLEncoder.encode("work", "UTF-8") + "=" + URLEncoder.encode(wemail, "UTF-8") + "&" +
                        URLEncoder.encode("fax", "UTF-8") + "=" + URLEncoder.encode(fax, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();

                httpURLConnection.disconnect();
                return "Upload Success...";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
