package com.example.brown.ecard;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by Brown on 5/22/2018.
 */

public class CardDisplay extends Fragment {

    ImageView logo,QRimage;
    ImageButton info;
    Bitmap bitmap;
    String id,first,last,comp,occ,num,em,joined;
    TextView names,company,occupation,number,email;

    cardDatabase cardHelper;
    SQLiteDatabase db;

    String SCRIPT;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_back,container,false);

        names = (TextView)v.findViewById(R.id.name);
        company = (TextView)v.findViewById(R.id.company);
        occupation = (TextView)v.findViewById(R.id.occupation);
        number = (TextView)v.findViewById(R.id.number);
        email = (TextView)v.findViewById(R.id.email);

        logo = (ImageView)v.findViewById(R.id.logo);
        QRimage = (ImageView)v.findViewById(R.id.QR);

        info = (ImageButton)v.findViewById(R.id.information);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        id = getActivity().getSharedPreferences("profileInfo",Context.MODE_PRIVATE).getString("id","");
        final CardHolder.CardFrontFragment cardFrontFragment = new CardHolder.CardFrontFragment();

        openData();
        String script = getSCRIPT(id);
        QRGen QR = new QRGen();
        try {
            bitmap = QR.TextToImage(script);
        } catch (WriterException e1) {
            e1.printStackTrace();
        }

        QRimage.setImageBitmap(bitmap);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.card_flip_right_in,R.anim.card_flip_right_out)
                        .replace(R.id.container,cardFrontFragment).addToBackStack(null).commit();
            }
        });
//        Cursor cursor;
//        id = getActivity().getSharedPreferences("profileInfo", Context.MODE_PRIVATE).getString("id","");
//        //cursor = cardHelper.getCard(id);
//        String[] args = new String[]{id};
//        cursor = db.rawQuery("SELECT * FROM CARD_TABLE WHERE _id = ?",args);
//        Cursor a,b,c,d,e_,f,g;
//        a = cardHelper.getFirstName(id);
//        b = cardHelper.getLastName(id);
//        c = cardHelper.getComp(id);
//        d = cardHelper.getOcc(id);
//        e_ = cardHelper.getNumb(id);
//        f = cardHelper.getEmail(id);
//
//        do {
//            first = a.toString();
//            last = a.toString();
//            comp = a.toString();
//            occ = a.toString();
//            num = a.toString();
//            em = a.toString();
//            //Generator();
//
//            joined = first + " " + last;
//
//            String name = "\"Name\"";
//            String com = "\"Company\"";
//            String oc = "\"Occupation\"";
//            String e = "\"Email\"";
//            String nu = "\"Number\"";
//            SCRIPT = "{"+name+":"+'"'+joined+'"'+","+com+":"+'"'+comp+'"'+","+oc+":"+'"'+occ+'"'+","
//                    +nu+":"+'"'+num+'"'+","+e+":"+'"'+em+'"'+",}";
//            Toast.makeText(getContext(), SCRIPT, Toast.LENGTH_LONG).show();
//
//            QRGenerator QR = new QRGenerator();
//            try {
//                bitmap = QR.TextToImage(SCRIPT);
//            } catch (WriterException e1) {
//                e1.printStackTrace();
//            }
//
//            QRimage.setImageBitmap(bitmap);
//        }while (cursor.moveToFirst());
    }

    public void openData(){
        cardHelper = new cardDatabase(getActivity());
        db = cardHelper.helper.getWritableDatabase();
        cardHelper.open();
    }

    public String getSCRIPT(String id) {
        Cursor cursor;
        //id = getActivity().getSharedPreferences("profileInfo", Context.MODE_PRIVATE).getString("id", "");
        //cursor = cardHelper.getCard(id);
        String[] args = new String[]{id};
        cursor = db.rawQuery("SELECT * FROM CARD_TABLE WHERE _id = ?", args);
        Cursor a, b, c, d, e_, f, g;
        a = cardHelper.getFirstName(id);
        b = cardHelper.getLastName(id);
        c = cardHelper.getComp(id);
        d = cardHelper.getOcc(id);
        e_ = cardHelper.getNumb(id);
        f = cardHelper.getEmail(id);

        if(cursor.moveToFirst()) {
            first = cursor.getString(cursor.getColumnIndex("_first"));
            Log.d("first",first);
            last = cursor.getString(cursor.getColumnIndex("_last"));
            Log.d("last",last);
            comp = cursor.getString(cursor.getColumnIndex("_company"));
            Log.d("comp",comp);
            occ = cursor.getString(cursor.getColumnIndex("_occupation"));
            Log.d("occ",occ);
            num = cursor.getString(cursor.getColumnIndex("_number"));
            Log.d("num",num);
            em = cursor.getString(cursor.getColumnIndex("_email"));
            Log.d("email",em);
            //Generator();

            joined = first + " " + last;

            names.setText(joined);
            company.setText(comp);
            occupation.setText(occ);
            number.setText(num);
            email.setText(em);

            String name = "\"Name\"";
            String com = "\"Company\"";
            String oc = "\"Occupation\"";
            String e = "\"Email\"";
            String nu = "\"Number\"";
            SCRIPT = "{" + name + ":" + '"' + joined + '"' + "," + com + ":" + '"' + comp + '"' + "," + oc + ":" + '"' + occ + '"' + ","
                    + nu + ":" + '"' + num + '"' + "," + e + ":" + '"' + em + '"' + "}";
            Toast.makeText(getContext(), SCRIPT, Toast.LENGTH_LONG).show();
        }
        return SCRIPT;
    }

    public class QRGen{
        public final static int QRcodeWidth = 500 ;

        public Bitmap TextToImage(String Text)throws WriterException{
            BitMatrix bitMatrix;
            try {
                bitMatrix = new MultiFormatWriter().encode(
                        Text,
                        BarcodeFormat.DATA_MATRIX.QR_CODE,
                        QRcodeWidth, QRcodeWidth, null
                );

            } catch (IllegalArgumentException Illegalargumentexception) {

                return null;
            }
            int bitMatrixWidth = bitMatrix.getWidth();

            int bitMatrixHeight = bitMatrix.getHeight();

            int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

            for (int y = 0; y < bitMatrixHeight; y++) {
                int offset = y * bitMatrixWidth;

                for (int x = 0; x < bitMatrixWidth; x++) {

                    pixels[offset + x] = bitMatrix.get(x, y) ?
                            getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

            bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
            return bitmap;
        }
    }
   /* public void Generator(){
        joined = first + " " + last;

        String name = "\"Name\"";
        String com = "\"Company\"";
        String oc = "\"Occupation\"";
        String e = "\"Email\"";
        String nu = "\"Number\"";
        SCRIPT = "{"+name+":"+'"'+joined+'"'+","+com+":"+'"'+comp+'"'+","+oc+":"+'"'+occ+'"'+","
                +nu+":"+'"'+num+'"'+","+e+":"+'"'+em+'"'+",}";

        QRGenerator QR = new QRGenerator();
        try {
            bitmap = QR.TextToImage(SCRIPT);
        } catch (WriterException e1) {
            e1.printStackTrace();
        }

        QRimage.setImageBitmap(bitmap);
    }*/
}
