package com.example.brown.ecard;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

public class HomePage extends AppCompatActivity {

    ViewPager pager;
    FloatingActionButton fab;
    pager_adapter pagerAdapter;

    SQLiteDatabase db;
    cardDatabase cardHelper;

    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        openData();

        qrScan = new IntentIntegrator(this);

        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        pager = (ViewPager)findViewById(R.id.page_holder);

        List fragments = new Vector<>();
        fragments.add(Fragment.instantiate(this,CardList.class.getName()));
        fragments.add(Fragment.instantiate(this,CardHolder.class.getName()));
        pagerAdapter = new pager_adapter(getSupportFragmentManager(),fragments);

        pager.setAdapter(new pager_adapter(getSupportFragmentManager(),fragments));
    }

    public void QRScan(View view){
        qrScan.initiateScan();
    }

    public void openData(){
        cardHelper = new cardDatabase(this);
        db = cardHelper.helper.getWritableDatabase();
        cardHelper.open();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this,"Result Not Found",Toast.LENGTH_LONG).show();
            }else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    String name = obj.getString("Name");
                    String company = obj.getString("Company");
                    String occupation = obj.getString("Occupation");
                    String email = obj.getString("Email");
                    String number = obj.getString("Number");

                    cardHelper.scanCard(name,company,occupation,email,number);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
