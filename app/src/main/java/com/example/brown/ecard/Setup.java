package com.example.brown.ecard;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class Setup extends Activity {

    cardDatabase cardHelper;
    SQLiteDatabase db;

    String id,comp,occ,wemail,wnumb,fx;
    String METHOD = "upload";

    Button submitcard;
    EditText company,occupation,workemail,worknumber,fax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup);

        openData();
        id = getSharedPreferences("profileInfo",MODE_PRIVATE).getString("id","");

        company = (EditText)findViewById(R.id.company);
        occupation = (EditText)findViewById(R.id.occupation);
        workemail = (EditText)findViewById(R.id.workemail);
        worknumber = (EditText)findViewById(R.id.worknumber);
        fax = (EditText)findViewById(R.id.fax);

        submitcard = (Button)findViewById(R.id.submitcard);
        submitcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comp = company.getText().toString();
                occ = occupation.getText().toString();
                wemail = workemail.getText().toString();
                wnumb = worknumber.getText().toString();
                fx = fax.getText().toString();

                cardHelper.updateCard(id,comp,occ,wnumb,wemail);

                UploadCardInfo uploadCardInfo = new UploadCardInfo(Setup.this);
                uploadCardInfo.execute(METHOD,comp,occ,wnumb,wemail,fx,id);

                Intent toHome = new Intent(Setup.this,HomePage.class);
                startActivity(toHome);
            }
        });
    }

    public void openData(){
        cardHelper = new cardDatabase(this);
        db = cardHelper.helper.getWritableDatabase();
        cardHelper.open();
    }
}
