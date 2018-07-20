package com.example.brown.ecard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Brown on 5/17/2018.
 */

public class cardDatabase {
    cardHelper helper;
    SQLiteDatabase db;

    public cardDatabase(Context context){helper = new cardHelper(context);}

    public cardDatabase open(){
        db = helper.getWritableDatabase();
        return this;
    }

    public long insertData(String idnum, String first, String last, String email){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cardHelper.KEY_ID, idnum);
        contentValues.put(cardHelper.KEY_FIRST, first);
        contentValues.put(cardHelper.KEY_LAST,last);
        contentValues.put(cardHelper.KEY_EMAIL, email);
        long id = db.insert(cardDatabase.cardHelper.TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }

    public long updateCard(String idnum,String comp, String occ, String numb, String wemail){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cardHelper.KEY_COMP, comp);
        contentValues.put(cardHelper.KEY_OCC, occ);
        contentValues.put(cardHelper.KEY_NUMB, numb);
        contentValues.put(cardHelper.KEY_WEMAIL,wemail);
        long id = db.update(cardHelper.TABLE_NAME,contentValues,"_id=?",new String[]{idnum});
        db.close();
        return id;
    }

    public long scanCard(String name,String comp, String occ, String numb, String wemail){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cardHelper.KEY_NAME, name);
        contentValues.put(cardHelper.KEY_COMP, comp);
        contentValues.put(cardHelper.KEY_OCC, occ);
        contentValues.put(cardHelper.KEY_NUMB, numb);
        contentValues.put(cardHelper.KEY_WEMAIL,wemail);
        long id = db.insert(cardHelper.TABLE_TWO,null,contentValues);
        db.close();
        return id;
    }

    public Cursor getCard(String id){
        String where = cardHelper.KEY_ID + "=?";
        String[] whereArgs = {id};
        String[] KEYS = new String[]{cardHelper.KEY_FIRST,cardHelper.KEY_LAST,cardHelper.KEY_EMAIL,cardHelper.KEY_COMP,
                        cardHelper.KEY_OCC,cardHelper.KEY_NUMB,cardHelper.KEY_WEMAIL};
        Cursor c = db.query(true, cardHelper.TABLE_NAME, KEYS, where, whereArgs, null, null, null, null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getFirstName(String id){
        String where = cardHelper.KEY_ID + "=?";
        String[] whereArgs = {id};
        String[] KEY = new String[]{cardHelper.KEY_FIRST};
        Cursor c = db.query(true, cardHelper.TABLE_NAME, KEY, where, whereArgs, null, null, null, null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getLastName(String id){
        String where = cardHelper.KEY_ID + "=?";
        String[] whereArgs = {id};
        String[] KEY = new String[]{cardHelper.KEY_LAST};
        Cursor c = db.query(true, cardHelper.TABLE_NAME, KEY, where, whereArgs, null, null, null, null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getEmail(String id){
        String where = cardHelper.KEY_ID + "=?";
        String[] whereArgs = {id};
        String[] KEY = new String[]{cardHelper.KEY_EMAIL};
        Cursor c = db.query(true, cardHelper.TABLE_NAME, KEY, where, whereArgs, null, null, null, null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getComp(String id){
        String where = cardHelper.KEY_ID + "=?";
        String[] whereArgs = {id};
        String[] KEY = new String[]{cardHelper.KEY_COMP};
        Cursor c = db.query(true, cardHelper.TABLE_NAME, KEY, where, whereArgs, null, null, null, null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getOcc(String id){
        String where = cardHelper.KEY_ID + "=?";
        String[] whereArgs = {id};
        String[] KEY = new String[]{cardHelper.KEY_OCC};
        Cursor c = db.query(true, cardHelper.TABLE_NAME, KEY, where, whereArgs, null, null, null, null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getNumb(String id){
        String where = cardHelper.KEY_ID + "=?";
        String[] whereArgs = {id};
        String[] KEY = new String[]{cardHelper.KEY_NUMB};
        Cursor c = db.query(true, cardHelper.TABLE_NAME, KEY, where, whereArgs, null, null, null, null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getWEmail(String id){
        String where = cardHelper.KEY_ID + "=?";
        String[] whereArgs = {id};
        String[] KEY = new String[]{cardHelper.KEY_WEMAIL};
        Cursor c = db.query(true, cardHelper.TABLE_NAME, KEY, where, whereArgs, null, null, null, null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }
    class cardHelper extends SQLiteOpenHelper{

        private static final String DATABASE_NAME = "cardinfo";
        public static final String TABLE_NAME = "CARD_TABLE";
        public static final String TABLE_TWO = "LIST_TABLE";
        private static final int DATABASE_VERSION = 3;

        public static final String KEY_ID = "_id";
        public static final String KEY_FIRST = "_first";
        public static final String KEY_LAST = "_last";
        public static final String KEY_EMAIL = "_email";
        public static final String KEY_OCC = "_occupation";
        public static final String KEY_COMP = "_company";
        public static final String KEY_NUMB = "_number";
        public static final String KEY_WEMAIL = "_wemail";

        public static final String KEY_NAME = "_name";

        private static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID +
                        " VARCHAR(255) , "
                        + KEY_FIRST + " VARCHAR(255), "
                        + KEY_LAST + " VARCHAR(255), "
                        + KEY_EMAIL + " VARCHAR(255), "
                        + KEY_OCC + " VARCHAR(255), "
                        + KEY_COMP + " VARCHAR(255), "
                        + KEY_NUMB + " VARCHAR(255), "
                        + KEY_WEMAIL + " VARCHAR(255)" + ");";

        private static final String LIST_TABLE =
                "CREATE TABLE " + TABLE_TWO + " ("
                        + KEY_NAME + " VARCHAR(255), "
                        + KEY_OCC + " VARCHAR(255), "
                        + KEY_COMP + " VARCHAR(255), "
                        + KEY_NUMB + " VARCHAR(255), "
                        + KEY_WEMAIL + " VARCHAR(255)" + ");";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
                + TABLE_NAME;
        private static final String DROP_LIST = "DROP TABLE IF EXISTS "
                + TABLE_TWO;
        private Context context;

        public cardHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(CREATE_TABLE);
                db.execSQL(LIST_TABLE);
            }catch(SQLException e){
                Log.d("Exception",""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                db.execSQL(DROP_LIST);
                onCreate(db);
            }catch(SQLException e){
                Log.d("Exception",""+e);
            }
        }
    }
}
