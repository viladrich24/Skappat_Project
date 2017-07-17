package viladrich.arnau.final_project.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class MyDatabaseHelper extends SQLiteOpenHelper {


    private final String TAG = "MyDataBaseHelper";

    public static final int DATABASE_VERSION = 35;
    public static final String DATABASE_NAME = "MyDataBase.db";


    private static final String SQL_CREATE_TABLE1 =
            "CREATE TABLE " + MyDatabaseContract.Table1.TABLE_NAME + " (" +
                    MyDatabaseContract.Table1.COLUMN_USER + " text unique," +
                    MyDatabaseContract.Table1.COLUMN_PASSWORD + " text," +
                    MyDatabaseContract.Table1.COLUMN_MAIL + " text," +
                    MyDatabaseContract.Table1.COLUMN_PHONE + " text," +
                    MyDatabaseContract.Table1.COLUMN_MEMORY + " text,"+
                    MyDatabaseContract.Table1.COLUMN_TIME + " text,"+
                    MyDatabaseContract.Table1.COLUMN_NOTI + " text,"+
                    MyDatabaseContract.Table1.COLUMN_IMAGE_ID + " text,"+
                    MyDatabaseContract.Table1.COLUMN_NUMERO + " text)";

    private static final String SQL_DELETE_TABLE1 =
            "DROP TABLE IF EXISTS " + MyDatabaseContract.Table1.TABLE_NAME;

    private static MyDatabaseHelper instance;
    private static SQLiteDatabase writable;
    private static SQLiteDatabase readable;


    public static MyDatabaseHelper getInstance(Context c) {
        if(instance == null){
            instance = new MyDatabaseHelper(c);
            if (writable == null) writable = instance.getWritableDatabase();
            if (readable == null) readable = instance.getReadableDatabase();
        }
        return instance;
    }

    public MyDatabaseHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(SQL_DELETE_TABLE1);
        onCreate(sqLiteDatabase);
    }

    public long createRow(String s1, String s2, String s3, String s4, String s5){

        ContentValues valors = new ContentValues();
        valors.put(MyDatabaseContract.Table1.COLUMN_USER, s1);
        valors.put(MyDatabaseContract.Table1.COLUMN_PASSWORD, s2);
        valors.put(MyDatabaseContract.Table1.COLUMN_MAIL, s3);
        valors.put(MyDatabaseContract.Table1.COLUMN_PHONE, s4);
        valors.put(MyDatabaseContract.Table1.COLUMN_MEMORY, "99");
        valors.put(MyDatabaseContract.Table1.COLUMN_TIME, "99:99");
        valors.put(MyDatabaseContract.Table1.COLUMN_NOTI, "none");
        valors.put(MyDatabaseContract.Table1.COLUMN_IMAGE_ID, s5);

        long newId = writable.insert(MyDatabaseContract.Table1.TABLE_NAME, null, valors);

        return newId;
    }

    public void addRegistrationNumber(String user, String num){

        ContentValues valors = new ContentValues();

        valors.put(MyDatabaseContract.Table1.COLUMN_NUMERO, num);

        readable.update(MyDatabaseContract.Table1.TABLE_NAME, valors, MyDatabaseContract.Table1.COLUMN_USER +
                " LIKE ? ", new String[] {user});
    }

    public void addNewRanking(String user, String record){ // paso user i record

        ContentValues valors = new ContentValues();

        valors.put(MyDatabaseContract.Table1.COLUMN_MEMORY, record);

        readable.update(MyDatabaseContract.Table1.TABLE_NAME, valors, MyDatabaseContract.Table1.COLUMN_USER +
                " LIKE ? ", new String[] {user});
    }

    public void resetRanking(){

        ContentValues valors = new ContentValues();

        valors.put(MyDatabaseContract.Table1.COLUMN_MEMORY, "99");
        valors.put(MyDatabaseContract.Table1.COLUMN_TIME, "99:99");
        readable.update(MyDatabaseContract.Table1.TABLE_NAME, valors, null, null);
    }

    public void addNewTime(String user, String time){ // paso user i record

        ContentValues valors = new ContentValues();

        valors.put(MyDatabaseContract.Table1.COLUMN_TIME, time);

        readable.update(MyDatabaseContract.Table1.TABLE_NAME, valors, MyDatabaseContract.Table1.COLUMN_USER +
                " LIKE ? ", new String[] {user});
    }

    public void addNewNoti(String user, String noti){ // paso user i record

        ContentValues valors = new ContentValues();

        valors.put(MyDatabaseContract.Table1.COLUMN_NOTI, noti);

        readable.update(MyDatabaseContract.Table1.TABLE_NAME, valors, MyDatabaseContract.Table1.COLUMN_USER +
                " LIKE ? ", new String[] {user});
    }

    public void addNewPhone(String user, String newPhone){ // paso user i record

        ContentValues valors = new ContentValues();

        valors.put(MyDatabaseContract.Table1.COLUMN_PHONE, newPhone);

        readable.update(MyDatabaseContract.Table1.TABLE_NAME, valors, MyDatabaseContract.Table1.COLUMN_USER +
                " LIKE ? ", new String[] {user});
    }

    public void addNewMail(String user, String newMail){ // paso user i record

        ContentValues valors = new ContentValues();

        valors.put(MyDatabaseContract.Table1.COLUMN_MAIL, newMail);

        readable.update(MyDatabaseContract.Table1.TABLE_NAME, valors, MyDatabaseContract.Table1.COLUMN_USER +
                " LIKE ? ", new String[] {user});
    }

    public void addNewPhoto(String user, String drawableId){ // paso user i record

        ContentValues valors = new ContentValues();

        valors.put(MyDatabaseContract.Table1.COLUMN_IMAGE_ID, drawableId);

        readable.update(MyDatabaseContract.Table1.TABLE_NAME, valors, MyDatabaseContract.Table1.COLUMN_USER +
                " LIKE ? ", new String[] {user});
    }

    public int queryRowMatch(String s1, String s2) { //troba si coincideix pas i user

        Cursor c;

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_USER, MyDatabaseContract.Table1.COLUMN_PASSWORD},
                MyDatabaseContract.Table1.COLUMN_USER + " LIKE ? AND " + MyDatabaseContract.Table1.COLUMN_PASSWORD + " LIKE ? ",
                new String[] {s1, s2}, null, null, null);

        int returnValue = -2;

        if (c.moveToFirst()) {  //torna -1 quan no hi ha res al cursor, es a dir cap posicio que es compleixi

            do {

                int index = c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_USER);
                returnValue = index;

            } while (c.moveToNext());
        }
        else returnValue = -1;

        c.close();

        return returnValue;
    }

    public int queryPasswordPhone(String s1, String s2) { //troba si coincideix pas i user

        Cursor c;

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_USER, MyDatabaseContract.Table1.COLUMN_PHONE},
                MyDatabaseContract.Table1.COLUMN_USER + " LIKE ? AND " + MyDatabaseContract.Table1.COLUMN_PHONE + " LIKE ? ",
                new String[] {s1, s2}, null, null, null);

        int returnValue = -2;

        if (c.moveToFirst()) {  //torna -1 quan no hi ha res al cursor, es a dir cap posicio que es compleixi

            do {

                int index = c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_USER);
                returnValue = index;

            } while (c.moveToNext());
        }
        else returnValue = -1;

        c.close();

        return returnValue;
    }

    public int queryPasswordMail(String s1, String s2) { //troba si coincideix pas i user

        Cursor c;

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_USER, MyDatabaseContract.Table1.COLUMN_MAIL},
                MyDatabaseContract.Table1.COLUMN_USER + " LIKE ? AND " + MyDatabaseContract.Table1.COLUMN_MAIL + " LIKE ? ",
                new String[] {s1, s2}, null, null, null);

        int returnValue = -2;

        if (c.moveToFirst()) {  //torna -1 quan no hi ha res al cursor, es a dir cap posicio que es compleixi

            do {

                int index = c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_USER);
                returnValue = index;

            } while (c.moveToNext());
        }
        else returnValue = -1;

        c.close();

        return returnValue;
    }

    public int queryRowExist(String s) { //troba si existeix user

        Cursor c;

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_USER},
                MyDatabaseContract.Table1.COLUMN_USER + " = ? ",
                new String[] {s}, null, null, null);

        int returnValue = -2;

        if (c.moveToFirst()) { //torna false si el cursor no apunta enlloc
            // si es true, es a dir, l'usuari s'ha trobat, tornarà un numero diferent a -1

            do {

                int index = c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_USER);
                returnValue = index;

            } while (c.moveToNext());
        }
        else returnValue = -1;

        c.close();

        return returnValue;
    }

    public String queryMemory(String user) { //troba si existeix user

        Cursor c;

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_MEMORY},
                MyDatabaseContract.Table1.COLUMN_USER + " = ? ",
                new String[] {user}, null, null, null);

        String record = "record";

        if (c.moveToFirst()) {

            do {

                record = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_MEMORY));

            } while (c.moveToNext());
        }

        c.close();

        return record;
    }

    public String queryTime(String user) { //troba si existeix user

        Cursor c;

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_TIME},
                MyDatabaseContract.Table1.COLUMN_USER + " = ? ",
                new String[] {user}, null, null, null);

        String record = "record";

        if (c.moveToFirst()) {

            do {

                record = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_TIME));

            } while (c.moveToNext());
        }

        c.close();

        return record;
    }

    public String queryPhone(String user) { //troba si existeix user

        Cursor c;

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_PHONE},
                MyDatabaseContract.Table1.COLUMN_USER + " = ? ",
                new String[] {user}, null, null, null);

        String record = "record";

        if (c.moveToFirst()) {

            do {

                record = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_PHONE));

            } while (c.moveToNext());
        }

        c.close();

        return record;
    }

    public String queryMail(String user) { //troba si existeix user

        Cursor c;

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_MAIL},
                MyDatabaseContract.Table1.COLUMN_USER + " = ? ",
                new String[] {user}, null, null, null);

        String record = "record";

        if (c.moveToFirst()) {

            do {

                record = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_MAIL));

            } while (c.moveToNext());
        }

        c.close();

        return record;
    }

    public String queryNoti(String user) { //troba si existeix user

        Cursor c;

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_NOTI},
                MyDatabaseContract.Table1.COLUMN_USER + " = ? ",
                new String[] {user}, null, null, null);

        String record = "record";

        if (c.moveToFirst()) {

            do {

                record = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_NOTI));

            } while (c.moveToNext());
        }

        c.close();

        return record;
    }

    public String queryImage(String user) { //troba si existeix user

        Cursor c;

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_IMAGE_ID},
                MyDatabaseContract.Table1.COLUMN_USER + " = ? ",
                new String[] {user}, null, null, null);

        String record = "record";

        if (c.moveToFirst()) {

            do {

                record = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_IMAGE_ID));

            } while (c.moveToNext());
        }

        c.close();

        return record;
    }

    public String queryRegistrationNumber(String user) { //troba si existeix user

        Cursor c;

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_NUMERO},
                MyDatabaseContract.Table1.COLUMN_USER + " = ? ",
                new String[] {user}, null, null, null);

        String num = "record";

        if (c.moveToFirst()) {

            do {

                num = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_NUMERO));

            } while (c.moveToNext());
        }

        c.close();

        return num;
    }

    public HashMap <String, String> agafarPhone(){

        Cursor c;

        HashMap < String, String> usuarisRecords = new HashMap<>();

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_USER, MyDatabaseContract.Table1.COLUMN_PHONE},
                null, null, null, null, null);

        String user = "record";
        String phone = "sd";


        if (c.moveToFirst()) {

            do {

                user = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_USER));
                phone = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_PHONE));

                usuarisRecords.put(user, phone);

            } while (c.moveToNext());
        }

        c.close();

        return usuarisRecords;
    }

    public HashMap <String, String> agafarRecord(){

        Cursor c;

        HashMap < String, String> usuarisRecords = new HashMap<>();

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_USER, MyDatabaseContract.Table1.COLUMN_MEMORY},
                null, null, null, null, null);

        String user = "record";
        String record = "sd";


        if (c.moveToFirst()) {

            do {

                user = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_USER));
                record = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_MEMORY));

                usuarisRecords.put(user, record);

            } while (c.moveToNext());
        }

        c.close();

        return usuarisRecords;
    }

    public HashMap <String, String> agafarIcon(){

        Cursor c;

        HashMap < String, String> usuarisRecords = new HashMap<>();

        c = readable.query(MyDatabaseContract.Table1.TABLE_NAME,
                new String[] {MyDatabaseContract.Table1.COLUMN_USER, MyDatabaseContract.Table1.COLUMN_IMAGE_ID},
                null, null, null, null, null);

        String user = "record";
        String image = "sd";


        if (c.moveToFirst()) {

            do {

                user = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_USER));
                image = c.getString(c.getColumnIndex(MyDatabaseContract.Table1.COLUMN_IMAGE_ID));

                usuarisRecords.put(user, image);

            } while (c.moveToNext());
        }

        c.close();

        return usuarisRecords;
    }

    @Override
    public synchronized void close() {
        super.close();
        writable.close();
        readable.close();
        Log.v(TAG,"close()");
    }

}

